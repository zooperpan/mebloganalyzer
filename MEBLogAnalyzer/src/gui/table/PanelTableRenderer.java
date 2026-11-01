package gui.table;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import gui.GUIStatic;

public class PanelTableRenderer implements TableCellRenderer {

	private Border unselectedBorder = null;
	private Border selectedBorder = null;
	private boolean isBordered = true;

	public PanelTableRenderer(boolean isBordered) {
		this.isBordered = isBordered;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (!(value instanceof SimpleCellPanel)) {
			return null;
		}
		SimpleCellPanel panel = (SimpleCellPanel)value;
		if (isBordered) {
			if (isSelected) {
				if (selectedBorder == null) {
					selectedBorder = BorderFactory.createMatteBorder(2, 2, 2, 2, table.getSelectionBackground());
				}
				panel.setBackground(GUIStatic.blend(table.getSelectionBackground(), panel.getDefaultBGColor()));
				panel.setBorder(selectedBorder);
			} else {
				if (unselectedBorder == null) {
					unselectedBorder = BorderFactory.createMatteBorder(2, 2, 2, 2, table.getBackground());
				}
				panel.setBackground(panel.getDefaultBGColor());
				panel.setBorder(unselectedBorder);
			}
		}
		return panel;
	}
}
