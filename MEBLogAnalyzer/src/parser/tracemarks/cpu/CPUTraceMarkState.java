package parser.tracemarks.cpu;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.cpu.CPUState;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>CPUTraceMarkState</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the CPU component and is responsible for finding state trace logs for CPU.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CPUTraceMarkState extends TraceMarkAction {

	/**
	 * Creates a new {@link CPUTraceMarkState} object.
	 */
	public CPUTraceMarkState() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"New call state: Call is in "</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.contains("New call state: Call is in ")) {
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
	 * Adds a {@link CPUState} state information in the graph. Also sets the CPU state 
	 * in the <code>Call</code> object.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		String message = trace.getMessage().toString();
		String helpString = message.replace("New call state: Call is in ", "");
		String state = helpString.substring(0, helpString.indexOf(' '));
		if (!call.getCPUCallState().equals(state)) {
			call.setCPUCallState(state);
			call.addBaseTrace(new CPUState(trace.getTime(), state, trace.getTrace()));
		}
	}
}
