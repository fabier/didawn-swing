package com.didawn.views;

import static java.text.MessageFormat.format;
import static java.util.ResourceBundle.getBundle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JProgressBar;

/**
 *
 * @author fabier
 */
public class SearchDialog extends JDialog {

    private static final long serialVersionUID = -4_307_482_843_652_610_662L;
    private static final ResourceBundle RES = getBundle("searchdialog");
    private static final Dimension DIALOG_SIZE = new Dimension(300, 60);

    /**
     *
     * @param owner
     * @param searchTerm
     */
    public SearchDialog(Frame owner, String searchTerm) {
	super(owner, true);
	this.setLayout(new BorderLayout());
	String pattern = RES.getString("searchdialog.searching.titleprefix");
	String title = format(pattern, searchTerm);
	this.setTitle(title);
	this.setSize(DIALOG_SIZE);
	this.setLocationRelativeTo(owner);
	this.setResizable(false);
	this.setDefaultCloseOperation(0);
	JProgressBar progressBar = new JProgressBar();
	progressBar.setStringPainted(false);
	progressBar.setIndeterminate(true);
	this.add(progressBar, "Center");
    }
}
