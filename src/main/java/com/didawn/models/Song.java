package com.didawn.models;

import java.util.concurrent.TimeUnit;

public class Song {

    private String id;
    private String name;
    private ArtistList artists;
    private String album;
    private long duration;
    private String year;
    private long trackNum;
    private Boolean downloaded;
    private String coverURL;
    private String downloadURL;
    private String alternativeID;
    private int diskNumber;
    private String genre;
    private int genreID;
    private String albumArtist;
    private String ISRC;
    private String composer;
    private String BPM;
    private String albumTrackCount;
    private String label;

    public Song(String id, String name, ArtistList artists) {
        this(id, name, artists, "", 0L, 0L, "");
    }

    public Song(String id, String downloadURL) {
        this(id, "", new ArtistList(), "", 0L, 0L, "");
        this.downloadURL = downloadURL;
    }

    public Song(String id, String name, ArtistList artists, String album, long duration, long trackNum, String year) {
        this.id = id;
        this.name = name.trim();
        this.artists = artists;
        this.album = album.trim();
        this.duration = duration;
        this.year = year;
        this.trackNum = trackNum;
        this.genre = "";
        if (this.artists.size() == 0) {
            artists.add("");
        }

        this.albumArtist = (String) this.artists.get(0);
        this.diskNumber = 1;
        this.ISRC = "";
        this.composer = "";
        this.BPM = "";
        this.alternativeID = "";
        this.albumTrackCount = "";
        this.label = "";
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDownloadURL() {
        return this.downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.name;
    }

    public void setTitle(String name) {
        this.name = name;
    }

    public ArtistList getArtists() {
        return this.artists;
    }

    public void setArtists(ArtistList artist) {
        this.artists = artist;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getReadableDuration() {
        return this.duration > 0L ? String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(this.duration), TimeUnit.MILLISECONDS.toSeconds(this.duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this.duration))) : "0:00";
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getYear() {
        return !this.year.isEmpty() ? this.year : "";
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Long getTrackNumber() {
        return this.trackNum;
    }

    public void setTrackNum(long trackNum) {
        this.trackNum = trackNum;
    }

    public boolean isDownloaded() {
        if (this.downloaded == null) {
            this.downloaded = false;
        }

        return this.downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Song other;
            label73:
            {
                other = (Song) obj;
                if (this.id == null) {
                    if (other.id == null) {
                        break label73;
                    }
                } else if (this.id.equals(other.id)) {
                    break label73;
                }

                return false;
            }

            label66:
            {
                if (this.name == null) {
                    if (other.name == null) {
                        break label66;
                    }
                } else if (this.name.equals(other.name)) {
                    break label66;
                }

                return false;
            }

            if (this.artists == null) {
                if (other.artists != null) {
                    return false;
                }
            } else if (!this.artists.equals(other.artists)) {
                return false;
            }

            if (this.album == null) {
                if (other.album != null) {
                    return false;
                }
            } else if (!this.album.equals(other.album)) {
                return false;
            }

            if (this.year == null) {
                if (other.year != null) {
                    return false;
                }
            } else if (!this.year.equals(other.year)) {
                return false;
            }

            return this.trackNum == other.trackNum;
        }
    }

    public String getAlternativeID() {
        return this.alternativeID;
    }

    public void setAlternativeID(String alternativeID) {
        this.alternativeID = alternativeID;
    }

    public String getCoverURL() {
        return this.coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public int getDiskNumber() {
        return this.diskNumber;
    }

    public void setDiskNumber(int diskNumber) {
        this.diskNumber = diskNumber;
    }

    public int getGenreID() {
        return this.genreID;
    }

    public void setGenreID(int genreID) {
        this.genreID = genreID;
    }

    public String toString() {
        return "Song{id='" + this.id + '\'' + ", name='" + this.name + '\'' + ", artists='" + this.artists + '\'' + ", album='" + this.album + '\'' + ", duration=" + this.duration + ", year='" + this.year + '\'' + ", trackNum=" + this.trackNum + ", coverURL='" + this.coverURL + '\'' + ", downloadURL='" + this.downloadURL + '\'' + ", alternativeID='" + this.alternativeID + '\'' + ", diskNumber=" + this.diskNumber + ", genre='" + this.genre + '\'' + ", genreID=" + this.genreID + '}';
    }

    public String getAlbumArtist() {
        return this.albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getISRC() {
        return this.ISRC;
    }

    public void setISRC(String ISRC) {
        this.ISRC = ISRC;
    }

    public String getComposer() {
        return this.composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getBPM() {
        return this.BPM;
    }

    public void setBPM(String BPM) {
        this.BPM = BPM;
    }

    public String getAlbumTrackCount() {
        return this.albumTrackCount;
    }

    public void setAlbumTrackCount(String albumTrackCount) {
        this.albumTrackCount = albumTrackCount;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
