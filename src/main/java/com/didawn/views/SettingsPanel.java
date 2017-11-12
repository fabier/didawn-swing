package com.didawn.views;

import static java.awt.Color.DARK_GRAY;
import static java.awt.Cursor.getPredefinedCursor;
import static java.lang.System.getProperty;
import static java.util.ResourceBundle.getBundle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author fabier
 */
public class SettingsPanel extends JPanel {

    private static final long serialVersionUID = -4_437_956_301_682_424_936L;
    private static final int COMPONENT_SPACING = 10;
    private static final ResourceBundle RES = getBundle("settingspanel");
    private final List<DownloadFolderSelectionListener> downloadFolderSelectionListeners = new ArrayList<>();
    private final List<SongFileNamePatternChangeListener> songFileNamePatternChangeListeners = new ArrayList<>();
    private JTextField songFileNamePatternTextField;
    private JTextField downloadFolderTextField;
    private JCheckBox artistSubdirectoryCheckbox;
    private JCheckBox albumSubdirectoryCheckbox;
    private JCheckBox overwriteCheckbox;
    private JFileChooser fileChooser;

    /**
     *
     */
    public SettingsPanel() {
	this.initializeComponents();
	this.initializeLayout();
    }

    private void initializeComponents() {
	this.fileChooser = new JFileChooser();
	this.fileChooser.setFileSelectionMode(1);
	this.fileChooser.addActionListener(new SettingsPanel.FileChooserListener());
	this.songFileNamePatternTextField = new JTextField();
	this.songFileNamePatternTextField.getDocument()
		.addDocumentListener(new SettingsPanel.SongFileNamePatternListener());
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
	helpButton.addActionListener(e -> availableTagsDialog.setVisible(true));
	JPanel songFileNamePatternPanel = new JPanel();
	songFileNamePatternPanel.setLayout(new BorderLayout(COMPONENT_SPACING, COMPONENT_SPACING));
	songFileNamePatternPanel.setBorder(new EmptyBorder(COMPONENT_SPACING, 0, 0, 0));
	songFileNamePatternPanel.add(patternLabel, "West");
	songFileNamePatternPanel.add(this.songFileNamePatternTextField, BorderLayout.CENTER);
	songFileNamePatternPanel.add(helpButton, "East");
	String downloadLabelText = RES.getString("settingspanel.label.downloaddestination");
	JLabel downloadDestinationLabel = new JLabel(downloadLabelText);
	downloadDestinationLabel.setPreferredSize(new Dimension(120, 25));
	String browseButtonText = RES.getString("settingspanel.button.browse");
	JButton browseFilesButton = new JButton(browseButtonText);
	browseFilesButton.setPreferredSize(new Dimension(120, 30));
	browseFilesButton.addActionListener(e -> {
	    File directoryToShow = new File(SettingsPanel.this.downloadFolderTextField.getText());
	    if (!directoryToShow.exists()) {
		directoryToShow = new File(getProperty("user.home"));
	    }

	    SettingsPanel.this.fileChooser.setCurrentDirectory(directoryToShow);
	    SettingsPanel.this.fileChooser.showOpenDialog(null);
	});
	JPanel downloadSettingsPanel = new JPanel();
	downloadSettingsPanel.setLayout(new BorderLayout(COMPONENT_SPACING, COMPONENT_SPACING));
	downloadSettingsPanel.setBorder(new EmptyBorder(COMPONENT_SPACING, 0, 0, 0));
	downloadSettingsPanel.add(downloadDestinationLabel, "West");
	downloadSettingsPanel.add(this.downloadFolderTextField, BorderLayout.CENTER);
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
	showSettingsLabel.setCursor(getPredefinedCursor(12));
	showSettingsLabel.setForeground(DARK_GRAY);
	showSettingsLabel.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		settingsPanel.setVisible(!settingsPanel.isVisible());
		SettingsPanel.this.revalidate();
		if (settingsPanel.isVisible()) {
		    showSettingsLabel.setText(RES.getString("settingspanel.label.hide"));
		} else {
		    showSettingsLabel.setText(RES.getString("settingspanel.label.show"));
		}

	    }
	});
	this.add(showSettingsLabel, "North");
	this.add(settingsPanel, "Center");
    }

    /**
     *
     * @param listener
     */
    public void addDownloadFolderSelectionListener(SettingsPanel.DownloadFolderSelectionListener listener) {
	this.downloadFolderSelectionListeners.add(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeDownloadFolderSelectionListener(SettingsPanel.DownloadFolderSelectionListener listener) {
	this.downloadFolderSelectionListeners.remove(listener);
    }

    /**
     *
     * @param folder
     */
    public void setDownloadFolder(String folder) {
	this.downloadFolderTextField.setText(folder);
    }

    /**
     *
     * @param listener
     */
    public void addArtistSubdirectoryCheckboxListener(ChangeListener listener) {
	this.artistSubdirectoryCheckbox.addChangeListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeArtistSubdirectoryCheckboxListener(ChangeListener listener) {
	this.artistSubdirectoryCheckbox.removeChangeListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void addAlbumSubdirectoryCheckboxListener(ChangeListener listener) {
	this.albumSubdirectoryCheckbox.addChangeListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeAlbumSubdirectoryCheckboxListener(ChangeListener listener) {
	this.albumSubdirectoryCheckbox.removeChangeListener(listener);
    }

    /**
     *
     * @param pattern
     */
    public void setSongFileNamePattern(String pattern) {
	this.songFileNamePatternTextField.setText(pattern);
    }

    /**
     *
     * @param listener
     */
    public void addSongFileNamePatternChangeListener(SettingsPanel.SongFileNamePatternChangeListener listener) {
	this.songFileNamePatternChangeListeners.add(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeSongFileNamePatternChangeListener(SettingsPanel.SongFileNamePatternChangeListener listener) {
	this.songFileNamePatternChangeListeners.remove(listener);
    }

    /**
     *
     * @param createArtistSubDirectoryCheckBoxSelected
     */
    public void setCreateArtistSubDirectoryCheckBoxSelected(boolean createArtistSubDirectoryCheckBoxSelected) {
	this.artistSubdirectoryCheckbox.setSelected(createArtistSubDirectoryCheckBoxSelected);
    }

    /**
     *
     * @param createAlbumSubDirectoryCheckBoxSelected
     */
    public void setCreateAlbumSubDirectoryCheckBoxSelected(boolean createAlbumSubDirectoryCheckBoxSelected) {
	this.albumSubdirectoryCheckbox.setSelected(createAlbumSubDirectoryCheckBoxSelected);
    }

    /**
     *
     * @param listener
     */
    public void addOverwriteCheckboxListener(ChangeListener listener) {
	this.overwriteCheckbox.addChangeListener(listener);
    }

    /**
     *
     * @param overwriteFilesCheckBoxSelected
     */
    public void setOverwriteFilesCheckBoxSelected(boolean overwriteFilesCheckBoxSelected) {
	this.overwriteCheckbox.setSelected(overwriteFilesCheckBoxSelected);
    }

    private class FileChooserListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    String command = e.getActionCommand();
	    if (command.equals("ApproveSelection")) {
		File selectedFile = SettingsPanel.this.fileChooser.getSelectedFile();
		this.notifyDownloadFolderSelected(selectedFile);
		SettingsPanel.this.downloadFolderTextField.setText(selectedFile.getAbsolutePath());
	    }

	}

	private void notifyDownloadFolderSelected(File downloadFolder) {
	    downloadFolderSelectionListeners.forEach(listener -> listener.onDownloadFolderSelected(downloadFolder));
	}
    }

    private class SongFileNamePatternListener implements DocumentListener {

	private SongFileNamePatternListener() {
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
	    this.notifySongFileNamePatternChanged();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
	    this.notifySongFileNamePatternChanged();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	    this.notifySongFileNamePatternChanged();
	}

	private void notifySongFileNamePatternChanged() {
	    songFileNamePatternChangeListeners.forEach(listener -> listener
		    .onSongFileNamePatternChanged(SettingsPanel.this.songFileNamePatternTextField.getText()));
	}
    }

    /**
     *
     */
    public interface SongFileNamePatternChangeListener {

	/**
	 *
	 * @param var1
	 */
	void onSongFileNamePatternChanged(String var1);
    }

    /**
     *
     */
    public interface DownloadFolderSelectionListener {

	/**
	 *
	 * @param var1
	 */
	void onDownloadFolderSelected(File var1);
    }
}
