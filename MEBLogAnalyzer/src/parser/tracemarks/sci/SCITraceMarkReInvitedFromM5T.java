package parser.tracemarks.sci;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.m5t.M5TMessageSCI;
import parser.RawTrace;
import parser.call.CallAnalysisState;

/**
 * The <code>SCITraceMarkReInvitedFromM5T</code> class is an abstract {@link SCITraceMarkMessageFromM5T} that 
 * analyzes traces created by the SCI component and is responsible for finding re-INVITE messages from M5T to SCI.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCITraceMarkReInvitedFromM5T extends SCITraceMarkMessageFromM5T {

	/**
	 * Creates a new {@link SCITraceMarkReInvitedFromM5T} object.
	 */
	public SCITraceMarkReInvitedFromM5T() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"EvReInvited()"</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		// Find the call first, since string 'EvReInvited()' appears also in non VISU traces
		if (super.markFound(calls, trace)) {
			// Expecting to contain re-INVITE message
			return trace.getMessage().toString().contains("EvReInvited()");
		}
		return false;
	}

	/**
	 * Depending on the {@link CallAnalysisState pre-state} of the call, adds a {@link M5TMessageSCI}
	 * message in the graph and sets the next pre-state for the call.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		Call result = null;
		// Call has already been found and checked by the parent in markFound() function, check its state
		switch (getCall().getCallAnalysisState()) {
		case ACKNOWLEDGED:
			// Add BaseTrace in call for re-INVITE
			getCall().addBaseTrace(new M5TMessageSCI(trace.getTime(), "re-INVITE", trace.getTrace()));
			// Set next state in call
			getCall().setCallAnalysisState(CallAnalysisState.REINVITED);
			result = getCall();
			break;
		case CALL_INITIATED:
			// Add BaseTrace in call for re-INVITE
			getCall().addBaseTrace(new M5TMessageSCI(trace.getTime(), "re-INVITE", trace.getTrace()));
			result = getCall();
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * Not used by this trace mark action.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {}
}
