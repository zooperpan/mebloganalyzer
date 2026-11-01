package parser.tracemarks.sci;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.sci.SCIInfo;
import parser.RawTrace;
import parser.call.CallAnalysisState;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>SCITraceMarkFindOutgoingSDP</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the SCI component and is responsible for finding the outgoing SDP of the call.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCITraceMarkFindOutgoingSDP extends TraceMarkAction {

	private Call call = null;
	private boolean incoming = true;

	/**
	 * Creates a new {@link SCITraceMarkFindOutgoingSDP} object.
	 */
	public SCITraceMarkFindOutgoingSDP() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"} DumpSDP(): Dumping capabilities About to send "</code> 
	 * or <code>"Dumping capabilities About to send 200 OK: "</code> strings. It also tries to find to 
	 * which call this trace belongs to.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		this.call = null;
		// Set always incoming, do not care if we find the callID
		incoming = true;
		if (message.contains("} DumpSDP(): Dumping capabilities About to send ")) {
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
		} else if (message.contains("Dumping capabilities About to send 200 OK: ")) {
			return true;
		} else if (message.contains("] DumpSDP(): Dumping capabilities About to send (Re)Invite")) {
			// This is probably an outgoing call
			incoming = false;
			return true;
		}
		return false;
	}

	/**
	 * Depending on the direction and {@link CallAnalysisState pre-state} of the call, it finds the outgoing
	 * SDP, sets it in the <code>Call</code> object and adds a {@link SCIInfo} information in the graph.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		if (call != null) {
			String helpString = trace.getMessage().toString().substring(trace.getMessage().toString().indexOf("DumpSDP(): Dumping capabilities About to send "));
			int startIndex = helpString.indexOf('{');
			String sdp = helpString.substring(startIndex);
			// Add BaseTrace in call for the outgoing SDP
			call.addBaseTrace(new SCIInfo(trace.getTime(), "Outgoing SDP:\n" + sdp, trace.getTrace()));
			// Set found outgoing SDP and next state in call
			call.setOutSDP(sdp);
			// In case of outgoing call in state OUT_CALL_ID_CREATED set new call state
			if (call.getCallAnalysisState() == CallAnalysisState.OUT_CALL_ID_CREATED) {
				call.setCallAnalysisState(CallAnalysisState.DUMPING_OUTGOING_SDP);
			}
			return call;
		} else {
			checkAmbiguousCalls(calls, trace, incoming ? CallAnalysisState.DUMPING_INCOMING_SDP : CallAnalysisState.OUT_CALL_ID_CREATED);
			for (Call call: calls) {
				// Find the call that is in DUMPING_INCOMING_SDP state
				if ((call.getCallAnalysisState() == CallAnalysisState.DUMPING_INCOMING_SDP) && incoming) {
					// Hopefully this is the call for this message, find the outgoing SDP in the trace
					String helpString = trace.getMessage().toString().substring(trace.getMessage().toString().indexOf("DumpSDP(): Dumping capabilities About to send 200 OK: "));
					String sdp = helpString.replace("DumpSDP(): Dumping capabilities About to send 200 OK: ", "");
					// Add BaseTrace in call for the outgoing SDP
					call.addBaseTrace(new SCIInfo(trace.getTime(), "Outgoing SDP:\n" + sdp, trace.getTrace()));
					// Set found outgoing SDP and next state in call
					call.setOutSDP(sdp);
					call.setCallAnalysisState(CallAnalysisState.DUMPING_OUTGOING_SDP);
					return call;
				} else if ((call.getCallAnalysisState() == CallAnalysisState.OUT_CALL_ID_CREATED) && !incoming) {
					// Hopefully this is the call for this message, find the outgoing SDP in the trace
					String helpString = trace.getMessage().toString().substring(trace.getMessage().toString().indexOf("] DumpSDP(): Dumping capabilities About to send (Re)Invite: "));
					String sdp = helpString.replace("] DumpSDP(): Dumping capabilities About to send (Re)Invite: ", "");
					// Add BaseTrace in call for the outgoing SDP
					call.addBaseTrace(new SCIInfo(trace.getTime(), "Outgoing SDP:\n" + sdp, trace.getTrace()));
					// Set found outgoing SDP and next state in call
					call.setOutSDP(sdp);
					call.setCallAnalysisState(CallAnalysisState.DUMPING_OUTGOING_SDP);
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
