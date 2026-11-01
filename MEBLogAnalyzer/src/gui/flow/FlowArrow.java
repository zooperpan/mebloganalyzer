package gui.flow;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The <code>FlowArrow</code> class is a flow object that represents an arrow between
 * two flow entities.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class FlowArrow extends FlowObject {

	private static final long serialVersionUID = -6580332018053654582L;
	private static final int BOX_HORIZONTAL_GAP = 20;
	private static final int BOX_VERTICAL_GAP = 35;
	private static final Color SELECTED_COLOR = new Color(163, 184, 204);
	public static final int ARROW_DIRECTION_RIGHT = 0;
	public static final int ARROW_DIRECTION_LEFT = 1;
	public static final int ARROW_DIRECTION_LOOP_RIGHT = 2;
	public static final int ARROW_DIRECTION_LOOP_LEFT = 3;

	private final Font font = new Font("Arial", Font.BOLD, 12);
	private boolean sizeReady = false;
	private String from;
	private String to;
	private String title;
	private int titleWidth;
	private int titleHeight;
	private int direction;
	private boolean selected;
	private FlowPanel parent;

	/**
	 * Creates a new {@link FlowArrow} object.
	 * @param from the name of the source flow entity
	 * @param to the name of the destination flow entity
	 * @param title the name of the arrow
	 * @param color the color of the arrow
	 * @param actionListener an action listener for mouse events on the arrow flow object
	 */
	public FlowArrow(String from, String to, String title, Color color, FlowActionListener actionListener) {
		super(BOX_HORIZONTAL_GAP, BOX_VERTICAL_GAP, color, actionListener);
		setOpaque(true);
		this.from = from;
		this.to = to;
		this.title = title;
		setSelected(false);
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (parent != null) {
					parent.arrowSelected(getInstance());
				}
				setSelected(true);
			}
		});
	}

	/**
	 * Creates a new {@link FlowArrow} object.
	 * @param from the name of the source flow entity
	 * @param to the name of the destination flow entity
	 * @param title the name of the arrow
	 * @param color the color of the arrow
	 */
	public FlowArrow(String from, String to, String title, Color color) {
		this(from, to, title, color, null);
	}

	/**
	 * Creates a new {@link FlowArrow} object.
	 * @param from the name of the source flow entity
	 * @param to the name of the destination flow entity
	 * @param title the name of the arrow
	 * @param actionListener an action listener for mouse events on the arrow flow object
	 */
	public FlowArrow(String from, String to, String title, FlowActionListener actionListener) {
		this(from, to, title, Color.BLACK, actionListener);
	}

	/**
	 * Creates a new {@link FlowArrow} object.
	 * @param from the name of the source flow entity
	 * @param to the name of the destination flow entity
	 * @param title the name of the arrow
	 */
	public FlowArrow(String from, String to, String title) {
		this(from, to, title, Color.BLACK);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		if (!sizeReady) {
			g2.setFont(font);
			FontMetrics fm = g2.getFontMetrics();
			titleWidth = fm.stringWidth(getTitle()) + BOX_HORIZONTAL_GAP;
			titleHeight = fm.getMaxAscent();
			setHeight(titleHeight + BOX_VERTICAL_GAP);
			sizeReady = true;
		}
		if (selected) {
			g2.setColor(SELECTED_COLOR);
		} else {
			g2.setColor(Color.WHITE);
		}
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setColor(Color.BLACK);
		g2.drawLine(0, 0, 0, getHeight());
		g2.setColor(getColor());

		g2.setFont(font);
		g2.drawString(getTitle(), (BOX_HORIZONTAL_GAP / 2) + (getWidth() / 2) - (titleWidth / 2), (getHeight() / 2) + (titleHeight / 2));
		if ((getDirection() == ARROW_DIRECTION_RIGHT) || (getDirection() == ARROW_DIRECTION_LEFT)) {
			g2.drawLine(0, getHeight() / 2, (getWidth() / 2) - (titleWidth / 2), getHeight() / 2);
			g2.drawLine((getWidth() / 2) + (titleWidth / 2), getHeight() / 2, getWidth(), getHeight() / 2);
		} else {
			if (getDirection() == ARROW_DIRECTION_LOOP_RIGHT) {
				g2.drawLine(0, 0, getHeight() / 2, getHeight() / 2);
				g2.drawLine(getHeight() / 2, getHeight() / 2, 0, getHeight());
			} else {
				g2.drawLine(getWidth(), 0, getWidth() - (getHeight() / 2), getHeight() / 2);
				g2.drawLine(getWidth() - (getHeight() / 2), getHeight() / 2, getWidth(), getHeight());
			}
		}

		int xPoints[] = new int[3];
		int yPoints[] = new int[3];
		if (getDirection() == ARROW_DIRECTION_RIGHT) {
			xPoints[0] = getWidth();
			yPoints[0] = getHeight() / 2;
			xPoints[1] = (getWidth()) - 10;
			yPoints[1] = (getHeight() / 2) - 10;
			xPoints[2] = (getWidth()) - 10;
			yPoints[2] = (getHeight() / 2) + 10;
		} else if (getDirection() == ARROW_DIRECTION_LEFT) {
			xPoints[0] = 0;
			yPoints[0] = getHeight() / 2;
			xPoints[1] = 10;
			yPoints[1] = (getHeight() / 2) - 10;
			xPoints[2] = 10;
			yPoints[2] = (getHeight() / 2) + 10;
		} else if (getDirection() == ARROW_DIRECTION_LOOP_RIGHT) {
			xPoints[0] = 1;
			yPoints[0] = getHeight() - 1;
			xPoints[1] = 1;
			yPoints[1] = getHeight() - 15;
			xPoints[2] = 15;
			yPoints[2] = getHeight() - 1;
		} else {
			xPoints[0] = getWidth() - 1;
			yPoints[0] = getHeight() - 1;
			xPoints[1] = getWidth() - 15;
			yPoints[1] = getHeight() - 1;
			xPoints[2] = getWidth() - 1;
			yPoints[2] = getHeight() - 15;
		}
		g2.drawPolygon(xPoints, yPoints, 3);
		g2.dispose();
	}

	/**
	 * Returns an instance of the object.
	 * @return an instance of the object
	 */
	private FlowArrow getInstance() {
		return this;
	}

	/**
	 * Returns true if the arrow is selected, false otherwise.
	 * @return true if the arrow is selected, false otherwise
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Sets the selection state of the arrow to the specified value.
	 * @param selected true if arrow should be selected, false otherwise
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
		repaint();
	}

	/**
	 * Returns the name of the source entity of the arrow.
	 * @return the name of the source entity of the arrow
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Returns the name of the destination entity of the arrow.
	 * @return the name of the destination entity of the arrow
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Returns the title of the arrow.
	 * @return the title of the arrow
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the direction of the arrow.
	 * @return the direction of the arrow
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * Sets the direction of the arrow.
	 * @param direction the new direction of the arrow
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * Sets the {@link FlowPanel} where this arrow belongs to.
	 * @param parent the parent panel
	 */
	public void setParent(FlowPanel parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "FlowArrow(" + getFrom() + " -> " + getTo() + ")";
	}
}
