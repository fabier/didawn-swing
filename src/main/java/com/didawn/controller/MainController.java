package com.didawn.controller;

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

public final class MainController {

    private static final ResourceBundle RES = ResourceBundle.getBundle("searchdialog");
    private static final ResourceBundle RES_ERROR = ResourceBundle.getBundle("errormessages");
    private static final String DEFAULT_DOWNLOAD_DESTINATION = System.getProperty("user.home") + "/Music";
    private static final String DEFAULT_SONG_FILE_NAME_PATTERN = "%Artist% - %Title%";
    private static final String PREFS_KEY_DOWNLOAD_DESTINATION = "download-destination";
    private static final String PREFS_KEY_SONG_FILE_NAME_PATTERN = "song-file-name-pattern";
    private static final String PREFS_KEY_CREATE_ARTIST_SUBDIRECTORY = "create-artist-subdirectory";
    private static final String PREFS_KEY_CREATE_ALBUM_SUBDIRECTORY = "create-album-subdirectory";
    private static final String PREFS_KEY_OVERWRITE_FILES = "overwrite-files";
    private final Preferences prefs = Preferences.userRoot().node("user-settings");
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

    public MainController(JFrame frame, SearchArea searchArea, SettingsPanel settingsPanel, SongTable songTable, DownloadArea downloadArea, DownloadStatusBar downloadStatusBar) {
        String downloadDestination = this.prefs.get("download-destination", DEFAULT_DOWNLOAD_DESTINATION);
        this.songDestinationGenerator = new SubdirectorySongDestinationGenerator(new File(downloadDestination));
        String songFileNamePattern = this.prefs.get("song-file-name-pattern", "%Artist% - %Title%");
        this.songFileNameGenerator = new PatternSongFileNameGenerator(songFileNamePattern);
        boolean createArtistSubdirectory = this.prefs.getBoolean("create-artist-subdirectory", false);
        boolean createAlbumSubdirectory = this.prefs.getBoolean("create-album-subdirectory", false);
        boolean overwriteFiles = this.prefs.getBoolean("overwrite-files", false);
        this.frame = frame;
        this.searchArea = searchArea;
        this.searchArea.addSearchListener(new MainController.SearchButtonListener());
        this.songTable = songTable;
        this.songTable.addSongSelectionListener(new MainController.SongTableSelectionListener());
        this.downloadArea = downloadArea;
        this.downloadArea.setDownloadAllButtonEnabled(false);
        this.downloadArea.addDownloadSelectionButtonListener(new MainController.DownloadSelectedSongsButtonListener());
        this.downloadArea.addDownloadAllButtonListener(new MainController.DownloadAllSongsButtonListener());
        this.downloadStatusBar = downloadStatusBar;
        this.downloadStatusBar.setProgressMax(1000);
        this.downloadStatusBar.addAbortDownloadButtonListener(new MainController.AbortDownloadListener());
        this.diDawnManager.addDownloadManagerListener(new MainController.DiDawnManagerListener());
        this.diDawnManager.setSongDestinationController(this.songDestinationGenerator);
        this.diDawnManager.setSongFileNameGenerator(this.songFileNameGenerator);
        settingsPanel.addArtistSubdirectoryCheckboxListener(new MainController.ArtistSubdirectoryListener());
        settingsPanel.addAlbumSubdirectoryCheckboxListener(new MainController.AlbumSubdirectoryListener());
        settingsPanel.addOverwriteCheckboxListener(new MainController.OverwriteFilesListener());
        settingsPanel.addDownloadFolderSelectionListener(new MainController.DownloadFolderSelectionListener());
        settingsPanel.addSongFileNamePatternChangeListener(new MainController.SongFileNamePatternChangeListener());
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

        private SearchButtonListener() {
        }

        public void actionPerformed(ActionEvent e) {
            String searchTerm = MainController.this.searchArea.getSearchTerm();
            if (searchTerm.trim().equals("")) {
                this.showHint();
            } else {
                this.searchAsync(searchTerm);
            }

        }

        private void showHint() {
            String title = MainController.RES.getString("searchdialog.emptysearch.title");
            String message = MainController.RES.getString("searchdialog.emptysearch.message");
            JOptionPane.showConfirmDialog(MainController.this.frame, message, title, -1, 2);
        }

        private void searchAsync(final String searchTerm) {
            final SearchDialog searchDialog = new SearchDialog(MainController.this.frame, searchTerm);
            (new Thread() {
                public void run() {
                    try {
                        List songs = MainController.this.diApi.query(searchTerm);
                        if (songs.size() > 0) {
                            MainController.this.songTable.setSongs(songs);
                        }

                        searchDialog.setVisible(false);
                        searchDialog.dispose();
                        MainController.this.downloadArea.setDownloadAllButtonEnabled(true);
                    } catch (Throwable var2) {
                        searchDialog.setVisible(false);
                        searchDialog.dispose();
                        if (var2.getCause() instanceof UnknownHostException || var2.getCause() instanceof NoRouteToHostException) {
                            MainController.this.noConnectionErrorDialog.setVisible(true);
                        }

                        var2.printStackTrace();
                    }

                }
            }).start();
            searchDialog.setVisible(true);
        }

        // $FF: synthetic method
        SearchButtonListener(Object x1) {
            this();
        }
    }

