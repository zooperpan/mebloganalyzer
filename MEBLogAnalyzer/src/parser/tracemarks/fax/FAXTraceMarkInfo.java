package parser.tracemarks.fax;

import java.awt.Color;
import java.util.ArrayList;

import meb.entities.Call;
import meb.trace.h323.H323Info;
import parser.RawTrace;
import parser.tracemarks.TraceMarkAction;

/**
 * The <code>FAXTraceMarkInfo</code> class is a {@link TraceMarkAction} that analyzes 
 * traces created by the FAX component and is responsible for finding information of a FAX call.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class FAXTraceMarkInfo extends TraceMarkAction {

	private String info = null;
	private Color color = null;
	private int type = Call.FAX_UNKNOWN;

	/**
	 * Creates a new {@link FAXTraceMarkInfo} object.
	 */
	public FAXTraceMarkInfo() {
		super(false);
	}

	/**
	 * Checks if the trace contains on of the following strings: 
	 * <pre>
	 *  <code>"Created FAX session"</code>,
	 *  <code>"StartTransmission: FileName:"</code>,
	 *  <code>"FAX transmission was "</code>,
	 *  <code>"Removed FAX session"</code>
	 * </pre>
	 * In case it finds a string that represents an error, sets the color to RED. 
	 */
	@Override
	public boolean markFound(ArrayList<Call> calls, RawTrace trace) {
		String message = trace.getMessage().toString();
		info = null;
		color = Color.ORANGE;
		type = Call.FAX_UNKNOWN;
		if (message.contains("Created FAX session")) {
			info = "Create FAX session";
			return true;
		} else if (message.contains("StartTransmission: FileName:")) {
			int startIndex = message.indexOf("type: ") + "type: ".length();
			String helpString = message.substring(startIndex);
			String type = helpString.substring(0,  helpString.indexOf(" "));
			if (type.equals("G.711")) {
				this.type = Call.FAX_G711;
			} else this.type = Call.FAX_T38;
			info = "Start FAX transmission (" + type + ")";
			return true;
		} else if (message.contains("FAX transmission was ")) {
			info = message;
			if (message.contains(" not ")) {
				color = Color.RED;
			} else if (message.contains(" cancelled")) {
				if (message.contains("cancelled but ")) {
					color = Color.GREEN;
				} else {
					color = Color.ORANGE;
				}
			} else {
				color = Color.GREEN;
			}
			return true;
		} else if (message.contains("Removed FAX session")) {
			info = "Remove FAX session";
			return true;
		}
		return false;
	}

	/**
	 * Not used by this trace mark action.
	 */
	@Override
	public Call markAction(ArrayList<Call> calls, RawTrace trace) {
		return null;
	}

	/**
	 * Adds a {@link H323Info} information with the relevant color in the graph. In case of RED color,
	 * sets the information also as error text in the <code>Call</code> object.
	 */
	@Override
	public void markAction(ArrayList<Call> calls, Call call, RawTrace trace) {
		if (color.equals(Color.RED)) {
			call.setErrorFound(true);
			call.setErrorString(info);
		}
		if (type != Call.FAX_UNKNOWN) {
			call.setFaxType(type);
		}
		call.addBaseTrace(new H323Info(trace.getTime(), info, trace.getTrace(), color));
	}
}