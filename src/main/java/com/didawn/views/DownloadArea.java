package com.didawn.views;

import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JPanel;

public class DownloadArea extends JPanel {

    private static final ResourceBundle RES = ResourceBundle.getBundle("downloadarea");
    private JButton downloadSelectionButton = new JButton();
    private JButton downloadAllButton = new JButton();

    public DownloadArea() {
        this.add(this.downloadSelectionButton);
        this.add(this.downloadAllButton);
        this.initializeDownloadAllButton();
        this.setNumberOfSongsToDownload(0);
    }

    private void initializeDownloadAllButton() {
        this.downloadAllButton.setText(RES.getString("downloadarea.button.allsongs"));
    }

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
            downloadSelectionButtonText = MessageFormat.format(pattern, numberOfSongsToDownload);
            downloadSelectionButtonEnabled = true;
        }

        this.downloadSelectionButton.setEnabled(downloadSelectionButtonEnabled);
        this.downloadSelectionButton.setText(downloadSelectionButtonText);
    }

    public void addDownloadSelectionButtonListener(ActionListener listener) {
        this.downloadSelectionButton.addActionListener(listener);
    }

    public void removeDownloadSelectionButtonListener(ActionListener listener) {
        this.downloadSelectionButton.removeActionListener(listener);
    }

    public void addDownloadAllButtonListener(ActionListener listener) {
        this.downloadAllButton.addActionListener(listener);
    }

    public void removeDownloadAllButtonListener(ActionListener listener) {
        this.downloadAllButton.removeActionListener(listener);
    }

    public void setDownloadAllButtonEnabled(boolean enabled) {
        this.downloadAllButton.setEnabled(enabled);
    }
}
