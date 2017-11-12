package com.didawn.views;

import static com.didawn.utils.TextPrompt.Show.ALWAYS;
import static java.util.ResourceBundle.getBundle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.didawn.utils.TextPrompt;

/**
 *
 * @author fabier
 */
public class SearchArea extends JPanel {

    private static final long serialVersionUID = 8_948_879_114_588_625_136L;
    private static final ResourceBundle RES = getBundle("searcharea");
    private static final int COMPONENT_SPACING = 10;
    private final JTextField searchField;
    private final JButton searchButton;

    /**
     *
     */
    public SearchArea() {
	this.setLayout(new BorderLayout(COMPONENT_SPACING, COMPONENT_SPACING));
	JLabel searchLabel = new JLabel(RES.getString("searcharea.search.label"));
	searchLabel.setPreferredSize(new Dimension(120, 25));
	this.searchField = new JTextField();
	TextPrompt namePrompt = new TextPrompt(RES.getString("searcharea.search.prompt"), this.searchField);
	namePrompt.setShow(ALWAYS);
	namePrompt.changeAlpha(0.5F);
	this.searchButton = new JButton(RES.getString("searcharea.search.button"));
	this.searchButton.setPreferredSize(new Dimension(120, 30));
	this.add(searchLabel, "West");
	this.add(this.searchField, "Center");
	this.add(this.searchButton, "East");
    }

    /**
     *
     * @return
     */
    public String getSearchTerm() {
	return this.searchField.getText();
    }

    /**
     *
     * @param listener
     */
    public void addSearchListener(ActionListener listener) {
	this.searchButton.addActionListener(listener);
	this.searchField.addActionListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeSearchListener(ActionListener listener) {
	this.searchButton.removeActionListener(listener);
	this.searchField.removeActionListener(listener);
    }
}
