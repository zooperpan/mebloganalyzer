package parser.tracemarks.cpu;

import java.util.ArrayList;

import meb.entities.Call;
import parser.RawTrace;
import parser.call.CallAnalysisState;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>CPUTraceMarkGenerateCall</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the CPU component and is responsible for finding the callID of an outgoing call.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CPUTraceMarkGenerateCall extends TraceMarkAction {

	/**
	 * Creates a new {@link CPUTraceMarkGenerateCall} object.
	 */
	public CPUTraceMarkGenerateCall() {
		// This trace mark action can find the callID of the call
		super(true);
	}

	/**
	 * Checks if the trace contains the <code>"Generated new callId."</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.equals("Generated new callId.")) {
			return true;
		}
		return false;
	}

	/**
	 * Finds the call that this trace belongs to and sets the callID in the relevant 
	 * <code>Call</code> object. The returned call should be added in the list of found calls.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		// Go through all call
		for (Call call: calls) {
			// Find the call that is in FAX_CALL_RECEIVED state
			if (call.getCallAnalysisState() == CallAnalysisState.FAX_CALL_RECEIVED) {
				// Hopefully this is the call for this message, get the callID
				long callID = trace.getCallID();
				// Set found callID and next state in call
				call.setCallID(callID);
				call.setCallAnalysisState(CallAnalysisState.OUT_CALL_ID_CREATED);
				return call;
			} else if (call.getCallAnalysisState() == CallAnalysisState.MWI_CALL_RECEIVED) {
				// Hopefully this is the call for this message, get the callID
				long callID = trace.getCallID();
				// Set found callID and next state in call
				call.setCallID(callID);
				call.setCallAnalysisState(CallAnalysisState.OUT_MWI_CALL_ID_CREATED);
				return call;
			} else if (call.getCallAnalysisState() == CallAnalysisState.MAKE_CALL_RECEIVED) {
				// Hopefully this is the call for this message, get the callID
				long callID = trace.getCallID();
				// Set found callID and next state in call
				call.setCallID(callID);
				call.setCallAnalysisState(CallAnalysisState.OUT_CALL_ID_CREATED);
				return call;
			} else if (call.getCallAnalysisState() == CallAnalysisState.CONSULT_CALL_RECEIVED) {
				// Hopefully this is the call for this message, get the callID
				long callID = trace.getCallID();
				// Set found callID and next state in call
				call.setCallID(callID);
				call.setCallAnalysisState(CallAnalysisState.OUT_CALL_ID_CREATED);
				return call;
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