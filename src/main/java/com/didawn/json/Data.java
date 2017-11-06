/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.didawn.json;

import com.didawn.Constants;
import com.didawn.di.Crypter;
import com.didawn.models.ArtistList;
import com.didawn.models.Song;
import com.google.gson.annotations.SerializedName;
import java.util.List;

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
    private long fileSizeMp3_320;

    @SerializedName("FILESIZE_MP3_256")
    private long fileSizeMp3_256;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSongID() {
        return songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Alternative getAlternative() {
        return alternative;
    }

    public void setAlternative(Alternative alternative) {
        this.alternative = alternative;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
            return Crypter.getDownloadURL(puid, 0, songID.substring(1), mediaVersion);
        } else {
            return Crypter.getDownloadURL(puid, getFormat(), songID, mediaVersion);
        }
    }

    public Song toSong() {
        Song song = new Song(songID, songTitle, getArtistList(), albumTitle, duration, trackNumber, year);
        song.setDownloadURL(getDownloadUrl());
        song.setDiskNumber(diskNumber);
        song.setAlbumArtist(this.artistName);
        song.setISRC(this.isrc);
        song.setComposer(composer);
        song.setBPM(bpm);
        String coverURL = String.format(
                "http://cdn-images." + Constants.dawnDotCom() + "/images/cover/%s/500x500-000000-80-0-0.jpg",
                this.albumPicture);
        song.setCoverURL(coverURL);
        return song;
    }

    private ArtistList getArtistList() {
        ArtistList artistList = new ArtistList();
        artistList.add(this.artistName);

        if (this.artists != null) {
            for (Artist artistJson : this.artists) {
                String artist = artistJson.getArtistName();
                if (artist.contains(" feat. ")) {
                    for (String tmpArtist : artist.split(" feat. ")) {
                        if (!artistList.contains(tmpArtist)) {
                            artistList.add(tmpArtist);
                        }
                    }
                } else if (!artistList.contains(artist)) {
                    artistList.add(artist);
                }
            }
        }

        return artistList;
    }
}
