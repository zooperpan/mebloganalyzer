package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;

import main.MEBStatic;
import meb.entities.Call;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = MEBStatic.serialVersionUID;

	private JDesktopPane desktop = null;
	private LinkedList<CallFrame> callFrames = null;

	public MainFrame() {
		super("MEB Trace Analyzer");
		callFrames = new LinkedList<CallFrame>();
		setMinimumSize(new Dimension(1200, 800));
		setTitle("MEB Trace Analyzer");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				MEBStatic.exitApplication();
			}
		});
		setupGUI();
	}

	private void setupGUI() {
		desktop = new JDesktopPane();
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BorderLayout());
		desktop.setBackground(contentPane.getBackground());
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		desktop.setTransferHandler(new MEBTraceTransferHandler());
		contentPane.add(desktop, BorderLayout.CENTER);
		// Create the menu bar
		setJMenuBar(createMenuBar(desktop));
		// Create the logging area
		contentPane.add(createLogArea(), BorderLayout.SOUTH);
	}

	private JMenuBar createMenuBar(final JDesktopPane desktop) {
		JMenuBar menuBar = new JMenuBar();
		return menuBar;
	}

	private class MEBTraceTransferHandler extends TransferHandler {
		private static final long serialVersionUID = MEBStatic.serialVersionUID;

		/**
		 * Indicates whether a component would accept an import of the given set of data flavors prior to 
		 * actually attempting to import it.
		 * @param comp - the component to receive the transfer.
		 * @param transferFlavors - the data formats available.
		 */
		public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
			if (transferFlavors.length != 1) 
				return false;
			DataFlavor flavor = transferFlavors[0];
			if (flavor.equals(DataFlavor.javaFileListFlavor)) {
				return true;
			}
			// Didn't find any that match, so:
			return false;
		}

		/**
		 * Causes a transfer to a component from a clipboard or a DND drop operation. 
		 * The Transferable represents the data to be imported into the component.
		 * @param comp - the component to receive the transfer.
		 * @param t - the data to import.
		 */
		public boolean importData(JComponent comp, Transferable t) {
			DataFlavor[] flavors = t.getTransferDataFlavors();
			if (flavors.length > 1) return false;
			DataFlavor flavor = flavors[0];
			try {
				if (flavor.equals(DataFlavor.javaFileListFlavor)) {
					List<?> fileList = (List<?>) t.getTransferData(DataFlavor.javaFileListFlavor);
					for (int i = 0; i < fileList.size(); i++) {
						File file = (File) fileList.get(i);
						String path = file.getCanonicalPath();
						String filename = file.getName();
						GUIStatic.printLog("Please wait while trying to analyze file " + path, StyledTextPane.MESSAGE);
						javax.swing.SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								(new ParseWorker(getFrame(), path, filename)).execute();
							}
						});
					}
					return true;
				} else return false;
			} catch (IOException ex) {
			} catch (UnsupportedFlavorException e) {}
			return false;
		}
	}

	private JPanel createLogArea() {
		// Create LOG panel and text Pane
		final JTextPane txtpnLog = new JTextPane();
		GUIStatic.textPaneStyle = new StyledTextPane(txtpnLog);
		final JMenuItem clearLogMenuItem = new JMenuItem("Clear");
		final JPopupMenu clearLogPopup = new JPopupMenu();
		clearLogMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUIStatic.textPaneStyle.clear();
			}
		});
		clearLogPopup.add(clearLogMenuItem);
		txtpnLog.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if(e.isPopupTrigger()) {
					clearLogPopup.show(txtpnLog, e.getX(), e.getY());
				}
			}
		});
		txtpnLog.setEditable(false);
		JPanel logPanel = new JPanel(new BorderLayout());
		logPanel.setPreferredSize(new Dimension(700, 200));
		logPanel.setBorder(BorderFactory.createTitledBorder("LOG"));
		JScrollPane logScrollPane = new JScrollPane(txtpnLog);
		logPanel.add(logScrollPane, BorderLayout.CENTER);
		return logPanel;
	}

	public MainFrame getFrame() {
		return this;
	}

	public void createFrame(String filename, ArrayList<Call> calls) {
		CallFrame callFrame = new CallFrame(this, filename, calls);
		callFrame.setVisible(true);
		callFrames.add(callFrame);
		desktop.add(callFrame);
		try {
			callFrame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {}
	}

	public void closeFrame(CallFrame callFrame) {
		callFrames.remove(callFrame);
	}
}
