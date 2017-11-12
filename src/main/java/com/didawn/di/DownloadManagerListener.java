package com.didawn.di;

import com.didawn.models.Song;

/**
 *
 * @author fabier
 */
public interface DownloadManagerListener {

    /**
     *
     * @param var1
     */
    void onStateChange(DiDawnManager.State var1);

    /**
     *
     * @param var1
     * @param var2
     * @param var3
     */
    void onProgress(Song var1, int var2, int var3);

    /**
     *
     * @param var1
     * @param var2
     * @param var3
     */
    void onExceptionDuringDownload(Song var1, int var2, Throwable var3);

    /**
     *
     * @param var1
     * @param var2
     */
    void onSpeedUpdate(Song var1, int var2);
}
