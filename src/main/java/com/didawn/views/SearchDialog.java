package com.didawn.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JProgressBar;

public class SearchDialog extends JDialog {

    private static final ResourceBundle RES = ResourceBundle.getBundle("searchdialog");
    private static final Dimension DIALOG_SIZE = new Dimension(300, 60);

    public SearchDialog(Frame owner, String searchTerm) {
        super(owner, true);
        this.setLayout(new BorderLayout());
        String pattern = RES.getString("searchdialog.searching.titleprefix");
        String title = MessageFormat.format(pattern, searchTerm);
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
