package parser;

/**
 * The <code>ParserProgressListener</code> interface is used in order to provide to the implementing
 * listener the progress and information of the parser. Periodically a parser will update its progress
 * and information regarding its actions by calling the abstract methods declared here.
 * 
 * @author Panos Katsikogiannis
 * 
 * @see #progress(int)
 * @see #setInfo(String)
 *
 */
public interface ParserProgressListener {

	/**
	 * Sets the progress of the parser to the specified value.
	 * @param progress the progress of the parser
	 */
	public void progress(int progress);

	/**
	 * Sets the information of the parser to the specified value.
	 * @param info the information of the parser
	 */
	public void setInfo(String info);
}
