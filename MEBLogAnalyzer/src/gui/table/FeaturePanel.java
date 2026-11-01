package gui.table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import main.MEBStatic;

public class FeaturePanel extends SimpleCellPanel {

	private static final long serialVersionUID = MEBStatic.serialVersionUID;
	private JPanel basePanel;
	private int x = 0;
	public static final int FEATURE_CT		= 0;
	public static final Color COLOR_CT		= Color.CYAN;
	public static final int FEATURE_SSCT	= 1;
	public static final Color COLOR_SSCT	= Color.LIGHT_GRAY;
	public static final int FEATURE_FAX		= 2;
	public static final Color COLOR_FAX		= Color.MAGENTA;
	public static final int FEATURE_MWI		= 3;
	public static final Color COLOR_MWI		= Color.PINK;
	public static final int FEATURE_G711	= 4;
	public static final Color COLOR_G711	= Color.YELLOW;
	public static final int FEATURE_T38		= 5;
	public static final Color COLOR_T38		= Color.GREEN;
	public static final int FEATURE_MK		= 6;
	public static final Color COLOR_MK		= Color.ORANGE;
	public static final int FEATURE_ANN		= 7;
	public static final Color COLOR_ANN		= new Color(150, 150, 240);

	public FeaturePanel(Color defaultBGColor) {
		super(defaultBGColor);
		basePanel = new JPanel(new GridBagLayout());
		basePanel.setBackground(defaultBGColor);
		basePanel.setOpaque(true);
		add(basePanel);
	}

	public void addFeature(int feature) {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(true);
		panel.setPreferredSize(new Dimension(40, 20));
		panel.setSize(getPreferredSize());
		switch (feature) {
		case FEATURE_CT:
			panel.setBackground(COLOR_CT);
			panel.add(new JLabel("CT"));
			break;
		case FEATURE_SSCT:
			panel.setBackground(COLOR_SSCT);
			panel.add(new JLabel("SSCT"));
			break;
		case FEATURE_FAX:
			panel.setBackground(COLOR_FAX);
			panel.add(new JLabel("FAX"));
			break;
		case FEATURE_MWI:
			panel.setBackground(COLOR_MWI);
			panel.add(new JLabel("MWI"));
			break;
		case FEATURE_G711:
			panel.setBackground(COLOR_G711);
			panel.add(new JLabel("G.711"));
			break;
		case FEATURE_T38:
			panel.setBackground(COLOR_T38);
			panel.add(new JLabel("T.38"));
			break;
		case FEATURE_MK:
			panel.setBackground(COLOR_MK);
			panel.add(new JLabel("MK"));
			break;
		case FEATURE_ANN:
			panel.setBackground(COLOR_ANN);
			panel.add(new JLabel("ANN"));
			break;
		default:
			return;
		}
		basePanel.add(panel, new GridBagConstraints(x++, 0, 1, 1,
				1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 1, 0, 1), 0, 0));
	}
}
