package parser.tracemarks.cornet;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.cornet.CornetState;
import meb.trace.cpu.CPUMessageCornet;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>CornetTraceMarkMessageFromCPU</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the CorNet component and is responsible for finding messages from CPU to CorNet.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CornetTraceMarkMessageFromCPU extends TraceMarkAction {

	/**
	 * Creates a new {@link CornetTraceMarkMessageFromCPU} object.
	 */
	public CornetTraceMarkMessageFromCPU() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"CcInterface::eventReceiveInterfaceMessageFromCpu()"</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.contains("CcInterface::eventReceiveInterfaceMessageFromCpu()")) {
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
	 * Adds a {@link CPUMessageCornet} message in the graph.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		String message = trace.getMessage().toString();
		int startIndex = message.indexOf("_methodType=") + "_methodType=".length();
		String helpString = message.substring(startIndex);
		int endIndex = helpString.indexOf("(");
		message = helpString.substring(0, endIndex).replace("eCPU_NQU_", "");
		if (!call.isFirstCornetMessage()) {
			call.addBaseTrace(new CornetState(trace.getTime(), "0_NULL", trace.getTrace()));
			call.setFirstCornetMessage(true);
		}
		call.addBaseTrace(new CPUMessageCornet(trace.getTime(), message, trace.getTrace()));
	}
}
