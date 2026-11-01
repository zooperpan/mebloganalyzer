package parser.tracemarks.cpu;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.cpu.CPUMessageCPU;
import meb.trace.cpu.CPUState;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>CPUTraceMarkMessageFromCPU</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the CPU component and is responsible for finding messages from CPU to CPU.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CPUTraceMarkMessageFromCPU extends TraceMarkAction {

	/**
	 * Creates a new {@link CPUTraceMarkMessageFromCPU} object.
	 */
	public CPUTraceMarkMessageFromCPU() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"_messageType=eCPU_CPU_"</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.contains("_messageType=eCPU_CPU_")) {
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
	 * Adds a {@link CPUMessageCPU} message in the graph. Also adds a {@link CPUState} with the
	 * initial state for the CPU component in case it has not already been set.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		String message = trace.getMessage().toString();
		int startIndex = message.indexOf("_messageType=eCPU_CPU_") + "_messageType=eCPU_CPU_".length();
		String helpString = message.substring(startIndex, message.length());
		if (helpString.contains(",")) {
			message = helpString.substring(0, helpString.indexOf(','));
		} else {
			message = helpString.substring(0, helpString.indexOf('}'));
		}
		if (!call.isFirstCPUMessage()) {
			call.addBaseTrace(new CPUState(trace.getTime(), "NULL", trace.getTrace()));
			call.setFirstCPUMessage(true);
		}
		call.addBaseTrace(new CPUMessageCPU(trace.getTime(), message, trace.getTrace()));
	}
}
