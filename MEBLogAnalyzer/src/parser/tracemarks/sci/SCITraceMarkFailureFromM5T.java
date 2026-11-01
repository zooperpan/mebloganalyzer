package parser.tracemarks.sci;

import java.awt.Color;
import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.m5t.M5TMessageSCI;
import parser.RawTrace;

/**
 * The <code>SCITraceMarkFailureFromM5T</code> class is an abstract {@link SCITraceMarkMessageFromM5T} that analyzes 
 * traces created by the SCI component and is responsible for finding FAILURE messages from M5T to SCI.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCITraceMarkFailureFromM5T extends SCITraceMarkMessageFromM5T {

	/**
	 * Creates a new {@link SCITraceMarkFailureFromM5T} object.
	 */
	public SCITraceMarkFailureFromM5T() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"EvFailure"</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		if (super.markFound(calls, trace)) {
			// Expecting to contain EvFailure message
			if (trace.getMessage().toString().contains("EvFailure")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds a {@link M5TMessageSCI} message in the graph.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		Call result = getCall();
		// Call has already been found by the parent in markFound() function
		result.addBaseTrace(new M5TMessageSCI(trace.getTime(), "Failure", trace.getTrace(), Color.RED));
		// 487 errors probably cause problems in the call
		if (trace.getTrace().contains("EvFailure  (487)")) {
			result.setWarningFound(true);
			result.setWarningString("Received 487 Failure from system!");
		}
		return result;
	}

	/**
	 * Not used by this trace mark action.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {}
}