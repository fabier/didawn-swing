package com.didawn.json;

import static com.didawn.Constants.dawnDotCom;
import static com.didawn.di.Crypter.getDownloadURL;
import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;

import java.util.List;

import com.didawn.models.ArtistList;
import com.didawn.models.Song;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author fabier
 */
public class Track {

    @SerializedName("SNG_ID")
    private String songID;

    @SerializedName("MD5_ORIGIN")
    private String puid;

    @SerializedName("MEDIA_VERSION")
    private int mediaVersion;

    @SerializedName("SNG_TITLE")
    private String title;

    @SerializedName("ALB_TITLE")
    private String albumTitle;

    @SerializedName("ART_NAME")
    private String artistName;

    @SerializedName("DURATION")
    private long duration;

    @SerializedName("TRACK_NUMBER")
    private long trackNumber;

    @SerializedName("DISK_NUMBER")
    private int diskNumber;

    @SerializedName("ALB_PICTURE")
    private String albumPicture;

    // SPECIFIC NON USER TRACKS
    @SerializedName("FILESIZE_MP3_320")
    private long fileSizeMp3_320;

    // SPECIFIC NON USER TRACKS
    @SerializedName("FILESIZE_MP3_256")
    private long fileSizeMp3_256;

    // SPECIFIC NON USER TRACKS
    @SerializedName("VERSION")
    private String version;

    // SPECIFIC NON USER TRACKS
    @SerializedName("ARTISTS")
    private List<Artist> artists;

    // SPECIFIC NON USER TRACKS
    @SerializedName("DIGITAL_RELEASE_DATE")
    private String year;

    // SPECIFIC NON USER TRACKS
    @SerializedName("ISRC")
    private String isrc;

    // SPECIFIC NON USER TRACKS
    @SerializedName("COMPOSER")
    private String composer;

    // SPECIFIC NON USER TRACKS
    @SerializedName("BPM")
    private String bpm;

    /**
     *
     * @return
     */
    public String getSongID() {
	return songID;
    }

    /**
     *
     * @param songID
     */
    public void setSongID(String songID) {
	this.songID = songID;
    }

    /**
     *
     * @return
     */
    public int getMediaVersion() {
	return mediaVersion;
    }

    /**
     *
     * @param mediaVersion
     */
    public void setMediaVersion(int mediaVersion) {
	this.mediaVersion = mediaVersion;
    }

    /**
     *
     * @return
     */
    public String getPuid() {
	return puid;
    }

    /**
     *
     * @param puid
     */
    public void setPuid(String puid) {
	this.puid = puid;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
	return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     *
     * @return
     */
    public String getAlbumTitle() {
	return albumTitle;
    }

    /**
     *
     * @param albumTitle
     */
    public void setAlbumTitle(String albumTitle) {
	this.albumTitle = albumTitle;
    }

    /**
     *
     * @return
     */
    public String getArtistName() {
	return artistName;
    }

    /**
     *
     * @param artistName
     */
    public void setArtistName(String artistName) {
	this.artistName = artistName;
    }

    /**
     *
     * @return
     */
    public long getDuration() {
	return duration;
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
    public long getTrackNumber() {
	return trackNumber;
    }

    /**
     *
     * @param trackNumber
     */
    public void setTrackNumber(long trackNumber) {
	this.trackNumber = trackNumber;
    }

    /**
     *
     * @return
     */
    public int getDiskNumber() {
	return diskNumber;
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
    public String getAlbumPicture() {
	return albumPicture;
    }

    /**
     *
     * @param albumPicture
     */
    public void setAlbumPicture(String albumPicture) {
	this.albumPicture = albumPicture;
    }

    /**
     *
     * @return
     */
    public long getFileSizeMp3_320() {
	return fileSizeMp3_320;
    }

    /**
     *
     * @param fileSizeMp3_320
     */
    public void setFileSizeMp3_320(long fileSizeMp3_320) {
	this.fileSizeMp3_320 = fileSizeMp3_320;
    }

    /**
     *
     * @return
     */
    public long getFileSizeMp3_256() {
	return fileSizeMp3_256;
    }

    /**
     *
     * @param fileSizeMp3_256
     */
    public void setFileSizeMp3_256(long fileSizeMp3_256) {
	this.fileSizeMp3_256 = fileSizeMp3_256;
    }

    /**
     *
     * @return
     */
    public String getVersion() {
	return version;
    }

    /**
     *
     * @param version
     */
    public void setVersion(String version) {
	this.version = version;
    }

    /**
     *
     * @return
     */
    public List<Artist> getArtists() {
	return unmodifiableList(artists);
    }

    /**
     *
     * @param artists
     */
    public void setArtists(List<Artist> artists) {
	this.artists = artists;
    }

    /**
     *
     * @return
     */
    public String getYear() {
	return year;
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
    public String getIsrc() {
	return isrc;
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
	return composer;
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
	return bpm;
    }

    /**
     *
     * @param bpm
     */
    public void setBpm(String bpm) {
	this.bpm = bpm;
    }

    private int getFormat() {
	if (isUserTrack()) {
	    return 0;
	} else {
	    return fileSizeMp3_320 > 0L ? 3 : fileSizeMp3_256 > 0L ? 5 : 1;
	}
    }

    private boolean isUserTrack() {
	return getSongID().startsWith("-");
    }

    private String getDownloadUrl() {
	if (isUserTrack()) {
	    return getDownloadURL(puid, 0, songID.substring(1), mediaVersion);
	} else {
	    return getDownloadURL(puid, getFormat(), songID, mediaVersion);
	}
    }

    /**
     *
     * @return
     */
    public Song toSong() {
	Song song = new Song(songID, title, getArtistList(), albumTitle, duration, trackNumber, year);
	song.setDownloadURL(getDownloadUrl());
	song.setDiskNumber(diskNumber);
	song.setAlbumArtist(this.artistName);
	song.setIsrc(this.isrc);
	song.setComposer(composer);
	song.setBpm(bpm);
	String coverURL = format("http://cdn-images." + dawnDotCom() + "/images/cover/%s/500x500-000000-80-0-0.jpg",
		this.albumPicture);
	song.setCoverURL(coverURL);
	return song;
    }

    private ArtistList getArtistList() {
	ArtistList artistList = new ArtistList();
	artistList.add(this.artistName);

	if (this.artists != null) {
	    this.artists.stream().map((artistJson) -> artistJson.getArtistName()).forEachOrdered((artist) -> {
		if (artist.contains(" feat. ")) {
		    for (String tmpArtist : artist.split(" feat. ")) {
			if (!artistList.contains(tmpArtist)) {
			    artistList.add(tmpArtist);
			}
		    }
		} else if (!artistList.contains(artist)) {
		    artistList.add(artist);
		}
	    });
	}

	return artistList;
    }
}
