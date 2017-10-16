package com.didawn.utils;

import com.didawn.models.Song;

public class PatternSongFileNameGenerator implements SongFileNameGenerator {

    private String pattern;

    public PatternSongFileNameGenerator(String pattern) {
        this.pattern = pattern;
    }

    public String getFileName(Song song) {
        String fileName = this.pattern;
        fileName = fileName.replace("%ID%", song.getId());
        fileName = fileName.replace("%AltID%", song.getAlternativeID());
        fileName = fileName.replace("%Artist%", (CharSequence) song.getArtists().get(0));
        fileName = fileName.replace("%AlbumArtist%", song.getAlbumArtist());
        fileName = fileName.replace("%Title%", song.getTitle());
        fileName = fileName.replace("%Album%", song.getAlbum());
        fileName = fileName.replace("%Genre%", song.getGenre());
        fileName = fileName.replace("%Year%", song.getYear());
        fileName = fileName.replace("%DiskNr%", String.valueOf(song.getDiskNumber()));
        if (!(fileName = fileName.replace("%TitleNr%", String.format("%02d", song.getTrackNumber()))).endsWith(".mp3")) {
            fileName = fileName + ".mp3";
        }

        return FileNameCleaner.removeIllegalChars(fileName);
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
