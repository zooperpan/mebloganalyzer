package meb.trace.h323;

import java.awt.Color;

import meb.entities.Component;
import meb.trace.InfoTrace;

/**
 * The <code>H323Info</code> class is an extension of a {@link InfoTrace} representing
 * information of the H323 component and should be shown in the graphical call flow.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class H323Info extends InfoTrace {

	/**
	 * Creates a new {@link H323Info} object.
	 * @param date the date that the trace was created
	 * @param info the information that should be shown in the call flow
	 * @param trace the string representation of the trace
	 * @param color the color that should be used in the graphical representation
	 */
	public H323Info(String date, String info, String trace, Color color) {
		super(date, Component.H323, info, trace, color);
	}

	/**
	 * Creates a new {@link H323Info} object.
	 * @param date the date that the trace was created
	 * @param info the information that should be shown in the call flow
	 * @param trace the string representation of the trace
	 */
	public H323Info(String date, String info, String trace) {
		super(date, Component.H323, info, trace);
	}
}
