package gui.flow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * The <code>FlowPanel</code> class is the main graphical frame of the flow. It is split
 * to a north panel that holds the {@link FlowEntity flow entities} and a center panel that
 * holds the actual flow area. The added {@link FlowObject flow objects} are handled by a 
 * {@link FlowManager}.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class FlowPanel extends ScrollablePanel {

	private static final long serialVersionUID = 1L;
	private static final int FRAME_WIDTH = 800;
	private static final int FRAME_HEIGHT = 1600;
	private FlowManager manager;
	private FlowArrow selectedArrow = null;
	private CenterPanel centerPanel;
	private NorthPanel northPanel;

	/**
	 * Creates a new {@link FlowPanel} object.
	 */
	public FlowPanel() {
		super(new BorderLayout());
		FlowArea flowArea = new FlowArea(FRAME_WIDTH, FRAME_HEIGHT, Color.WHITE);
		manager = new FlowManager(flowArea);
		centerPanel = new CenterPanel(flowArea);
		northPanel = new NorthPanel();
		setBackground(Color.WHITE);
		JScrollPane scrollPane = new JScrollPane(centerPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(15);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		setScrollableWidth(ScrollablePanel.ScrollableSizeHint.STRETCH);
		setScrollableHeight(ScrollablePanel.ScrollableSizeHint.FIT);
		add(northPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Adds a new flow object in the graph.
	 * @param object the flow object to be added
	 */
	public void addFlowObject(final FlowObject object) {
		if (object instanceof FlowArrow) {
			((FlowArrow)object).setParent(this);
			if (!manager.containsEntity(((FlowArrow)object).getFrom()))
				return;
			if (!manager.containsEntity(((FlowArrow)object).getTo()))
				return;
		}
		if (object instanceof FlowState) {
			if (!manager.containsEntity(((FlowState)object).getComponentTitle()))
				return;
		}
		if (object instanceof FlowMessage) {
			if (!manager.containsEntity(((FlowMessage)object).getComponentTitle()))
				return;
		}
		manager.addFlowObject(object);
		if (object instanceof FlowEntity) {
			centerPanel.add(((FlowEntity)object).getLine());
			northPanel.add((FlowEntity)object);
			new FlowMouseDragListener(object) {
				@Override
				public void dragEnded(Point start, Point end) {
					manager.reorderEntities(manager.getEntityPosition((FlowEntity)object), centerPanel.getWidth(), end.x - northPanel.getLocationOnScreen().x);
				}

				@Override
				public void drag(Point end) {
					object.setXPos(end.x - northPanel.getLocationOnScreen().x);
				}
			};
		} else
			centerPanel.add(object);
	}

	/**
	 * Sets the selected flow arrow and un-selects the one that was previously selected.
	 * @param instance the new selected flow arrow
	 */
	public void arrowSelected(FlowArrow instance) {
		if (selectedArrow != instance) {
			if (selectedArrow != null)
				selectedArrow.setSelected(false);
			selectedArrow = instance;
		}
	}

	/**
	 * The <code>NorthPanel</code> class is the north panel of the main flow panel.
	 * It holds all the flow entities.
	 * 
	 * @author Panos Katsikogiannis
	 *
	 */
	private class NorthPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private FlowEntity entity = null;

		/**
		 * Creates a new {@link NorthPanel} object.
		 */
		public NorthPanel() {
			super(null);
			setBackground(Color.WHITE);
			setPreferredSize(new Dimension(FRAME_WIDTH, 32));
			setSize(new Dimension(FRAME_WIDTH, 32));
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setPreferredSize(new Dimension(manager.getMinAreaWidth(), entity.calculateHeight(g)));
		}

		/**
		 * Adds a new flow entity to the north panel.
		 * @param entity the flow entity to be added
		 */
		public void add(FlowEntity entity) {
			if (this.entity == null)
				this.entity = entity;
			super.add(entity);
		}
	}

	/**
	 * The <code>CenterPanel</code> class is the center panel of the main flow panel.
	 * It holds the {@link FlowArea flow area} the creates the graphical representation of the flow.
	 * 
	 * @author Panos Katsikogiannis
	 *
	 */
	private class CenterPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		/**
		 * Creates a new {@link CenterPanel} object.
		 * @param flowArea the flow area added in the panel
		 */
		public CenterPanel(FlowArea flowArea) {
			super(null);
			add(flowArea);
			setBackground(Color.WHITE);
			addComponentListener(new ResizeListener(this));
			setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
			setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			manager.repaintObjects(getWidth());
			setPreferredSize(new Dimension(manager.getMinAreaWidth(), manager.getTotalHeight()));
		}
	}

	/**
	 * The <code>ResizeListener</code> is a component listener that is used to repaint
	 * the passed panel when resized.
	 * 
	 * @author Panos Katsikogiannis
	 *
	 */
	private class ResizeListener extends ComponentAdapter {

		private JPanel panel;

		/**
		 * Creates a new {@link ResizeListener} object.
		 * @param panel the panel to be repainted when the component is resized
		 */
		public ResizeListener(JPanel panel) {
			this.panel = panel;
		}

		@Override
		public void componentResized(ComponentEvent e) {
			panel.repaint();
		}
	}
}
