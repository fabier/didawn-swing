package com.didawn.di;

import static com.didawn.di.DiDawnManager.State.ABORTING;
import static com.didawn.di.DiDawnManager.State.DOWNLOADING;
import static com.didawn.di.DiDawnManager.State.IDLE;
import static java.lang.System.nanoTime;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.didawn.models.Song;
import com.didawn.utils.DefaultSongDestinationGenerator;
import com.didawn.utils.DefaultSongFileNameGenerator;
import com.didawn.utils.SongDestinationGenerator;
import com.didawn.utils.SongFileNameGenerator;

/**
 *
 * @author fabier
 */
public class DiDawnManager {

    private final List<DownloadManagerListener> listeners = new ArrayList<>();
    private final DiDawn downloader = new DiDawn();
    private final List<Song> downloadQueue = new ArrayList<>();
    private boolean overwriteFiles = false;
    private DiDawnManager.InternalDownloader internalDownloader;
    private DiDawnManager.State state;
    private SongFileNameGenerator fileNameGenerator;
    private SongDestinationGenerator songDestinationController;

    /**
     *
     */
    public DiDawnManager() {
	this.state = IDLE;
	this.fileNameGenerator = new DefaultSongFileNameGenerator();
	this.songDestinationController = new DefaultSongDestinationGenerator();
	this.downloader.addDownloadProgressListener(new DiDawnManager.InternalDownloadProgressListener());
    }

    /**
     *
     * @param songsToDownload
     */
    public void download(List<Song> songsToDownload) {
	if (this.state == IDLE) {
	    this.downloadQueue.clear();
	    this.downloadQueue.addAll(songsToDownload);
	    this.internalDownloader = new DiDawnManager.InternalDownloader();
	    this.internalDownloader.start();
	} else {
	    this.downloadQueue.addAll(songsToDownload);
	}

    }

    /**
     *
     */
    public void abortDownload() {
	this.setState(ABORTING);
    }

    /**
     *
     * @return
     */
    public int getNumberOfSongsInQueue() {
	return this.downloadQueue.size();
    }

    /**
     *
     * @param listener
     */
    public void addDownloadManagerListener(DownloadManagerListener listener) {
	this.listeners.add(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeDownloadManagerListener(DownloadManagerListener listener) {
	this.listeners.remove(listener);
    }

    /**
     *
     * @param songDestinationController
     */
    public void setSongDestinationController(SongDestinationGenerator songDestinationController) {
	this.songDestinationController = songDestinationController;
    }

    /**
     *
     * @param fileNameGenerator
     */
    public void setSongFileNameGenerator(SongFileNameGenerator fileNameGenerator) {
	this.fileNameGenerator = fileNameGenerator;
    }

    /**
     *
     * @param overwriteFiles
     */
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
	listeners.forEach(listener -> listener.onStateChange(this.state));
    }

    private void publishProgressUpdate(Song song, int indexInQueue, int progress) {
	listeners.forEach(listener -> listener.onProgress(song, indexInQueue, progress));
    }

    private void publishSpeedUpdate(Song song, int speed) {
	listeners.forEach(listener -> listener.onSpeedUpdate(song, speed));
    }

    private void publishExceptionDuringDownload(Song song, int indexInQueue, Throwable trowableThatOccured) {
	listeners.forEach(listener -> listener.onExceptionDuringDownload(song, indexInQueue, trowableThatOccured));
    }

    /**
     *
     */
    public enum State {
	/**
	 *
	 */
	IDLE,
	/**
	*
	*/
	DOWNLOADING,
	/**
	*
	*/
	ABORTING
    }

    private class InternalDownloader extends Thread {

	private int downloadSongIndex;

	private InternalDownloader() {
	    this.downloadSongIndex = 0;
	}

	@Override
	public void run() {
	    DiDawnManager.this.setState(DOWNLOADING);

	    for (; this.downloadSongIndex < DiDawnManager.this.downloadQueue.size()
		    && DiDawnManager.this.state == DOWNLOADING; ++this.downloadSongIndex) {
		Song songToDownload = DiDawnManager.this.downloadQueue.get(this.downloadSongIndex);
		File destinationFolder = DiDawnManager.this.songDestinationController
			.getDestinationFolder(songToDownload);
		String destinationFileName = DiDawnManager.this.fileNameGenerator.getFileName(songToDownload);
		File destinationFile = new File(destinationFolder, destinationFileName);

		try {
		    DiDawnManager.this.downloader.download(songToDownload, destinationFile,
			    DiDawnManager.this.overwriteFiles);
		} catch (IOException var6) {
		    DiDawnManager.this.publishExceptionDuringDownload(songToDownload, this.downloadSongIndex, var6);
		}
	    }

	    DiDawnManager.this.setState(IDLE);
	    DiDawnManager.this.publishSpeedUpdate(null, 0);
	}

	public int getDownloadSongIndex() {
	    return this.downloadSongIndex;
	}
    }

    private class InternalDownloadProgressListener implements DownloadProgressListener {

	private long lastUpdateLoadedBytes;
	private long lastUpdateTime;
	private int lastSpeed;

	private InternalDownloadProgressListener() {
	    this.lastUpdateLoadedBytes = 0L;
	    this.lastUpdateTime = nanoTime();
	    this.lastSpeed = 0;
	}

	@Override
	public void onProgress(Song song, long bytesLoaded, long bytesAtAll) {
	    int indexInQueue = DiDawnManager.this.internalDownloader.getDownloadSongIndex();
	    int progressInPromille = (int) (bytesLoaded / (double) bytesAtAll * 1000.0D);
	    DiDawnManager.this.publishProgressUpdate(song, indexInQueue, progressInPromille);
	    long timeTemp = nanoTime();
	    double timeInterval = (timeTemp - this.lastUpdateTime);
	    double bits = (bytesLoaded - this.lastUpdateLoadedBytes) * 8.0D;
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
    }

}
