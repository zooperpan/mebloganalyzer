package meb.trace.sci;

import java.awt.Color;

import meb.entities.Component;
import meb.trace.InfoTrace;

/**
 * The <code>SCIInfo</code> class is an extension of a {@link InfoTrace} representing
 * information of the SCI component and should be shown in the graphical call flow.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class SCIInfo extends InfoTrace {

	/**
	 * Creates a new {@link SCIInfo} object.
	 * @param date the date that the trace was created
	 * @param info the information that should be shown in the call flow
	 * @param trace the string representation of the trace
	 * @param color the color that should be used in the graphical representation
	 */
	public SCIInfo(String date, String info, String trace, Color color) {
		super(date, Component.SCI, info, trace, color);
	}

	/**
	 * Creates a new {@link SCIInfo} object.
	 * @param date the date that the trace was created
	 * @param info the information that should be shown in the call flow
	 * @param trace the string representation of the trace
	 */
	public SCIInfo(String date, String info, String trace) {
		super(date, Component.SCI, info, trace);
	}
}
