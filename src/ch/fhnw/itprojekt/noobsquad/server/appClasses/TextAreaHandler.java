package ch.fhnw.itprojekt.noobsquad.server.appClasses;

/**
 * 
 * @author Brad Richards
 * @author Simon Zahnd
 * 
 */

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * A log-handler that writes to a TextArea. Platform.runLater is a means of
 * putting work onto the JavaFX application thread. Anything that modifies with
 * the GUI should be on this thread. In this case, the critical line is
 * textArea.setText(...).
 * 
 * Adapted from:
 * http://stackoverflow.com/questions/10785560/write-logger-message-to-file-and-
 * textarea-while-maintaining-default-behaviour-in
 */
public class TextAreaHandler extends Handler {

	// A Handler object takes log messages from a Logger and exports them.
	// It might for example, write them to a console or write them to a file,
	// or send them to a network logging service, or forward them to an OS log,
	// or whatever.

	private TextArea textArea = new TextArea();

	@Override
	public void publish(LogRecord record) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				StringWriter text = new StringWriter();
				PrintWriter out = new PrintWriter(text);
				out.println(textArea.getText());
				out.printf("[%s] [Thread-%d]:\n %s.%s ->\n %s", record.getLevel(), record.getThreadID(),
						record.getSourceClassName(), record.getSourceMethodName(), record.getMessage());
				textArea.setText(text.toString());
			}
		});
	}

	@Override
	public void flush() {
		// nothing to do here
	}

	@Override
	public void close() throws SecurityException {
		// nothing to do here
	}

	public TextArea getTextArea() {
		textArea.setEditable(false);
		return textArea;
	}
}
