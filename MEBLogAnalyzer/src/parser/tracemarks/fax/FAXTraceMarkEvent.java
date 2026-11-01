package parser.tracemarks.fax;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.h323.H323Info;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>FAXTraceMarkEvent</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the FAX component and is responsible for finding events coming from Commetrex,
 * as well as reading the result state of a FAX call.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class FAXTraceMarkEvent extends TraceMarkAction {

	/**
	 * Creates a new {@link FAXTraceMarkEvent} object.
	 */
	public FAXTraceMarkEvent() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"Got event '"</code> or 
	 * <code>"State: "</code> strings.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.startsWith("Got event '")) {
			return true;
		} else if (message.startsWith("State: ")) {
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
	 * Adds a {@link H323Info} information in the graph.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.startsWith("Got event '")) {
			String helpString = message.replace("Got event '", "");
			message = helpString.substring(0, helpString.indexOf('\''));
		} else {
			String helpString[] = message.split("'");
			message = "State: " + helpString[1] + "\nResult: " + helpString[3] + "\nError: " + helpString[5];
		}
		call.addBaseTrace(new H323Info(trace.getTime(), message, trace.getTrace()));
	}
}
