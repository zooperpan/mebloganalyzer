package parser.tracemarks.h323;

import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.h323.H323Info;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>H323TraceMarkRTPAddress</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the H323 component and is responsible for finding the local and remote 
 * RTP addresses and ports used in this call.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class H323TraceMarkRTPAddress extends TraceMarkAction {

	/**
	 * Creates a new {@link H323TraceMarkRTPAddress} object.
	 */
	public H323TraceMarkRTPAddress() {
		// This trace mark action cannot find the callID of the call
		super(false);
	}

	/**
	 * Checks if the trace contains the <code>"localRtpAddress: "</code> or 
	 * <code>"remoteRtpAddress: "</code> strings.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		if (message.startsWith("localRtpAddress: ")) {
			return true;
		} else if (message.startsWith("remoteRtpAddress: ")) {
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
	 * Adds an {@link H323Info} information in the graph.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		String info = trace.getMessage().toString();
		if (info.startsWith("localRtpAddress: ")) {
			String helpString = info.replace("localRtpAddress: ip:", "");
			String localRTPAddress = helpString.substring(0, helpString.indexOf(' '));
			if (!call.getLocalRTPAddress().equals(localRTPAddress)) {
				call.setLocalRTPAddress(localRTPAddress);
				info = "localRtpAddress:\n" + helpString.substring(0, helpString.indexOf(' '));
				call.addBaseTrace(new H323Info(trace.getTime(), info, trace.getTrace()));
			}
		} else {
			String helpString = info.replace("remoteRtpAddress: ip:", "");
			String remoteRTPAddress = helpString.substring(0, helpString.indexOf(' '));
			if (!call.getRemoteRTPAddress().equals(remoteRTPAddress)) {
				call.setRemoteRTPAddress(remoteRTPAddress);
				info = "remoteRtpAddress:\n" + helpString.substring(0, helpString.indexOf(' '));
				call.addBaseTrace(new H323Info(trace.getTime(), info, trace.getTrace()));
			}
		}
	}
}
