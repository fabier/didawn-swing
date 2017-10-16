package com.didawn.views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class AvailableTagsDialog extends JDialog {

    private static final ResourceBundle RES = ResourceBundle.getBundle("tagdescription");
    private static final Dimension DIALOG_SIZE = new Dimension(300, 500);

    public AvailableTagsDialog() {
        this.setTitle(RES.getString("tagdescription.dialog.title"));
        this.setLayout(new GridBagLayout());
        this.setLocationRelativeTo((Component) null);
        this.setSize(DIALOG_SIZE);
        this.setResizable(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 5, 5);
        c.fill = 2;
        c.weightx = 0.2D;
        c.gridx = 0;
        c.gridy = 0;
        this.add(new AvailableTagsDialog.TagLabel("%ID%"), c);
        c.insets = new Insets(5, 10, 5, 5);
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
        c.insets = new Insets(5, 10, 10, 5);
        c.gridx = 0;
        c.gridy = 8;
        this.add(new AvailableTagsDialog.TagLabel("%DiskNr%"), c);
        c.insets = new Insets(10, 5, 5, 10);
        c.weightx = 0.8D;
        c.gridx = 1;
        c.gridy = 0;
        this.add(new AvailableTagsDialog.DescriptionArea(RES.getString("tagdescription.id")), c);
        c.insets = new Insets(5, 5, 5, 10);
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
        c.insets = new Insets(5, 5, 10, 10);
        c.gridx = 1;
        c.gridy = 8;
        this.add(new AvailableTagsDialog.DescriptionArea(RES.getString("tagdescription.disknr")), c);
    }

    private class TagLabel extends JTextPane {

        public TagLabel(String text) {
            StyleContext sc = new StyleContext();
            DefaultStyledDocument doc = new DefaultStyledDocument(sc);
            this.setStyledDocument(doc);
            Style defaultStyle = sc.getStyle("default");
            StyleConstants.setBold(defaultStyle, true);
            this.setEditable(false);
            this.setBorder((Border) null);
            this.setText(text);
        }
    }

    private class DescriptionArea extends JTextPane {

        public DescriptionArea(String text) {
            this.setEditable(false);
            this.setBorder((Border) null);
            this.setText(text);
        }
    }
}
