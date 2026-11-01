package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gui.GUIStatic;
import gui.StyledTextPane;
import meb.entities.Call;
import parser.call.CallParser;

/**
 * Basic class for performing the MEB trace analysis. It opens the trace file and parses it. It creates a list of
 * {@link RawTrace raw trace lines} and gives them to the {@link CallParser} to be further analyzed into calls.
 *  
 * @author Panos Katsikogiannis
 *
 */
public class TraceParser {

	/**
	 * Number of lines interval to read from the trace file, before updating the progress to the 
	 * {@link ParserProgressListener progress listener}
	 */
	private static final int LINES_BEFORE_PROGRESS = 1000;

	/**
	 * Trace lines should start with the specific date pattern (e.g.: <b>2022-03-16 11:36:48.838</b>)
	 */
	private static final Pattern DATE_PATTERN = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3})[\\s\\S]*$");

	private ParserProgressListener progressListener = null;
	private String path;
	private List<RawTrace> rawTraceLines;
	private CallParser callParser = null;

	/**
	 * Creates a new {@link TraceParser} object. The path of the MEB trace file to be analyzed is passed as parameter. In addition, it needs a
	 * {@link ParserProgressListener} in order to publish the progress of the parsing.
	 * @param path the path of the MEB trace file to analyze
	 * @param progressListener a listener to publish the parsing progress
	 */
	public TraceParser(String path, ParserProgressListener progressListener) {
		this.progressListener = progressListener;
		this.path = path;
		rawTraceLines = new ArrayList<RawTrace>();
	}

	/**
	 * Initiates the parsing of the MEB trace file.
	 * @return true if parse was successful, false otherwise.
	 */
	public boolean parse() {
		boolean result = false;
		try {
			progressListener.setInfo("Parsing trace file...");
			result = readTraceFile(path);
			Thread.sleep(100);
			progressListener.setInfo("Parsing calls...");
			callParser = new CallParser(rawTraceLines, progressListener);
			result = callParser.parse();
		} catch (Exception e) {
			GUIStatic.printLog(e.getMessage(), StyledTextPane.ERROR);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Tries to read and parse the MEB trace file and create the a list {@link RawTrace traces}.
	 * @return true if parse was successful, false otherwise.
	 */
	private boolean readTraceFile(String filename) {
		boolean result = true;
		String line;
		int readForProgress = LINES_BEFORE_PROGRESS;
		long completedBytes = 0;

		File file = new File(filename);
		long fileSize = file.length();
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e1) {
			return false;
		}
		BufferedReader bufferedReaderInput = new BufferedReader(fileReader);
		try {
			boolean previousTraceComplete = true;
			while ((line = bufferedReaderInput.readLine()) != null) {
				completedBytes += line.length();
				if (previousTraceComplete)
					previousTraceComplete = parseLine(line);
				else
					previousTraceComplete = parseMessage(line);
				if (readForProgress-- == 0) {
					readForProgress = LINES_BEFORE_PROGRESS;
					progressListener.progress((int)((completedBytes * 100) / fileSize));
				}
			}
			bufferedReaderInput.close();
			fileReader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			result = false;
		}
		progressListener.progress(100);
		return result;
	}

	/**
	 * Tries to parse a line in the trace file as message of a previous {@link RawTrace}. Called when the last RawTrace has no message.
	 * @param line the line in the trace file to parse
	 * @return true if the the line has been completely analyzed, false otherwise
	 */
	private boolean parseMessage(String line) {
		boolean result = false;
		Matcher matcher = DATE_PATTERN.matcher(line);
		// Check if we match the date pattern (happens when we have reached the end of the previous message)
		if (matcher.find()) {
			// No message in the previous trace! Just parse normally this trace line
			result = parseLine(line);
		} else if (rawTraceLines.size() > 0) {
			// Else the line contains the message of the previous (last) raw trace line
			rawTraceLines.get(rawTraceLines.size() - 1).appendMessage(line + "\n");
		}
		return result;
	}

	/**
	 * Tries to analyze a line in the trace file and create the relevant {@link RawTrace}.
	 * @param line the trace line to be analyzed
	 * @return true if the analysis is complete, false if it detects that more data (next trace lines) are needed 
	 * to complete it.
	 */
	private boolean parseLine(String line) {
		boolean result = true;
		Matcher matcher = DATE_PATTERN.matcher(line);
		if (matcher.find()) {
			String time = matcher.group(1);
			line = line.replaceFirst(time, "").trim();
			String component = line.substring(0, 12).trim();
			int level = 0;
			// In case of VISU trace at SCI, we may have only 1 byte for level and no callID
			if (line.length() < 14)
				level = Integer.parseInt(line.substring(12, 13).trim());
			// Otherwise, check 2 bytes for level
			else level = Integer.parseInt(line.substring(12, 14).trim());
			long callID = -1;
			try {
				// In case of VISU trace at SCI we do not have a callID
				if (line.length() > 14) {
					callID = Long.parseLong(line.substring(14, 22).trim(), 16);
				}
			} catch (NumberFormatException e) {}
			String message = "";
			// In case of VISU trace we do not have a message
			if (line.length() > 22)
				message = line.substring(22).trim();
			rawTraceLines.add(new RawTrace(time, component, level, callID, new StringBuffer(message)));
			// If no message found, return that trace is not complete
			if (message.length() == 0)
				result = false;
		} else {
			if ((rawTraceLines.size() > 0) && (line.length() > 0)) {
				rawTraceLines.get(rawTraceLines.size() - 1).appendMessage("\n"+ line);
			}
		}
		return result;
	}

	/**
	 * Returns the list of found calls by the {@link CallParser}.
	 * @return the list of found calls
	 */
	public ArrayList<Call> getCalls() {
		return callParser.getCalls();
	}
}
