package meb.trace;

import java.awt.Color;

import meb.entities.Component;

/**
 * The <code>MessageTrace</code> class is an extension of a {@link BaseTrace} representing
 * a message that has been sent from one component to another and should be shown in the graphical 
 * call flow.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class MessageTrace extends BaseTrace {

	private String message;

	/**
	 * Creates a new {@link MessageTrace} object.
	 * @param date the date that the trace was created
	 * @param component the component that printed the trace
	 * @param destComponent the destination component that received the message
	 * @param message the message that should be shown in the call flow
	 * @param trace the string representation of the trace
	 * @param color the color that should be used in the graphical representation
	 */
	public MessageTrace(String date, Component component, Component destComponent, String message, String trace, Color color) {
		super(TraceType.MESSAGE, date, component, destComponent, trace, color);
		setMessage(message);
	}

	/**
	 * Creates a new {@link MessageTrace} object. The color of the message in the
	 * call flow will be black if this constructor is used.
	 * @param date the date that the trace was created
	 * @param component the component that printed the trace
	 * @param destComponent the destination component that received the message
	 * @param message the message that should be shown in the call flow
	 * @param trace the string representation of the trace
	 */
	public MessageTrace(String date, Component component, Component destComponent, String message, String trace) {
		this(date, component, destComponent, message, trace, Color.BLACK);
	}

	/**
	 * Returns the message that should be shown in the call flow.
	 * @return the message that should be shown in the call flow
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message that should be shown in the call flow to the specified value.
	 * @param message the message that should be shown in the call flow
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
