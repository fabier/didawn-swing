package com.didawn.views;

import com.didawn.utils.TextPrompt;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchArea extends JPanel {

    private static final ResourceBundle RES = ResourceBundle.getBundle("searcharea");
    private static final int COMPONENT_SPACING = 10;
    private final JTextField searchField;
    private final JButton searchButton;

    public SearchArea() {
        this.setLayout(new BorderLayout(10, 10));
        JLabel searchLabel = new JLabel(RES.getString("searcharea.search.label"));
        searchLabel.setPreferredSize(new Dimension(120, 25));
        this.searchField = new JTextField();
        TextPrompt namePrompt = new TextPrompt(RES.getString("searcharea.search.prompt"), this.searchField);
        namePrompt.setShow(TextPrompt.Show.ALWAYS);
        namePrompt.changeAlpha(0.5F);
        this.searchButton = new JButton(RES.getString("searcharea.search.button"));
        this.searchButton.setPreferredSize(new Dimension(120, 30));
        this.add(searchLabel, "West");
        this.add(this.searchField, "Center");
        this.add(this.searchButton, "East");
    }

    public String getSearchTerm() {
        return this.searchField.getText();
    }

    public void addSearchListener(ActionListener listener) {
        this.searchButton.addActionListener(listener);
        this.searchField.addActionListener(listener);
    }

    public void removeSearchListener(ActionListener listener) {
        this.searchButton.removeActionListener(listener);
        this.searchField.removeActionListener(listener);
    }
}
