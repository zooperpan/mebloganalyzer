package meb.trace;

import java.awt.Color;

import meb.entities.Component;

/**
 * The <code>BaseTrace</code> class is a basic object holding information regarding the graphical
 * representation of a trace. Parsing of this class can be evaluated in order to create a graphical 
 * call flow. It is extended to more specific objects representing corresponding graphical elements.
 * 
 * @author Panos Katsikogiannis
 * 
 * @see {@link InfoTrace}
 * @see {@link MessageTrace}
 * @see {@link StateTrace}
 *
 */
public class BaseTrace {

	private TraceType type;
	private Component component;
	private Component destComponent;
	private String date;
	private String trace;
	private Color color;

	/**
	 * Creates a new {@link BaseTrace} object.
	 * @param type the type of the <code>BaseTrace</code>
	 * @param date the date that the trace was created
	 * @param component the component that printed the trace
	 * @param destComponent the destination component in case of a message between two components
	 * @param trace the string representation of the trace
	 * @param color the color that should be used in the graphical representation
	 */
	public BaseTrace(TraceType type, String date, Component component, Component destComponent, String trace, Color color) {
		setType(type);
		setDate(date);
		setComponent(component);
		setDestComponent(destComponent);
		setTrace(trace);
		setColor(color);
	}

	/**
	 * Creates a new {@link BaseTrace} object.
	 * @param type the type of the <code>BaseTrace</code>
	 * @param date the date that the trace was created
	 * @param component the component that printed the trace
	 * @param trace the string representation of the trace
	 * @param color the color that should be used in the graphical representation
	 */
	public BaseTrace(TraceType type, String date, Component component, String trace, Color color) {
		this(type, date, component, null, trace, color);
	}

	/**
	 * Returns the type of the trace.
	 * @return the type of the trace
	 */
	public TraceType getType() {
		return type;
	}

	/**
	 * Sets the type of the trace to the specified value.
	 * @param type the type of the trace
	 */
	public void setType(TraceType type) {
		this.type = type;
	}

	/**
	 * Returns the component that created the trace.
	 * @return the component that created the trace
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * Sets the component that created the trace to the specified value.
	 * @param component the component that created the trace
	 */
	public void setComponent(Component component) {
		this.component = component;
	}

	/**
	 * Returns the date that the trace was created.
	 * @return the date that the trace was created
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date that the trace was created to the specified value.
	 * @param date the date that the trace was created
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Returns the destination component in case of a message between two components.
	 * @return the destination component in case of a message between two components
	 */
	public Component getDestComponent() {
		return destComponent;
	}

	/**
	 * Sets the the destination component in case of a message between two components to the specified value.
	 * @param destComponent the destination component in case of a message between two components
	 */
	public void setDestComponent(Component destComponent) {
		this.destComponent = destComponent;
	}

	/**
	 * Returns the string representation of the trace.
	 * @return the string representation of the trace
	 */
	public String getTrace() {
		return trace;
	}

	/**
	 * Sets the string representation of the trace to the specified value.
	 * @param trace the string representation of the trace
	 */
	public void setTrace(String trace) {
		this.trace = trace;
	}

	/**
	 * Returns the color that should be used in the graphical representation.
	 * @return the color that should be used in the graphical representation
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color that should be used in the graphical representation to the specified value.
	 * @param color the color that should be used in the graphical representation
	 */
	public void setColor(Color color) {
		this.color = color;
	}
}
