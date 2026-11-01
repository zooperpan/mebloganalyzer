package parser.call;

import java.util.ArrayList;

import meb.entities.Call;
import meb.entities.Component;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;
import parser.tracemarks.audio.SesAudioTraceMarkFindCallID;
import parser.tracemarks.audio.SesAudioTraceMarkPayload;
import parser.tracemarks.cornet.CornetTraceMarkMessageFromCPU;
import parser.tracemarks.cornet.CornetTraceMarkMessageToCPU;
import parser.tracemarks.cpu.CPUTraceMarkFindCallNumbers;
import parser.tracemarks.cpu.CPUTraceMarkEndCall;
import parser.tracemarks.cpu.CPUTraceMarkFindAdditionalCallNumber;
import parser.tracemarks.cpu.CPUTraceMarkGenErrorInfo;
import parser.tracemarks.cpu.CPUTraceMarkGenerateCall;
import parser.tracemarks.cpu.CPUTraceMarkMessageFromCPU;
import parser.tracemarks.cpu.CPUTraceMarkMessageFromH323;
import parser.tracemarks.cpu.CPUTraceMarkMessageFromVSL;
import parser.tracemarks.cpu.CPUTraceMarkState;
import parser.tracemarks.fax.FAXTraceMarkEvent;
import parser.tracemarks.fax.FAXTraceMarkInfo;
import parser.tracemarks.h323.H323TraceMarkFindCallNumbers;
import parser.tracemarks.h323.H323TraceMarkMessageFromCPU;
import parser.tracemarks.h323.H323TraceMarkMessageFromH323;
import parser.tracemarks.h323.H323TraceMarkMessageFromSCI;
import parser.tracemarks.h323.H323TraceMarkRTPAddress;
import parser.tracemarks.cpu.CPUTraceMarkMessageToVSL;
import parser.tracemarks.sci.SCITraceMark100TryingToM5T;
import parser.tracemarks.sci.SCITraceMark200OKToM5T;
import parser.tracemarks.sci.SCITraceMarkAckFromM5T;
import parser.tracemarks.sci.SCITraceMarkAckToM5T;
import parser.tracemarks.sci.SCITraceMarkByeFromM5T;
import parser.tracemarks.sci.SCITraceMarkFindIncomingSDP;
import parser.tracemarks.sci.SCITraceMarkFindM5TCallID;
import parser.tracemarks.sci.SCITraceMarkFindOutgoingSDP;
import parser.tracemarks.sci.SCITraceMarkINFOFromM5T;
import parser.tracemarks.sci.SCITraceMarkINFOToM5T;
import parser.tracemarks.sci.SCITraceMarkInviteToM5T;
import parser.tracemarks.sci.SCITraceMarkInvited;
import parser.tracemarks.sci.SCITraceMarkReInvitedFromM5T;
import parser.tracemarks.sci.SCITraceMarkState;
import parser.tracemarks.sci.SCITraceMarkByeToM5T;
import parser.tracemarks.sci.SCITraceMarkFailureFromM5T;

