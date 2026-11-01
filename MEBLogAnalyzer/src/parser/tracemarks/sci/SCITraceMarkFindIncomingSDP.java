package parser.tracemarks.sci;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.m5t.M5TMessageSCI;
import meb.trace.sci.SCIInfo;
import parser.RawTrace;
import parser.call.CallAnalysisState;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>SCITraceMarkFindIncomingSDP</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the SCI component and is responsible for finding the incoming SDP of the call.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCITraceMarkFindIncomingSDP extends TraceMarkAction {

	private Call call = null;

	/**
	 * Creates a new {@link SCITraceMarkFindIncomingSDP} object.
	 */
	public SCITraceMarkFindIncomingSDP() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"} DumpSDP(): Dumping capabilities Found SDP in incoming "</code> 
	 * string. It also tries to find to which call this trace belongs to.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		this.call = null;
		if (message.contains("} DumpSDP(): Dumping capabilities Found SDP in incoming ")) {
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
		} else if (message.contains("Dumping capabilities Found SDP in incoming (Re)Invite")) {
			return true;
		}
		return false;
	}

	/**
	 * Depending on the direction and {@link CallAnalysisState pre-state} of the call, it finds the incoming
	 * SDP, sets it in the <code>Call</code> object and adds a {@link SCIInfo} information in the graph.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		if (call != null) {
			String helpString = trace.getMessage().toString().substring(trace.getMessage().toString().indexOf("DumpSDP(): Dumping capabilities Found SDP in incoming"));
			helpString = helpString.replace("DumpSDP(): Dumping capabilities Found SDP in incoming", "");
			// Special case, no Ack message is printed in case of empty re-INVITE. Add it manually
			if (helpString.contains("Ack:")) {
				call.addBaseTrace(new M5TMessageSCI(trace.getTime(), "ACK", trace.getTrace()));
			}
			int startIndex = helpString.indexOf('{');
			String sdp = helpString.substring(startIndex);
			// Add BaseTrace in call for the incoming SDP
			call.addBaseTrace(new SCIInfo(trace.getTime(), "Incoming SDP:\n" + sdp, trace.getTrace()));
			// Set found incoming SDP in call
			call.setInSDP(sdp);
			return call;
		} else {
			// Find the call that is in SEND_100_TRYING state
			for (Call call: calls) {
				try {
					if (call.getCallAnalysisState() == CallAnalysisState.SEND_100_TRYING) {
						// Hopefully this is the call for this message, find the incoming SDP in the trace
						String helpString = trace.getMessage().toString().substring(trace.getMessage().toString().indexOf("DumpSDP(): Dumping capabilities Found SDP in incoming (Re)Invite:"));
						String sdp = helpString.replace("DumpSDP(): Dumping capabilities Found SDP in incoming (Re)Invite:", "");
						// Add BaseTrace in call for the incoming SDP
						call.addBaseTrace(new SCIInfo(trace.getTime(), "Incoming SDP:\n" + sdp, trace.getTrace()));
						// Set found incoming SDP and next state in call
						call.setInSDP(sdp);
						call.setCallAnalysisState(CallAnalysisState.DUMPING_INCOMING_SDP);
						return call;
					}
				} catch (StringIndexOutOfBoundsException e) {
					System.out.println(trace.getMessage().toString());
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
