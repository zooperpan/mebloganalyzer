package parser.tracemarks.audio;

import java.util.ArrayList;

import meb.entities.Call;
import parser.RawTrace;
import parser.call.CallAnalysisState;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>SesAudioTraceMarkFindCallID</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the AUDIO component and is responsible for finding the callID of an incoming call.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SesAudioTraceMarkFindCallID extends TraceMarkAction {

	/**
	 * Creates a new {@link SesAudioTraceMarkFindCallID} object.
	 */
	public SesAudioTraceMarkFindCallID() {
		// This trace mark action can find the callID of the call
		super(true);
	}

	/**
	 * Checks if the trace contains the <code>"Created NetworkSessionAudio."</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.contains("Created NetworkSessionAudio.")) {
			return true;
		}
		return false;
	}

	/**
	 * Tries to find for which call this trace line belongs to. If found, it will set the callID
	 * of the call and return the <code>Call</code> object in order to be added in the list of found calls.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		// Find the call that is in SEND_FINAL_200OK state
		for (Call call: calls) {
			if (call.getCallAnalysisState() == CallAnalysisState.SEND_FINAL_200OK) {
				// Hopefully this is the call for this message, get the callID
				long callID = trace.getCallID();
				// Set found callID and next state in call
				call.setCallID(callID);
				call.setCallAnalysisState(CallAnalysisState.CALL_INITIATED);
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
