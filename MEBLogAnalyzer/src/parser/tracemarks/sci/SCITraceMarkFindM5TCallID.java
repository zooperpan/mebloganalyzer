package parser.tracemarks.sci;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.m5t.M5TInfo;
import parser.RawTrace;
import parser.call.CallAnalysisState;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>SCITraceMarkFindM5TCallID</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the SCI component and is responsible for finding M5T callID of a call.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCITraceMarkFindM5TCallID extends TraceMarkAction {

	private boolean incoming = true;
	private boolean haveCallID = false;

	/**
	 * Creates a new {@link SCITraceMarkFindM5TCallID} object.
	 */
	public SCITraceMarkFindM5TCallID() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"EvInvited(): Found Call-ID '"</code> and 
	 * <code>"' in incoming initial Invite"</code>, or 
	 * <code>"} loopedSendInitialInvite(): Found Call-ID '"</code> and 
	 * <code>"' in outgoing initial Invite"</code> strings.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		haveCallID = false;
		if (message.contains("EvInvited(): Found Call-ID '") && message.endsWith("' in incoming initial Invite")) {
			incoming = true;
			return true;
		} else if (message.contains("} loopedSendInitialInvite(): Found Call-ID '") && message.endsWith("' in outgoing initial Invite")) {
			incoming = false;
			haveCallID = true;
			return true;
		} else if (message.contains("loopedSendInitialInvite(): Found Call-ID '") && message.endsWith("' in outgoing initial Invite")) {
			incoming = false;
			return true;
		}
		return false;
	}

	/**
	 * Depending on the direction and {@link CallAnalysisState pre-state} of the call, it finds the M5T
	 * callID, sets it in the <code>Call</code> object and adds a {@link M5TInfo} information in the graph.
	 * It also sets the next pre-state of the call.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		if (incoming) {
			checkAmbiguousCalls(calls, trace, CallAnalysisState.INVITED);
			// For incoming calls find the call that is in INVITED state
			for (Call call: calls) {
				if ((call.getCallAnalysisState() == CallAnalysisState.INVITED)) {
					// Hopefully this is the call for this message, find the M5T callID in the trace
					String helpString = trace.getMessage().toString().substring(trace.getMessage().toString().indexOf("EvInvited(): Found Call-ID"));
					String callID = helpString.replace("EvInvited(): Found Call-ID '", "").replace("' in incoming initial Invite", "");
					// Set found M5T callID and next state in call
					call.setM5TCallID(callID);
					call.setCallAnalysisState(CallAnalysisState.M5T_CALL_ID_FOUND);
					// Add BaseTrace in call for the M5T callID
					call.addBaseTrace(new M5TInfo(trace.getTime(), callID, trace.getTrace()));
					return call;
				}
			} 
		} else if (haveCallID){
			// For outgoing call, find the callID
			String message = trace.getMessage().toString();
			int startIndex = message.indexOf('{') + 1;
			String callIDStr = message.substring(startIndex, message.indexOf('}'));
			long callID = Long.parseLong(callIDStr, 16);
			// Find the call with the found callID
			for (Call call: calls) {
				if ((call.getCallID() == callID) &&
						((call.getCallAnalysisState() == CallAnalysisState.DUMPING_OUTGOING_SDP) ||
								(call.getCallAnalysisState() == CallAnalysisState.OUT_MWI_CALL_ID_CREATED))) {
					// Find the M5T callID in the trace
					startIndex = message.indexOf("Found Call-ID '") + "Found Call-ID '".length();
					String helpString = message.substring(startIndex);
					String m5tCallID = helpString.substring(0, helpString.indexOf('\''));
					// Set found M5T callID and next state in call
					call.setM5TCallID(m5tCallID);
					call.setCallAnalysisState(CallAnalysisState.M5T_CALL_ID_FOUND);
					// Add BaseTrace in call for the M5T callID
					call.addBaseTrace(new M5TInfo(trace.getTime(), m5tCallID, trace.getTrace()));
					return call;
				}
			}
		} else {
			// We come here if we do not have a callID
			String message = trace.getMessage().toString();
			// Find a call with the appropriate state
			for (Call call: calls) {
				if ((call.getCallAnalysisState() == CallAnalysisState.DUMPING_OUTGOING_SDP) || 
						(call.getCallAnalysisState() == CallAnalysisState.OUT_MWI_CALL_ID_CREATED)) {
					// Find the M5T callID in the trace
					int startIndex = message.indexOf("Found Call-ID '") + "Found Call-ID '".length();
					String helpString = message.substring(startIndex);
					String m5tCallID = helpString.substring(0, helpString.indexOf('\''));
					// Set found M5T callID and next state in call
					call.setM5TCallID(m5tCallID);
					call.setCallAnalysisState(CallAnalysisState.M5T_CALL_ID_FOUND);
					// Add BaseTrace in call for the M5T callID
					call.addBaseTrace(new M5TInfo(trace.getTime(), m5tCallID, trace.getTrace()));
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