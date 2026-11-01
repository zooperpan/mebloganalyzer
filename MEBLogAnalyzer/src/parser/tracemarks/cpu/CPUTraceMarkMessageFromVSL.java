package parser.tracemarks.cpu;

import java.awt.Color;
import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.cpu.CPUState;
import meb.trace.vsl.VSLMessageCPU;
import parser.RawTrace;
import parser.call.CallAnalysisState;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>CPUTraceMarkMessageFromVSL</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the CPU component and is responsible for finding messages from VSL to CPU.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CPUTraceMarkMessageFromVSL extends TraceMarkAction {

	/**
	 * Creates a new {@link CPUTraceMarkMessageFromVSL} object.
	 */
	public CPUTraceMarkMessageFromVSL() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"Found App Message 'eAPP_MEB_"</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.startsWith("Found App Message 'eAPP_MEB_")) {
			return true;
		}
		return false;
	}

	/**
	 * Called when this is the start of an outgoing call (the printed trace does not have a callID).
	 * Depending on the message, it will create a new <code>Call</code> object and set the corresponding
	 * state. It will also add the relevant {@link VSLMessageCPU} and {@link CPUState} in the graph.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.startsWith("Found App Message 'eAPP_MEB_Req_SendFax")) {
			// This is an new outgoing call, create Call object and set direction to outgoing and initial state
			Call call = new Call(Call.OUTGOING);
			call.setDate(trace.getTime());
			call.setCallAnalysisState(CallAnalysisState.FAX_CALL_RECEIVED);
			// This is a FAX call
			call.setFAX(true);
			// Add BaseTrace in call for incoming Req_SendFax
			call.addBaseTrace(new VSLMessageCPU(trace.getTime(), "Req_SendFax", trace.getTrace()));
			// Add it in the list of calls
			calls.add(call);
			if (!call.isFirstCPUMessage()) {
				call.addBaseTrace(new CPUState(trace.getTime(), "NULL", trace.getTrace()));
				call.setFirstCPUMessage(true);
			}
			return call;
		} else if (message.startsWith("Found App Message 'eAPP_MEB_Req_SetMWI")) {
			// This is an new outgoing call, create Call object and set direction to outgoing and initial state
			Call call = new Call(Call.OUTGOING);
			call.setDate(trace.getTime());
			call.setCallAnalysisState(CallAnalysisState.MWI_CALL_RECEIVED);
			// This is a MWI call
			call.setMWI(true);
			// Add BaseTrace in call for incoming Req_SendFax
			call.addBaseTrace(new VSLMessageCPU(trace.getTime(), "Req_SetMWI", trace.getTrace()));
			// Add it in the list of calls
			calls.add(call);
			if (!call.isFirstCPUMessage()) {
				call.addBaseTrace(new CPUState(trace.getTime(), "NULL", trace.getTrace()));
				call.setFirstCPUMessage(true);
			}
			return call;
		} else if (message.startsWith("Found App Message 'eAPP_MEB_Req_MakeCall")) {
			// This is an new outgoing call, create Call object and set direction to outgoing and initial state
			Call call = new Call(Call.OUTGOING);
			call.setDate(trace.getTime());
			call.setCallAnalysisState(CallAnalysisState.MAKE_CALL_RECEIVED);
			// This is a MakeCall call
			call.setMakeCall(true);
			// Add BaseTrace in call for incoming Req_SendFax
			call.addBaseTrace(new VSLMessageCPU(trace.getTime(), "Req_MakeCall", trace.getTrace()));
			// Add it in the list of calls
			calls.add(call);
			if (!call.isFirstCPUMessage()) {
				call.addBaseTrace(new CPUState(trace.getTime(), "NULL", trace.getTrace()));
				call.setFirstCPUMessage(true);
			}
			return call;
		}
		return null;
	}

	/**
	 * Adds a  {@link VSLMessageCPU} message in the graph. In case of consultation call, it
	 * creates a new <code>Call</code> object and sets its initial state. Also sets the relevant
	 * feature activated in the call.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		String message = trace.getMessage().toString();
		String helpString = message.replace("Found App Message 'eAPP_MEB_", "");
		message = helpString.substring(0, helpString.indexOf('\'')).replace("eAPP_MEB_", "");
		if (!call.isFirstCPUMessage()) {
			call.addBaseTrace(new CPUState(trace.getTime(), "NULL", trace.getTrace()));
			call.setFirstCPUMessage(true);
		}
		if (message.equals("Req_AnswerCall")) {
			call.setWarningFound(false);
			call.setWarningString("");
		} else if (message.equals("Req_AnswerCallWithFax")) {
			call.setWarningFound(false);
			call.setWarningString("");
			call.setFAX(true);
		} else if (message.equals("Req_ConsultCall")) {
			// This is an new outgoing call, create Call object and set direction to outgoing and initial state
			Call newCall = new Call(Call.OUTGOING);
			newCall.setDate(trace.getTime());
			newCall.setCallAnalysisState(CallAnalysisState.CONSULT_CALL_RECEIVED);
			// This is a consultation call
			newCall.setConsultCall(true);
			// Set also for original call
			call.setConsultCall(true);
			// Add BaseTrace in call for incoming Req_SendFax
			newCall.addBaseTrace(new VSLMessageCPU(trace.getTime(), "Req_ConsultCall", trace.getTrace()));
			// Add it in the list of calls
			calls.add(newCall);
			if (!newCall.isFirstCPUMessage()) {
				newCall.addBaseTrace(new CPUState(trace.getTime(), "NULL", trace.getTrace()));
				newCall.setFirstCPUMessage(true);
			}
		} else if (message.equals("Req_SingleStepTransfer")) {
			call.setSsctCall(true);
		} else if (message.equals("Req_Media_AddFileToOutputFileList")) {
			call.setAnnouncementCall(true);
		}
		// Check if call was rejected
		if (message.equals("Req_RejectCall")) {
			call.setErrorFound(true);
			call.setErrorString("Call rejected by VSL!");
			call.addBaseTrace(new VSLMessageCPU(trace.getTime(), message, trace.getTrace(), Color.RED));
		} else {
			call.addBaseTrace(new VSLMessageCPU(trace.getTime(), message, trace.getTrace()));
		}
	}
}