package com.didawn.models;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

import java.util.Objects;

/**
 *
 * @author fabier
 */
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
    private String isrc;
    private String composer;
    private String bpm;
    private String albumTrackCount;
    private String label;

    /**
     *
     * @param id
     * @param name
     * @param artists
     */
    public Song(String id, String name, ArtistList artists) {
	this(id, name, artists, "", 0L, 0L, "");
    }

    /**
     *
     * @param id
     * @param downloadURL
     */
    public Song(String id, String downloadURL) {
	this(id, "", new ArtistList(), "", 0L, 0L, "");
	this.downloadURL = downloadURL;
    }

    /**
     *
     * @param id
     * @param name
     * @param artists
     * @param album
     * @param duration
     * @param trackNum
     * @param year
     */
    public Song(String id, String name, ArtistList artists, String album, long duration, long trackNum, String year) {
	this.id = id;
	this.name = name.trim();
	this.artists = artists;
	this.album = album.trim();
	this.duration = duration;
	this.year = year;
	this.trackNum = trackNum;
	this.genre = "";
	if (this.artists.isEmpty()) {
	    artists.add("");
	}

	this.albumArtist = this.artists.get(0);
	this.diskNumber = 1;
	this.isrc = "";
	this.composer = "";
	this.bpm = "";
	this.alternativeID = "";
	this.albumTrackCount = "";
	this.label = "";
    }

    /**
     *
     * @return
     */
    public String getGenre() {
	return this.genre;
    }

    /**
     *
     * @param genre
     */
    public void setGenre(String genre) {
	this.genre = genre;
    }

    /**
     *
     * @return
     */
    public String getDownloadURL() {
	return this.downloadURL;
    }

    /**
     *
     * @param downloadURL
     */
    public void setDownloadURL(String downloadURL) {
	this.downloadURL = downloadURL;
    }

    /**
     *
     * @return
     */
    public String getId() {
	return this.id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
	return this.name;
    }

    /**
     *
     * @param name
     */
    public void setTitle(String name) {
	this.name = name;
    }

    /**
     *
     * @return
     */
    public ArtistList getArtists() {
	return this.artists;
    }

    /**
     *
     * @param artist
     */
    public void setArtists(ArtistList artist) {
	this.artists = artist;
    }

    /**
     *
     * @return
     */
    public String getAlbum() {
	return this.album;
    }

    /**
     *
     * @param album
     */
    public void setAlbum(String album) {
	this.album = album;
    }

    /**
     *
     * @return
     */
    public String getReadableDuration() {
	return this.duration > 0L
		? format("%d:%02d", MILLISECONDS.toMinutes(this.duration),
			MILLISECONDS.toSeconds(this.duration)
				- MINUTES.toSeconds(MILLISECONDS.toMinutes(this.duration)))
		: "0:00";
    }

    /**
     *
     * @return
     */
    public long getDuration() {
	return this.duration;
    }

    /**
     *
     * @param duration
     */
    public void setDuration(long duration) {
	this.duration = duration;
    }

    /**
     *
     * @return
     */
    public String getYear() {
	return !this.year.isEmpty() ? this.year : "";
    }

    /**
     *
     * @param year
     */
    public void setYear(String year) {
	this.year = year;
    }

    /**
     *
     * @return
     */
    public Long getTrackNumber() {
	return this.trackNum;
    }

    /**
     *
     * @param trackNum
     */
    public void setTrackNum(long trackNum) {
	this.trackNum = trackNum;
    }

    /**
     *
     * @return
     */
    public boolean isDownloaded() {
	if (this.downloaded == null) {
	    this.downloaded = false;
	}

	return this.downloaded;
    }

    /**
     *
     * @param downloaded
     */
    public void setDownloaded(boolean downloaded) {
	this.downloaded = downloaded;
    }

    @Override
    public int hashCode() {
	return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	} else if (this.getClass() != obj.getClass()) {
	    return false;
	} else {
	    Song other = (Song) obj;
	    return Objects.equals(this.id, other.id) && Objects.equals(this.name, other.name)
		    && Objects.deepEquals(this.albumArtist, other.albumArtist)
		    && Objects.equals(this.album, other.album) && Objects.equals(this.year, other.year)
		    && Objects.equals(this.trackNum, other.trackNum);
	}
    }

    /**
     *
     * @return
     */
    public String getAlternativeID() {
	return this.alternativeID;
    }

    /**
     *
     * @param alternativeID
     */
    public void setAlternativeID(String alternativeID) {
	this.alternativeID = alternativeID;
    }

    /**
     *
     * @return
     */
    public String getCoverURL() {
	return this.coverURL;
    }

    /**
     *
     * @param coverURL
     */
    public void setCoverURL(String coverURL) {
	this.coverURL = coverURL;
    }

    /**
     *
     * @return
     */
    public int getDiskNumber() {
	return this.diskNumber;
    }

    /**
     *
     * @param diskNumber
     */
    public void setDiskNumber(int diskNumber) {
	this.diskNumber = diskNumber;
    }

    /**
     *
     * @return
     */
    public int getGenreID() {
	return this.genreID;
    }

    /**
     *
     * @param genreID
     */
    public void setGenreID(int genreID) {
	this.genreID = genreID;
    }

    @Override
    public String toString() {
	return "Song{id='" + this.id + '\'' + ", name='" + this.name + '\'' + ", artists='" + this.artists + '\''
		+ ", album='" + this.album + '\'' + ", duration=" + this.duration + ", year='" + this.year + '\''
		+ ", trackNum=" + this.trackNum + ", coverURL='" + this.coverURL + '\'' + ", downloadURL='"
		+ this.downloadURL + '\'' + ", alternativeID='" + this.alternativeID + '\'' + ", diskNumber="
		+ this.diskNumber + ", genre='" + this.genre + '\'' + ", genreID=" + this.genreID + '}';
    }

    /**
     *
     * @return
     */
    public String getAlbumArtist() {
	return this.albumArtist;
    }

    /**
     *
     * @param albumArtist
     */
    public void setAlbumArtist(String albumArtist) {
	this.albumArtist = albumArtist;
    }

    /**
     *
     * @return
     */
    public String getIsrc() {
	return this.isrc;
    }

    /**
     *
     * @param isrc
     */
    public void setIsrc(String isrc) {
	this.isrc = isrc;
    }

    /**
     *
     * @return
     */
    public String getComposer() {
	return this.composer;
    }

    /**
     *
     * @param composer
     */
    public void setComposer(String composer) {
	this.composer = composer;
    }

    /**
     *
     * @return
     */
    public String getBpm() {
	return this.bpm;
    }

    /**
     *
     * @param bpm
     */
    public void setBpm(String bpm) {
	this.bpm = bpm;
    }

    /**
     *
     * @return
     */
    public String getAlbumTrackCount() {
	return this.albumTrackCount;
    }

    /**
     *
     * @param albumTrackCount
     */
    public void setAlbumTrackCount(String albumTrackCount) {
	this.albumTrackCount = albumTrackCount;
    }

    /**
     *
     * @return
     */
    public String getLabel() {
	return this.label;
    }

    /**
     *
     * @param label
     */
    public void setLabel(String label) {
	this.label = label;
    }
}
