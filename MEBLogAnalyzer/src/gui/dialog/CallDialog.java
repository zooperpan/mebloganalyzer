package gui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

import gui.flow.FlowActionAdapter;
import gui.flow.FlowArrow;
import gui.flow.FlowEntity;
import gui.flow.FlowMessage;
import gui.flow.FlowObject;
import gui.flow.FlowPanel;
import gui.flow.FlowState;
import main.MEBStatic;
import meb.entities.Call;
import meb.entities.Component;
import meb.trace.BaseTrace;
import meb.trace.InfoTrace;
import meb.trace.MessageTrace;
import meb.trace.StateTrace;

public class CallDialog extends JFrame {

	private static final long serialVersionUID = MEBStatic.serialVersionUID;
	private Call call = null;
	private JTextPane tracePane = null;

	public CallDialog(Call call) {
		super();
		setTitle("Call Message Flow " + String.format("(%08x)", call.getCallID()));
		this.call = call;
		try {
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			jbInit();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			setSize((9 * screenSize.width) / 10, (9 * screenSize.height) / 10);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void jbInit() {
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BorderLayout());

		FlowPanel flowPanel = new FlowPanel();
		flowPanel.addFlowObject(new FlowEntity(Component.M5T.toString()));
		flowPanel.addFlowObject(new FlowEntity(Component.SCI.toString()));
		flowPanel.addFlowObject(new FlowEntity(Component.H323.toString()));
		flowPanel.addFlowObject(new FlowEntity(Component.CPU.toString()));
		flowPanel.addFlowObject(new FlowEntity(Component.CORNET.toString()));
		flowPanel.addFlowObject(new FlowEntity(Component.VSL.toString()));

		for (final BaseTrace bt: call.getBaseTraces()) {
			switch (bt.getType()) {
			case INFO:
				InfoTrace it = (InfoTrace) bt;
				flowPanel.addFlowObject(new FlowMessage(it.getComponent().toString(), it.getInfo(), it.getColor(), new FlowActionAdapter() {
					@Override
					public void leftClick(FlowObject source) {
						tracePane.setText(bt.getTrace());
						tracePane.setCaretPosition(0);
					}
				}));
				break;
			case MESSAGE:
				MessageTrace mt = (MessageTrace) bt;
				flowPanel.addFlowObject(new FlowArrow(mt.getComponent().toString(), mt.getDestComponent().toString(), mt.getMessage(), mt.getColor(), new FlowActionAdapter() {
					@Override
					public void leftClick(FlowObject source) {
						tracePane.setText(bt.getTrace());
						tracePane.setCaretPosition(0);
					}
				}));
				break;
			case STATE:
				StateTrace st = (StateTrace) bt;
				flowPanel.addFlowObject(new FlowState(st.getComponent().toString(), st.getState(), st.getColor(), new FlowActionAdapter() {
					@Override
					public void leftClick(FlowObject source) {
						tracePane.setText(bt.getTrace());
						tracePane.setCaretPosition(0);
					}
				}));
				break;
			default:
				break;
			}
		}

		JScrollPane scrollPane = new JScrollPane(flowPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		JPanel tracePanel = new JPanel(new BorderLayout());
		tracePane = new JTextPane();
		tracePane.setEditable(false);
		tracePane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
		JPanel noWrapPanel = new JPanel(new BorderLayout());
		noWrapPanel.add(tracePane);
		JScrollPane traceScrollPane = new JScrollPane(noWrapPanel);
		traceScrollPane.getVerticalScrollBar().setUnitIncrement(15);
		traceScrollPane.getHorizontalScrollBar().setUnitIncrement(15);
		tracePanel.add(traceScrollPane);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, tracePanel);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		splitPane.setSize((9 * screenSize.width) / 10, (8 * screenSize.height) / 10);
		splitPane.setDividerLocation(0.75d);
		contentPane.add(splitPane, BorderLayout.CENTER);

		javax.swing.Action dispose = new AbstractAction() {
			private static final long serialVersionUID = MEBStatic.serialVersionUID;
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "released");
		contentPane.getActionMap().put("released", dispose);
	}
}
