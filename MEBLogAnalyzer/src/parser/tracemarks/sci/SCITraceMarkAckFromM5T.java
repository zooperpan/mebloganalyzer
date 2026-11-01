package parser.tracemarks.sci;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.m5t.M5TMessageSCI;
import parser.RawTrace;
import parser.call.CallAnalysisState;

/**
 * The <code>SCITraceMarkAckFromM5T</code> class is an abstract {@link SCITraceMarkMessageFromM5T} that analyzes 
 * traces created by the SCI component and is responsible for finding ACK messages from M5T to SCI.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCITraceMarkAckFromM5T extends SCITraceMarkMessageFromM5T {

	/**
	 * Creates a new {@link SCITraceMarkAckFromM5T} object.
	 */
	public SCITraceMarkAckFromM5T() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"EvAcknowledged(): Incoming Ack"</code> string.
	 * Responsible also to find the <code>Call</code> object that this trace belongs to (does not
	 * utilize the {@link SCITraceMarkMessageFromM5T#markFound(ArrayList, RawTrace) markFound()}
	 * of the super-class).
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		// Expecting to contain ACK message
		if (trace.getMessage().toString().contains("EvAcknowledged(): Incoming Ack")) {
			// Find call for trace (return false is call is not found)
			String message = trace.getMessage().toString();
			String helpString = message.substring(message.indexOf("EvAcknowledged(): Incoming Ack"));
			helpString = helpString.replace("EvAcknowledged(): Incoming Ack", "");
			String m5tCallID = "";
			if (helpString.startsWith(" {")) {
				m5tCallID = helpString.substring(3, helpString.indexOf('}'));
			} else if (helpString.startsWith(" ")) {
				m5tCallID = helpString.substring(1, helpString.indexOf(','));
			} 
			for (Call call: calls) {
				if ((call.getM5TCallID() != null) && call.getM5TCallID().equals(m5tCallID)) {
					setCall(call);
					return true;
				}
			}
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
		// Call has already been found by the parent in markFound() function, check its state
		switch (getCall().getCallAnalysisState()) {
		case SEND_INITIAL_200OK:
			// Add BaseTrace in call for ACK
			getCall().addBaseTrace(new M5TMessageSCI(trace.getTime(), "ACK", trace.getTrace()));
			// Set next state in call
			getCall().setCallAnalysisState(CallAnalysisState.ACKNOWLEDGED);
			result = getCall();
			break;
		case SEND_FINAL_200OK:
		case CALL_INITIATED:
			// Add BaseTrace in call for ACK
			getCall().addBaseTrace(new M5TMessageSCI(trace.getTime(), "ACK", trace.getTrace()));
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
