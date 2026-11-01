package meb.trace.m5t;

import java.awt.Color;

import meb.entities.Component;
import meb.trace.InfoTrace;

/**
 * The <code>M5TInfo</code> class is an extension of a {@link InfoTrace} representing
 * information of the M5T component and should be shown in the graphical call flow.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class M5TInfo extends InfoTrace {

	/**
	 * Creates a new {@link M5TInfo} object.
	 * @param date the date that the trace was created
	 * @param info the information that should be shown in the call flow
	 * @param trace the string representation of the trace
	 * @param color the color that should be used in the graphical representation
	 */
	public M5TInfo(String date, String info, String trace, Color color) {
		super(date, Component.M5T, info, trace, color);
	}

	/**
	 * Creates a new {@link M5TInfo} object.
	 * @param date the date that the trace was created
	 * @param info the information that should be shown in the call flow
	 * @param trace the string representation of the trace
	 */
	public M5TInfo(String date, String info, String trace) {
		super(date, Component.M5T, info, trace);
	}
}
