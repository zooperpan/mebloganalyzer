package gui;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class StyledTextPane {

	public static final String MESSAGE	= "message";
	public static final String WARNING	= "warning";
	public static final String ERROR	= "error";
	public static final String SUCCESS	= "success";
	private static final String TIME	= "time";
	private static final int LINE_LIMIT	= 4000;
	private int lines;
	private StyledDocument doc;
	private JTextPane textPane;

	public StyledTextPane(JTextPane textPane) {
		this.textPane = textPane;
		doc = textPane.getStyledDocument();
		addStylesToDocument(doc);
	}

	private void addStylesToDocument(StyledDocument doc) {
		//Initialize some styles.
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

		Style message = doc.addStyle(MESSAGE, def);
		StyleConstants.setFontFamily(message, "SansSerif");

		Style time = doc.addStyle(TIME, message);
		StyleConstants.setBold(time, true);

		Style error = doc.addStyle(ERROR, message);
		StyleConstants.setForeground(error, Color.RED);
		
		Style warning = doc.addStyle(WARNING, message);
		StyleConstants.setForeground(warning, Color.ORANGE);

		Style success = doc.addStyle(SUCCESS, message);
		StyleConstants.setForeground(success, new Color(20, 150, 20));
	}

	public void print(String message, String type) {
		lines++;
		Calendar c = GregorianCalendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy - HH:mm:ss,SSS");
		String timeStamp = format.format(new Date(c.getTimeInMillis()));
		try {
			doc.insertString(doc.getLength(), timeStamp + ": ", doc.getStyle(TIME));
			doc.insertString(doc.getLength(), message + "\n", doc.getStyle(type));
		} catch (BadLocationException e) {}
		checkLineLimit();
		if (textPane.getCaretPosition() < textPane.getText().length() - lines) {
			textPane.setCaretPosition(textPane.getText().length() - lines);
		}
	}

	private void checkLineLimit() {
		if (lines > LINE_LIMIT) {
			try {
				int len = doc.getText(0, doc.getLength() - 1).indexOf("\n");
				doc.remove(0, len + 1);
				lines--;
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	public void clear() {
		textPane.setText("");
		textPane.setCaretPosition(0);
		lines = 0;
	}
}
