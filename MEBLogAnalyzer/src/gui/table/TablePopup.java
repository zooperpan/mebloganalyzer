package gui.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import main.MEBStatic;
import meb.entities.Call;

public class TablePopup extends JPopupMenu {

	private static final long serialVersionUID = MEBStatic.serialVersionUID;;

	public TablePopup(Call call) {
		super();
		jbInit(call);
	}

	/**
	 * Initialization method.
	 */
	private void jbInit(Call call) {
		int x = 0, y = 0;

		this.setBackground(new Color(220, 220, 220));
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(new Color(220, 220, 220));
		// Starting time
		panel.add(new JLabel("Starting time:"), new GridBagConstraints (x, y, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		JLabel label = new JLabel(call.getDate());
		label.setOpaque(false);
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		panel.add(label, new GridBagConstraints (x + 1, y++, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		// Ending time
		panel.add(new JLabel("Ending time:"), new GridBagConstraints (x, y, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		label = new JLabel(call.getEndDate());
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		panel.add(label, new GridBagConstraints (x + 1, y++, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		// Duration
		panel.add(new JLabel("Duration:"), new GridBagConstraints (x, y, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		label = new JLabel(call.getDuration());
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		panel.add(label, new GridBagConstraints (x + 1, y++, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		// Establishment duration
		panel.add(new JLabel("Establishment:"), new GridBagConstraints (x, y, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		label = new JLabel(String.format("%dms", call.getEstablishmentDurationMillis()));
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		panel.add(label, new GridBagConstraints (x + 1, y++, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		// Direction
		panel.add(new JLabel("Direction:"), new GridBagConstraints (x, y, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		label = new JLabel(call.getDirection() == Call.INCOMING ? "Incoming" : "Outgoing");
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		panel.add(label, new GridBagConstraints (x + 1, y++, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		// Originator
		panel.add(new JLabel("Originator:"), new GridBagConstraints (x, y, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		label = new JLabel(call.getCallingPartyNumber());
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		panel.add(label, new GridBagConstraints (x + 1, y++, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		// CallID
		panel.add(new JLabel("Call ID:"), new GridBagConstraints (x, y, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		label = new JLabel(String.format("%08x", call.getCallID()));
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		panel.add(label, new GridBagConstraints (x + 1, y++, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		// M5T call ID
		panel.add(new JLabel("M5T call ID:"), new GridBagConstraints (x, y, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		label = new JLabel(call.getM5TCallID());
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		panel.add(label, new GridBagConstraints (x + 1, y++, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		// CSTA call ID
		panel.add(new JLabel("CSTA call ID:"), new GridBagConstraints (x, y, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		label = new JLabel(call.getCstaCallID());
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		panel.add(label, new GridBagConstraints (x + 1, y++, 1, 1, 
				1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		if (!call.isMWI()) {
			// Local RTP Address
			panel.add(new JLabel("Local RTP Address:"), new GridBagConstraints (x, y, 1, 1, 
					1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
					GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
			label = new JLabel(call.getLocalRTPAddress());
			label.setFont(new Font("Arial", Font.PLAIN, 12));
			panel.add(label, new GridBagConstraints (x + 1, y++, 1, 1, 
					1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
					GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
			// Remote RTP Address
			panel.add(new JLabel("Remote RTP Address:"), new GridBagConstraints (x, y, 1, 1, 
					1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
					GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
			label = new JLabel(call.getRemoteRTPAddress());
			label.setFont(new Font("Arial", Font.PLAIN, 12));
			panel.add(label, new GridBagConstraints (x + 1, y++, 1, 1, 
					1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
					GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		}
		// Error
		if (call.isErrorFound()) {
			panel.add(new JLabel("Error:"), new GridBagConstraints (x, y, 1, 1, 
					1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
					GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
			label = new JLabel(call.getErrorString());
			label.setFont(new Font("Arial", Font.PLAIN, 12));
			label.setForeground(Color.RED);
			panel.add(label, new GridBagConstraints (x + 1, y++, 1, 1, 
					1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
					GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		} else if (call.isWarningFound()) {
			// Warning
			panel.add(new JLabel("Warning:"), new GridBagConstraints (x, y, 1, 1, 
					1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
					GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
			label = new JLabel(call.getWarningString());
			label.setFont(new Font("Arial", Font.PLAIN, 12));
			label.setForeground(new Color(200, 150, 50));
			panel.add(label, new GridBagConstraints (x + 1, y++, 1, 1, 
					1.0, 1.0, GridBagConstraints.BASELINE_LEADING,
					GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		}
		this.add(panel);
	}
}