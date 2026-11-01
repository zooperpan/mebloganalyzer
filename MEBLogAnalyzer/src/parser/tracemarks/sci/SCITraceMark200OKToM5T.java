package parser.tracemarks.sci;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.m5t.M5TMessageSCI;
import meb.trace.sci.SCIMessageM5T;
import parser.RawTrace;
import parser.call.CallAnalysisState;

/**
 * The <code>SCITraceMark200OKToM5T</code> class is an abstract {@link SCITraceMarkMessageToM5T} that analyzes 
 * traces created by the SCI component and is responsible for finding 200 OK messages from SCI to M5T.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCITraceMark200OKToM5T extends SCITraceMarkMessageToM5T {

	/**
	 * Creates a new {@link SCITraceMark200OKToM5T} object.
	 */
	public SCITraceMark200OKToM5T() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"EvSuccess(200)"</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		if (super.markFound(calls, trace)) {
			if (getCall() != null) {
				// Expecting to contain 200OK message
				return trace.getMessage().toString().contains("EvSuccess(200)");
			}
		}
		return false;
	}

	/**
	 * Depending on the {@link CallAnalysisState pre-state} of the call, adds a {@link SCIMessageM5T}
	 * message in the graph and sets the next pre-state for the call.
	 * 
	 * @apiNote Due to an error in the MEB code, 200 OK messages from M5T to SCI are printed in the
	 * wrong direction. In some pre-states we fix this here by adding a {@link M5TMessageSCI}
	 * message. Should be fixed in MEB traces in the future. 
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		Call result = null;
		// Call has already been found by the parent in markFound() function, check its state
		switch (getCall().getCallAnalysisState()) {
		case M5T_CALL_ID_FOUND:
			// Add BaseTrace in call for 200OK
			getCall().addBaseTrace(new SCIMessageM5T(trace.getTime(), "200 OK", trace.getTrace()));
			// Set next state in call
			getCall().setCallAnalysisState(CallAnalysisState.SEND_INITIAL_200OK);
			result = getCall();
			break;
		case DUMPING_OUTGOING_SDP:
			// Add BaseTrace in call for 200OK
			getCall().addBaseTrace(new SCIMessageM5T(trace.getTime(), "200 OK", trace.getTrace()));
			// Set next state in call
			getCall().setCallAnalysisState(CallAnalysisState.SEND_FINAL_200OK);
			result = getCall();
			break;
		case OUT_SEND_INVITE:
			// Add BaseTrace in call for 200OK
			getCall().addBaseTrace(new M5TMessageSCI(trace.getTime(), "200 OK", trace.getTrace()));
			// Set next state in call
			getCall().setCallAnalysisState(CallAnalysisState.OUT_RECEIVED_INITIAL_200OK);
			result = getCall();
			break;
		case OUT_MWI_SEND_INVITE:
			// Add BaseTrace in call for 200OK
			getCall().addBaseTrace(new M5TMessageSCI(trace.getTime(), "200 OK", trace.getTrace()));
			// Set next state in call
			getCall().setCallAnalysisState(CallAnalysisState.CALL_INITIATED);
			result = getCall();
			break;
		case OUT_SEND_REINVITE:
			// Add BaseTrace in call for 200OK
			getCall().addBaseTrace(new M5TMessageSCI(trace.getTime(), "200 OK", trace.getTrace()));
			// Set next state in call
			getCall().setCallAnalysisState(CallAnalysisState.OUT_RECEIVED_FINAL_200OK);
			result = getCall();
			break;
		case CALL_INITIATED:
			// Add BaseTrace in call for 200OK
			getCall().addBaseTrace(new SCIMessageM5T(trace.getTime(), "200 OK", trace.getTrace()));
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