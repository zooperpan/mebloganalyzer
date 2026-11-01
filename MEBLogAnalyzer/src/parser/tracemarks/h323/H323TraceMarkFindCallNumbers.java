package parser.tracemarks.h323;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.cpu.CPUInfo;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>H323TraceMarkFindCallNumbers</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the H323 component and is responsible for finding the calling and called party 
 * numbers of an outgoing MEB call.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class H323TraceMarkFindCallNumbers extends TraceMarkAction {

	/**
	 * Creates a new {@link H323TraceMarkFindCallNumbers} object.
	 */
	public H323TraceMarkFindCallNumbers() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"SCISession::SendH225Setup  Calling:"</code> string.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.startsWith("SCISession::SendH225Setup  Calling:")) {
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
	 * Finds and sets the calling/called party numbers in the <code>Call</code> object. Also 
	 * adds a {@link CPUInfo} with the relevant number information in the graph.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		String message = trace.getMessage().toString();
		String helpString = message.replace("SCISession::SendH225Setup  Calling: '", "");
		String calling = helpString.substring(0, helpString.indexOf('\''));
		int startIndex = helpString.indexOf("Called: '") + "Called: '".length();
		helpString = helpString.substring(startIndex);
		String called = helpString.substring(0, helpString.indexOf('\''));
		call.setCallingPartyNumber(calling);
		call.setCalledPartyNumber(called);
		call.addBaseTrace(new CPUInfo(trace.getTime(), "CallingPartyNumber\n" + calling, trace.getTrace()));
		call.addBaseTrace(new CPUInfo(trace.getTime(), "CalledPartyNumber\n" + called, trace.getTrace()));
	}
}
