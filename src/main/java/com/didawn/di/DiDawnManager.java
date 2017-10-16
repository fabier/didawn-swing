package com.didawn.di;

import com.didawn.models.Song;
import com.didawn.utils.DefaultSongDestinationGenerator;
import com.didawn.utils.DefaultSongFileNameGenerator;
import com.didawn.utils.SongDestinationGenerator;
import com.didawn.utils.SongFileNameGenerator;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DiDawnManager {

    private final List listeners = new ArrayList();
    private final DiDawn downloader = new DiDawn();
    private final List downloadQueue = new ArrayList();
    private boolean overwriteFiles = false;
    private DiDawnManager.InternalDownloader internalDownloader;
    private DiDawnManager.State state;
    private SongFileNameGenerator fileNameGenerator;
    private SongDestinationGenerator songDestinationController;

    public DiDawnManager() {
        this.state = DiDawnManager.State.IDLE;
        this.fileNameGenerator = new DefaultSongFileNameGenerator();
        this.songDestinationController = new DefaultSongDestinationGenerator();
        this.downloader.addDownloadProgressListener(new DiDawnManager.InternalDownloadProgressListener());
    }

    public void download(List songsToDownload) {
        if (this.state == DiDawnManager.State.IDLE) {
            this.downloadQueue.clear();
            this.downloadQueue.addAll(songsToDownload);
            this.internalDownloader = new DiDawnManager.InternalDownloader();
            this.internalDownloader.start();
        } else {
            this.downloadQueue.addAll(songsToDownload);
        }

    }

    public void abortDownload() {
        this.setState(DiDawnManager.State.ABORTING);
    }

    public int getNumberOfSongsInQueue() {
        return this.downloadQueue.size();
    }

    public void addDownloadManagerListener(DownloadManagerListener listener) {
        this.listeners.add(listener);
    }

    public void removeDownloadManagerListener(DownloadManagerListener listener) {
        this.listeners.remove(listener);
    }

    public void setSongDestinationController(SongDestinationGenerator songDestinationController) {
        this.songDestinationController = songDestinationController;
    }

    public void setSongFileNameGenerator(SongFileNameGenerator fileNameGenerator) {
        this.fileNameGenerator = fileNameGenerator;
    }

    public void setOverwriteFiles(boolean overwriteFiles) {
        this.overwriteFiles = overwriteFiles;
    }

    private void setState(DiDawnManager.State state) {
        if (this.state != state) {
            this.state = state;
            this.publishStateChange();
        }

    }

    private void publishStateChange() {
        Iterator var1 = this.listeners.iterator();

        while (var1.hasNext()) {
            DownloadManagerListener listener = (DownloadManagerListener) var1.next();
            listener.onStateChange(this.state);
        }

    }

    private void publishProgressUpdate(Song song, int indexInQueue, int progress) {
        Iterator var4 = this.listeners.iterator();

        while (var4.hasNext()) {
            DownloadManagerListener listener = (DownloadManagerListener) var4.next();
            listener.onProgress(song, indexInQueue, progress);
        }

    }

    private void publishSpeedUpdate(Song song, int speed) {
        Iterator var3 = this.listeners.iterator();

        while (var3.hasNext()) {
            DownloadManagerListener listener = (DownloadManagerListener) var3.next();
            listener.onSpeedUpdate(song, speed);
        }

    }

    private void publishExceptionDuringDownload(Song song, int indexInQueue, Throwable trowableThatOccured) {
        Iterator var4 = this.listeners.iterator();

        while (var4.hasNext()) {
            DownloadManagerListener listener = (DownloadManagerListener) var4.next();
            listener.onExceptionDuringDownload(song, indexInQueue, trowableThatOccured);
        }

    }

    private class InternalDownloader extends Thread {

        private int downloadSongIndex;

        private InternalDownloader() {
            this.downloadSongIndex = 0;
        }

        public void run() {
            DiDawnManager.this.setState(DiDawnManager.State.DOWNLOADING);

            for (; this.downloadSongIndex < DiDawnManager.this.downloadQueue.size() && DiDawnManager.this.state == DiDawnManager.State.DOWNLOADING; ++this.downloadSongIndex) {
                Song songToDownload = (Song) DiDawnManager.this.downloadQueue.get(this.downloadSongIndex);
                File destinationFolder = DiDawnManager.this.songDestinationController.getDestinationFolder(songToDownload);
                String destinationFileName = DiDawnManager.this.fileNameGenerator.getFileName(songToDownload);
                File destinationFile = new File(destinationFolder, destinationFileName);

                try {
                    DiDawnManager.this.downloader.download(songToDownload, destinationFile, DiDawnManager.this.overwriteFiles);
                } catch (Throwable var6) {
                    DiDawnManager.this.publishExceptionDuringDownload(songToDownload, this.downloadSongIndex, var6);
                }
            }

            DiDawnManager.this.setState(DiDawnManager.State.IDLE);
            DiDawnManager.this.publishSpeedUpdate((Song) null, 0);
        }

        public int getDownloadSongIndex() {
            return this.downloadSongIndex;
        }

        // $FF: synthetic method
        InternalDownloader(Object x1) {
            this();
        }
    }

    private class InternalDownloadProgressListener implements DownloadProgressListener {

        private static final double BYTES_TO_BITS = 8.0D;
        private long lastUpdateLoadedBytes;
        private long lastUpdateTime;
        private int lastSpeed;

        private InternalDownloadProgressListener() {
            this.lastUpdateLoadedBytes = 0L;
            this.lastUpdateTime = System.nanoTime();
            this.lastSpeed = 0;
        }

        public void onProgress(Song song, long bytesLoaded, long bytesAtAll) {
            int indexInQueue = DiDawnManager.this.internalDownloader.getDownloadSongIndex();
            int progressInPromille = (int) ((double) bytesLoaded / (double) bytesAtAll * 1000.0D);
            DiDawnManager.this.publishProgressUpdate(song, indexInQueue, progressInPromille);
            long timeTemp = System.nanoTime();
            double timeInterval = (double) (timeTemp - this.lastUpdateTime);
            double bits = (double) (bytesLoaded - this.lastUpdateLoadedBytes) * 8.0D;
            if (bits < 0.0D) {
                bits = 0.0D;
            }

            int speed;
            if (this.lastSpeed != (speed = (int) (bits * 1.0E9D / timeInterval))) {
                DiDawnManager.this.publishSpeedUpdate(song, speed);
            }

            this.lastSpeed = speed;
            this.lastUpdateLoadedBytes = bytesLoaded;
            this.lastUpdateTime = timeTemp;
        }

        // $FF: synthetic method
        InternalDownloadProgressListener(Object x1) {
            this();
        }
    }

    public static enum State {
        IDLE,
        DOWNLOADING,
        ABORTING;
    }
}
