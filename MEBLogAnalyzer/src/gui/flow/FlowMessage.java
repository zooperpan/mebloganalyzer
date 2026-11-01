package gui.flow;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * The <code>FlowMessage</code> class is a flow object that represents a message that resides
 * on a flow entity.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class FlowMessage extends FlowObject {
	private static final long serialVersionUID = -6580332018053654582L;
	private static final int BOX_HORIZONTAL_GAP = 20;
	private static final int BOX_VERTICAL_GAP = 20;
	private final Font font = new Font("Arial", Font.BOLD, 12);
	private boolean sizeReady = false;
	private String message;
	private String componentTitle;
	private int messageWidth = 0;
	private int messageHeight = 0;

	/**
	 * Creates a new {@link FlowMessage} object.
	 * @param componentTitle the name of the flow entity that this message flow object belongs to
	 * @param message the message to be shown in the message flow object
	 * @param color the color of the message flow object
	 * @param actionListener an action listener for mouse events on the message flow object
	 */
	public FlowMessage(String componentTitle, String message, Color color, FlowActionListener actionListener) {
		super(BOX_HORIZONTAL_GAP, BOX_VERTICAL_GAP, color, actionListener);
		setOpaque(true);
		this.message = message;
		this.componentTitle = componentTitle;
	}

	/**
	 * Creates a new {@link FlowMessage} object.
	 * @param componentTitle the name of the flow entity that this message flow object belongs to
	 * @param message the message to be shown in the message flow object
	 * @param color the color of the message flow object
	 */
	public FlowMessage(String componentTitle, String message, Color color) {
		this(componentTitle, message, color, null);
	}

	/**
	 * Creates a new {@link FlowMessage} object.
	 * @param componentTitle the name of the flow entity that this message flow object belongs to
	 * @param message the message to be shown in the message flow object
	 * @param actionListener an action listener for mouse events on the message flow object
	 */
	public FlowMessage(String componentTitle, String message, FlowActionListener actionListener) {
		this(componentTitle, message, Color.ORANGE, actionListener);
	}

	/**
	 * Creates a new {@link FlowMessage} object.
	 * @param componentTitle the name of the flow entity that this message flow object belongs to
	 * @param message the message to be shown in the message flow object
	 */
	public FlowMessage(String componentTitle, String message) {
		this(componentTitle, message, Color.ORANGE);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		if (!sizeReady) {
			g2.setFont(font);
			calculateMessageSize(g2);
			setWidth(messageWidth + BOX_HORIZONTAL_GAP);
			setHeight(messageHeight + BOX_VERTICAL_GAP);
			sizeReady = true;
		}
		g2.setColor(getColor());
		g2.fillRect(0, 0, getWidth(), getHeight());
		//g2.fillRect(0, 0, boxWidth, boxHeight);

		g2.setColor(Color.BLACK);
		g2.setFont(font);
		drawString(g2, getMessage());
		g2.dispose();
	}

	/**
	 * Draws a string in the message flow object.
	 * @param g2 the used graphic component
	 * @param text the string to be written
	 */
	private void drawString(Graphics2D g2, String text) {
		FontMetrics fm = g2.getFontMetrics();
		int lineHeightStep = fm.getMaxAscent();
		int lineHeight = lineHeightStep;
		for (String line: text.split("\n")) {
			int lineWidth = fm.stringWidth(line);
			g2.drawString(line, (getWidth() / 2) - ((lineWidth) / 2), (BOX_VERTICAL_GAP / 2) + lineHeight);
			lineHeight += lineHeightStep + 5;
		}
	}

	/**
	 * Calculates the size of the message flow object.
	 * @param g2 the used graphic component
	 */
	private void calculateMessageSize(Graphics2D g2) {
		FontMetrics fm = g2.getFontMetrics();
		int lineHeight = fm.getMaxAscent();
		for (String line: getMessage().split("\n")) {
			messageHeight += lineHeight;
			int lineWidth = fm.stringWidth(line);
			messageHeight += 5;
			if (lineWidth > messageWidth)
				messageWidth = lineWidth;
		}
	}

	/**
	 * Returns the message written in the message flow object.
	 * @return the message written in the message flow object
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns the name of the flow entity that this message flow object belongs to.
	 * @return the name of the flow entity that this message flow object belongs to
	 */
	public String getComponentTitle() {
		return componentTitle;
	}

	@Override
	public String toString() {
		return "FlowMessage(" + componentTitle + ":" + message + ")";
	}
}
