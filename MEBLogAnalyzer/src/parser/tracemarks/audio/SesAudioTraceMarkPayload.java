package parser.tracemarks.audio;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.h323.H323Info;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>SesAudioTraceMarkPayload</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the AUDIO component and is responsible for finding traces related with the 
 * payload handling within MEB.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SesAudioTraceMarkPayload extends TraceMarkAction {

	private String info = "";

	/**
	 * Creates a new {@link SesAudioTraceMarkPayload} object.
	 */
	public SesAudioTraceMarkPayload() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains any of the payload related strings.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.startsWith("Found data from application")) {
			info = "Payload to network started";
			return true;
		} else if (message.startsWith("No data to be sent")) {
			info = "Payload to network stopped";
			return true;
		} else if (message.startsWith("Successfully sent first packet to driver")) {
			info = "Payload to VSL started";
			return true;
		} else if (message.startsWith("Received first packet from driver")) {
			info = "Payload from VSL started";
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
	 * Adds a {@link H323Info} box with the payload information in the graph.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		call.addBaseTrace(new H323Info(trace.getTime(), info, trace.getTrace()));
	}
}