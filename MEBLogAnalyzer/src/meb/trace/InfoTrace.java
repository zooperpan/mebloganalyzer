package meb.trace;

import java.awt.Color;

import meb.entities.Component;

/**
 * The <code>InfoTrace</code> class is an extension of a {@link BaseTrace} representing
 * information that should be shown in the graphical call flow.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class InfoTrace extends BaseTrace {

	private String info;

	/**
	 * Creates a new {@link InfoTrace} object.
	 * @param date the date that the trace was created
	 * @param component the component that printed the trace
	 * @param info the information that should be shown in the call flow
	 * @param trace the string representation of the trace
	 * @param color the color that should be used in the graphical representation
	 */
	public InfoTrace(String date, Component component, String info, String trace, Color color) {
		super(TraceType.INFO, date, component, trace, color);
		setInfo(info);
	}

	/**
	 * Creates a new {@link InfoTrace} object. The color of the information in the
	 * call flow will be orange if this constructor is used.
	 * @param date the date that the trace was created
	 * @param component the component that printed the trace
	 * @param info the information that should be shown in the call flow
	 * @param trace the string representation of the trace
	 */
	public InfoTrace(String date, Component component, String info, String trace) {
		this(date, component, info, trace, Color.ORANGE);
	}

	/**
	 * Returns the information that should be shown in the call flow.
	 * @return the information that should be shown in the call flow
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * Sets the information that should be shown in the call flow to the specified value.
	 * @param info the information that should be shown in the call flow
	 */
	public void setInfo(String info) {
		this.info = info;
	}
}
