package meb.trace.vsl;

import java.awt.Color;

import meb.entities.Component;
import meb.trace.MessageTrace;

/**
 * The <code>VSLMessageCPU</code> class is an extension of a {@link MessageTrace} representing
 * a message that has been sent from VSL to CPU and should be shown in the graphical 
 * call flow.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class VSLMessageCPU extends MessageTrace {

	/**
	 * Creates a new {@link VSLMessageCPU} object.
	 * @param date the date that the trace was created
	 * @param message the message that should be shown in the call flow
	 * @param trace the string representation of the trace
	 * @param color the color that should be used in the graphical representation
	 */
	public VSLMessageCPU(String date, String message, String trace, Color color) {
		super(date, Component.VSL, Component.CPU, message, trace, color);
	}

	/**
	 * Creates a new {@link VSLMessageCPU} object.
	 * @param date the date that the trace was created
	 * @param message the message that should be shown in the call flow
	 * @param trace the string representation of the trace
	 */
	public VSLMessageCPU(String date, String message, String trace) {
		super(date, Component.VSL, Component.CPU, message, trace);
	}
}
