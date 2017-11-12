package com.didawn.controller;

import static com.didawn.di.DiDawnManager.State.DOWNLOADING;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.getProperty;
import static java.util.ResourceBundle.getBundle;
import static java.util.prefs.Preferences.userRoot;
import static javax.swing.JOptionPane.showConfirmDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.didawn.di.Di;
import com.didawn.di.DiDawnManager;
import com.didawn.di.DownloadManagerListener;
import com.didawn.models.Song;
import com.didawn.utils.PatternSongFileNameGenerator;
import com.didawn.utils.SubdirectorySongDestinationGenerator;
import com.didawn.views.DownloadArea;
import com.didawn.views.DownloadStatusBar;
import com.didawn.views.SearchArea;
import com.didawn.views.SearchDialog;
import com.didawn.views.SettingsPanel;
import com.didawn.views.SongTable;

/**
 *
 * @author fabier
 */
public class MainController {

    private static final ResourceBundle RES = getBundle("searchdialog");
    private static final ResourceBundle RES_ERROR = getBundle("errormessages");
    private static final String DEFAULT_DOWNLOAD_DESTINATION = getProperty("user.home") + "/Music";
    private static final String DEFAULT_SONG_FILE_NAME_PATTERN = "%Artist% - %Title%";
    private static final String PREFS_KEY_DOWNLOAD_DESTINATION = "download-destination";
    private static final String PREFS_KEY_SONG_FILE_NAME_PATTERN = "song-file-name-pattern";
    private static final String PREFS_KEY_CREATE_ARTIST_SUBDIRECTORY = "create-artist-subdirectory";
    private static final String PREFS_KEY_CREATE_ALBUM_SUBDIRECTORY = "create-album-subdirectory";
    private static final String PREFS_KEY_OVERWRITE_FILES = "overwrite-files";
    private final Preferences prefs = userRoot().node("user-settings");
    private final DiDawnManager diDawnManager = new DiDawnManager();
    private final Di diApi = new Di();
    private final SubdirectorySongDestinationGenerator songDestinationGenerator;
    private final PatternSongFileNameGenerator songFileNameGenerator;
    private final JFrame frame;
    private final SearchArea searchArea;
    private final SongTable songTable;
    private final DownloadArea downloadArea;
    private final DownloadStatusBar downloadStatusBar;
    private final JDialog noConnectionErrorDialog;

