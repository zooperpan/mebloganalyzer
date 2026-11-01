package parser.tracemarks.sci;

import java.util.ArrayList;

import meb.entities.Call;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>SCITraceMarkMessageToM5T</code> class is an abstract {@link TraceMarkAction} that analyzes 
 * traces created by the SCI component and is responsible for finding messages from SCI to M5T. The main propose
 * of this class is to find the <code>Call</code> object that this trace belongs to. It should be extended
 * to more specific trace mark actions in order to provide the actions for each trace.
 * 
 * @author Panos Katsikogiannis
 *
 */
public abstract class SCITraceMarkMessageToM5T extends TraceMarkAction {

	private Call call = null;

	/**
	 * Creates a new {@link SCITraceMarkMessageToM5T} object. Every subclass that extends this one, 
	 * should declare when calling this constructor if the analysis of a trace results to a new call.
	 * @param callToBeAdded true if the analysis results to a new call, false otherwise.
	 */
	public SCITraceMarkMessageToM5T(boolean callToBeAdded) {
		super(callToBeAdded);
	}

	/**
	 * Checks if the trace contains the <code>"VISU-MS|SCI\\n|M5T\\n"</code> or
	 * <code>"VISU-MS|SCI\\n"</code> string and sets the <code>Call</code> object if found.
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		// Clear Call object since it may have been saved from a previous trace analysis
		setCall(null);
		if (message.contains("VISU-MS|SCI\\n|M5T\\n")) {
			String helpString = trace.getMessage().toString().substring(trace.getMessage().toString().indexOf("VISU-MS|SCI\\n|M5T\\n"));
			helpString = helpString.replace("VISU-MS|SCI\\n|M5T\\n", "");
			String callID = helpString.substring(0, helpString.indexOf('|'));
			for (Call call: calls) {
				if ((call.getM5TCallID() != null) && call.getM5TCallID().equals(callID)) {
					setCall(call);
					break;
				}
			}
			return true;
		} else if (message.contains("VISU-MS|SCI\\n") && message.contains("|M5T\\n")) {
			String helpString = trace.getMessage().toString().substring(trace.getMessage().toString().indexOf("|M5T\\n"));
			helpString = helpString.replace("|M5T\\n", "");
			String callID = helpString.substring(0, helpString.indexOf('|'));
			for (Call call: calls) {
				if ((call.getM5TCallID() != null) && call.getM5TCallID().equals(callID)) {
					setCall(call);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Abstract method that should be implemented by the subclasses.
	 */
	@Override
	public abstract Call markAction(ArrayList<Call> calls, RawTrace trace);

	/**
	 * Abstract method that should be implemented by the subclasses.
	 */
	@Override
	public abstract void markAction(ArrayList<Call> calls, Call call, RawTrace trace);

	/**
	 * Returns the <code>Call</code> object that this trace belongs to.
	 * @return the <code>Call</code> object that this trace belongs to.
	 */
	public Call getCall() {
		return call;
	}

	/**
	 * Sets the <code>Call</code> object that this trace belongs to.
	 * @param call the <code>Call</code> object that this trace belongs to.
	 */
	public void setCall(Call call) {
		this.call = call;
	}
}
