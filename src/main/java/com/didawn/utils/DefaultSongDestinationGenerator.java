package com.didawn.utils;

import com.didawn.models.Song;
import java.io.File;

public class DefaultSongDestinationGenerator implements SongDestinationGenerator {

    private final File destinationFolder = new File(System.getProperty("user.home"), "Music/DiDawn");

    public File getDestinationFolder() {
        return this.destinationFolder;
    }

    public File getDestinationFolder(Song song) {
        return this.getDestinationFolder();
    }
}
