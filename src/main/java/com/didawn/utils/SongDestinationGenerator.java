package com.didawn.utils;

import java.io.File;

import com.didawn.models.Song;

/**
 *
 * @author fabier
 */
public interface SongDestinationGenerator {

    /**
     *
     * @return
     */
    File getDestinationFolder();

    /**
     *
     * @param var1
     * @return
     */
    File getDestinationFolder(Song var1);
}
