package parser.tracemarks.sci;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.sci.SCIMessageM5T;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>SCITraceMarkByeToM5T</code> class is an abstract {@link TraceMarkAction} that analyzes 
 * traces created by the SCI component and is responsible for finding BYE messages from SCI to M5T.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCITraceMarkByeToM5T extends TraceMarkAction {

	private Call call = null;

	/**
	 * Creates a new {@link SCITraceMarkByeToM5T} object.
	 */
	public SCITraceMarkByeToM5T() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"eMsg_SendBye: About to send BYE{"</code> string. 
	 * It tries to find and set the <code>Call</code> object that this trace belongs to.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		this.call = null;
		if (message.contains("eMsg_SendBye: About to send BYE{")) {
			int startIndex = message.indexOf('{');
			String helpString = message.substring(startIndex + 1);
			String callIDStr = helpString.substring(0, helpString.indexOf('}'));
			long callID = Long.parseLong(callIDStr, 16);
			for (Call call: calls) {
				if (call.getCallID() == callID) {
					this.call = call;
					break;
				}
			}
			if (this.call != null)
				return true;
		}
		return false;
	}

	/**
	 * Adds a {@link SCIMessageM5T} message in the graph.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		if (call != null) {
			// Add BaseTrace in call for Bye message
			call.addBaseTrace(new SCIMessageM5T(trace.getTime(), "Bye", trace.getTrace()));
			return call;
		}
		return null;
	}

	/**
	 * Not used by this trace mark action.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {}
}