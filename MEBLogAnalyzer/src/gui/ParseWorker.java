package gui;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import gui.dialog.ProgressBarDialog;
import parser.ParserProgressListener;
import parser.TraceParser;

public class ParseWorker extends SwingWorker<Void, Integer> implements ParserProgressListener {

	private MainFrame frame = null;
	private TraceParser parser = null;
	private JProgressBar progressBar = null;
	private ProgressBarDialog pbd = null;
	private String path = null;

	public ParseWorker(MainFrame frame, String path, String filename) {
		this.frame = frame;
		this.path = path;
		parser = new TraceParser(path, this);
		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);
		pbd = new ProgressBarDialog(progressBar, path);
		GUIStatic.setComponentCenterLocation(pbd);
		pbd.setVisible(true);
	}

	@Override
	protected void process(List<Integer> progress) {
		progressBar.setValue(progress.get(progress.size() - 1));
	}

	@Override
	protected Void doInBackground() throws Exception {
		parser.parse();
		return null;
	}

	@Override
	public void progress(int progress) {
		publish(progress);
	}

	@Override
	public void done() {
		pbd.dispose();
		if (parser.getCalls().size() > 0) {
			GUIStatic.textPaneStyle.print("Number of calls found: " + parser.getCalls().size(), StyledTextPane.MESSAGE);
			frame.createFrame(path, parser.getCalls());
		} else {
			JOptionPane.showMessageDialog(frame, "No calls found in the log file!", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	@Override
	public void setInfo(final String info) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pbd.setInfo(info);
			}
		});
	}
}
