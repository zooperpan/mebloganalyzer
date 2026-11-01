package meb.entities;

/**
 * An enumeration of all the components in MEB that can print traces.
 * @author Panos Katsikogiannis
 *
 */
public enum Component {
	/**
	 * Used to indicate that a trace has been created by the H323 component.
	 */
	H323,
	
	/**
	 * Used to indicate that a trace has been created by the CPU component.
	 */
	CPU,
	
	/**
	 * Used to indicate that a trace has been created by the CORNET component.
	 */
	CORNET,
	
	/**
	 * Used to indicate that a trace has been created by the SCI component.
	 */
	SCI,

	/**
	 * Used to indicate that a trace has been created for the M5T component. There is no M5T
	 * component that creates traces in MEB, but we need it in order to show the graphical representation
	 * of the messages from/to LDH.
	 */
	M5T,
	
	/**
	 * Used to indicate that a trace has been created by the AUDIO component.
	 */
	AUDIO,
	
	/**
	 * Used to indicate that a trace has been created for the VSL component. There is no VSL
	 * component that creates traces in MEB, but we need it in order to show the graphical representation
	 * of the messages from/to VisualSuite.
	 */
	VSL,
	
	/**
	 * Used to indicate that a trace has been created by the FAX component.
	 */
	FAX,
	
	/**
	 * Used to indicate that a trace has been created by an UNKNOWN component (should not happen).
	 */
	UNKNOWN;

	/**
	 * Returns the {@link Component} that created the trace based on the provided string.
	 * @param s the string in the trace declaring the component that created it
	 * @return the found component
	 */
	public static Component getComponent(String s) {
		if (s.equals("H323") || s.equals("H323 Ses")) {
			return H323;
		} else if (s.equals("CP") || s.equals("CP Call")) {
			return CPU;
		} else if (s.equals("CorNet")) {
			return CORNET;
		} else if (s.equals("SCI")) {
			return SCI;
		} else if (s.equals("Ses Audio") || s.equals("Wave Hdlr")) {
			return AUDIO;
		} else if (s.equals("FAX")) {
			return FAX;
		}
		return UNKNOWN;
	}
}
