package parser.tracemarks;

import java.util.ArrayList;

import meb.entities.Call;
import parser.RawTrace;
import parser.call.CallAnalysisState;

/**
 * The abstract <code>TraceMarkAction</code> class can be used in order to analyze specific trace lines within
 * the MEB log. It provides 3 abstract methods that should be implemented by the subclasses. The 
 * {@link parser.call.CallManager CallManager} class has lists of <code>TraceMarkAction</code> objects and each
 * trace line is passed to each one of them until the trace line has been analyzed.
 * 
 * @see #markFound(ArrayList, RawTrace)
 * @see #markAction(ArrayList, RawTrace)
 * @see #markAction(ArrayList, Call, RawTrace)
 * 
 * @author Panos Katsikogiannis
 *
 */
public abstract class TraceMarkAction {

	private boolean callToBeAdded = false;

	/**
	 * Creates a new {@link TraceMarkAction} object. Every subclass that extends this one, should declare when
	 * calling this constructor if the analysis of a trace results to a new call.
	 * @param callToBeAdded true if the analysis results to a new call, false otherwise.
	 */
	public TraceMarkAction(boolean callToBeAdded) {
		setCallToBeAdded(callToBeAdded);
	}

	/**
	 * Called by the {@link parser.call.CallManager CallManager} when a trace does not provide the callID that 
	 * it belongs to. It calls the implemented {@link #markAction(ArrayList, RawTrace) markAction()} of the 
	 * subclass. In case the subclass has declared that the found call should be added in the list of calls, 
	 * it returns the found call. 
	 * @param calls the list of already found calls
	 * @param trace the trace line to be analyzed
	 * @return a <code>Call</code> object if a new call has been created and should be added in the list of 
	 * calls, null otherwise
	 */
	public Call action(ArrayList<Call> calls, RawTrace trace) {
		Call call = markAction(calls, trace);
		if (callToBeAdded) {
			return call;
		}
		return null;
	}

	/**
	 * An abstract method that should be implemented by the subclasses in order to check if the provided trace 
	 * line should be analyzed by this {@link TraceMarkAction}. Called by the {@link parser.call.CallManager CallManager}.
	 * @param calls the list of already found calls
	 * @param trace the trace line to be analyzed
	 * @return true if the trace line should be analyzed by this {@link TraceMarkAction}, false otherwise
	 */
	public abstract boolean markFound(ArrayList<Call> calls, RawTrace trace);

	/**
	 * An abstract method that should be implemented by the subclasses in order to take action upon the provided
	 * trace line. Called when a trace does not provide the callID that it belongs to.
	 * @param calls the list of already found calls
	 * @param trace the trace line to be analyzed
	 * @return the <code>Call</code> object that the trace belongs to if found, null otherwise
	 */
	public abstract Call markAction(ArrayList<Call> calls, RawTrace trace);

	/**
	 * An abstract method that should be implemented by the subclasses in order to take action upon the provided
	 * trace line. Called when a trace provides the callID that it belongs to.
	 * @param calls the list of already found calls
	 * @param call the call that the trace belongs to
	 * @param trace the trace line to be analyzed
	 */
	public abstract void markAction(ArrayList<Call> calls, Call call, RawTrace trace);

	/**
	 * Returns true if the analysis results to a new call, false otherwise.
	 * @return true if the analysis results to a new call, false otherwise.
	 */
	public boolean isCallToBeAdded() {
		return callToBeAdded;
	}

	/**
	 * Sets weather the analysis performed by this {@link TraceMarkAction} results to a new call.
	 * @param callToBeAdded should be true if the analysis results to a new call, false otherwise
	 */
	public void setCallToBeAdded(boolean callToBeAdded) {
		this.callToBeAdded = callToBeAdded;
	}

	/**
	 * Checks if more than one call is waiting in the specified {@link CallAnalysisState analysis states}. A trace
	 * without callID is assigned to the first call found in the expected state, so if several calls are waiting
	 * the trace may be assigned to the wrong call. In that case all the waiting calls are marked as
	 * {@link Call#isAmbiguous() ambiguous}. Should be called before the state of the chosen call is changed.
	 * @param calls the list of already found calls
	 * @param trace the trace line being analyzed
	 * @param states the analysis states in which a call can be waiting for this trace
	 */
	protected void checkAmbiguousCalls(ArrayList<Call> calls, RawTrace trace, CallAnalysisState... states) {
		ArrayList<Call> waitingCalls = new ArrayList<Call>();
		for (Call call: calls) {
			for (CallAnalysisState state: states) {
				if (call.getCallAnalysisState() == state) {
					waitingCalls.add(call);
					break;
				}
			}
		}
		if (waitingCalls.size() > 1) {
			String warning = waitingCalls.size() + " calls were waiting for the same trace at " + trace.getTime() +
					", traces of this call may belong to another call";
			for (Call call: waitingCalls) {
				// Keep the first ambiguity found for each call
				if (!call.isAmbiguous())
					call.setAmbiguityWarning(warning);
			}
		}
	}
}
