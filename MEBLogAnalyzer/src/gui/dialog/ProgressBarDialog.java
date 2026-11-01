package gui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import main.MEBStatic;

public class ProgressBarDialog extends JDialog {

	private static final long serialVersionUID = MEBStatic.serialVersionUID;
	
	public ProgressBarDialog(JProgressBar progressBar, String title) {
		super();
		setResizable(false);
		setPreferredSize(new Dimension(600, 70));
		setTitle(title);
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(progressBar, BorderLayout.CENTER);
		pack();
	}

	public void setInfo(String info) {
		// TODO Auto-generated method stub
		
	}
}
