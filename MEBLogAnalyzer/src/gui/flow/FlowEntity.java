package gui.flow;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * The <code>FlowEntity</code> class is a flow object used as a component that should
 * be depicted on the flow area.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class FlowEntity extends FlowObject {

	private static final long serialVersionUID = -6580332018053654582L;
	private static final int BOX_HORIZONTAL_GAP = 20;
	private static final int BOX_VERTICAL_GAP = 20;
	private String title;
	private final Font font = new Font("Arial", Font.BOLD, 12);
	private boolean sizeReady = false;
	private int titleXPos;
	private int titleYPos;
	private FlowEntityLine line;

	/**
	 * Creates a new {@link FlowEntity} object.
	 * @param title the title of the entity
	 * @param color the color of the entity
	 */
	public FlowEntity(String title, Color color) {
		super(BOX_HORIZONTAL_GAP, BOX_VERTICAL_GAP, color);
		line = new FlowEntityLine();
		setTitle(title);
	}

	/**
	 * Creates a new {@link FlowEntity} object.
	 * @param title the title of the entity
	 */
	public FlowEntity(String title) {
		this(title, Color.YELLOW);
	}

	/**
	 * Returns the title of the entity.
	 * @return the title of the entity
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the entity.
	 * @param title the new title of the entity
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		if (!sizeReady) {
			g2.setFont(font);
			FontMetrics fm = g2.getFontMetrics();
			setWidth(fm.stringWidth(getTitle()) + BOX_HORIZONTAL_GAP);
			setHeight(fm.getMaxAscent() + BOX_VERTICAL_GAP);
			titleXPos = BOX_HORIZONTAL_GAP / 2;
			titleYPos = fm.getMaxAscent() + (BOX_VERTICAL_GAP / 2);
			sizeReady = true;
		}
		g2.setColor(getColor());
		Rectangle2D r2d = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
		g2.fill(r2d);

		g2.setColor(Color.BLACK);
		g2.setFont(font);
		g2.drawString(getTitle(), titleXPos, titleYPos);
	}

	/**
	 * Calculates the height of the entity flow object.
	 * @param g the used graphic component
	 */
	public int calculateHeight(Graphics g) {
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		return fm.getMaxAscent() + BOX_VERTICAL_GAP;
	}

	/**
	 * Returns the {@link FlowEntityLine} that is linked with this entity.
	 * @return the FlowEntityLine that is linked with this entity
	 */
	public FlowEntityLine getLine() {
		return line;
	}

	@Override
	public String toString() {
		return "FlowEntity(" + title + ")";
	}
}
