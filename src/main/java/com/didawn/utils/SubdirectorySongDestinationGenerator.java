package com.didawn.utils;

import com.didawn.models.Song;
import java.io.File;

public class SubdirectorySongDestinationGenerator implements SongDestinationGenerator {

    private File destinationFolder;
    private boolean createArtistSubdirectory;
    private boolean createAlbumSubdirectory;

    public SubdirectorySongDestinationGenerator(File destinationFolder) {
        this.destinationFolder = destinationFolder;
    }

    public File getDestinationFolder() {
        return this.destinationFolder;
    }

    public void setDestinationFolder(File destinationFolder) {
        this.destinationFolder = destinationFolder;
    }

    public File getDestinationFolder(Song song) {
        File destinationFolder = this.getDestinationFolder();
        if (this.createArtistSubdirectory) {
            destinationFolder = new File(destinationFolder, FileNameCleaner.removeIllegalChars(song.getAlbumArtist()));
        }

        if (this.createAlbumSubdirectory) {
            destinationFolder = new File(destinationFolder, FileNameCleaner.removeIllegalChars(song.getAlbum()));
        }

        return destinationFolder;
    }

    public void setCreateArtistSubdirectory(boolean createSubdirectory) {
        this.createArtistSubdirectory = createSubdirectory;
    }

    public void setCreateAlbumSubdirectory(boolean createSubdirectory) {
        this.createAlbumSubdirectory = createSubdirectory;
    }
}