    private class SongTableSelectionListener implements SongTable.SongSelectionListener {

        private SongTableSelectionListener() {
        }

        public void onSelectionChanged(List selectedSongs) {
            MainController.this.downloadArea.setNumberOfSongsToDownload(selectedSongs.size());
        }

        // $FF: synthetic method
        SongTableSelectionListener(Object x1) {
            this();
        }
    }

    private class DownloadSelectedSongsButtonListener implements ActionListener {

        private DownloadSelectedSongsButtonListener() {
        }

        public void actionPerformed(ActionEvent e) {
            List selectedSongs = MainController.this.songTable.getSelectedSongs();
            MainController.this.diDawnManager.download(selectedSongs);
        }

        // $FF: synthetic method
        DownloadSelectedSongsButtonListener(Object x1) {
            this();
        }
    }

    private class DownloadAllSongsButtonListener implements ActionListener {

        private DownloadAllSongsButtonListener() {
        }

        public void actionPerformed(ActionEvent e) {
            List songs = MainController.this.songTable.getSongs();
            MainController.this.diDawnManager.download(songs);
        }

        // $FF: synthetic method
        DownloadAllSongsButtonListener(Object x1) {
            this();
        }
    }

    private class AbortDownloadListener extends MouseAdapter {

        private AbortDownloadListener() {
        }

        public void mouseClicked(MouseEvent e) {
            MainController.this.diDawnManager.abortDownload();
        }

        // $FF: synthetic method
        AbortDownloadListener(Object x1) {
            this();
        }
    }

    private class DiDawnManagerListener implements DownloadManagerListener {

        private static final int DOWNLOAD_SPEED_DISPLAY_INTERVAL = 1000;
        private long lastUpdate;

        private DiDawnManagerListener() {
            this.lastUpdate = 0L;
        }

        public void onStateChange(DiDawnManager.State newState) {
            MainController.this.downloadStatusBar.setDownloaderState(newState);
            if (newState == DiDawnManager.State.DOWNLOADING) {
                MainController.this.downloadStatusBar.setAbortDownloadButtonVisible(true);
            } else {
                MainController.this.downloadStatusBar.setAbortDownloadButtonVisible(false);
            }

        }

        public void onProgress(Song song, int songIndexInQueue, int progressInPromille) {
            MainController.this.downloadStatusBar.setCurrentlyDownloadedSongIndex(songIndexInQueue + 1);
            MainController.this.downloadStatusBar.setProgress(progressInPromille);
            MainController.this.downloadStatusBar.setNumberOfSongsToDownload(MainController.this.diDawnManager.getNumberOfSongsInQueue());
        }

