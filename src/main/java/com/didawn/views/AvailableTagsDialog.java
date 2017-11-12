package com.didawn.views;

import static java.util.ResourceBundle.getBundle;
import static javax.swing.text.StyleConstants.setBold;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;

/**
 *
 * @author fabier
 */
public class AvailableTagsDialog extends JDialog {

    private static final long serialVersionUID = 1_375_150_313_192_961_471L;
    private static final ResourceBundle RES = getBundle("tagdescription");
    private static final int COMPONENT_SPACING = 10;
    private static final int HORIZONTAL_SPACING = 5;
    private static final Dimension DIALOG_SIZE = new Dimension(300, 500);

    /**
     *
     */
    public AvailableTagsDialog() {
	this.setTitle(RES.getString("tagdescription.dialog.title"));
	this.setLayout(new GridBagLayout());
	this.setLocationRelativeTo(null);
	this.setSize(DIALOG_SIZE);
	this.setResizable(false);
	GridBagConstraints c = new GridBagConstraints();
	c.insets = new Insets(COMPONENT_SPACING, COMPONENT_SPACING, HORIZONTAL_SPACING, HORIZONTAL_SPACING);
	c.fill = 2;
	c.weightx = 0.2D;
	c.gridx = 0;
	c.gridy = 0;
	this.add(new AvailableTagsDialog.TagLabel("%ID%"), c);
	c.insets = new Insets(HORIZONTAL_SPACING, COMPONENT_SPACING, HORIZONTAL_SPACING, HORIZONTAL_SPACING);
	c.gridx = 0;
	c.gridy = 1;
	this.add(new AvailableTagsDialog.TagLabel("%Title%"), c);
	c.gridx = 0;
	c.gridy = 2;
	this.add(new AvailableTagsDialog.TagLabel("%Artist%"), c);
	c.gridx = 0;
	c.gridy = 3;
	this.add(new AvailableTagsDialog.TagLabel("%Album%"), c);
	c.gridx = 0;
	c.gridy = 4;
	this.add(new AvailableTagsDialog.TagLabel("%AlbumArtist%"), c);
	c.gridx = 0;
	c.gridy = 5;
	this.add(new AvailableTagsDialog.TagLabel("%Genre%"), c);
	c.gridx = 0;
	c.gridy = 6;
	this.add(new AvailableTagsDialog.TagLabel("%Year%"), c);
	c.gridx = 0;
	c.gridy = 7;
	this.add(new AvailableTagsDialog.TagLabel("%TitleNr%"), c);
	c.insets = new Insets(HORIZONTAL_SPACING, COMPONENT_SPACING, COMPONENT_SPACING, HORIZONTAL_SPACING);
	c.gridx = 0;
	c.gridy = 8;
	this.add(new AvailableTagsDialog.TagLabel("%DiskNr%"), c);
	c.insets = new Insets(COMPONENT_SPACING, HORIZONTAL_SPACING, HORIZONTAL_SPACING, COMPONENT_SPACING);
	c.weightx = 0.8D;
	c.gridx = 1;
	c.gridy = 0;
	this.add(new AvailableTagsDialog.DescriptionArea(RES.getString("tagdescription.id")), c);
	c.insets = new Insets(HORIZONTAL_SPACING, HORIZONTAL_SPACING, HORIZONTAL_SPACING, COMPONENT_SPACING);
	c.gridx = 1;
	c.gridy = 1;
	this.add(new AvailableTagsDialog.DescriptionArea(RES.getString("tagdescription.title")), c);
	c.gridx = 1;
	c.gridy = 2;
	this.add(new AvailableTagsDialog.DescriptionArea(RES.getString("tagdescription.artist")), c);
	c.gridx = 1;
	c.gridy = 3;
	this.add(new AvailableTagsDialog.DescriptionArea(RES.getString("tagdescription.album")), c);
	c.gridx = 1;
	c.gridy = 4;
	this.add(new AvailableTagsDialog.DescriptionArea(RES.getString("tagdescription.albumartist")), c);
	c.gridx = 1;
	c.gridy = 5;
	this.add(new AvailableTagsDialog.DescriptionArea(RES.getString("tagdescription.genre")), c);
	c.gridx = 1;
	c.gridy = 6;
	this.add(new AvailableTagsDialog.DescriptionArea(RES.getString("tagdescription.year")), c);
	c.gridx = 1;
	c.gridy = 7;
	this.add(new AvailableTagsDialog.DescriptionArea(RES.getString("tagdescription.titlenr")), c);
	c.insets = new Insets(HORIZONTAL_SPACING, HORIZONTAL_SPACING, COMPONENT_SPACING, COMPONENT_SPACING);
	c.gridx = 1;
	c.gridy = 8;
	this.add(new AvailableTagsDialog.DescriptionArea(RES.getString("tagdescription.disknr")), c);
    }

    private class TagLabel extends JTextPane {

	private static final long serialVersionUID = 3_485_370_872_712_413_885L;

	TagLabel(String text) {
	    StyleContext sc = new StyleContext();
	    DefaultStyledDocument doc = new DefaultStyledDocument(sc);
	    this.setStyledDocument(doc);
	    Style defaultStyle = sc.getStyle("default");
	    setBold(defaultStyle, true);
	    this.setEditable(false);
	    this.setBorder(null);
	    this.setText(text);
	}
    }

    private class DescriptionArea extends JTextPane {

	private static final long serialVersionUID = -6_154_845_422_106_242_108L;

	DescriptionArea(String text) {
	    this.setEditable(false);
	    this.setBorder(null);
	    this.setText(text);
	}
    }
}
