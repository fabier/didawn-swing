package com.didawn.di;

import static com.didawn.di.Utils.deleteFile;
import static com.didawn.di.Utils.writeTrackInfo;
import static java.io.File.createTempFile;
import static org.apache.commons.io.FileUtils.forceMkdir;
import static org.apache.commons.io.FileUtils.moveFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.didawn.models.Song;

class DiDawn {

    private final List<DownloadProgressListener> listeners = new ArrayList<>();
    private final Di diApi = new Di();
    private final DiDawn.ApiDownloadProgressListener apiDownloadProgressListener = new DiDawn.ApiDownloadProgressListener();

    public void download(Song songToDownload, File destinationFile, boolean overwriteFiles) throws IOException {
	if (!destinationFile.exists() || overwriteFiles) {
	    File tmpSongFile = createTempFile("tmp", ".mp3");
	    forceMkdir(destinationFile.getParentFile());
	    FileOutputStream out = new FileOutputStream(tmpSongFile);
	    if (this.diApi.downloadHelper(songToDownload, out, this.apiDownloadProgressListener)) {
		writeTrackInfo(songToDownload, tmpSongFile);
		if (destinationFile.exists() && overwriteFiles) {
		    deleteFile(destinationFile);
		}

		moveFile(tmpSongFile, destinationFile);
	    }

	}
    }

    public void addDownloadProgressListener(DownloadProgressListener listener) {
	this.listeners.add(listener);
    }

    public void removeDownloadProgressListener(DownloadProgressListener listener) {
	this.listeners.remove(listener);
    }

    private void publishProgress(Song song, long bytesLoaded, long bytesAtAll) {
	listeners.forEach((listener) -> {
	    listener.onProgress(song, bytesLoaded, bytesAtAll);
	});
    }

    private class ApiDownloadProgressListener implements DownloadProgressListener {

	private ApiDownloadProgressListener() {
	}

	@Override
	public void onProgress(Song song, long bytesLoaded, long bytesAtAll) {
	    DiDawn.this.publishProgress(song, bytesLoaded, bytesAtAll);
	}
    }
}
