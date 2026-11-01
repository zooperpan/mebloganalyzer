package parser.tracemarks.sci;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.m5t.M5TMessageSCI;
import parser.RawTrace;

/**
 * The <code>SCITraceMarkINFOFromM5T</code> class is an abstract {@link SCITraceMarkMessageFromM5T} that analyzes 
 * traces created by the SCI component and is responsible for finding INFO messages from M5T to SCI.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCITraceMarkINFOFromM5T extends SCITraceMarkMessageFromM5T {

	/**
	 * Creates a new {@link SCITraceMarkINFOFromM5T} object.
	 */
	public SCITraceMarkINFOFromM5T() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"INFO"</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		if (super.markFound(calls, trace)) {
			// Expecting to contain INFO message
			if (trace.getMessage().toString().contains("INFO")) {
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
		// Call has already been found by the parent in markFound() function, check its state
		result.addBaseTrace(new M5TMessageSCI(trace.getTime(), "INFO", trace.getTrace()));
		return result;
	}

	/**
	 * Not used by this trace mark action.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {}
}