        public void onExceptionDuringDownload(Song song, int indexInQueue, Throwable trowableThatOccured) {
            if (!(trowableThatOccured instanceof UnknownHostException) && !(trowableThatOccured.getCause() instanceof UnknownHostException) && !(trowableThatOccured instanceof NoRouteToHostException) && !(trowableThatOccured.getCause() instanceof NoRouteToHostException)) {
                trowableThatOccured.printStackTrace();
            } else {
                MainController.this.noConnectionErrorDialog.setVisible(true);
            }

        }

        public void onSpeedUpdate(Song song, int speedInKilobitsPerSecond) {
            if (System.currentTimeMillis() - this.lastUpdate > 1000L || song == null) {
                MainController.this.downloadStatusBar.setDownloadSpeed(speedInKilobitsPerSecond);
                this.lastUpdate = System.currentTimeMillis();
            }

        }

        // $FF: synthetic method
        DiDawnManagerListener(Object x1) {
            this();
        }
    }

    private class ArtistSubdirectoryListener implements ChangeListener {

        private ArtistSubdirectoryListener() {
        }

        public void stateChanged(ChangeEvent e) {
            JCheckBox artistCheckBox = (JCheckBox) e.getSource();
            boolean createArtistSubdirectory = artistCheckBox.isSelected();
            MainController.this.songDestinationGenerator.setCreateArtistSubdirectory(createArtistSubdirectory);
            MainController.this.prefs.putBoolean("create-artist-subdirectory", createArtistSubdirectory);
        }

        // $FF: synthetic method
        ArtistSubdirectoryListener(Object x1) {
            this();
        }
    }

    private class AlbumSubdirectoryListener implements ChangeListener {

        private AlbumSubdirectoryListener() {
        }

        public void stateChanged(ChangeEvent e) {
            JCheckBox albumCheckBox = (JCheckBox) e.getSource();
            boolean createAlbumSubdirectory = albumCheckBox.isSelected();
            MainController.this.songDestinationGenerator.setCreateAlbumSubdirectory(createAlbumSubdirectory);
            MainController.this.prefs.putBoolean("create-album-subdirectory", createAlbumSubdirectory);
        }

        // $FF: synthetic method
        AlbumSubdirectoryListener(Object x1) {
            this();
        }
    }

    private class OverwriteFilesListener implements ChangeListener {

        private OverwriteFilesListener() {
        }

        public void stateChanged(ChangeEvent e) {
            JCheckBox overwriteCheckBox = (JCheckBox) e.getSource();
            boolean overwrite = overwriteCheckBox.isSelected();
            MainController.this.diDawnManager.setOverwriteFiles(overwrite);
            MainController.this.prefs.putBoolean("overwrite-files", overwrite);
        }

        // $FF: synthetic method
        OverwriteFilesListener(Object x1) {
            this();
        }
    }

    private class DownloadFolderSelectionListener implements SettingsPanel.DownloadFolderSelectionListener {

        private DownloadFolderSelectionListener() {
        }

        public void onDownloadFolderSelected(File newDownloadFolder) {
            MainController.this.songDestinationGenerator.setDestinationFolder(newDownloadFolder);
            MainController.this.prefs.put("download-destination", newDownloadFolder.getAbsolutePath());
        }

        // $FF: synthetic method
        DownloadFolderSelectionListener(Object x1) {
            this();
        }
    }

    private class SongFileNamePatternChangeListener implements SettingsPanel.SongFileNamePatternChangeListener {

        private SongFileNamePatternChangeListener() {
        }

        public void onSongFileNamePatternChanged(String newPattern) {
            MainController.this.songFileNameGenerator.setPattern(newPattern);
            MainController.this.prefs.put("song-file-name-pattern", newPattern);
        }

        // $FF: synthetic method
        SongFileNamePatternChangeListener(Object x1) {
            this();
        }
    }
}
