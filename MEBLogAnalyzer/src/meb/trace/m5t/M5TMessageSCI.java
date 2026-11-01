package meb.trace.m5t;

import java.awt.Color;

import meb.entities.Component;
import meb.trace.MessageTrace;

/**
 * The <code>M5TMessageSCI</code> class is an extension of a {@link MessageTrace} representing
 * a message that has been sent from M5T to SCI and should be shown in the graphical 
 * call flow.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class M5TMessageSCI extends MessageTrace {

	/**
	 * Creates a new {@link M5TMessageSCI} object.
	 * @param date the date that the trace was created
	 * @param message the message that should be shown in the call flow
	 * @param trace the string representation of the trace
	 * @param color the color that should be used in the graphical representation
	 */
	public M5TMessageSCI(String date, String message, String trace, Color color) {
		super(date, Component.M5T, Component.SCI, message, trace, color);
	}

	/**
	 * Creates a new {@link M5TMessageSCI} object.
	 * @param date the date that the trace was created
	 * @param message the message that should be shown in the call flow
	 * @param trace the string representation of the trace
	 */
	public M5TMessageSCI(String date, String message, String trace) {
		super(date, Component.M5T, Component.SCI, message, trace);
	}
}
