package parser.tracemarks.cpu;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.cpu.CPUInfo;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>CPUTraceMarkEndCall</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the CPU component and is responsible for finding the number of active calls
 * and end date of the call after the release/destruction of an MEB call.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CPUTraceMarkEndCall extends TraceMarkAction {

	/**
	 * Creates a new {@link CPUTraceMarkEndCall} object.
	 */
	public CPUTraceMarkEndCall() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"CCall::~CCall. Calls: "</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.contains("CCall::~CCall. Calls: ")) {
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
	 * Adds a {@link CPUInfo} with the relevant information in the graph. Also sets the end date in the 
	 * <code>Call</code> object.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		call.setEndDate(trace.getTime());
		String helpString = trace.getMessage().toString().replace("CCall::~CCall. Calls: ", "");
		call.addBaseTrace(new CPUInfo(trace.getTime(), "Active calls: " + helpString, trace.getTrace()));
	}
}
