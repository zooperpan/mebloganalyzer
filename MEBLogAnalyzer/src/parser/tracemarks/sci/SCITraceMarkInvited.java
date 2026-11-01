package parser.tracemarks.sci;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.m5t.M5TMessageSCI;
import parser.RawTrace;
import parser.call.CallAnalysisState;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>SCITraceMarkInvited</code> class is an abstract {@link TraceMarkAction} that analyzes 
 * traces created by the SCI component and is responsible for finding INVITE messages from M5T to SCI.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCITraceMarkInvited extends TraceMarkAction {

	/**
	 * Creates a new {@link SCITraceMarkInvited} object.
	 */
	public SCITraceMarkInvited() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"EvInvited(): Incoming M5T-Event, current callstate: 0."</code> 
	 * string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.contains("EvInvited(): Incoming M5T-Event, current callstate: 0.")) {
			return true;
		}
		return false;
	}

	/**
	 * Adds a {@link M5TMessageSCI} message in the graph. Also creates a new <code>Call</code>
	 * object and sets its start date and next {@link CallAnalysisState pre-state}. The created call
	 * is added in the list of calls.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		// This is an new incoming call, create Call object and set direction to incoming and initial state
		Call call = new Call(Call.INCOMING);
		call.setDate(trace.getTime());
		call.setCallAnalysisState(CallAnalysisState.INVITED);
		// Add BaseTrace in call for incoming INVITE
		call.addBaseTrace(new M5TMessageSCI(trace.getTime(), "INVITE", trace.getTrace()));
		// Add it in the list of calls
		calls.add(call);
		return call;
	}

	/**
	 * Not used by this trace mark action.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {}
}
