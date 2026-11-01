package parser.tracemarks.h323;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.h323.H323MessageH323;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>H323TraceMarkMessageFromH323</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the H323 component and is responsible for finding messages from H323 to H323.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class H323TraceMarkMessageFromH323 extends TraceMarkAction {

	/**
	 * Creates a new {@link H323TraceMarkMessageFromH323} object.
	 */
	public H323TraceMarkMessageFromH323() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"Found eH323_H323_LoopFunction for"</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.startsWith("Found eH323_H323_LoopFunction for")) {
			return true;
		}
		return false;
	}

	/**
	 * Not used by this trace mark action.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		return null;
	}

	/**
	 * Adds a {@link H323MessageH323} message in the graph.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		String message = trace.getMessage().toString();
		String helpString = message.replace("Found eH323_H323_LoopFunction for '", "");
		message = helpString.substring(0, helpString.indexOf('\''));
		call.addBaseTrace(new H323MessageH323(trace.getTime(), message, trace.getTrace()));
	}
}