/**
 * The <code>CallManager</code> class is responsible for analyzing all {@link RawTrace traces} found in the MEB log 
 * file and generating {@link Call calls}. It utilizes a number of {@link TraceMarkAction trace mark actions} to 
 * find/create the relevant <code>Call</code> object and/or analyze each trace. Traces that have the callID information 
 * are analyzed through the {@link #handleTrace(RawTrace, Call) handleTrace} function, while traces that do not have 
 * the callID information are analyzed through the {@link #checkCallForTrace(RawTrace) checkCallForTrace} function.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CallManager {
	private ArrayList<Call> calls = null;
	private ArrayList<TraceMarkAction> traceMarkActionsSCI;
	private ArrayList<TraceMarkAction> traceMarkActionsAudio;
	private ArrayList<TraceMarkAction> traceMarkActionsCPU;
	private ArrayList<TraceMarkAction> traceMarkActionsH323;
	private ArrayList<TraceMarkAction> traceMarkActionsCornet;
	private ArrayList<TraceMarkAction> traceMarkActionsFAX;

	/**
	 * Creates a new {@link CallManager} object
	 */
	public CallManager() {
		// Create a local list of calls
		calls = new ArrayList<Call>();

		// Create all SCI trace mark actions
		traceMarkActionsSCI = new ArrayList<TraceMarkAction>();
		traceMarkActionsSCI.add(new SCITraceMarkInvited());
		traceMarkActionsSCI.add(new SCITraceMarkFindM5TCallID());
		traceMarkActionsSCI.add(new SCITraceMark200OKToM5T());
		traceMarkActionsSCI.add(new SCITraceMarkAckFromM5T());
		traceMarkActionsSCI.add(new SCITraceMarkReInvitedFromM5T());
		traceMarkActionsSCI.add(new SCITraceMark100TryingToM5T());
		traceMarkActionsSCI.add(new SCITraceMarkFindIncomingSDP());
		traceMarkActionsSCI.add(new SCITraceMarkFindOutgoingSDP());
		traceMarkActionsSCI.add(new SCITraceMarkINFOToM5T());
		traceMarkActionsSCI.add(new SCITraceMarkINFOFromM5T());
		traceMarkActionsSCI.add(new SCITraceMarkByeFromM5T());
		traceMarkActionsSCI.add(new SCITraceMarkFailureFromM5T());
		traceMarkActionsSCI.add(new SCITraceMarkInviteToM5T());
		traceMarkActionsSCI.add(new SCITraceMarkAckToM5T());
		traceMarkActionsSCI.add(new SCITraceMarkByeToM5T());
		traceMarkActionsSCI.add(new SCITraceMarkState());

		// Create all AUDIO trace mark actions
		traceMarkActionsAudio = new ArrayList<TraceMarkAction>();
		traceMarkActionsAudio.add(new SesAudioTraceMarkFindCallID());
		traceMarkActionsAudio.add(new SesAudioTraceMarkPayload());

		// Create all CPU trace mark actions
		traceMarkActionsCPU = new ArrayList<TraceMarkAction>();
		traceMarkActionsCPU.add(new CPUTraceMarkEndCall());
		traceMarkActionsCPU.add(new CPUTraceMarkState());
		traceMarkActionsCPU.add(new CPUTraceMarkMessageToVSL());
		traceMarkActionsCPU.add(new CPUTraceMarkMessageFromVSL());
		traceMarkActionsCPU.add(new CPUTraceMarkMessageFromH323());
		traceMarkActionsCPU.add(new CPUTraceMarkFindCallNumbers());
		traceMarkActionsCPU.add(new CPUTraceMarkGenErrorInfo());
		traceMarkActionsCPU.add(new CPUTraceMarkMessageFromCPU());
		traceMarkActionsCPU.add(new CPUTraceMarkGenerateCall());
		traceMarkActionsCPU.add(new CPUTraceMarkFindAdditionalCallNumber());

		// Create all H323 trace mark actions
		traceMarkActionsH323 = new ArrayList<TraceMarkAction>();
		traceMarkActionsH323.add(new H323TraceMarkMessageFromCPU());
		traceMarkActionsH323.add(new H323TraceMarkMessageFromSCI());
		traceMarkActionsH323.add(new H323TraceMarkMessageFromH323());
		traceMarkActionsH323.add(new H323TraceMarkRTPAddress());
		traceMarkActionsH323.add(new H323TraceMarkFindCallNumbers());

		// Create all CorNet trace mark actions
		traceMarkActionsCornet = new ArrayList<TraceMarkAction>();
		traceMarkActionsCornet.add(new CornetTraceMarkMessageFromCPU());
		traceMarkActionsCornet.add(new CornetTraceMarkMessageToCPU());

		// Create all FAX trace mark actions
		traceMarkActionsFAX = new ArrayList<TraceMarkAction>();
		traceMarkActionsFAX.add(new FAXTraceMarkInfo());
		traceMarkActionsFAX.add(new FAXTraceMarkEvent());
	}

	/**
	 * Initializes the {@link CallManager}. It actually clears the local list of calls.
	 */
	public void init() {
		calls.clear();
	}

	/**
	 * Returns the list of trace mark actions of the specific component. If there is no trace mark action list
	 * for the component that created the trace, it returns null.
	 * @param component the component for which to find the list of trace mark actions 
	 * @return the found list of trace mark actions, null if none is found
	 */
	private ArrayList<TraceMarkAction> getTraceMarkAction(Component component) {
		ArrayList<TraceMarkAction> result = null;
		switch(component) {
		case CPU:
			result = traceMarkActionsCPU;
			break;
		case H323:
			result = traceMarkActionsH323;
			break;
		case CORNET:
			result = traceMarkActionsCornet;
			break;
		case AUDIO:
			result = traceMarkActionsAudio;
			break;
		case FAX:
			result = traceMarkActionsFAX;
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * Handles the specified trace in order to add the relevant data in the provided <code>Call</code> object. 
	 * Calls all {@link TraceMarkAction trace mark actions} until it finds the suitable for the specified trace 
	 * and takes action on it.
	 * @param trace the RawTrace to analyze
	 * @param call the call that the trace belongs to
	 */
	public void handleTrace(RawTrace trace, Call call) {
		// Find the trace mark action list of this trace (if any) depending on the component that created the trace
		ArrayList<TraceMarkAction> traceMarkActions = getTraceMarkAction(Component.getComponent(trace.getComponent()));
		// If we found a trace mark action list for this trace...
		if (traceMarkActions != null) {
			// Go through all trace mark actions in the list
			for (TraceMarkAction traceMarkAction: traceMarkActions) {
				// Check if this trace line corresponds to the specific trace mark action
				if (traceMarkAction.markFound(calls, trace)) {
					// Take action on the trace line
					traceMarkAction.markAction(calls, call, trace);
					// No need to check the rest of the trace mark actions, break the loop
					break;
				}
			}
		}
	}

	/**
	 * Analyzes a trace in order to find in which call it belongs. Calls all {@link TraceMarkAction trace mark actions} 
	 * until it finds the suitable for the specific trace and takes action on it. The trace mark action is responsible 
	 * for finding to which {@link Call} the trace belongs to. It will add the relevant data to the found call or may 
	 * create a new <code>Call</code> object and add it in the list of calls. If the action results to a new call that 
	 * has been initiated (callID has been found), it will return the created <code>Call</code> object.
	 * @param trace the RawTrace to analyze
	 * @return a Call object in case a call has been initiated (callID has been found), null otherwise.
	 */
	public Call checkCallForTrace(RawTrace trace) {
		Call result = null;
		// Check if we have an SCI trace
		if (Component.getComponent(trace.getComponent()) == Component.SCI) {
			// Go through all SCI trace mark actions
			for (TraceMarkAction traceMarkAction: traceMarkActionsSCI) {
				// Check if this trace line corresponds to the specific trace mark action
				if (traceMarkAction.markFound(calls, trace)) {
					// Take action on the trace line
					result = traceMarkAction.action(calls, trace);
					// No need to check the rest of the trace mark actions, break the loop
					break;
				}
			}
		} else if (Component.getComponent(trace.getComponent()) == Component.AUDIO) {
			// Go through all AUDIO trace mark actions
			for (TraceMarkAction traceMarkAction: traceMarkActionsAudio) {
				// Check if this trace line corresponds to the specific trace mark action
				if (traceMarkAction.markFound(calls, trace)) {
					// Take action on the trace line
					result = traceMarkAction.action(calls, trace);
					// No need to check the rest of the trace mark actions, break the loop
					break;
				}
			}
		} else if (Component.getComponent(trace.getComponent()) == Component.CPU) {
			// Go through all CPU trace mark actions
			for (TraceMarkAction traceMarkAction: traceMarkActionsCPU) {
				// Check if this trace line corresponds to the specific trace mark action
				if (traceMarkAction.markFound(calls, trace)) {
					// Take action on the trace line
					result = traceMarkAction.action(calls, trace);
					// No need to check the rest of the trace mark actions, break the loop
					break;
				}
			}
		}
		return result;
	}
}
