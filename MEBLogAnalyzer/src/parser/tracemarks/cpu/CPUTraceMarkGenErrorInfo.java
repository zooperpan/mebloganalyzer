package parser.tracemarks.cpu;

import java.awt.Color;
import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.cpu.CPUInfo;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>CPUTraceMarkGenErrorInfo</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the CPU component and is responsible for finding specific error traces within 
 * the MEB logs.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CPUTraceMarkGenErrorInfo extends TraceMarkAction {

	private String error = "";

	/**
	 * Creates a new {@link CPUTraceMarkGenErrorInfo} object.
	 */
	public CPUTraceMarkGenErrorInfo() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains specific error strings.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.contains("CheckNumber: Number not found")) {
			error = "VSL number not found!";
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
	 * Adds a {@link CPUInfo} with the relevant error (using RED color) in the graph. Also sets the
	 * error string in the <code>Call</code> object.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		call.setErrorFound(true);
		call.setErrorString(error);
		call.addBaseTrace(new CPUInfo(trace.getTime(), error, trace.getTrace(), Color.RED));
	}
}