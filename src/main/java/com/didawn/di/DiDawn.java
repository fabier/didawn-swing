package com.didawn.di;

import com.didawn.models.Song;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

class DiDawn {

    private static final Logger log = Logger.getLogger(DiDawn.class.getName());
    private final List listeners = new ArrayList();
    private final Di diApi = new Di();
    private final DiDawn.ApiDownloadProgressListener apiDownloadProgressListener = new DiDawn.ApiDownloadProgressListener();

    public void download(Song songToDownload, File destinationFile, boolean overwriteFiles) throws IOException {
        if (!destinationFile.exists() || overwriteFiles) {
            File tmpSongFile = File.createTempFile("tmp", ".mp3");
            FileUtils.forceMkdir(destinationFile.getParentFile());
            FileOutputStream out = new FileOutputStream(tmpSongFile);
            if (this.diApi.downloadHelper(songToDownload, out, this.apiDownloadProgressListener)) {
                Utils.writeTrackInfo(songToDownload, tmpSongFile);
                if (destinationFile.exists() && overwriteFiles) {
                    Utils.deleteFile(destinationFile);
                }

                FileUtils.moveFile(tmpSongFile, destinationFile);
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
        Iterator var6 = this.listeners.iterator();

        while (var6.hasNext()) {
            DownloadProgressListener listener = (DownloadProgressListener) var6.next();
            listener.onProgress(song, bytesLoaded, bytesAtAll);
        }

    }

    private class ApiDownloadProgressListener implements DownloadProgressListener {

        private ApiDownloadProgressListener() {
        }

        public void onProgress(Song song, long bytesLoaded, long bytesAtAll) {
            DiDawn.this.publishProgress(song, bytesLoaded, bytesAtAll);
        }

        // $FF: synthetic method
        ApiDownloadProgressListener(Object x1) {
            this();
        }
    }
}
