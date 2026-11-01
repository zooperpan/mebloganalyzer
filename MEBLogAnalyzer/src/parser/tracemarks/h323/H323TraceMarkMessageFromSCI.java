package parser.tracemarks.h323;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.sci.SCIMessageH323;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>H323TraceMarkMessageFromSCI</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the H323 component and is responsible for finding messages from SCI to H323.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class H323TraceMarkMessageFromSCI extends TraceMarkAction {

	/**
	 * Creates a new {@link H323TraceMarkMessageFromSCI} object.
	 */
	public H323TraceMarkMessageFromSCI() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"ReceivedTunnelledMessage, size "</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.startsWith("ReceivedTunnelledMessage, size ")) {
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
	 * Adds a {@link SCIMessageH323} message in the graph.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		call.addBaseTrace(new SCIMessageH323(trace.getTime(), "TunnelledMessage", trace.getTrace()));
	}
}
