package meb.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import meb.trace.BaseTrace;
import parser.call.CallAnalysisState;

/**
 * The <code>Call</code> class holds all information regarding a call that has been identified
 * within the MEB logs.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class Call {

	/**
	 * Indicates that the call is incoming (from LDH to MEB)
	 */
	public static final int INCOMING = 0;
	/**
	 * Indicates that the call is outgoing (from MEB to LDH)
	 */
	public static final int OUTGOING = 1;

	/**
	 * Default value of used FAX protocol when initializing a call
	 */
	public static final int FAX_UNKNOWN = -1;

	/**
	 * Indicates that the FAX call is using G.711 protocol
	 */
	public static final int FAX_G711 = 0;

	/**
	 * Indicates that the FAX call is using T.38 protocol
	 */
	public static final int FAX_T38 = 1;

	private long callID;
	private String m5tCallID;
	private String cstaCallID;
	private int direction;
	private String inSDP;
	private String outSDP;
	private String date;
	private String endDate;
	private String establishedDate;
	private String cpuCallState;
	private String cornetCallState;
	private String callingPartyNumber;
	private String calledPartyNumber;
	private String additionalcallingPartyNumber;
	private ArrayList<BaseTrace> baseTraces;
	private boolean firstCPUMessage;
	private boolean firstCornetMessage;
	private String localRTPAddress;
	private String remoteRTPAddress;
	private boolean errorFound;
	private String errorString;
	private boolean warningFound;
	private String warningString;
	private String ambiguityWarning;
	private boolean fax;
	private boolean mwi;
	private boolean makeCall;
	private boolean consultCall;
	private boolean ssctCall;
	private boolean announcementCall;
	private int faxType;
	private CallAnalysisState callAnalysisState;

	/**
	 * Creates a new {@link Call} object with the specified callID and direction.
	 * @param callID the ID of the call
	 * @param direction the direction of the call
	 */
	public Call(long callID, int direction) {
		setCallID(callID);
		setM5TCallID(null);
		setCstaCallID(null);
		setDirection(direction);
		baseTraces = new ArrayList<BaseTrace>();
		setCPUCallState("NULL");
		setCornetCallState("0_NULL");
		setFirstCPUMessage(false);
		setFirstCornetMessage(false);
		setCallingPartyNumber("");
		setCalledPartyNumber("");
		setAdditionalcallingPartyNumber("");
		setLocalRTPAddress("");
		setRemoteRTPAddress("");
		setErrorFound(false);
		setErrorString("");
		setAmbiguityWarning("");
		setEndDate("");
		setEstablishedDate("");
		setFAX(false);
		setMWI(false);
		setMakeCall(false);
		setConsultCall(false);
		setSsctCall(false);
		setFaxType(FAX_UNKNOWN);
		if (direction == INCOMING) {
			setWarningFound(true);
			setWarningString("Call is not connected");
		} else {
			setWarningFound(false);
			setWarningString("");
		}
	}

	/**
	 * Creates a new {@link Call} object with the specified direction.
	 * @param direction the direction of the call
	 */
	public Call(int direction) {
		this(-1, direction);
	}

	/**
	 * Returns the callID of the call.
	 * @return the callID of the call
	 */
	public long getCallID() {
		return callID;
	}

	/**
	 * Sets the callID of the call to the specified value.
	 * @param callID the callID of the call
	 */
	public void setCallID(long callID) {
		this.callID = callID;
	}

	/**
	 * Returns the pre-state of the call.
	 * @return the pre-state of the call
	 */
	public CallAnalysisState getCallAnalysisState() {
		return callAnalysisState;
	}

	/**
	 * Sets the pre-state of the call to the specified value.
	 * @param callAnalysisState the pre-state of the call
	 */
	public void setCallAnalysisState(CallAnalysisState callAnalysisState) {
		this.callAnalysisState = callAnalysisState;
	}

	/**
	 * Returns the M5T callID of the call.
	 * @return the M5T callID of the call
	 */
	public String getM5TCallID() {
		return m5tCallID;
	}

	/**
	 * Sets the M5T callID of the call to the specified value.
	 * @param m5tCallID the M5T callID of the call
	 */
	public void setM5TCallID(String m5tCallID) {
		this.m5tCallID = m5tCallID;
	}

	/**
	 * Returns the CSTA callID of the call.
	 * @return the CSTA callID of the call
	 */
	public String getCstaCallID() {
		return cstaCallID;
	}

	/**
	 * Sets the CSTA callID of the call to the specified value.
	 * @param cstaCallID the CSTA callID of the call
	 */
	public void setCstaCallID(String cstaCallID) {
		this.cstaCallID = cstaCallID;
	}

	/**
	 * Returns the direction of the call.
	 * @return the direction of the call
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * Sets the direction of the call to the specified value.
	 * @param direction the direction of the call
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * Returns the incoming SDP of the call.
	 * @return the incoming SDP of the call
	 */
	public String getInSDP() {
		return inSDP;
	}

	/**
	 * Sets the incoming SDP of the call to the specified value.
	 * @param inSDP the incoming SDP of the call
	 */
	public void setInSDP(String inSDP) {
		this.inSDP = inSDP;
	}

	/**
	 * Returns the outgoing SDP of the call.
	 * @return the outgoing SDP of the call
	 */
	public String getOutSDP() {
		return outSDP;
	}

	/**
	 * Sets the outgoing SDP of the call to the specified value.
	 * @param outSDP the outgoing SDP of the call
	 */
	public void setOutSDP(String outSDP) {
		this.outSDP = outSDP;
	}

	/**
	 * Returns the start date of the call.
	 * @return the start date of the call
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the start date of the call to the specified value.
	 * @param date the start date of the call
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Returns the list of <code>BaseTrace</code> of the call.
	 * @return the list of <code>BaseTrace</code> of the call.
	 */
	public ArrayList<BaseTrace> getBaseTraces() {
		return baseTraces;
	}

	/**
	 * Sets the list of <code>BaseTrace</code> of the call to the specified value.
	 * @param baseTraces the list of <code>BaseTrace</code> of the call
	 */
	public void setBaseTraces(ArrayList<BaseTrace> baseTraces) {
		this.baseTraces = baseTraces;
	}

	/**
	 * Adds the specified <code>BaseTrace</code> in the list of traces.
	 * @param trace the <code>BaseTrace</code> to be added
	 */
	public void addBaseTrace(BaseTrace trace) {
		baseTraces.add(trace);
	}

	/**
	 * Returns the end date of the call.
	 * @return the end date of the call
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date of the call to the specified value.
	 * @param endDate the end date of the call
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * Returns the CPU call state of the call.
	 * @return the CPU call state of the call
	 */
	public String getCPUCallState() {
		return cpuCallState;
	}

	/**
	 * Sets the CPU call state of the call to the specified value.
	 * @param cpuCallState the CPU call state of the call
	 */
	public void setCPUCallState(String cpuCallState) {
		this.cpuCallState = cpuCallState;
	}

	/**
	 * Returns false if no CPU message for this call has arrived, true otherwise.
	 * @return false if no CPU message for this call has arrived, true otherwise
	 */
	public boolean isFirstCPUMessage() {
		return firstCPUMessage;
	}

	/**
	 * Sets weather a CPU message for this call has arrived to the specified value.
	 * @param firstCPUMessage should be false in no CPU message for this call has arrived, true otherwise
	 */
	public void setFirstCPUMessage(boolean firstCPUMessage) {
		this.firstCPUMessage = firstCPUMessage;
	}

	/**
	 * Returns the CorNet call state of the call.
	 * @return the CorNet call state of the call
	 */
	public String getCornetCallState() {
		return cornetCallState;
	}

	/**
	 * Sets the CorNet call state of the call to the specified value.
	 * @param cornetCallState the CorNet call state of the call
	 */
	public void setCornetCallState(String cornetCallState) {
		this.cornetCallState = cornetCallState;
	}

	/**
	 * Returns false if no CorNet message for this call has arrived, true otherwise.
	 * @return false if no CorNet message for this call has arrived, true otherwise
	 */
	public boolean isFirstCornetMessage() {
		return firstCornetMessage;
	}

	/**
	 * Sets weather a CorNet message for this call has arrived to the specified value.
	 * @param firstCornetMessage should be false in no CorNet message for this call has arrived, true otherwise
	 */
	public void setFirstCornetMessage(boolean firstCornetMessage) {
		this.firstCornetMessage = firstCornetMessage;
	}

	/**
	 * Returns the calling party number of the call.
	 * @return the calling party number of the call
	 */
	public String getCallingPartyNumber() {
		return callingPartyNumber;
	}

	/**
	 * Sets the calling party number of the call to the specified value.
	 * @param callingPartyNumber the calling party number of the call
	 */
	public void setCallingPartyNumber(String callingPartyNumber) {
		this.callingPartyNumber = callingPartyNumber;
	}

	/**
	 * Returns the local RTP address of the call.
	 * @return the local RTP address of the call
	 */
	public String getLocalRTPAddress() {
		return localRTPAddress;
	}

	/**
	 * Sets the local RTP address of the call to the specified value.
	 * @param localRTPAddress the local RTP address of the call
	 */
	public void setLocalRTPAddress(String localRTPAddress) {
		this.localRTPAddress = localRTPAddress;
	}

	/**
	 * Returns the remote RTP address of the call.
	 * @return the remote RTP address of the call
	 */
	public String getRemoteRTPAddress() {
		return remoteRTPAddress;
	}

	/**
	 * Sets the remote RTP address of the call to the specified value.
	 * @param remoteRTPAddress the remote RTP address of the call
	 */
	public void setRemoteRTPAddress(String remoteRTPAddress) {
		this.remoteRTPAddress = remoteRTPAddress;
	}

	/**
	 * Returns true if an error for this call has been found.
	 * @return true if an error for this call has been found
	 */
	public boolean isErrorFound() {
		return errorFound;
	}

	/**
	 * Sets weather an error for this call has been found to the specified value.
	 * @param errorFound should be false in no error for this call has been found, true otherwise
	 */
	public void setErrorFound(boolean errorFound) {
		this.errorFound = errorFound;
	}

	/**
	 * Returns the error message of the call.
	 * @return the error message of the call
	 */
	public String getErrorString() {
		return errorString;
	}

	/**
	 * Sets the error message of the call to the specified value.
	 * @param errorString the error message of the call
	 */
	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	/**
	 * Returns true if a warning for this call has been found.
	 * @return true if a warning for this call has been found
	 */
	public boolean isWarningFound() {
		return warningFound;
	}

	/**
	 * Sets weather a warning for this call has been found to the specified value.
	 * @param warningFound should be false in no warning for this call has been found, true otherwise
	 */
	public void setWarningFound(boolean warningFound) {
		this.warningFound = warningFound;
	}

	/**
	 * Returns the warning message of the call.
	 * @return the warning message of the call
	 */
	public String getWarningString() {
		return warningString;
	}

	/**
	 * Sets the warning message of the call to the specified value.
	 * @param warningString the warning message of the call
	 */
	public void setWarningString(String warningString) {
		this.warningString = warningString;
	}

	/**
	 * Returns the duration time of this call.
	 * @return the duration time of this call.
	 */
	public String getDuration() {
		if (endDate.length() > 0) {
			long startTime = getDateMillis(date);
			long endTime = getDateMillis(endDate);
			long totalMillis = endTime - startTime;
			int totlaSec = (int) (totalMillis / 1000);
			int totalMin = totlaSec / 60;
			int totalHour = totalMin / 60;
			return (String.format("%02d", totalHour) + ":" + String.format("%02d", totalMin % 60) + 
					":" + String.format("%02d", totlaSec % 60));
		} else return "Unknown";
	}

	/**
	 * Returns the establishment time of this call in milliseconds.
	 * @return the establishment time of this call in milliseconds.
	 */
	public long getEstablishmentDurationMillis() {
		if (establishedDate.length() > 0) {
			long startTime = getDateMillis(date);
			long endTime = getDateMillis(establishedDate);
			long totalMillis = endTime - startTime;
			return totalMillis;
		} else return 0L;
	}

	/**
	 * Returns the milliseconds representation of the provided date.
	 * @return the milliseconds representation of the provided date
	 */
	private long getDateMillis(String dateStr) {
		long result = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
			result = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Returns true if the call is a FAX call, false otherwise.
	 * @return true if the call is a FAX call, false otherwise
	 */
	public boolean isFAX() {
		return fax;
	}

	/**
	 * Sets weather this is a FAX call to the specified value.
	 * @param fax should be false if this is not a FAX call, true otherwise
	 */
	public void setFAX(boolean fax) {
		this.fax = fax;
	}

	/**
	 * Returns the FAX type of the call.
	 * @return the FAX type of the call.
	 */
	public int getFaxType() {
		return faxType;
	}

	/**
	 * Sets the FAX type of the call to the specified value.
	 * @param faxType the FAX type of the call.
	 */
	public void setFaxType(int faxType) {
		this.faxType = faxType;
	}

	/**
	 * Returns the called party number of the call.
	 * @return the called party number of the call
	 */
	public String getCalledPartyNumber() {
		return calledPartyNumber;
	}

	/**
	 * Sets the called party number of the call to the specified value.
	 * @param calledPartyNumber the called party number of the call
	 */
	public void setCalledPartyNumber(String calledPartyNumber) {
		this.calledPartyNumber = calledPartyNumber;
	}

	/**
	 * Returns true if the call is a MWI call, false otherwise.
	 * @return true if the call is a MWI call, false otherwise
	 */
	public boolean isMWI() {
		return mwi;
	}

	/**
	 * Sets weather this is a MWI call to the specified value.
	 * @param mwi should be false if this is not a MWI call, true otherwise
	 */
	public void setMWI(boolean mwi) {
		this.mwi = mwi;
	}

	/**
	 * Returns the additional calling party number of the call.
	 * @return the additional calling party number of the call
	 */
	public String getAdditionalcallingPartyNumber() {
		return additionalcallingPartyNumber;
	}

	/**
	 * Sets the additional calling party number of the call to the specified value.
	 * @param additionalcallingPartyNumber the additional calling party number of the call
	 */
	public void setAdditionalcallingPartyNumber(String additionalcallingPartyNumber) {
		this.additionalcallingPartyNumber = additionalcallingPartyNumber;
	}

	/**
	 * Returns true if the call is a MakeCall call, false otherwise.
	 * @return true if the call is a MakeCall call, false otherwise
	 */
	public boolean isMakeCall() {
		return makeCall;
	}

	/**
	 * Sets weather this is a MakeCall call to the specified value.
	 * @param makeCall should be false if this is not a MakeCall call, true otherwise
	 */
	public void setMakeCall(boolean makeCall) {
		this.makeCall = makeCall;
	}

	/**
	 * Returns true if the call is a consultation call, false otherwise.
	 * @return true if the call is a consultation call, false otherwise
	 */
	public boolean isConsultCall() {
		return consultCall;
	}

	/**
	 * Sets weather this is a consultation call to the specified value.
	 * @param consultCall should be false if this is not a consultation call, true otherwise
	 */
	public void setConsultCall(boolean consultCall) {
		this.consultCall = consultCall;
	}

	/**
	 * Returns true if the call is a single step transfer call, false otherwise.
	 * @return true if the call is a single step transfer call, false otherwise
	 */
	public boolean isSsctCall() {
		return ssctCall;
	}

	/**
	 * Sets weather this is a single step transfer call to the specified value.
	 * @param ssctCall should be false if this is not a single step transfer call, true otherwise
	 */
	public void setSsctCall(boolean ssctCall) {
		this.ssctCall = ssctCall;
	}

	/**
	 * Returns true if the call is an announcement call, false otherwise.
	 * @return true if the call is an announcement call, false otherwise
	 */
	public boolean isAnnouncementCall() {
		return announcementCall;
	}

	/**
	 * Sets weather this is an announcement call to the specified value.
	 * @param announcementCall should be false if this is not an announcement call, true otherwise
	 */
	public void setAnnouncementCall(boolean announcementCall) {
		this.announcementCall = announcementCall;
	}

	/**
	 * Returns the date of the establishment (from SIP point of view) of the call.
	 * @return the establishment date
	 */
	public String getEstablishedDate() {
		return establishedDate;
	}

	/**
	 * Sets the established date to the specified value.
	 * @param establishedDate the date of the establishment (from SIP point of view) of the call
	 */
	public void setEstablishedDate(String establishedDate) {
		this.establishedDate = establishedDate;
	}

	/**
	 * Returns true if a trace without callID may have been assigned to the wrong call, because several calls
	 * were waiting for it at the same time.
	 * @return true if the traces of this call may be mixed with another call
	 */
	public boolean isAmbiguous() {
		return ambiguityWarning.length() > 0;
	}

	/**
	 * Returns the ambiguity warning of the call. It is kept separately from the {@link #getWarningString() warning},
	 * since that one is overwritten or cleared during the analysis of the call.
	 * @return the ambiguity warning of the call, an empty string if there is none
	 */
	public String getAmbiguityWarning() {
		return ambiguityWarning;
	}

	/**
	 * Sets the ambiguity warning of the call to the specified value.
	 * @param ambiguityWarning the ambiguity warning of the call, an empty string if there is none
	 */
	public void setAmbiguityWarning(String ambiguityWarning) {
		this.ambiguityWarning = ambiguityWarning;
	}
}
