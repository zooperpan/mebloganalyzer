package gui.flow;

/**
 * The <code>FlowActionAdapter</code> class is an abstract adapter class for 
 * receiving flow action events.The methods in this class are empty. This class exists as
 * convenience for creating listener objects.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class FlowActionAdapter implements FlowActionListener {

	@Override
	public void rightClick(FlowObject source) {}

	@Override
	public void doubleClick(FlowObject source) {}

	@Override
	public void leftClick(FlowObject source) {}
}
