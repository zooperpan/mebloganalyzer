package gui.table;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import main.MEBStatic;

public class SimpleCellPanel extends JPanel {

	private static final long serialVersionUID = MEBStatic.serialVersionUID;
	private Color defaultBGColor = null;

	public SimpleCellPanel(String text, Color defaultBGColor) {
		super(new FlowLayout());
		setDefaultBGColor(defaultBGColor);
		JLabel label = new JLabel(text);
		label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
		add(label);
	}

	public SimpleCellPanel(Color defaultBGColor) {
		super(new FlowLayout());
		setDefaultBGColor(defaultBGColor);
	}

	public Color getDefaultBGColor() {
		return defaultBGColor;
	}

	public void setDefaultBGColor(Color defaultBGColor) {
		this.defaultBGColor = defaultBGColor;
	}
}
