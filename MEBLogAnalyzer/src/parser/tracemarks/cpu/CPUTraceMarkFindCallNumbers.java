package parser.tracemarks.cpu;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.cpu.CPUInfo;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>CPUTraceMarkFindCallNumbers</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the CPU component and is responsible for finding the calling and called party 
 * numbers of an incoming MEB call.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CPUTraceMarkFindCallNumbers extends TraceMarkAction {

	boolean calling = false;

	/**
	 * Creates a new {@link CPUTraceMarkFindCallNumbers} object.
	 */
	public CPUTraceMarkFindCallNumbers() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"Calling Party Number: "</code> or 
	 * <code>"Called Party Number: "</code> strings.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.startsWith("Calling Party Number: ")) {
			calling = true;
			return true;
		} else if (message.startsWith("Called Party Number: ")) {
			calling = false;
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
	 * Finds and sets the calling/called party numbers in the <code>Call</code> object. Also 
	 * adds a {@link CPUInfo} with the relevant number information in the graph.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		String message = trace.getMessage().toString();
		int startIndex = message.indexOf("_numberDigits=\"") + "_numberDigits=\"".length();
		String helpString = message.substring(startIndex);
		String number = helpString.substring(0, helpString.indexOf("\""));
		if (calling) {
			call.setCallingPartyNumber(number);
			call.addBaseTrace(new CPUInfo(trace.getTime(), "CallingPartyNumber\n" + number, trace.getTrace()));
		} else {
			call.setCalledPartyNumber(number);
			call.addBaseTrace(new CPUInfo(trace.getTime(), "CalledPartyNumber\n" + number, trace.getTrace()));
		}
	}
}