    /**
     *
     * @param frame
     * @param searchArea
     * @param settingsPanel
     * @param songTable
     * @param downloadArea
     * @param downloadStatusBar
     */
    public MainController(JFrame frame, SearchArea searchArea, SettingsPanel settingsPanel, SongTable songTable,
	    DownloadArea downloadArea, DownloadStatusBar downloadStatusBar) {
	String downloadDestination = this.prefs.get(PREFS_KEY_DOWNLOAD_DESTINATION, DEFAULT_DOWNLOAD_DESTINATION);
	this.songDestinationGenerator = new SubdirectorySongDestinationGenerator(new File(downloadDestination));
	String songFileNamePattern = this.prefs.get(PREFS_KEY_SONG_FILE_NAME_PATTERN, DEFAULT_SONG_FILE_NAME_PATTERN);
	this.songFileNameGenerator = new PatternSongFileNameGenerator(songFileNamePattern);
	boolean createArtistSubdirectory = this.prefs.getBoolean(PREFS_KEY_CREATE_ARTIST_SUBDIRECTORY, false);
	boolean createAlbumSubdirectory = this.prefs.getBoolean(PREFS_KEY_CREATE_ALBUM_SUBDIRECTORY, false);
	boolean overwriteFiles = this.prefs.getBoolean(PREFS_KEY_OVERWRITE_FILES, false);
	this.frame = frame;
	this.searchArea = searchArea;
	this.searchArea.addSearchListener(new SearchButtonListener());
	this.songTable = songTable;
	this.songTable.addSongSelectionListener(songs -> downloadArea.setNumberOfSongsToDownload(songs.size()));
	this.downloadArea = downloadArea;
	this.downloadArea.setDownloadAllButtonEnabled(false);
	this.downloadArea.addDownloadSelectionButtonListener(new DownloadSelectedSongsButtonListener());
	this.downloadArea.addDownloadAllButtonListener(new DownloadAllSongsButtonListener());
	this.downloadStatusBar = downloadStatusBar;
	this.downloadStatusBar.setProgressMax(1_000);
	this.downloadStatusBar.addAbortDownloadButtonListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		MainController.this.diDawnManager.abortDownload();
	    }
	});
	this.diDawnManager.addDownloadManagerListener(new DiDawnManagerListener());
	this.diDawnManager.setSongDestinationController(this.songDestinationGenerator);
	this.diDawnManager.setSongFileNameGenerator(this.songFileNameGenerator);
	settingsPanel.addArtistSubdirectoryCheckboxListener(new ArtistSubdirectoryListener());
	settingsPanel.addAlbumSubdirectoryCheckboxListener(new AlbumSubdirectoryListener());
	settingsPanel.addOverwriteCheckboxListener(new OverwriteFilesListener());
	settingsPanel.addDownloadFolderSelectionListener(newDownloadFolder -> {
	    songDestinationGenerator.setDestinationFolder(newDownloadFolder);
	    prefs.put("download-destination", newDownloadFolder.getAbsolutePath());
	});
	settingsPanel.addSongFileNamePatternChangeListener(newPattern -> {
	    songFileNameGenerator.setPattern(newPattern);
	    prefs.put("song-file-name-pattern", newPattern);
	});
	settingsPanel.setDownloadFolder(this.songDestinationGenerator.getDestinationFolder().getAbsolutePath());
	settingsPanel.setSongFileNamePattern(this.songFileNameGenerator.getPattern());
	settingsPanel.setCreateArtistSubDirectoryCheckBoxSelected(createArtistSubdirectory);
	settingsPanel.setCreateAlbumSubDirectoryCheckBoxSelected(createAlbumSubdirectory);
	settingsPanel.setOverwriteFilesCheckBoxSelected(overwriteFiles);
	String message = RES_ERROR.getString("errormessage.noconnection.message");
	String title = RES_ERROR.getString("errormessage.noconnection.title");
	JOptionPane noConnectionErrorPane = new JOptionPane(message, 0, -1);
	this.noConnectionErrorDialog = noConnectionErrorPane.createDialog(frame, title);
    }

    private class SearchButtonListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    String searchTerm = MainController.this.searchArea.getSearchTerm();
	    if (searchTerm.trim().isEmpty()) {
		this.showHint();
	    } else {
		this.searchAsync(searchTerm);
	    }
	}

	private void showHint() {
	    String title = RES.getString("searchdialog.emptysearch.title");
	    String message = RES.getString("searchdialog.emptysearch.message");
	    showConfirmDialog(MainController.this.frame, message, title, -1, 2);
	}

	private void searchAsync(final String searchTerm) {
	    final SearchDialog searchDialog = new SearchDialog(MainController.this.frame, searchTerm);
	    new Thread() {
		@Override
		public void run() {
		    try {
			List<Song> songs = MainController.this.diApi.query(searchTerm);
			if (!songs.isEmpty()) {
			    MainController.this.songTable.setSongs(songs);
			}

			searchDialog.setVisible(false);
			searchDialog.dispose();
			MainController.this.downloadArea.setDownloadAllButtonEnabled(true);
		    } catch (Exception e) {
			searchDialog.setVisible(false);
			searchDialog.dispose();
			if (e.getCause() instanceof UnknownHostException
				|| e.getCause() instanceof NoRouteToHostException) {
			    MainController.this.noConnectionErrorDialog.setVisible(true);
			}
		    }
		}
	    }.start();
	    searchDialog.setVisible(true);
	}
    }

    private class DownloadSelectedSongsButtonListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    List<Song> selectedSongs = MainController.this.songTable.getSelectedSongs();
	    MainController.this.diDawnManager.download(selectedSongs);
	}
    }

    private class DownloadAllSongsButtonListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    List<Song> songs = MainController.this.songTable.getSongs();
	    MainController.this.diDawnManager.download(songs);
	}
    }

    private class DiDawnManagerListener implements DownloadManagerListener {

	private static final int DOWNLOAD_SPEED_DISPLAY_INTERVAL = 1_000;
	private long lastUpdate = 0L;

	@Override
	public void onStateChange(DiDawnManager.State newState) {
	    MainController.this.downloadStatusBar.setDownloaderState(newState);
	    if (newState == DOWNLOADING) {
		MainController.this.downloadStatusBar.setAbortDownloadButtonVisible(true);
	    } else {
		MainController.this.downloadStatusBar.setAbortDownloadButtonVisible(false);
	    }

	}

	@Override
	public void onProgress(Song song, int songIndexInQueue, int progressInPromille) {
	    MainController.this.downloadStatusBar.setCurrentlyDownloadedSongIndex(songIndexInQueue + 1);
	    MainController.this.downloadStatusBar.setProgress(progressInPromille);
	    MainController.this.downloadStatusBar
		    .setNumberOfSongsToDownload(MainController.this.diDawnManager.getNumberOfSongsInQueue());
	}

	@Override
	public void onExceptionDuringDownload(Song song, int indexInQueue, Throwable trowableThatOccured) {
	    if (!(trowableThatOccured instanceof UnknownHostException)
		    && !(trowableThatOccured.getCause() instanceof UnknownHostException)
		    && !(trowableThatOccured instanceof NoRouteToHostException)
		    && !(trowableThatOccured.getCause() instanceof NoRouteToHostException)) {
		// Not possible to handle this
	    } else {
		MainController.this.noConnectionErrorDialog.setVisible(true);
	    }
	}

	@Override
	public void onSpeedUpdate(Song song, int speedInKilobitsPerSecond) {
	    if (currentTimeMillis() - this.lastUpdate > DOWNLOAD_SPEED_DISPLAY_INTERVAL || song == null) {
		MainController.this.downloadStatusBar.setDownloadSpeed(speedInKilobitsPerSecond);
		this.lastUpdate = currentTimeMillis();
	    }
	}
    }

    private class ArtistSubdirectoryListener implements ChangeListener {

	@Override
	public void stateChanged(ChangeEvent e) {
	    JCheckBox artistCheckBox = (JCheckBox) e.getSource();
	    boolean createArtistSubdirectory = artistCheckBox.isSelected();
	    MainController.this.songDestinationGenerator.setCreateArtistSubdirectory(createArtistSubdirectory);
	    MainController.this.prefs.putBoolean("create-artist-subdirectory", createArtistSubdirectory);
	}
    }

    private class AlbumSubdirectoryListener implements ChangeListener {

	@Override
	public void stateChanged(ChangeEvent e) {
	    JCheckBox albumCheckBox = (JCheckBox) e.getSource();
	    boolean createAlbumSubdirectory = albumCheckBox.isSelected();
	    MainController.this.songDestinationGenerator.setCreateAlbumSubdirectory(createAlbumSubdirectory);
	    MainController.this.prefs.putBoolean("create-album-subdirectory", createAlbumSubdirectory);
	}
    }

    private class OverwriteFilesListener implements ChangeListener {

	@Override
	public void stateChanged(ChangeEvent e) {
	    JCheckBox overwriteCheckBox = (JCheckBox) e.getSource();
	    boolean overwrite = overwriteCheckBox.isSelected();
	    MainController.this.diDawnManager.setOverwriteFiles(overwrite);
	    MainController.this.prefs.putBoolean("overwrite-files", overwrite);
	}
    }
}
