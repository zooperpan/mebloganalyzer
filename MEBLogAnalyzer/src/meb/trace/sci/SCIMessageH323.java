package meb.trace.sci;

import java.awt.Color;

import meb.entities.Component;
import meb.trace.MessageTrace;

/**
 * The <code>SCIMessageH323</code> class is an extension of a {@link MessageTrace} representing
 * a message that has been sent from SCI to H323 and should be shown in the graphical 
 * call flow.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCIMessageH323 extends MessageTrace {

	/**
	 * Creates a new {@link SCIMessageH323} object.
	 * @param date the date that the trace was created
	 * @param message the message that should be shown in the call flow
	 * @param trace the string representation of the trace
	 * @param color the color that should be used in the graphical representation
	 */
	public SCIMessageH323(String date, String message, String trace, Color color) {
		super(date, Component.SCI, Component.H323, message, trace, color);
	}

	/**
	 * Creates a new {@link SCIMessageH323} object.
	 * @param date the date that the trace was created
	 * @param message the message that should be shown in the call flow
	 * @param trace the string representation of the trace
	 */
	public SCIMessageH323(String date, String message, String trace) {
		super(date, Component.SCI, Component.H323, message, trace);
	}
}
