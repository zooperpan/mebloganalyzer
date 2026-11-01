package gui.flow;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * The <code>FlowEntityLine</code> class is a flow object used as the flow entity's line
 * that goes vertically under the entity until the bottom of the flow area.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class FlowEntityLine extends FlowObject {

	private static final long serialVersionUID = -6580332018053654582L;

	/**
	 * Creates a new {@link FlowEntityLine} object.
	 */
	public FlowEntityLine() {
		super(2, 2, Color.BLACK);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(getColor());
		g2.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
		g2.dispose();
	}
}
