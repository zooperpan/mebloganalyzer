package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

public class GUIStatic {

	public static StyledTextPane textPaneStyle;

	public static void setComponentCenterLocation(Component component) {
		Dimension componentSize = component.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (componentSize.height > screenSize.height) componentSize.height = screenSize.height;
		if (componentSize.width > screenSize.width) componentSize.width = screenSize.width;
		component.setLocation((screenSize.width - componentSize.width) / 2,
				(screenSize.height - componentSize.height) / 2);
	}

	public static void printLog(final String text, final String type) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textPaneStyle.print(text, type);
			}
		});
	}

	public static Color blend(Color c0, Color c1) {
		double totalAlpha = c0.getAlpha() + c1.getAlpha();
		double weight0 = c0.getAlpha() / totalAlpha;
		double weight1 = c1.getAlpha() / totalAlpha;

		double r = weight0 * c0.getRed() + weight1 * c1.getRed();
		double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
		double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
		double a = Math.max(c0.getAlpha(), c1.getAlpha());

		return new Color((int) r, (int) g, (int) b, (int) a);
	}
}
