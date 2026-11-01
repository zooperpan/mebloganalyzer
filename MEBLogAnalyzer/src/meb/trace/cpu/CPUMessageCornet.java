package meb.trace.cpu;

import java.awt.Color;

import meb.entities.Component;
import meb.trace.MessageTrace;

/**
 * The <code>CPUMessageCornet</code> class is an extension of a {@link MessageTrace} representing
 * a message that has been sent from CPU to CorNet and should be shown in the graphical 
 * call flow.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CPUMessageCornet extends MessageTrace {

	/**
	 * Creates a new {@link CPUMessageCornet} object.
	 * @param date the date that the trace was created
	 * @param message the message that should be shown in the call flow
	 * @param trace the string representation of the trace
	 * @param color the color that should be used in the graphical representation
	 */
	public CPUMessageCornet(String date, String message, String trace, Color color) {
		super(date, Component.CPU, Component.CORNET, message, trace, color);
	}

	/**
	 * Creates a new {@link CPUMessageCornet} object.
	 * @param date the date that the trace was created
	 * @param message the message that should be shown in the call flow
	 * @param trace the string representation of the trace
	 */
	public CPUMessageCornet(String date, String message, String trace) {
		super(date, Component.CPU, Component.CORNET, message, trace);
	}
}
