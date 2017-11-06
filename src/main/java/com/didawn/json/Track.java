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

    public final String getSongID() {
        return songID;
    }

    public final void setSongID(String songID) {
        this.songID = songID;
    }

    public final int getMediaVersion() {
        return mediaVersion;
    }

    public final void setMediaVersion(int mediaVersion) {
        this.mediaVersion = mediaVersion;
    }

    public final String getPuid() {
        return puid;
    }

    public final void setPuid(String puid) {
        this.puid = puid;
    }

    public final String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        this.title = title;
    }

    public final String getAlbumTitle() {
        return albumTitle;
    }

    public final void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public final String getArtistName() {
        return artistName;
    }

    public final void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public final long getDuration() {
        return duration;
    }

    public final void setDuration(long duration) {
        this.duration = duration;
    }

    public final long getTrackNumber() {
        return trackNumber;
    }

    public final void setTrackNumber(long trackNumber) {
        this.trackNumber = trackNumber;
    }

    public final int getDiskNumber() {
        return diskNumber;
    }

    public final void setDiskNumber(int diskNumber) {
        this.diskNumber = diskNumber;
    }

    public final String getAlbumPicture() {
        return albumPicture;
    }

    public final void setAlbumPicture(String albumPicture) {
        this.albumPicture = albumPicture;
    }

    public long getFileSizeMp3_320() {
        return fileSizeMp3_320;
    }

    public void setFileSizeMp3_320(long fileSizeMp3_320) {
        this.fileSizeMp3_320 = fileSizeMp3_320;
    }

    public long getFileSizeMp3_256() {
        return fileSizeMp3_256;
    }

    public void setFileSizeMp3_256(long fileSizeMp3_256) {
        this.fileSizeMp3_256 = fileSizeMp3_256;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getIsrc() {
        return isrc;
    }

    public void setIsrc(String isrc) {
        this.isrc = isrc;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getBpm() {
        return bpm;
    }

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
            return Crypter.getDownloadURL(puid, 0, songID.substring(1), mediaVersion);
        } else {
            return Crypter.getDownloadURL(puid, getFormat(), songID, mediaVersion);
        }
    }

    public Song toSong() {
        Song song = new Song(songID, title, getArtistList(), albumTitle, duration, trackNumber, year);
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
