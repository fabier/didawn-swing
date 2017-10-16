package com.didawn.di;

import com.didawn.models.Song;

public interface DownloadManagerListener {

    void onStateChange(DiDawnManager.State var1);

    void onProgress(Song var1, int var2, int var3);

    void onExceptionDuringDownload(Song var1, int var2, Throwable var3);

    void onSpeedUpdate(Song var1, int var2);
}
