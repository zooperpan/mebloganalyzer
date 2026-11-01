package parser;

/**
 * The <code>RawTrace</code> class holds all needed information of a trace line created by the MEB application.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class RawTrace {

	private String time;
	private String component;
	private int level;
	private long callID;
	private StringBuffer message;

	/**
	 * Creates a new {@link RawTrace} object.
	 * @param time the time that the trace was printed
	 * @param component the component that printed the trace
	 * @param level the trace level
	 * @param callID the callID that the trace belongs to (if any)
	 * @param message the actual message that the component printed (if any)
	 */
	public RawTrace(String time, String component, int level, long callID, StringBuffer message) {
		setTime(time);
		setComponent(component);
		setLevel(level);
		setCallID(callID);
		setMessage(message);
	}

	/**
	 * Returns the time that the trace was printed.
	 * @return the time that the trace was printed
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Sets the time that the trace was printed to the specified value.
	 * @param time the time that the trace was printed
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * Returns the component that printed the trace.
	 * @return the component that printed the trace
	 */
	public String getComponent() {
		return component;
	}

	/**
	 * Sets the component that printed the trace to the specified value.
	 * @param component the component printed the trace
	 */
	public void setComponent(String component) {
		this.component = component;
	}

	/**
	 * Returns the trace level.
	 * @return the trace level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets the trace level to the specified value.
	 * @param level the trace level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Returns the callID that the trace belongs to.
	 * @return the callID that the trace belongs to
	 */
	public long getCallID() {
		return callID;
	}

	/**
	 * Sets the callID that the trace belongs to the specified value.
	 * @param callID the callID that the trace belongs
	 */
	public void setCallID(long callID) {
		this.callID = callID;
	}

	/**
	 * Returns the actual message that the component printed.
	 * @return the actual message that the component printed
	 */
	public StringBuffer getMessage() {
		return message;
	}

	/**
	 * Sets the actual message that the component printed to the specified value.
	 * @param message the actual message that the component printed
	 */
	public void setMessage(StringBuffer message) {
		this.message = message;
	}

	/**
	 * Appends a string in the actual message that the component printed.
	 * @param message the string to be appended
	 */
	public void appendMessage(String message) {
		this.message.append(message);
	}

	/**
	 * Returns a visual representation of the trace (containing the time and message).
	 * @return a visual representation of the trace
	 */
	public String getTrace() {
		return (time + " " + message.toString());
	}
}
