package parser.tracemarks.cpu;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.cpu.CPUState;
import meb.trace.h323.H323MessageCPU;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>CPUTraceMarkMessageFromH323</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the CPU component and is responsible for finding messages from H323 to CPU.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CPUTraceMarkMessageFromH323 extends TraceMarkAction {

	/**
	 * Creates a new {@link CPUTraceMarkMessageFromH323} object.
	 */
	public CPUTraceMarkMessageFromH323() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"_messageType=eH323_CPU_"</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.contains("_messageType=eH323_CPU_")) {
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
	 * Adds a {@link H323MessageCPU} message in the graph. Also adds a {@link CPUState} with the
	 * initial state for the CPU component in case it has not already been set.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		String message = trace.getMessage().toString();
		int startIndex = message.indexOf("_messageType=eH323_CPU_") + "_messageType=eH323_CPU_".length();
		String helpString = message.substring(startIndex);
		if (helpString.contains(",")) {
			message = helpString.substring(0, helpString.indexOf(','));
		} else {
			message = helpString.substring(0, helpString.indexOf('}'));
		}
		if (!call.isFirstCPUMessage()) {
			call.addBaseTrace(new CPUState(trace.getTime(), "NULL", trace.getTrace()));
			call.setFirstCPUMessage(true);
		}
		call.addBaseTrace(new H323MessageCPU(trace.getTime(), furtherAnalysis(call, message, helpString), trace.getTrace()));
	}

	/**
	 * Provides a further analysis of the trace line. It actually checks if this is a "GotDTMF" message and finds
	 * the digit that has been pressed. the found digit will be added in the message string that will be returned.
	 * Also it checks if media have started in case of RequestMediaResponse.
	 * @param call the call that the trace belongs to
	 * @param message the message to be added
	 * @param helpString the string containing part of the trace
	 * @return the resulted string after the further analysis of the trace
	 */
	private String furtherAnalysis(Call call, String message, String helpString) {
		StringBuffer result = new StringBuffer(message);
		if (message.equals("GotDTMF")) {
			String digit = helpString.substring(helpString.indexOf("_cDigit=") + 8, helpString.indexOf("_cDigit=") + 9);
			result.append(" (" + digit + ")");
		} else if (message.equals("RequestMediaResponse")) {
			String mediaResult = helpString.substring(helpString.indexOf("Success=") + 8, helpString.indexOf("Success=") + 13);
			if (mediaResult.equals("false")) {
				call.setErrorFound(true);
				call.setErrorString("No media connection!");
			}
		}
		return result.toString();
	}
}