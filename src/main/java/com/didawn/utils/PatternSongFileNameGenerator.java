package com.didawn.utils;

import static com.didawn.utils.FileNameCleaner.removeIllegalChars;
import static java.lang.String.format;
import static java.lang.String.valueOf;

import com.didawn.models.Song;

/**
 *
 * @author fabier
 */
public class PatternSongFileNameGenerator implements SongFileNameGenerator {

    private String pattern;

    /**
     *
     * @param pattern
     */
    public PatternSongFileNameGenerator(String pattern) {
	this.pattern = pattern;
    }

    /**
     *
     * @param song
     * @return
     */
    @Override
    public String getFileName(Song song) {
	String fileName = this.pattern;
	fileName = fileName.replace("%ID%", song.getId());
	fileName = fileName.replace("%AltID%", song.getAlternativeID());
	fileName = fileName.replace("%Artist%", song.getArtists().get(0));
	fileName = fileName.replace("%AlbumArtist%", song.getAlbumArtist());
	fileName = fileName.replace("%Title%", song.getTitle());
	fileName = fileName.replace("%Album%", song.getAlbum());
	fileName = fileName.replace("%Genre%", song.getGenre());
	fileName = fileName.replace("%Year%", song.getYear());
	fileName = fileName.replace("%DiskNr%", valueOf(song.getDiskNumber()));
	if (!(fileName = fileName.replace("%TitleNr%", format("%02d", song.getTrackNumber()))).endsWith(".mp3")) {
	    fileName += ".mp3";
	}

	return removeIllegalChars(fileName);
    }

    /**
     *
     * @return
     */
    public String getPattern() {
	return this.pattern;
    }

    /**
     *
     * @param pattern
     */
    public void setPattern(String pattern) {
	this.pattern = pattern;
    }
}
