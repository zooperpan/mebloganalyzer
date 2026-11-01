package gui.flow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * The <code>FlowObject</code> class is the basic object for all Flow objects. It holds
 * the height/width, the <x,y> position and the color of the object. It also handles 
 * mouse events if needed by adding new action listeners for mouse events.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class FlowObject extends JComponent {

	private static final long serialVersionUID = -7700693309422956007L;
	private int height, width;
	private int xPos, yPos;
	private Color color;

	/**
	 * Creates a new {@link FlowObject} object.
	 * @param width the width of the object
	 * @param height the height of the object
	 * @param color the color of the object
	 * @param actionListener an action listener to add to the object
	 */
	public FlowObject(int width, int height, Color color, FlowActionListener actionListener) {
		super();
		setOpaque(true);
		setColor(color);
		setHeight(height);
		setWidth(width);
		xPos = width / 2;
		yPos = height / 2;
		setPreferredSize(new Dimension(width, height));
		setBounds(getXPos() - (getWidth() / 2), getYPos() - (getHeight() / 2), getWidth(), getHeight());
		if (actionListener != null)
			addActionListener(actionListener);
	}

	/**
	 * Creates a new {@link FlowObject} object.
	 * @param width the width of the object
	 * @param height the height of the object
	 * @param color the color of the object
	 */
	public FlowObject(int width, int height, Color color) {
		this(width, height, color, null);
	}

	/**
	 * Sets the horizontal position of the object.
	 * @param x the new horizontal position of the object
	 */
	public void setXPos(int x) {
		xPos = x;
		setBounds(getXPos() - (getWidth() / 2), getYPos() - (getHeight() / 2), getWidth(), getHeight());
	}

	/**
	 * Sets the vertical position of the object.
	 * @param y the new vertical position of the object
	 */
	public void setYPos(int y) {
		yPos = y;
		setBounds(getXPos() - (getWidth() / 2), getYPos() - (getHeight() / 2), getWidth(), getHeight());
	}

	/**
	 * Returns the horizontal position of the object.
	 * @return the horizontal position of the object.
	 */
	public int getXPos() {
		return xPos;
	}

	/**
	 * Returns the vertical position of the object.
	 * @return the vertical position of the object.
	 */
	public int getYPos() {
		return yPos;
	}

	/**
	 * Sets the height of the object.
	 * @param height the new height of the object
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the width of the object.
	 * @param width the new width of the object
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the color of the object.
	 * @param color the new color of the object
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Returns the color of the object.
	 * @return the color of the object
	 */
	public Color getColor() {
		return color;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	/**
	 * Adds a {@link FlowActionListener} to the object in order to listen for mouse events.
	 * @param actionListener the listener to add
	 */
	public void addActionListener(final FlowActionListener actionListener) {
		final FlowObject source = this;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent me) {
				if (SwingUtilities.isRightMouseButton(me)) {
					actionListener.rightClick(source);
				} else if ((me.getClickCount() == 2) && SwingUtilities.isLeftMouseButton(me)) {
					actionListener.doubleClick(source);
				} else if (SwingUtilities.isLeftMouseButton(me)) {
					actionListener.leftClick(source);
				}
			}
		});
	}
}
