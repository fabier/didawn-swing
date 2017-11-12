package com.didawn.sp;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;

/**
 *
 * @author fabier
 */
public class WebPanel {

    /**
     *
     * @param args
     * @throws MalformedURLException
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void main(String[] args) throws MalformedURLException, IOException, URISyntaxException {
	new WebPanel(new URI("http://www.google.fr"));
    }

    /**
     *
     * @param uri
     * @throws IOException
     */
    public WebPanel(URI uri) throws IOException {
	// 1. Create the frame.
	JFrame frame = new JFrame("FrameDemo");

	// 2. Optional: What happens when the frame closes?
	frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

	// 3. Create components and put them in the frame.
	// ...create emptyLabel...
	JEditorPane editorPane = new JEditorPane();
	editorPane.setEditable(false);
	editorPane.setPage(uri.toURL());
	frame.getContentPane().add(editorPane, CENTER);

	// 4. Size the frame.
	frame.pack();

	// 5. Show it.
	frame.setVisible(true);
    }
}
