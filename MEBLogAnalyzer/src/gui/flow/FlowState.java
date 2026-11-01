package gui.flow;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 * The <code>FlowState</code> class is a flow object that represents the state of
 * a flow entity.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class FlowState extends FlowObject {

	private static final long serialVersionUID = -6580332018053654582L;
	private static final int BOX_HORIZONTAL_GAP = 40;
	private static final int BOX_VERTICAL_GAP = 20;
	private final Font font = new Font("Arial", Font.BOLD, 12);
	private boolean sizeReady = false;
	private String state;
	private String componentTitle;
	private int stateXPos;
	private int stateYPos;
	private int boxWidth;
	private int boxHeight;

	/**
	 * Creates a new {@link FlowState} object.
	 * @param componentTitle the name of the flow entity that this state flow object belongs to
	 * @param state the state's name
	 * @param color the color of the state flow object
	 * @param actionListener an action listener for mouse events on the state flow object
	 */
	public FlowState(String componentTitle, String state, Color color, FlowActionListener actionListener) {
		super(BOX_VERTICAL_GAP + 12, BOX_VERTICAL_GAP + 12, color, actionListener);
		setOpaque(true);
		this.state = state;
		this.componentTitle = componentTitle;
	}

	/**
	 * Creates a new {@link FlowState} object.
	 * @param componentTitle the name of the flow entity that this state flow object belongs to
	 * @param state the state's name
	 * @param color the color of the state flow object
	 */
	public FlowState(String componentTitle, String state, Color color) {
		this(componentTitle, state, color, null);
	}

	/**
	 * Creates a new {@link FlowState} object.
	 * @param componentTitle the name of the flow entity that this state flow object belongs to
	 * @param state the state's name
	 * @param actionListener an action listener for mouse events on the state flow object
	 */
	public FlowState(String componentTitle, String state, FlowActionListener actionListener) {
		this(componentTitle, state, Color.GREEN, actionListener);
	}

	/**
	 * Creates a new {@link FlowState} object.
	 * @param componentTitle the name of the flow entity that this state flow object belongs to
	 * @param state the state's name
	 */
	public FlowState(String componentTitle, String state) {
		this(componentTitle, state, Color.GREEN);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		if (!sizeReady) {
			g2.setFont(font);
			FontMetrics fm = g2.getFontMetrics();
			boxWidth = fm.stringWidth(getState()) + BOX_HORIZONTAL_GAP;
			boxHeight = fm.getMaxAscent() + BOX_VERTICAL_GAP;
			setWidth(fm.stringWidth(getState()) + BOX_HORIZONTAL_GAP);
			stateXPos = BOX_HORIZONTAL_GAP / 2;
			stateYPos = fm.getMaxAscent() + (BOX_VERTICAL_GAP / 2);
			sizeReady = true;
		}
		g2.setColor(getColor());
		g2.clearRect(0, 0, getWidth(), getHeight());
		Ellipse2D e2d = new Ellipse2D.Double(0, 0, boxWidth,  boxHeight);
		g2.fill(e2d);

		g2.setColor(Color.BLACK);
		g2.setFont(font);
		g2.drawString(getState(), stateXPos, stateYPos);
		g2.dispose();
	}

	/**
	 * Returns the state's name.
	 * @return the state's name
	 */
	public String getState() {
		return state;
	}

	/**
	 * Returns the name of the flow entity that this state flow object belongs to.
	 * @return the name of the flow entity that this state flow object belongs to
	 */
	public String getComponentTitle() {
		return componentTitle;
	}

	@Override
	public String toString() {
		return "FlowState(" + componentTitle + ":" + state + ")";
	}
}
