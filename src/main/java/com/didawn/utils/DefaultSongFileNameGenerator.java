package com.didawn.utils;

import static java.util.Arrays.binarySearch;

import com.didawn.models.Song;

/**
 *
 * @author fabier
 */
public class DefaultSongFileNameGenerator implements SongFileNameGenerator {

    static final int[] illegalChars = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
	    20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 34, 42, 47, 58, 60, 62, 63, 92, 124 };

    private static String cleanFileName(String badFileName) {
	StringBuilder cleanName = new StringBuilder();

	for (int i = 0; i < badFileName.length(); ++i) {
	    char c = badFileName.charAt(i);
	    if (binarySearch(illegalChars, c) < 0) {
		cleanName.append(c);
	    }
	}

	return cleanName.toString();
    }

    /**
     *
     * @param song
     * @return
     */
    @Override
    public String getFileName(Song song) {
	return cleanFileName(song.getArtists() + " - " + song.getTitle() + ".mp3");
    }
}
