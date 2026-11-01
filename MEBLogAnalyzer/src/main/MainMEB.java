package main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import gui.MainFrame;

public class MainMEB {

	public MainMEB() {
		MainFrame frame = new MainFrame();
		frame.setPreferredSize(new Dimension(1200, 680));
		frame.setSize(new Dimension(1200, 680));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
		frame.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> new MainMEB());
	}
}
