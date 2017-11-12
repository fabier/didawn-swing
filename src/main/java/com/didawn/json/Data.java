package com.didawn.json;

import static com.didawn.Constants.dawnDotCom;
import static com.didawn.di.Crypter.getDownloadURL;
import static java.lang.String.format;

import java.util.List;

import com.didawn.models.ArtistList;
import com.didawn.models.Song;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author fabier
 */
public class Data {

    @SerializedName("link")
    private String link;

    @SerializedName("SNG_ID")
    private String songID;

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("album")
    private Album album;

    @SerializedName("artist")
    private Artist artist;

    @SerializedName("duration")
    private long duration;

    @SerializedName("alternative")
    private Alternative alternative;

    @SerializedName("name")
    private String name;

    /////////////////// ADDED
    @SerializedName("SNG_TITLE")
    private String songTitle;

    @SerializedName("ALB_TITLE")
    private String albumTitle;

    @SerializedName("MEDIA_VERSION")
    private int mediaVersion;

    @SerializedName("ART_NAME")
    private String artistName;

    @SerializedName("ARTISTS")
    private List<Artist> artists;

    @SerializedName("MD5_ORIGIN")
    private String puid;

    @SerializedName("FILESIZE_MP3_320")
    private long fileSize320kMp3;

    @SerializedName("FILESIZE_MP3_256")
    private long fileSize256kMp3;

    @SerializedName("DISK_NUMBER")
    private int diskNumber;

    @SerializedName("ISRC")
    private String isrc;

    @SerializedName("COMPOSER")
    private String composer;

    @SerializedName("BPM")
    private String bpm;

    @SerializedName("ALB_PICTURE")
    private String albumPicture;

    @SerializedName("TRACK_NUMBER")
    private long trackNumber;

    @SerializedName("DIGITAL_RELEASE_DATE")
    private String year;

    /**
     *
     * @return
     */
    public String getLink() {
	return link;
    }

    /**
     *
     * @param link
     */
    public void setLink(String link) {
	this.link = link;
    }

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
    public String getId() {
	return id;
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
    public Album getAlbum() {
	return album;
    }

    /**
     *
     * @param album
     */
    public void setAlbum(Album album) {
	this.album = album;
    }

    /**
     *
     * @return
     */
    public Artist getArtist() {
	return artist;
    }

    /**
     *
     * @param artist
     */
    public void setArtist(Artist artist) {
	this.artist = artist;
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
    public Alternative getAlternative() {
	return alternative;
    }

    /**
     *
     * @param alternative
     */
    public void setAlternative(Alternative alternative) {
	this.alternative = alternative;
    }

    /**
     *
     * @return
     */
    public String getName() {
	return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
	this.name = name;
    }

    private int getFormat() {
	if (isUserTrack()) {
	    return 0;
	} else {
	    int formatIf256k = fileSize256kMp3 > 0L ? 5 : 1;
	    return fileSize320kMp3 > 0L ? 3 : formatIf256k;
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
	Song song = new Song(songID, songTitle, getArtistList(), albumTitle, duration, trackNumber, year);
	song.setDownloadURL(getDownloadUrl());
	song.setDiskNumber(diskNumber);
	song.setAlbumArtist(this.artistName);
	song.setIsrc(this.isrc);
	song.setComposer(composer);
	song.setBpm(bpm);
	String coverURL = format("http://cdn-images.%s/images/cover/%s/500x500-000000-80-0-0.jpg", dawnDotCom(),
		this.albumPicture);
	song.setCoverURL(coverURL);
	return song;
    }

    private ArtistList getArtistList() {
	ArtistList artistList = new ArtistList();
	artistList.add(this.artistName);

	if (this.artists != null) {
	    this.artists.stream().map(Artist::getArtistName).forEachOrdered(artist -> {
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
