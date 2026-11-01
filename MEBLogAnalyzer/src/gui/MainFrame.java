package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

	/**
	 * Flavor for dropped files given as a list of URIs, used by some Linux file managers instead of the java file list flavor.
	 */
	private static final DataFlavor uriListFlavor = createUriListFlavor();

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

	private static DataFlavor createUriListFlavor() {
		try {
			return new DataFlavor("text/uri-list;class=java.lang.String");
		} catch (ClassNotFoundException e) {
			// Cannot happen, java.lang.String is always available
			throw new IllegalStateException(e);
		}
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
			// The drop source may offer many flavors (e.g. on Linux), accept it if any of them is a file list
			for (DataFlavor flavor: transferFlavors) {
				if (flavor.equals(DataFlavor.javaFileListFlavor) || flavor.equals(uriListFlavor)) {
					return true;
				}
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
			try {
				List<File> fileList = getFileList(t);
				if (fileList == null) return false;
				for (File file: fileList) {
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
			} catch (IOException ex) {
			} catch (UnsupportedFlavorException e) {}
			return false;
		}

		/**
		 * Returns the list of dropped files. Uses the java file list flavor if available, otherwise the
		 * <code>text/uri-list</code> flavor that some Linux file managers provide instead.
		 * @param t the dropped data
		 * @return the list of dropped files, null if the data does not contain files
		 */
		private List<File> getFileList(Transferable t) throws UnsupportedFlavorException, IOException {
			List<File> files = new ArrayList<File>();
			if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				for (Object o: (List<?>) t.getTransferData(DataFlavor.javaFileListFlavor)) {
					files.add((File) o);
				}
				return files;
			}
			if (t.isDataFlavorSupported(uriListFlavor)) {
				String uriList = (String) t.getTransferData(uriListFlavor);
				for (String uri: uriList.split("\r\n|\n")) {
					uri = uri.trim();
					// Lines starting with '#' are comments in a uri-list
					if (uri.isEmpty() || uri.startsWith("#")) continue;
					try {
						files.add(new File(new URI(uri)));
					} catch (URISyntaxException | IllegalArgumentException e) {
						GUIStatic.printLog("Ignoring dropped item that is not a local file: " + uri, StyledTextPane.WARNING);
					}
				}
				return files;
			}
			return null;
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
		// Swing shows the popup on the right platform trigger (mouse press on Linux, mouse release on Windows)
		txtpnLog.setComponentPopupMenu(clearLogPopup);
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
