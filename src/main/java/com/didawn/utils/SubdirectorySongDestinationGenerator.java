package com.didawn.utils;

import static com.didawn.utils.FileNameCleaner.removeIllegalChars;

import java.io.File;

import com.didawn.models.Song;

/**
 *
 * @author fabier
 */
public class SubdirectorySongDestinationGenerator implements SongDestinationGenerator {

    private File destinationFolder;
    private boolean createArtistSubdirectory;
    private boolean createAlbumSubdirectory;

    /**
     *
     * @param destinationFolder
     */
    public SubdirectorySongDestinationGenerator(File destinationFolder) {
	this.destinationFolder = destinationFolder;
    }

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
     * @param destinationFolder
     */
    public void setDestinationFolder(File destinationFolder) {
	this.destinationFolder = destinationFolder;
    }

    /**
     *
     * @param song
     * @return
     */
    @Override
    public File getDestinationFolder(Song song) {
	File destFolder = this.getDestinationFolder();
	if (this.createArtistSubdirectory) {
	    destFolder = new File(destFolder, removeIllegalChars(song.getAlbumArtist()));
	}

	if (this.createAlbumSubdirectory) {
	    destFolder = new File(destFolder, removeIllegalChars(song.getAlbum()));
	}

	return destFolder;
    }

    /**
     *
     * @param createSubdirectory
     */
    public void setCreateArtistSubdirectory(boolean createSubdirectory) {
	this.createArtistSubdirectory = createSubdirectory;
    }

    /**
     *
     * @param createSubdirectory
     */
    public void setCreateAlbumSubdirectory(boolean createSubdirectory) {
	this.createAlbumSubdirectory = createSubdirectory;
    }
}
