package parser.tracemarks.cpu;

import java.awt.Color;
import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.cpu.CPUMessageVSL;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>CPUTraceMarkMessageToVSL</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the CPU component and is responsible for finding messages from CPU to VSL.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CPUTraceMarkMessageToVSL extends TraceMarkAction {

	/**
	 * Creates a new {@link CPUTraceMarkMessageToVSL} object.
	 */
	public CPUTraceMarkMessageToVSL() {
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>""Sending eMEB_APP_""</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.startsWith("Sending eMEB_APP_")) {
			return true;
		} else if (message.startsWith("Sending undefined NAck:")) {
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
	 * Adds a {@link CPUMessageVSL} message in the graph.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		String message = trace.getMessage().toString();
		boolean setWarning = false;
		if (message.startsWith("Sending eMEB_APP_")) {
			String helpString = message.replace("Sending ", "");
			message = helpString.substring(0, helpString.indexOf(':')).replace("eMEB_APP_", "");
			
			if ((call.getCstaCallID() == null) && (message.equals("Ack_MakeCall") || (message.equals("Event_IncomingCall")) 
					&& helpString.contains("globallyUniqueId"))) {
				helpString = helpString.substring(helpString.indexOf("globallyUniqueId"), helpString.length() - 1).replace("globallyUniqueId", "");
				helpString = helpString.substring(0, helpString.indexOf("'}")).replace("'}", "");
				call.setCstaCallID(helpString.substring(helpString.length() - 11, helpString.length()).replace(" ", ""));
			} else if (message.equals("Event_MediaInfo") && (trace.getMessage().toString().contains("eME_ErrorOpeningFile"))) {
				call.setWarningFound(true);
				setWarning = true;
				call.setWarningString("Could not find media info file!");
			}
			call.addBaseTrace(new CPUMessageVSL(trace.getTime(), message, trace.getTrace(), setWarning ? Color.ORANGE : Color.BLACK));
		} else {
			// Sending undefined NAck
			int helpIndex = message.indexOf("MEB_APP_Ack_");
			if (helpIndex > 0) {
				message = message.substring(helpIndex, message.length() - 1);
				String helpString = message.substring(0, message.indexOf(',')).replace("MEB_APP_", "");
				if (message.contains("_bResult=true")) {
					call.addBaseTrace(new CPUMessageVSL(trace.getTime(), helpString, trace.getTrace()));
				} else {
					call.addBaseTrace(new CPUMessageVSL(trace.getTime(), "N" + helpString, trace.getTrace(), Color.RED));
					call.setWarningFound(true);
					call.setWarningString("Service " + helpString.replace("Ack_", "") + " failed!");
				}
			}
		}
	}
}
