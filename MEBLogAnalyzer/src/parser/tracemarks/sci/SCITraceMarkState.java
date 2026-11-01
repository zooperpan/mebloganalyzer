package parser.tracemarks.sci;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.sci.SCIState;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>SCITraceMarkState</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the SCI component and is responsible for finding changes in the SCI state of the call.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCITraceMarkState extends TraceMarkAction {

	/**
	 * Creates a new {@link SCITraceMarkState} object.
	 */
	public SCITraceMarkState() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"VISU-SI|SCI\\n"</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.contains("VISU-SI|SCI\\n")) {
			return true;
		}
		return false;
	}

	/**
	 * Finds the SCI state and <code>Call</code> object that the trace belongs to and
	 * adds a {@link SCIState} state information in the graph.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		int startIndex = message.indexOf("VISU-SI|SCI\\n") + "VISU-SI|SCI\\n".length();
		String helpString = message.substring(startIndex);
		if ((helpString.length() > 0) && (helpString.charAt(0) != '|')) {
			String callIDStr = helpString.substring(0, helpString.indexOf('|'));
			String state = helpString.substring(helpString.indexOf('|') + 1);
			long callID = Long.parseLong(callIDStr, 16);
			for (Call call: calls) {
				if (call.getCallID() == callID) {
					call.addBaseTrace(new SCIState(trace.getTime(), state, trace.getTrace()));
					if (state.equals("Call Established") && call.getEstablishedDate().length() == 0) {
						call.setEstablishedDate(trace.getTime());
					}
					return call;
				}
			}
		}
		return null;
	}

	/**
	 * Not used by this trace mark action.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {}
}
