package parser.tracemarks.sci;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.sci.SCIMessageM5T;
import parser.RawTrace;
import parser.call.CallAnalysisState;

/**
 * The <code>SCITraceMarkInviteToM5T</code> class is an abstract {@link SCITraceMarkMessageToM5T} that analyzes 
 * traces created by the SCI component and is responsible for finding INVITE messages from SCI to M5T.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCITraceMarkInviteToM5T extends SCITraceMarkMessageToM5T {

	private boolean haveCallID = false;
	/**
	 * Creates a new {@link SCITraceMarkInviteToM5T} object.
	 */
	public SCITraceMarkInviteToM5T() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"} SendInvite():"</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		// Expecting to contain SendInvite message
		if (trace.getMessage().toString().contains("} SendInvite():")) {
			// Find call for trace (return false is call is not found)
			haveCallID = true;
			return super.markFound(calls, trace);
		}
		if (trace.getMessage().toString().contains("] SendInvite(): Sending Invite <<{")) {
			// Clear Call object since it may have been saved from a previous trace analysis
			setCall(null);
			haveCallID = false;
			return true;
		}
		return false;
	}

	/**
	 * Depending on the {@link CallAnalysisState pre-state} of the call, adds a {@link SCIMessageM5T}
	 * message in the graph and sets the next pre-state for the call.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		Call result = null;
		if (haveCallID) {
			// Call has already been found and checked by the parent in markFound() function, check its state
			switch (getCall().getCallAnalysisState()) {
			case M5T_CALL_ID_FOUND:
				// Make sure this is an outgoing call
				if (getCall().getDirection() == Call.OUTGOING) {
					// Different next pre-state in MWI calls
					if (getCall().isMWI()) {
						// Add BaseTrace in call for INVITE
						getCall().addBaseTrace(new SCIMessageM5T(trace.getTime(), "INVITE", trace.getTrace()));
						// Set next state in call
						getCall().setCallAnalysisState(CallAnalysisState.OUT_MWI_SEND_INVITE);
						result = getCall();
					} else {
						// Add BaseTrace in call for INVITE
						getCall().addBaseTrace(new SCIMessageM5T(trace.getTime(), "INVITE", trace.getTrace()));
						// Set next state in call
						getCall().setCallAnalysisState(CallAnalysisState.OUT_SEND_INVITE);
						result = getCall();
					}
				}
				break;
			case OUT_RECEIVED_INITIAL_200OK:
				// Add BaseTrace in call for re-INVITE
				getCall().addBaseTrace(new SCIMessageM5T(trace.getTime(), "re-INVITE", trace.getTrace()));
				// Set next state in call
				getCall().setCallAnalysisState(CallAnalysisState.OUT_SEND_REINVITE);
				result = getCall();
				break;
			case CALL_INITIATED:
				// Add BaseTrace in call for re-INVITE
				getCall().addBaseTrace(new SCIMessageM5T(trace.getTime(), "re-INVITE", trace.getTrace()));
				// Set next state in call
				getCall().setCallAnalysisState(CallAnalysisState.OUT_SEND_REINVITE);
				result = getCall();
				break;
			default:
				break;
			}
		} else {
			// Found message that does not already have a callID, find one in appropriate state
			String message = trace.getMessage().toString();
			// Find the M5T callID
			int startIndex = message.indexOf("<<{") + "<<{".length();
			String helpString = message.substring(startIndex);
			String m5tCallID = helpString.substring(0, helpString.indexOf(','));
			// Find the call with the specific M5T callID
			for (Call call: calls) {
				if (m5tCallID.equals(call.getM5TCallID())) {
					call.addBaseTrace(new SCIMessageM5T(trace.getTime(), "INVITE", trace.getTrace()));
					// Set next state in call
					call.setCallAnalysisState(CallAnalysisState.OUT_SEND_INVITE);
					result = call;
					break;
				}
			}
		}
		
		return result;
	}

	/**
	 * Not used by this trace mark action.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {}
}