package meb.trace.cpu;

import java.awt.Color;

import meb.entities.Component;
import meb.trace.InfoTrace;

/**
 * The <code>CPUInfo</code> class is an extension of a {@link InfoTrace} representing
 * information of the CPU component and should be shown in the graphical call flow.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CPUInfo extends InfoTrace {

	/**
	 * Creates a new {@link CPUInfo} object.
	 * @param date the date that the trace was created
	 * @param info the information that should be shown in the call flow
	 * @param trace the string representation of the trace
	 * @param color the color that should be used in the graphical representation
	 */
	public CPUInfo(String date, String info, String trace, Color color) {
		super(date, Component.CPU, info, trace, color);
	}

	/**
	 * Creates a new {@link CPUInfo} object.
	 * @param date the date that the trace was created
	 * @param info the information that should be shown in the call flow
	 * @param trace the string representation of the trace
	 */
	public CPUInfo(String date, String info, String trace) {
		super(date, Component.CPU, info, trace);
	}
}
