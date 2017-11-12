package com.didawn.utils;

import static com.didawn.utils.TextPrompt.Show.ALWAYS;
import static com.didawn.utils.TextPrompt.Show.FOCUS_GAINED;
import static com.didawn.utils.TextPrompt.Show.FOCUS_LOST;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 *
 * @author fabier
 */
public class TextPrompt extends JLabel implements FocusListener, DocumentListener {

    private static final long serialVersionUID = 9_191_947_133_422_322_795L;
    private JTextComponent component;
    private Document document;
    private TextPrompt.Show show;
    private boolean showPromptOnce;
    private int focusLost;

    /**
     *
     * @param text
     * @param component
     */
    public TextPrompt(String text, JTextComponent component) {
	this(text, component, ALWAYS);
    }

    /**
     *
     * @param text
     * @param component
     * @param show
     */
    public TextPrompt(String text, JTextComponent component, TextPrompt.Show show) {
	this.component = component;
	this.setShow(show);
	this.document = component.getDocument();
	this.setText(text);
	this.setFont(component.getFont());
	this.setForeground(component.getForeground());
	this.setBorder(new EmptyBorder(component.getInsets()));
	this.setHorizontalAlignment(10);
	component.addFocusListener(this);
	this.document.addDocumentListener(this);
	component.setLayout(new BorderLayout());
	component.add(this);
	this.checkForPrompt();
    }

    /**
     *
     * @param alpha
     */
    public void changeAlpha(float alpha) {
	this.changeAlpha((int) (alpha * 255.0F));
    }

    /**
     *
     * @param alpha
     */
    public void changeAlpha(int alpha) {
	alpha = alpha > 255 ? 255 : (alpha < 0 ? 0 : alpha);
	Color foreground = this.getForeground();
	int red = foreground.getRed();
	int green = foreground.getGreen();
	int blue = foreground.getBlue();
	Color withAlpha = new Color(red, green, blue, alpha);
	super.setForeground(withAlpha);
    }

    /**
     *
     * @param style
     */
    public void changeStyle(int style) {
	this.setFont(this.getFont().deriveFont(style));
    }

    /**
     *
     * @return
     */
    public TextPrompt.Show getShow() {
	return this.show;
    }

    /**
     *
     * @param show
     */
    public void setShow(TextPrompt.Show show) {
	this.show = show;
    }

    /**
     *
     * @return
     */
    public boolean getShowPromptOnce() {
	return this.showPromptOnce;
    }

    /**
     *
     * @param showPromptOnce
     */
    public void setShowPromptOnce(boolean showPromptOnce) {
	this.showPromptOnce = showPromptOnce;
    }

    private void checkForPrompt() {
	if (this.document.getLength() > 0) {
	    this.setVisible(false);
	} else if (this.showPromptOnce && this.focusLost > 0) {
	    this.setVisible(false);
	} else {
	    if (this.component.hasFocus()) {
		if (this.show != ALWAYS && this.show != FOCUS_GAINED) {
		    this.setVisible(false);
		} else {
		    this.setVisible(true);
		}
	    } else if (this.show != ALWAYS && this.show != FOCUS_LOST) {
		this.setVisible(false);
	    } else {
		this.setVisible(true);
	    }

	}
    }

    @Override
    public void focusGained(FocusEvent e) {
	this.checkForPrompt();
    }

    @Override
    public void focusLost(FocusEvent e) {
	++this.focusLost;
	this.checkForPrompt();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
	this.checkForPrompt();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
	this.checkForPrompt();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    /**
     *
     */
    public static enum Show {

	/**
	 *
	 */
	ALWAYS,

	/**
	 *
	 */
	FOCUS_GAINED,

	/**
	 *
	 */
	FOCUS_LOST;
    }
}
