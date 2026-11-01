package gui.flow;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * The <code>FlowArea</code> class is a flow object used as background
 * of all the flow components that create the graphical representation 
 * of the flow (except of the flow entities). 
 * 
 * @author Panos Katsikogiannis
 *
 */
public class FlowArea extends FlowObject {

	private static final long serialVersionUID = -6580332018053654582L;

	/**
	 * Creates a new {@link FlowArea} object.
	 * @param width the width of the area
	 * @param height the height of the area
	 * @param color the color of the area
	 */
	public FlowArea(int width, int height, Color color) {
		super(width, height, color);
		setOpaque(false);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(getColor());
		g2.setStroke(new BasicStroke(1.0f));
		Rectangle2D r2d = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
		g2.draw(r2d);
		g2.dispose();
	}

	@Override
	public String toString() {
		return "FlowArea(" + getWidth() + ", " + getHeight() + ")";
	}
}
