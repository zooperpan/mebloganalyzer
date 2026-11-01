package parser.call;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gui.GUIStatic;
import gui.StyledTextPane;
import meb.entities.Call;
import meb.entities.Component;
import parser.ParserProgressListener;
import parser.RawTrace;

/**
 * CallParser is responsible for parsing all the {@link RawTrace traces} and creating the relevant {@link Call calls}.
 * It utilizes the {@link CallManager} in order to create and manage the calls.
 * At the end it will hold a HashMap and an ArrayList with all the calls that have been found.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CallParser {

	/**
	 * Number of {@link RawTrace traces} interval to read from the trace list, before updating the progress to the 
	 * {@link ParserProgressListener progress listener}
	 */
	private static final int TRACES_BEFORE_PROGRESS = 1000;

	/**
	 * Maximum number of skipped traces that are reported individually in the log. Any further skipped traces are only counted.
	 */
	private static final int MAX_REPORTED_SKIPPED_TRACES = 10;

	/**
	 * Maximum number of characters of a skipped trace that are shown in the log.
	 */
	private static final int MAX_REPORTED_TRACE_LENGTH = 100;

	/**
	 * Start of the trace written by the CPU component when MEB starts (e.g.: <b>Starting MEB Version 4.0.0.156</b>)
	 */
	private static final String MEB_START_MARK = "Starting MEB Version";

	private List<RawTrace> rawTraceLines = null;
	private int skippedTraces = 0;
	private HashMap<Long, Call> callsMap = null;
	private ArrayList<Call> callsList = null;
	private ParserProgressListener progressListener = null;
	private CallManager callManager = null;

	/**
	 * Creates a new {@link CallParser} object. The list of {@link RawTrace traces} to be analyzed are passed as parameter. In addition, it needs a
	 * {@link ParserProgressListener} in order to publish the progress of the parsing.
	 * @param rawTraceLines the traces to be analyzed
	 * @param progressListener a listener to publish the parsing progress
	 */
	public CallParser(List<RawTrace> rawTraceLines, ParserProgressListener progressListener) {
		this.rawTraceLines = rawTraceLines;
		this.progressListener = progressListener;
		callsMap = new HashMap<Long, Call>();
		callsList = new ArrayList<Call>();
		callManager = new CallManager();
	}

	/**
	 * Tries to parse all the {@link RawTrace traces} and create the relevant {@link Call calls}.
	 * @return true if parse was successful, false otherwise.
	 */
	public boolean parse() {
		boolean result = true;
		int traceSize = rawTraceLines.size();
		int completedTraces = 0;
		int readForProgress = TRACES_BEFORE_PROGRESS;
		String lastTraceTime = null;
		// Go through all trace lines
		for (RawTrace trace: rawTraceLines) {
			completedTraces++;
			try {
				if (isMEBStart(trace)) {
					// MEB has (re)started, callIDs start again from the beginning
					handleMEBStart(trace, lastTraceTime);
				} else if (callsMap.containsKey(Long.valueOf(trace.getCallID()))) {
					// We have a callID and there is already a known call for it
					// Call Manager will handle the trace for the found call
					callManager.handleTrace(trace, callsMap.get(Long.valueOf(trace.getCallID())));
				} else {
					// Call Manager will check for which call this trace is for
					Call call = callManager.checkCallForTrace(trace);
					// Returns != null only if this is a trace indicating a new call
					if (call != null) {
						// Add the new call in the map and list of calls
						callsMap.put(call.getCallID(), call);
						callsList.add(call);
					}
				}
			} catch (RuntimeException e) {
				// A trace with unexpected content should not stop the analysis of the rest of the calls
				skipTrace(trace, e);
			}
			lastTraceTime = trace.getTime();
			// Check if it is time to update the progress
			if (readForProgress-- == 0) {
				readForProgress = TRACES_BEFORE_PROGRESS;
				progressListener.progress((int)((completedTraces * 100) / traceSize));
			}
		}
		if (skippedTraces > 0)
			GUIStatic.printLog("Skipped " + skippedTraces + " trace(s) that could not be analyzed", StyledTextPane.WARNING);
		int ambiguousCalls = 0;
		for (Call call: callsList) {
			if (call.isAmbiguous())
				ambiguousCalls++;
		}
		if (ambiguousCalls > 0)
			GUIStatic.printLog(ambiguousCalls + " call(s) may contain traces of another call, since several calls were waiting " +
					"for the same trace at the same time (see the Ambiguous entry in the call popup)", StyledTextPane.WARNING);
		return result;
	}

	/**
	 * Checks if the trace is the first trace written by MEB when it starts.
	 * @param trace the trace to check
	 * @return true if the trace indicates that MEB has (re)started, false otherwise
	 */
	private boolean isMEBStart(RawTrace trace) {
		return (Component.getComponent(trace.getComponent()) == Component.CPU) &&
				trace.getMessage().toString().startsWith(MEB_START_MARK);
	}

	/**
	 * Handles a (re)start of MEB. After a restart MEB gives callIDs again from the beginning, so the known callIDs
	 * are forgotten in order not to mix the new calls with the calls before the restart. Calls that had not ended
	 * before the restart are marked with a warning, their end time is left unknown.
	 * @param trace the trace indicating the MEB start
	 * @param lastTraceTime the time of the last trace before the MEB start, null if there is none
	 */
	private void handleMEBStart(RawTrace trace, String lastTraceTime) {
		int interruptedCalls = 0;
		// Only the calls since the previous MEB start are in the map
		for (Call call: callsMap.values()) {
			if (call.getEndDate().length() == 0) {
				interruptedCalls++;
				String warning = "Interrupted by MEB restart at " + trace.getTime();
				if (lastTraceTime != null)
					warning += " (last trace before it at " + lastTraceTime + ")";
				// Keep any existing warning
				if (call.isWarningFound() && (call.getWarningString().length() > 0))
					warning += "; " + call.getWarningString();
				call.setWarningFound(true);
				call.setWarningString(warning);
			}
		}
		callsMap.clear();
		// Forget also the calls that are still waiting for their callID
		callManager.init();
		String message = "MEB start at " + trace.getTime() + " (" + trace.getMessage().toString().trim() + ")";
		if (interruptedCalls > 0) {
			GUIStatic.printLog(message + ", " + interruptedCalls + " call(s) active before it are marked as interrupted", StyledTextPane.WARNING);
		} else {
			GUIStatic.printLog(message, StyledTextPane.MESSAGE);
		}
	}

	/**
	 * Skips a trace that could not be analyzed. The first {@link #MAX_REPORTED_SKIPPED_TRACES} skipped traces are
	 * reported in the log, the rest are only counted.
	 * @param trace the trace to be skipped
	 * @param e the exception thrown while analyzing the trace
	 */
	private void skipTrace(RawTrace trace, RuntimeException e) {
		skippedTraces++;
		if (skippedTraces <= MAX_REPORTED_SKIPPED_TRACES) {
			String text = trace.getTrace().replace('\n', ' ');
			if (text.length() > MAX_REPORTED_TRACE_LENGTH)
				text = text.substring(0, MAX_REPORTED_TRACE_LENGTH) + "...";
			GUIStatic.printLog("Could not analyze trace (" + e + "): " + text, StyledTextPane.WARNING);
			e.printStackTrace();
		}
	}

	/**
	 * Returns the list of found calls.
	 * @return the list of found calls.
	 */
	public ArrayList<Call> getCalls() {
		return callsList;
	}
}
