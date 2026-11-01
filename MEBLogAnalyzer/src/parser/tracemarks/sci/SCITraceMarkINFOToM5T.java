package parser.tracemarks.sci;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.h323.H323MessageSCI;
import meb.trace.sci.SCIMessageM5T;
import parser.RawTrace;

/**
 * The <code>SCITraceMarkINFOToM5T</code> class is an abstract {@link SCITraceMarkMessageToM5T} that analyzes 
 * traces created by the SCI component and is responsible for finding INFO messages from SCI to M5T.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCITraceMarkINFOToM5T extends SCITraceMarkMessageToM5T {

	/**
	 * Creates a new {@link SCITraceMarkINFOToM5T} object.
	 */
	public SCITraceMarkINFOToM5T() {
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
			return trace.getMessage().toString().contains("INFO");
		}
		return false;
	}

	/**
	 * Adds a {@link SCIMessageM5T} message in the graph. In case the trace also contains the
	 * <code>"loopedSendTunnelledMessage"</code> string, it also adds a {@link H323MessageSCI}
	 * message in the graph.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		Call result = getCall();
		if (trace.getMessage().toString().contains("loopedSendTunnelledMessage")) {
			result.addBaseTrace(new H323MessageSCI(trace.getTime(), "TunnelledMessage", trace.getTrace()));
		}
		result.addBaseTrace(new SCIMessageM5T(trace.getTime(), "INFO", trace.getTrace()));
		
		return result;
	}

	/**
	 * Not used by this trace mark action.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {}
}