package meb.trace.sci;

import java.awt.Color;

import meb.entities.Component;
import meb.trace.MessageTrace;

/**
 * The <code>SCIMessageM5T</code> class is an extension of a {@link MessageTrace} representing
 * a message that has been sent from SCI to M5T and should be shown in the graphical 
 * call flow.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCIMessageM5T extends MessageTrace {

	/**
	 * Creates a new {@link SCIMessageM5T} object.
	 * @param date the date that the trace was created
	 * @param message the message that should be shown in the call flow
	 * @param trace the string representation of the trace
	 * @param color the color that should be used in the graphical representation
	 */
	public SCIMessageM5T(String date, String message, String trace, Color color) {
		super(date, Component.SCI, Component.M5T, message, trace, color);
	}

	/**
	 * Creates a new {@link SCIMessageM5T} object.
	 * @param date the date that the trace was created
	 * @param message the message that should be shown in the call flow
	 * @param trace the string representation of the trace
	 */
	public SCIMessageM5T(String date, String message, String trace) {
		super(date, Component.SCI, Component.M5T, message, trace);
	}
}
