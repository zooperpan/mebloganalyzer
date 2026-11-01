package parser.tracemarks.cornet;

import java.awt.Color;
import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.cornet.CornetMessageCPU;
import meb.trace.cornet.CornetState;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>CornetTraceMarkMessageToCPU</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the CorNet component and is responsible for finding messages from CorNet to CPU.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CornetTraceMarkMessageToCPU extends TraceMarkAction {

	/**
	 * Creates a new {@link CornetTraceMarkMessageToCPU} object.
	 */
	public CornetTraceMarkMessageToCPU() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"CcInterface::sendNqInterfaceMessageToCpu()"</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.contains("CcInterface::sendNqInterfaceMessageToCpu()")) {
			return true;
		}
		return false;
	}

	/**
	 * Not used by this trace mark action.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		return null;
	}

	/**
	 * Adds a {@link CornetMessageCPU} message in the graph.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		//_methodType=
		String message = trace.getMessage().toString();
		int startIndex = message.indexOf("_methodType=") + "_methodType=".length();
		String helpString = message.substring(startIndex);
		int endIndex = helpString.indexOf("(");
		message = helpString.substring(0, endIndex).replace("eNQU_CPU_", "");
		
		Color color = Color.BLACK;
		if (message.equals("ASYNC_RESULT")) {
			startIndex = helpString.indexOf("_asyncResult=") + "_asyncResult=".length();
			String asyncResult = helpString.substring(startIndex);
			asyncResult = asyncResult.substring(0, asyncResult.indexOf('('));
			if (!asyncResult.equals("eNQINTERFACEASYNCRESULT_OK") && !asyncResult.equals("eNQINTERFACEASYNCRESULT_FAILED_UNKNOWN_CALL_ID")) {
				color = Color.RED;
				if (!call.isWarningFound()) {
					call.setWarningFound(true);
					call.setWarningString("ASYNC_RESULT failed with " + asyncResult);
				}
			}
		}
		
		call.addBaseTrace(new CornetMessageCPU(trace.getTime(), furtherAnalysis(message, helpString, call, trace), trace.getTrace()
				, color));
	}

	/**
	 * Provides a further analysis of the trace line. It actually checks if a state change has occurred on
	 * the CorNet component in order to add {@link CornetState} with the new state information in the graph.
	 * @param message the message to be added
	 * @param helpString the string containing part of the trace
	 * @param call the call that the trace belongs to
	 * @param trace the trace line to be analyzed
	 * @return the resulted string after the further analysis of the trace
	 */
	private String furtherAnalysis(String message, String helpString, Call call, RawTrace trace) {
		String result = message;
		if (message.equals("EXCEPTIONAL_STATE_CHANGE_INDICATION")) {
			int startIndex = helpString.indexOf("_newCallState=") + "_newCallState=".length();
			String state = helpString.substring(startIndex);
			state = state.substring(0, state.indexOf('('));
			if (!call.getCornetCallState().equals(state)) {
				call.addBaseTrace(new CornetState(trace.getTime(), state.replace("eNQCALLSTATEVALUE_", ""), trace.getTrace()));
			}
		}
		return result;
	}
}
