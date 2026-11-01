package parser.tracemarks.cpu;

import java.util.ArrayList;

import meb.entities.Call;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>CPUTraceMarkFindAdditionalCallNumber</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the CPU component and is responsible for finding the additional calling party number 
 * of an incoming call in the trace.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CPUTraceMarkFindAdditionalCallNumber extends TraceMarkAction {

	/**
	 * Creates a new {@link CPUTraceMarkFindAdditionalCallNumber} object.
	 */
	public CPUTraceMarkFindAdditionalCallNumber() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"Found Additional Calling Party Number,"</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.startsWith("Found Additional Calling Party Number,")) {
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
	 * Finds and sets the additional calling party number in the <code>Call</code> object.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (call.getAdditionalcallingPartyNumber().length() == 0) {
			int startIndex = message.indexOf("number=") + "number=".length();
			String helpString = message.substring(startIndex);
			String number = helpString.substring(0, helpString.indexOf("."));
			call.setAdditionalcallingPartyNumber(number);
		}
	}
}