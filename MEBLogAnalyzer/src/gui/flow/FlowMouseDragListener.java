package gui.flow;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

/**
 * The <code>FlowMouseDragListener</code> class is an abstract calls that is responsible for listening
 * for mouse motion events. It is used in order to reorder the positions of the flow entities.
 * 
 * @author Panos Katsikogiannis
 *
 */
public abstract class FlowMouseDragListener {

	private Point dragStart;

	/**
	 * An abstract function that is called when the component is being dragged.
	 * @param end the point that the component currently resides on
	 */
	public abstract void drag(Point end);

	/**
	 * An abstract function that is called when the component has stopped being dragged.
	 * @param start the initial point of the component
	 * @param end the ending point of the component
	 */
	public abstract void dragEnded(Point start, Point end);

	/**
	 * Creates a new {@link FlowMouseDragListener} object for the specified component.
	 * It adds a mouse motion listener to listen for moving or dragging events and a
	 * mouse listener to listen when the mouse button has been released.
	 * @param component the specified component
	 */
	public FlowMouseDragListener(JComponent component) {
		super();
		component.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				dragStart = null;
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (dragStart == null) {
					dragStart = e.getLocationOnScreen();
				}
				drag(e.getLocationOnScreen());
			}
		});
		component.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (dragStart != null) {
					dragEnded(dragStart, e.getLocationOnScreen());
				}
			}
		});
	}
}
