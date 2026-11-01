package meb.trace.cpu;

import java.awt.Color;

import meb.entities.Component;
import meb.trace.StateTrace;

/**
 * The <code>CPUState</code> class is an extension of a {@link StateTrace} representing
 * the state of the CPU component and should be shown in the graphical call flow.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CPUState extends StateTrace {

	/**
	 * Creates a new {@link CPUState} object.
	 * @param date the date that the trace was created
	 * @param state the component state that should be shown in the call flow
	 * @param trace the string representation of the trace
	 * @param color the color that should be used in the graphical representation
	 */
	public CPUState(String date, String state, String trace, Color color) {
		super(date, Component.CPU, state, trace, color);
	}

	/**
	 * Creates a new {@link CPUState} object.
	 * @param date the date that the trace was created
	 * @param state the component state that should be shown in the call flow
	 * @param trace the string representation of the trace
	 */
	public CPUState(String date, String state, String trace) {
		super(date, Component.CPU, state, trace);
	}
}
