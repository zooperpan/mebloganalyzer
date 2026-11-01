package parser.tracemarks.h323;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.cpu.CPUMessageH323;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>H323TraceMarkMessageFromCPU</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the H323 component and is responsible for finding messages from CPU to H323.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class H323TraceMarkMessageFromCPU extends TraceMarkAction {

	/**
	 * Creates a new {@linkH323TraceMarkMessageFromCPU} object.
	 */
	public H323TraceMarkMessageFromCPU() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"Found eCPU_H323_"</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.startsWith("Found eCPU_H323_")) {
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
	 * Adds a {@link CPUMessageH323} message in the graph.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		String message = trace.getMessage().toString();
		String helpString = message.replace("Found ", "");
		if (helpString.contains(" ")) {
			message = helpString.substring(0, helpString.indexOf(' ')).replace("eCPU_H323_", "");
		} else message = helpString.replace("eCPU_H323_", "");
		call.addBaseTrace(new CPUMessageH323(trace.getTime(), message, trace.getTrace()));
	}
}
