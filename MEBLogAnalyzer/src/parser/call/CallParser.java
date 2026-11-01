package parser.call;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import meb.entities.Call;
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
	private List<RawTrace> rawTraceLines = null;
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
		// Go through all trace lines
		for (RawTrace trace: rawTraceLines) {
			completedTraces++;
			// Check if we have a callID and there is already a known call for it
			if (callsMap.containsKey(Long.valueOf(trace.getCallID()))) {
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
			// Check if it is time to update the progress
			if (readForProgress-- == 0) {
				readForProgress = TRACES_BEFORE_PROGRESS;
				progressListener.progress((int)((completedTraces * 100) / traceSize));
			}
		}
		return result;
	}

	/**
	 * Returns the list of found calls.
	 * @return the list of found calls.
	 */
	public ArrayList<Call> getCalls() {
		return callsList;
	}
}
