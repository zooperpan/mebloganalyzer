package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import gui.dialog.CallDialog;
import gui.table.CallTable;
import gui.table.TablePopup;
import main.MEBStatic;
import meb.entities.Call;

public class CallFrame extends JInternalFrame {

	private static final long serialVersionUID = MEBStatic.serialVersionUID;
	private MainFrame frame = null;
	private ArrayList<Call> calls = null;
	private CallTable callTable = null;

	public CallFrame(MainFrame frame, String fileName, ArrayList<Call> calls) {
		super(fileName , 
				true,  // Can be resized
				true,  // Can be closed
				true,  // Can be maximized
				true); // Can be iconified
		this.frame = frame;
		this.calls = calls;

		try {
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			this.addInternalFrameListener(new InternalFrameAdapter() {
				public void internalFrameClosing(InternalFrameEvent evt) {
					// Notify main frame that this frame is closing
					frameClosing();
					dispose();
				}
			});
			jbInit();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Initialization method.
	 */
	private void jbInit() {
		JPanel contentPane = (JPanel) getContentPane();
		callTable = new CallTable(calls);
		callTable.addMouseListener(new TableMouseListener());
		contentPane.setLayout(new BorderLayout());
		contentPane.add(callTable);
		setSize(1000, 500);
		//setLocation(((id % 10) + 1) * 30, ((id % 10) + 1) * 30);
		javax.swing.Action dispose = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				frameClosing();
				dispose();
			}
		};
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "released");
		contentPane.getActionMap().put("released", dispose);
	}

	/**
	 * Notifies the main frame when this internal frame is closing.
	 */
	private void frameClosing() {
		frame.closeFrame(this);
	}

	private class TableMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent me) {
			CallDialog callDialog = null;
			if ((me.getClickCount() > 1) && (me.getButton() == MouseEvent.BUTTON1)) {
				int selectedRow = callTable.getSelectedRow();
				if (selectedRow < 0) return;
				callDialog = new CallDialog(callTable.getCall(selectedRow));
				if (callDialog != null) {
					GUIStatic.setComponentCenterLocation(callDialog);
					callDialog.setVisible(true);
				}
			}
		}

		public void mousePressed(MouseEvent me) {
			// On Linux the popup trigger is the mouse press
			if (me.isPopupTrigger()) {
				selectRowAtPoint(me);
				showPopup(me);
			}
		}

		public void mouseReleased(MouseEvent me) {
			selectRowAtPoint(me);
			// On Windows the popup trigger is the mouse release
			if (me.isPopupTrigger()) {
				showPopup(me);
			}
		}

		/**
		 * Selects the table row under the mouse, or clears the selection if there is no row there.
		 */
		private void selectRowAtPoint(MouseEvent me) {
			int r = callTable.rowAtPoint(me.getPoint());
			if (r >= 0 && r < callTable.getRowCount()) {
				callTable.setRowSelectionInterval(r, r);
			} else {
				callTable.clearSelection();
			}
		}

		/**
		 * Shows the popup with the details of the selected call.
		 */
		private void showPopup(MouseEvent me) {
			int selectedRow = callTable.getSelectedRow();
			if (selectedRow < 0) return;
			Call call = callTable.getCall(selectedRow);
			if (me.getComponent() instanceof JTable) {
				TablePopup popup = new TablePopup(call);
				popup.show(me.getComponent(), me.getX(), me.getY());
			}
		}
	}
}
