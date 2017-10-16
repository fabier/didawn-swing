package com.didawn.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SettingsPanel extends JPanel {

    private static final ResourceBundle RES = ResourceBundle.getBundle("settingspanel");
    private static final int COMPONENT_SPACING = 10;
    private List downloadFolderSelectionListeners = new ArrayList();
    private List songFileNamePatternChangeListeners = new ArrayList();
    private JTextField songFileNamePatternTextField;
    private JTextField downloadFolderTextField;
    private JCheckBox artistSubdirectoryCheckbox;
    private JCheckBox albumSubdirectoryCheckbox;
    private JCheckBox overwriteCheckbox;
    private JFileChooser fileChooser;

    public SettingsPanel() {
        this.initializeComponents();
        this.initializeLayout();
    }

    private void initializeComponents() {
        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileSelectionMode(1);
        this.fileChooser.addActionListener(new SettingsPanel.FileChooserListener(null));
        this.songFileNamePatternTextField = new JTextField();
        this.songFileNamePatternTextField.getDocument().addDocumentListener(new SettingsPanel.SongFileNamePatternListener(null));
        this.downloadFolderTextField = new JTextField();
        this.downloadFolderTextField.setEditable(false);
        String artistSubdirectoryCheckBoxText = RES.getString("settingspanel.option.artistsubdirectory");
        this.artistSubdirectoryCheckbox = new JCheckBox(artistSubdirectoryCheckBoxText);
        String albumSubdirectoryCheckBoxText = RES.getString("settingspanel.option.albumsubdirectory");
        this.albumSubdirectoryCheckbox = new JCheckBox(albumSubdirectoryCheckBoxText);
        String overwriteCheckBoxText = RES.getString("settingspanel.option.overwrite");
        this.overwriteCheckbox = new JCheckBox(overwriteCheckBoxText);
    }

    private void initializeLayout() {
        this.setLayout(new BorderLayout());
        final AvailableTagsDialog availableTagsDialog = new AvailableTagsDialog();
        String patternLabelText = RES.getString("settingspanel.label.filenamepattern");
        JLabel patternLabel = new JLabel(patternLabelText);
        patternLabel.setPreferredSize(new Dimension(120, 25));
        String helpButtonText = RES.getString("settingspanel.button.addtag");
        JButton helpButton = new JButton(helpButtonText);
        helpButton.setPreferredSize(new Dimension(120, 30));
        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                availableTagsDialog.setVisible(true);
            }
        });
        JPanel songFileNamePatternPanel = new JPanel();
        songFileNamePatternPanel.setLayout(new BorderLayout(10, 10));
        songFileNamePatternPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        songFileNamePatternPanel.add(patternLabel, "West");
        songFileNamePatternPanel.add(this.songFileNamePatternTextField, "Center");
        songFileNamePatternPanel.add(helpButton, "East");
        String downloadLabelText = RES.getString("settingspanel.label.downloaddestination");
        JLabel downloadDestinationLabel = new JLabel(downloadLabelText);
        downloadDestinationLabel.setPreferredSize(new Dimension(120, 25));
        String browseButtonText = RES.getString("settingspanel.button.browse");
        JButton browseFilesButton = new JButton(browseButtonText);
        browseFilesButton.setPreferredSize(new Dimension(120, 30));
        browseFilesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File directoryToShow = new File(SettingsPanel.this.downloadFolderTextField.getText());
                if (!directoryToShow.exists()) {
                    directoryToShow = new File(System.getProperty("user.home"));
                }

                SettingsPanel.this.fileChooser.setCurrentDirectory(directoryToShow);
                SettingsPanel.this.fileChooser.showOpenDialog((Component) null);
            }
        });
        JPanel downloadSettingsPanel = new JPanel();
        downloadSettingsPanel.setLayout(new BorderLayout(10, 10));
        downloadSettingsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        downloadSettingsPanel.add(downloadDestinationLabel, "West");
        downloadSettingsPanel.add(this.downloadFolderTextField, "Center");
        downloadSettingsPanel.add(browseFilesButton, "East");
        JPanel subdirectorySettingsPanel = new JPanel();
        subdirectorySettingsPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
        subdirectorySettingsPanel.add(this.artistSubdirectoryCheckbox);
        subdirectorySettingsPanel.add(new JLabel("     "));
        subdirectorySettingsPanel.add(this.albumSubdirectoryCheckbox);
        subdirectorySettingsPanel.add(new JLabel("     "));
        subdirectorySettingsPanel.add(this.overwriteCheckbox);
        final JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BorderLayout());
        settingsPanel.setVisible(false);
        settingsPanel.add(songFileNamePatternPanel, "North");
        settingsPanel.add(downloadSettingsPanel, "Center");
        settingsPanel.add(subdirectorySettingsPanel, "South");
        String showSettingsLabelText = RES.getString("settingspanel.label.show");
        final JLabel showSettingsLabel = new JLabel(showSettingsLabelText, 0);
        String fontName = showSettingsLabel.getFont().getFamily();
        showSettingsLabel.setFont(new Font(fontName, 0, 11));
        showSettingsLabel.setCursor(Cursor.getPredefinedCursor(12));
        showSettingsLabel.setForeground(Color.DARK_GRAY);
        showSettingsLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                settingsPanel.setVisible(!settingsPanel.isVisible());
                SettingsPanel.this.revalidate();
                if (settingsPanel.isVisible()) {
                    showSettingsLabel.setText(SettingsPanel.RES.getString("settingspanel.label.hide"));
                } else {
                    showSettingsLabel.setText(SettingsPanel.RES.getString("settingspanel.label.show"));
                }

            }
        });
        this.add(showSettingsLabel, "North");
        this.add(settingsPanel, "Center");
    }

    private JPopupMenu createTagDropdownMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItemArtistTag = new JMenuItem("Artist");
        menuItemArtistTag.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SettingsPanel.this.songFileNamePatternTextField.setText(SettingsPanel.this.songFileNamePatternTextField.getText() + "%Artist%");
            }
        });
        popupMenu.add(menuItemArtistTag);
        JMenuItem menuItemTitleTag = new JMenuItem("Title");
        menuItemTitleTag.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SettingsPanel.this.songFileNamePatternTextField.setText(SettingsPanel.this.songFileNamePatternTextField.getText() + "%Title%");
            }
        });
        popupMenu.add(menuItemTitleTag);
        return popupMenu;
    }

    public void addDownloadFolderSelectionListener(SettingsPanel.DownloadFolderSelectionListener listener) {
        this.downloadFolderSelectionListeners.add(listener);
    }

    public void removeDownloadFolderSelectionListener(SettingsPanel.DownloadFolderSelectionListener listener) {
        this.downloadFolderSelectionListeners.remove(listener);
    }

    public void setDownloadFolder(String folder) {
        this.downloadFolderTextField.setText(folder);
    }

    public void addArtistSubdirectoryCheckboxListener(ChangeListener listener) {
        this.artistSubdirectoryCheckbox.addChangeListener(listener);
    }

    public void removeArtistSubdirectoryCheckboxListener(ChangeListener listener) {
        this.artistSubdirectoryCheckbox.removeChangeListener(listener);
    }

    public void addAlbumSubdirectoryCheckboxListener(ChangeListener listener) {
        this.albumSubdirectoryCheckbox.addChangeListener(listener);
    }

    public void removeAlbumSubdirectoryCheckboxListener(ChangeListener listener) {
        this.albumSubdirectoryCheckbox.removeChangeListener(listener);
    }

    public void setSongFileNamePattern(String pattern) {
        this.songFileNamePatternTextField.setText(pattern);
    }

    public void addSongFileNamePatternChangeListener(SettingsPanel.SongFileNamePatternChangeListener listener) {
        this.songFileNamePatternChangeListeners.add(listener);
    }

    public void removeSongFileNamePatternChangeListener(SettingsPanel.SongFileNamePatternChangeListener listener) {
        this.songFileNamePatternChangeListeners.remove(listener);
    }

    public void setCreateArtistSubDirectoryCheckBoxSelected(boolean createArtistSubDirectoryCheckBoxSelected) {
        this.artistSubdirectoryCheckbox.setSelected(createArtistSubDirectoryCheckBoxSelected);
    }

    public void setCreateAlbumSubDirectoryCheckBoxSelected(boolean createAlbumSubDirectoryCheckBoxSelected) {
        this.albumSubdirectoryCheckbox.setSelected(createAlbumSubDirectoryCheckBoxSelected);
    }

    public void addOverwriteCheckboxListener(ChangeListener listener) {
        this.overwriteCheckbox.addChangeListener(listener);
    }

    public void setOverwriteFilesCheckBoxSelected(boolean overwriteFilesCheckBoxSelected) {
        this.overwriteCheckbox.setSelected(overwriteFilesCheckBoxSelected);
    }

    private class FileChooserListener implements ActionListener {

        private FileChooserListener() {
        }

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("ApproveSelection")) {
                File selectedFile = SettingsPanel.this.fileChooser.getSelectedFile();
                this.notifyDownloadFolderSelected(selectedFile);
                SettingsPanel.this.downloadFolderTextField.setText(selectedFile.getAbsolutePath());
            }

        }

        private void notifyDownloadFolderSelected(File downloadFolder) {
            Iterator var2 = SettingsPanel.this.downloadFolderSelectionListeners.iterator();

            while (var2.hasNext()) {
                SettingsPanel.DownloadFolderSelectionListener listener = (SettingsPanel.DownloadFolderSelectionListener) var2.next();
                listener.onDownloadFolderSelected(downloadFolder);
            }

        }

        // $FF: synthetic method
        FileChooserListener(Object x1) {
            this();
        }
    }

    private class SongFileNamePatternListener implements DocumentListener {

        private SongFileNamePatternListener() {
        }

        public void insertUpdate(DocumentEvent e) {
            this.notifySongFileNamePatternChanged();
        }

        public void removeUpdate(DocumentEvent e) {
            this.notifySongFileNamePatternChanged();
        }

        public void changedUpdate(DocumentEvent e) {
            this.notifySongFileNamePatternChanged();
        }

        private void notifySongFileNamePatternChanged() {
            Iterator var1 = SettingsPanel.this.songFileNamePatternChangeListeners.iterator();

            while (var1.hasNext()) {
                SettingsPanel.SongFileNamePatternChangeListener listener = (SettingsPanel.SongFileNamePatternChangeListener) var1.next();
                listener.onSongFileNamePatternChanged(SettingsPanel.this.songFileNamePatternTextField.getText());
            }

        }

        // $FF: synthetic method
        SongFileNamePatternListener(Object x1) {
            this();
        }
    }

    public interface SongFileNamePatternChangeListener {

        void onSongFileNamePatternChanged(String var1);
    }

    public interface DownloadFolderSelectionListener {

        void onDownloadFolderSelected(File var1);
    }
}
