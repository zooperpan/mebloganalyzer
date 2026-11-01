package gui.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import main.MEBStatic;
import meb.entities.Call;

/**
 * The <code>CallTable</code> class is the main table shown to the user when the analysis of a trace has ended.
 * It contains the required information of each call that has been identified in the trace file.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class CallTable extends JPanel {

	private static final long serialVersionUID = MEBStatic.serialVersionUID;
	public static final Color bgColor = new Color (230, 230, 230);
	private JTable table;
	private ArrayList<Call> calls = null;

	public CallTable(ArrayList<Call> calls) {
		super(new GridLayout(1,0));
		this.calls = calls;
		table = new JTable(new CallTableModel(calls));
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setSelectionBackground(bgColor);
		table.setDefaultRenderer(JPanel.class, new PanelTableRenderer(true));
		table.setRowHeight(34);
		table.getColumnModel().getColumn(0).setMinWidth(85);
		table.getColumnModel().getColumn(0).setMaxWidth(85);
		table.getColumnModel().getColumn(1).setMinWidth(200);
		table.getColumnModel().getColumn(1).setMaxWidth(200);
		table.getColumnModel().getColumn(2).setMinWidth(85);
		table.getColumnModel().getColumn(2).setMaxWidth(85);
		table.getColumnModel().getColumn(6).setMinWidth(200);
		table.getColumnModel().getColumn(6).setMaxWidth(200);
		table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
	}

	/**
	 * Returns the index of the first selected row, -1 if no row is selected.
	 * @return the index of the first selected row
	 */
	public int getSelectedRow() {
		return table.getSelectedRow();
	}

	/**
	 * Deselects all selected columns and rows.
	 */
	public void clearSelection() {
		table.clearSelection();
	}

	/**
	 * Selects the rows from index0 to index1, inclusive. 
	 * @param idx1 - one end of the interval
	 * @param idx2 - the other end of the interval 
	 */
	public void  setRowSelectionInterval(int idx1, int idx2) {
		table.setRowSelectionInterval(idx1, idx2);
	}

	/**
	 * Returns the index of the row that point lies in, or -1 if the result is not in 
	 * the range [0, getRowCount()-1]. 
	 * @param p - the location of interest 
	 * @return the index of the row that point lies in, or -1 if the result is not in the 
	 * range [0, getRowCount()-1]
	 */
	public int rowAtPoint(Point p) {
		return table.rowAtPoint(p);
	}

	/**
	 * Returns the number of rows that can be shown in the JTable, given unlimited space. 
	 * If a RowSorter with a filter has been specified, the number of rows returned may differ 
	 * from that of the underlying TableModel. 
	 * @return the number of rows shown in the JTable
	 */
	public int getRowCount() {
		return table.getRowCount();
	}

	/**
	 * Adds the specified mouse listener to receive mouse events from this component. 
	 * If listener is null, no exception is thrown and no action is performed. 
	 */
	public void addMouseListener(MouseListener mouseListener) {
		table.addMouseListener(mouseListener);
	}

	public Call getCall(int row) {
		return calls.get(row);
	}

	private class CallTableModel extends AbstractTableModel {
		private static final long serialVersionUID = MEBStatic.serialVersionUID;
		private String[] columnNames = {
				"Call ID",
				"Starting Time",
				"Direction",
				"Originator",
				"Destination",
				"Features",
				"Ending Time"
		};

		private Object[][] data;

		public CallTableModel(ArrayList<Call> calls) {
			int callsSize = calls.size();
			data = new Object[callsSize][columnNames.length];
			Color defaultColor = Color.WHITE;
			Color errorColor = new Color(255, 100, 100);
			Color warningColor = new Color(255, 200, 50);
			for (int row = 0; row < callsSize; row++) {
				Call call = calls.get(row);
				Color bgColor = defaultColor;
				if (call.isErrorFound()) {
					bgColor = errorColor;
				} else if (call.isWarningFound()) {
					bgColor = warningColor;
				}
				data[row][0] = new SimpleCellPanel(String.format("%08x", call.getCallID()), bgColor);
				data[row][1] = new SimpleCellPanel(call.getDate(), bgColor);
				data[row][2] = new SimpleCellPanel((call.getDirection() == Call.INCOMING) ? "Incoming" : "Outgoing", bgColor);
				data[row][3] = new SimpleCellPanel(call.getCallingPartyNumber(), bgColor);
				if (call.getAdditionalcallingPartyNumber().length() > 0) {
					data[row][4] = new SimpleCellPanel(call.getCalledPartyNumber() + " (" +
							call.getAdditionalcallingPartyNumber() + ")", bgColor);
				} else {
					data[row][4] = new SimpleCellPanel(call.getCalledPartyNumber(), bgColor);
				}
				FeaturePanel panel = new FeaturePanel(bgColor);
				if (call.isConsultCall()) {
					panel.addFeature(FeaturePanel.FEATURE_CT);
				} else if (call.isSsctCall()) {
					if (call.isMakeCall()) {
						panel.addFeature(FeaturePanel.FEATURE_MK);
					}
					panel.addFeature(FeaturePanel.FEATURE_SSCT);
				} else if (call.isFAX()) {
					panel.addFeature(FeaturePanel.FEATURE_FAX);
					if (call.getFaxType() == Call.FAX_G711) {
						panel.addFeature(FeaturePanel.FEATURE_G711);
					} else {
						panel.addFeature(FeaturePanel.FEATURE_T38);
					}
				} else if (call.isMWI()) {
					panel.addFeature(FeaturePanel.FEATURE_MWI);
				} else if (call.isMakeCall()) {
					panel.addFeature(FeaturePanel.FEATURE_MK);
				} else if (call.isAnnouncementCall()) {
					panel.addFeature(FeaturePanel.FEATURE_ANN);
				}
				data[row][5] = panel;
				data[row][6] = new SimpleCellPanel(call.getEndDate(), bgColor);
			}
		}

		/**
		 * Returns the number of columns in the model. A JTable uses this method to determine how 
		 * many columns it should create and display by default.
		 * @return the number of columns in the model
		 */
		public int getColumnCount() {
			return columnNames.length;
		}

		/**
		 * Returns the number of rows in the model. A JTable uses this method to determine how many rows 
		 * it should display. This method should be quick, as it is called frequently during rendering. 
		 * @return the number of rows in the model
		 */
		public int getRowCount() {
			return data.length;
		}

		/**
		 * Returns a default name for the column using spreadsheet conventions: A, B, C, ... Z, AA, AB, etc. 
		 * If column cannot be found, returns an empty string.
		 * @param col - the column being queried 
		 * @return a string containing the default name of column
		 */
		public String getColumnName(int col) {
			return columnNames[col];
		}

		/**
		 * Returns the value for the cell at columnIndex and rowIndex. 
		 * @param row - the row whose value is to be queried
		 * @param col - the column whose value is to be queried 
		 * @return the value Object at the specified cell
		 */
		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		/**
		 * Returns Object.class regardless of columnIndex. 
		 * @param c - the column being queried
		 * @return the Object.class
		 */
		public Class<? extends Object> getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/**
		 * This empty implementation is provided so users don't have to implement this method if their 
		 * data model is not editable.
		 * @param value - value to assign to cell
		 * @param row - row of cell
		 * @param col - column of cell
		 */
		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}
	}
}
