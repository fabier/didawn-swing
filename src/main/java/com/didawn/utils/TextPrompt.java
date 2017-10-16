package com.didawn.utils;

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

public class TextPrompt extends JLabel implements FocusListener, DocumentListener {

    private JTextComponent component;
    private Document document;
    private TextPrompt.Show show;
    private boolean showPromptOnce;
    private int focusLost;

    public TextPrompt(String text, JTextComponent component) {
        this(text, component, TextPrompt.Show.ALWAYS);
    }

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

    public void changeAlpha(float alpha) {
        this.changeAlpha((int) (alpha * 255.0F));
    }

    public void changeAlpha(int alpha) {
        alpha = alpha > 255 ? 255 : (alpha < 0 ? 0 : alpha);
        Color foreground = this.getForeground();
        int red = foreground.getRed();
        int green = foreground.getGreen();
        int blue = foreground.getBlue();
        Color withAlpha = new Color(red, green, blue, alpha);
        super.setForeground(withAlpha);
    }

    public void changeStyle(int style) {
        this.setFont(this.getFont().deriveFont(style));
    }

    public TextPrompt.Show getShow() {
        return this.show;
    }

    public void setShow(TextPrompt.Show show) {
        this.show = show;
    }

    public boolean getShowPromptOnce() {
        return this.showPromptOnce;
    }

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
                if (this.show != TextPrompt.Show.ALWAYS && this.show != TextPrompt.Show.FOCUS_GAINED) {
                    this.setVisible(false);
                } else {
                    this.setVisible(true);
                }
            } else if (this.show != TextPrompt.Show.ALWAYS && this.show != TextPrompt.Show.FOCUS_LOST) {
                this.setVisible(false);
            } else {
                this.setVisible(true);
            }

        }
    }

    public void focusGained(FocusEvent e) {
        this.checkForPrompt();
    }

    public void focusLost(FocusEvent e) {
        ++this.focusLost;
        this.checkForPrompt();
    }

    public void insertUpdate(DocumentEvent e) {
        this.checkForPrompt();
    }

    public void removeUpdate(DocumentEvent e) {
        this.checkForPrompt();
    }

    public void changedUpdate(DocumentEvent e) {
    }

    public static enum Show {
        ALWAYS,
        FOCUS_GAINED,
        FOCUS_LOST;
    }
}
