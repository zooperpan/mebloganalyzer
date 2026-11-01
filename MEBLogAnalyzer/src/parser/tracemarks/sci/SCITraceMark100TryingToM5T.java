package parser.tracemarks.sci;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.sci.SCIMessageM5T;
import parser.RawTrace;
import parser.call.CallAnalysisState;

/**
 * The <code>SCITraceMark100TryingToM5T</code> class is an abstract {@link SCITraceMarkMessageToM5T} that analyzes 
 * traces created by the SCI component and is responsible for finding 100 Trying messages from SCI to M5T.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCITraceMark100TryingToM5T extends SCITraceMarkMessageToM5T {

	/**
	 * Creates a new {@link SCITraceMark100TryingToM5T} object.
	 */
	public SCITraceMark100TryingToM5T() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"EvProgress(100)"</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		// Expecting to contain 100 Trying message
		if (trace.getMessage().toString().contains("EvProgress(100)")) {
			// Find call for trace (return false is call is not found)
			if (super.markFound(calls, trace) && getCall() != null)
				return true;
		}
		return false;
	}

	/**
	 * Depending on the {@link CallAnalysisState pre-state} of the call, adds a {@link SCIMessageM5T}
	 * message in the graph and sets the next pre-state for the call.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		Call result = null;
		// Call has already been found and checked by the parent in markFound() function, check its state
		switch (getCall().getCallAnalysisState()) {
		case REINVITED:
			// Add BaseTrace in call for 100 Trying
			getCall().addBaseTrace(new SCIMessageM5T(trace.getTime(), "100 Trying", trace.getTrace()));
			// Set next state in call
			getCall().setCallAnalysisState(CallAnalysisState.SEND_100_TRYING);
			result = getCall();
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * Not used by this trace mark action.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {}
}