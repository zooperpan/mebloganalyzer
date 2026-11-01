package gui.flow;

/**
 * The <code>public interface FlowActionListener</code> interface is used in order to
 * add mouse event listeners to a flow object.
 * 
 * @author Panos Katsikogiannis
 *
 */
public interface FlowActionListener {

	/**
	 * Called when the right mouse button has been clicked.
	 * @param source the flow object where the button was clicked 
	 */
	public void rightClick(FlowObject source);

	/**
	 * Called when the left mouse button has been clicked.
	 * @param source the flow object where the button was clicked
	 */
	public void leftClick(FlowObject source);

	/**
	 * Called when the left mouse button has been double clicked.
	 * @param source source the flow object where the button was double clicked
	 */
	public void doubleClick(FlowObject source);
}
