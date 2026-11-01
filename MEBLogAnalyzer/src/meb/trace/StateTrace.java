package meb.trace;

import java.awt.Color;

import meb.entities.Component;

/**
 * The <code>StateTrace</code> class is an extension of a {@link BaseTrace} representing
 * a component state that should be shown in the graphical call flow.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class StateTrace extends BaseTrace {

	private String state;

	/**
	 * Creates a new {@link StateTrace} object.
	 * @param date the date that the trace was created
	 * @param component the component that printed the trace
	 * @param state the component state that should be shown in the call flow
	 * @param trace the string representation of the trace
	 * @param color the color that should be used in the graphical representation
	 */
	public StateTrace(String date, Component component, String state, String trace, Color color) {
		super(TraceType.STATE, date, component, trace, color);
		setState(state);
	}

	/**
	 * Creates a new {@link StateTrace} object. The color of the component state in the
	 * call flow will be green if this constructor is used.
	 * @param date the date that the trace was created
	 * @param component the component that printed the trace
	 * @param state the component state that should be shown in the call flow
	 * @param trace the string representation of the trace
	 */
	public StateTrace(String date, Component component, String state, String trace) {
		this(date, component, state, trace, Color.GREEN);
	}

	/**
	 * Returns the component state that should be shown in the call flow.
	 * @return the component state that should be shown in the call flow
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the component state that should be shown in the call flow to the specified value.
	 * @param state the component state that should be shown in the call flow
	 */
	public void setState(String state) {
		this.state = state;
	}
}
