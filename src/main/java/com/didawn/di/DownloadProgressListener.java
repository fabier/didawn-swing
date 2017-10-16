package com.didawn.di;

import com.didawn.models.Song;

interface DownloadProgressListener {

    void onProgress(Song var1, long var2, long var4);
}
