package com.didawn.views;

import static java.util.ResourceBundle.getBundle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author fabier
 */
public class VersionInformationDialog extends JDialog {

    private static final long serialVersionUID = -7_376_620_359_868_588_297L;
    private static final ResourceBundle RES = getBundle("versioninformationdialog");
    private static final Dimension DIALOG_SIZE = new Dimension(300, 400);

    /**
     *
     * @param parent
     */
    public VersionInformationDialog(JFrame parent) {
	super(parent);
	this.setLayout(new BorderLayout());
	this.setTitle(RES.getString("versioninformation.title"));
	this.setModal(true);
	this.setSize(DIALOG_SIZE);
	this.setLocationRelativeTo(parent);
	this.setDefaultCloseOperation(2);
	JTextPane versionDescriptionTextPane = new JTextPane();
	versionDescriptionTextPane.setEditable(false);
	versionDescriptionTextPane.setContentType("text/html");
	versionDescriptionTextPane.setText("<html" + RES.getString("versioninformation.description") + "</html>");
	versionDescriptionTextPane.setBorder(new EmptyBorder(20, 20, 20, 20));
	this.add(versionDescriptionTextPane, "Center");
    }
}
