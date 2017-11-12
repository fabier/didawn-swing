package com.didawn.views;

import static java.text.MessageFormat.format;
import static java.util.ResourceBundle.getBundle;

import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author fabier
 */
public class DownloadArea extends JPanel {

    private static final long serialVersionUID = -6_389_323_183_359_661_958L;
    private static final ResourceBundle RES = getBundle("downloadarea");
    private final JButton downloadSelectionButton = new JButton();
    private final JButton downloadAllButton = new JButton();

    /**
     *
     */
    public DownloadArea() {
	this.add(this.downloadSelectionButton);
	this.add(this.downloadAllButton);
	this.initializeDownloadAllButton();
	this.setNumberOfSongsToDownload(0);
    }

    private void initializeDownloadAllButton() {
	this.downloadAllButton.setText(RES.getString("downloadarea.button.allsongs"));
    }

    /**
     *
     * @param numberOfSongsToDownload
     */
    public void setNumberOfSongsToDownload(int numberOfSongsToDownload) {
	String downloadSelectionButtonText;
	boolean downloadSelectionButtonEnabled;
	if (numberOfSongsToDownload <= 0) {
	    downloadSelectionButtonText = RES.getString("downloadarea.button.onesong");
	    downloadSelectionButtonEnabled = false;
	} else if (numberOfSongsToDownload == 1) {
	    downloadSelectionButtonText = RES.getString("downloadarea.button.onesong");
	    downloadSelectionButtonEnabled = true;
	} else {
	    String pattern = RES.getString("downloadarea.button.moresongs");
	    downloadSelectionButtonText = format(pattern, numberOfSongsToDownload);
	    downloadSelectionButtonEnabled = true;
	}

	this.downloadSelectionButton.setEnabled(downloadSelectionButtonEnabled);
	this.downloadSelectionButton.setText(downloadSelectionButtonText);
    }

    /**
     *
     * @param listener
     */
    public void addDownloadSelectionButtonListener(ActionListener listener) {
	this.downloadSelectionButton.addActionListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeDownloadSelectionButtonListener(ActionListener listener) {
	this.downloadSelectionButton.removeActionListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void addDownloadAllButtonListener(ActionListener listener) {
	this.downloadAllButton.addActionListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeDownloadAllButtonListener(ActionListener listener) {
	this.downloadAllButton.removeActionListener(listener);
    }

    /**
     *
     * @param enabled
     */
    public void setDownloadAllButtonEnabled(boolean enabled) {
	this.downloadAllButton.setEnabled(enabled);
    }
}
