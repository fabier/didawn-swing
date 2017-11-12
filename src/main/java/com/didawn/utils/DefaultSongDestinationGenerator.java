package com.didawn.utils;

import static java.lang.System.getProperty;

import java.io.File;

import com.didawn.models.Song;

/**
 *
 * @author fabier
 */
public class DefaultSongDestinationGenerator implements SongDestinationGenerator {

    private final File destinationFolder = new File(getProperty("user.home"), "DiDawn");

    /**
     *
     * @return
     */
    @Override
    public File getDestinationFolder() {
	return this.destinationFolder;
    }

    /**
     *
     * @param song
     * @return
     */
    @Override
    public File getDestinationFolder(Song song) {
	return this.getDestinationFolder();
    }
}
