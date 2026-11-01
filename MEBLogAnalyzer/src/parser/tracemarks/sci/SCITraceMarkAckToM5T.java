package parser.tracemarks.sci;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.sci.SCIMessageM5T;
import parser.RawTrace;
import parser.call.CallAnalysisState;

/**
 * The <code>SCITraceMarkAckToM5T</code> class is an abstract {@link SCITraceMarkMessageToM5T} that analyzes 
 * traces created by the SCI component and is responsible for finding ACK messages from SCI to M5T.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCITraceMarkAckToM5T extends SCITraceMarkMessageToM5T {

	/**
	 * Creates a new {@link SCITraceMarkAckToM5T} object.
	 */
	public SCITraceMarkAckToM5T() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"} SendAck():"</code> and 
	 * <code>"pSessionSvc->Ack()"</code> strings.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		if (super.markFound(calls, trace)) {
			// Expecting to contain ACK message
			if (trace.getMessage().toString().contains("} SendAck():") &&
					trace.getMessage().toString().contains("pSessionSvc->Ack()")) {
				return true;
			}
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
		// Call has already been found by the parent in markFound() function, check its state
		switch (getCall().getCallAnalysisState()) {
		case OUT_RECEIVED_FINAL_200OK:
			// Add BaseTrace in call for ACK
			getCall().addBaseTrace(new SCIMessageM5T(trace.getTime(), "ACK", trace.getTrace()));
			getCall().setCallAnalysisState(CallAnalysisState.CALL_INITIATED);
			result = getCall();
			break;
		default:
			getCall().addBaseTrace(new SCIMessageM5T(trace.getTime(), "ACK", trace.getTrace()));
			result = getCall();
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