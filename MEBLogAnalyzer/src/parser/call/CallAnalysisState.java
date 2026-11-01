package parser.call;

/**
 * An enumeration that declares the pre-state of a new call. Used in order to parse the initial SCI/CPU traces of an
 * incoming/outgoing call until the callID is found. When the callID of the call is determined, the call pre-state will be 
 * {@link CallAnalysisState#CALL_INITIATED CALL_INITIATED}
 * 
 * @author Panos Katsikogiannis
 *
 */
public enum CallAnalysisState {
	/**
	 * Initial state of the call. It means that an SCI trace line with <i>"EvInvited(): Incoming M5T-Event, current callstate: 0."</i> has been found.
	 */
	INVITED,

	/**
	 * Go to this state when the M5T callID has been found.
	 */
	M5T_CALL_ID_FOUND,

	/**
	 * Go to this state when SCI has send the 1st 200OK to M5T (after the initial empty INVITE from LDH)
	 */
	SEND_INITIAL_200OK,

	/**
	 * Go to this state when SCI receives the ACK from LDH as a response for the first 200OK. 
	 */
	ACKNOWLEDGED,

	/**
	 * Go to this state when SCI receives the re-INVITE from LDH
	 */
	REINVITED,

	/**
	 * Go to this state when SCI has sends the 100 Trying to M5T (after the re-INVITE from LDH)
	 */
	SEND_100_TRYING,

	/**
	 * Go to this state when we have parsed the incoming SDP from LDH
	 */
	DUMPING_INCOMING_SDP,

	/**
	 * Go to this state when we have parsed the outgoing SDP to LDH
	 */
	DUMPING_OUTGOING_SDP,

	/**
	 * Go to this state when SCI has send the 2nd 200OK to M5T (after the re-INVITE from LDH)
	 */
	SEND_FINAL_200OK,

	/**
	 * Go to this state when CPU receives an eAPP_MEB_Req_SendFax from VSL
	 */
	FAX_CALL_RECEIVED,

	/**
	 * Go to this state when CPU receives an eAPP_MEB_Req_SetMWI from VSL
	 */
	MWI_CALL_RECEIVED,

	/**
	 * Go to this state when CPU receives an eAPP_MEB_Req_MakeCall from VSL
	 */
	MAKE_CALL_RECEIVED,

	/**
	 * Go to this state when CPU receives an eAPP_MEB_Req_ConsultCall from VSL
	 */
	CONSULT_CALL_RECEIVED,

	/**
	 * Go to this state when MEB has created the callID for the outgoing call
	 */
	OUT_CALL_ID_CREATED,

	/**
	 * Go to this state when MEB has created the callID for the outgoing MWI call
	 */
	OUT_MWI_CALL_ID_CREATED,

	/**
	 * Go to this state when SCI has send the 1st INVITE to M5T
	 */
	OUT_SEND_INVITE,

	/**
	 * Go to this state when SCI has send the INVITE to M5T for the outgoing MWI call
	 */
	OUT_MWI_SEND_INVITE,

	/**
	 * Go to this state when SCI receives the first 200 OK from LDH
	 */
	OUT_RECEIVED_INITIAL_200OK,

	/**
	 * Go to this state when SCI has send the 2nd INVITE to M5T
	 */
	OUT_SEND_REINVITE,

	/**
	 * Go to this state when SCI receives the 2nd 200 OK from LDH
	 */
	OUT_RECEIVED_FINAL_200OK,

	/**
	 * Go to this state when the callID of the call has been determined.
	 */
	CALL_INITIATED
}
