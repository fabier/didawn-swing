package com.didawn.json;

import static java.util.Collections.unmodifiableList;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author fabier
 */
public class SearchResponse {

    @SerializedName("tracks")
    private Datas tracks;

    @SerializedName("genres")
    private Datas genres;

    @SerializedName("artist")
    private Artist artist;

    @SerializedName("data")
    private List<Data> data;

    @SerializedName("title")
    private String title;

    @SerializedName("label")
    private String label;

    @SerializedName("nb_tracks")
    private String nbTracks;

    @SerializedName("next")
    private String next;

    @SerializedName("cover_xl")
    private String coverXL;

    @SerializedName("cover_big")
    private String coverBig;

    @SerializedName("error")
    private Error error;

    /**
     *
     * @return
     */
    public Datas getTracks() {
	return tracks;
    }

    /**
     *
     * @param tracks
     */
    public void setTracks(Datas tracks) {
	this.tracks = tracks;
    }

    /**
     *
     * @return
     */
    public Datas getGenres() {
	return genres;
    }

    /**
     *
     * @param genres
     */
    public void setGenres(Datas genres) {
	this.genres = genres;
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
    public List<Data> getData() {
	return unmodifiableList(data);
    }

    /**
     *
     * @param data
     */
    public void setData(List<Data> data) {
	this.data = data;
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
    public String getLabel() {
	return label;
    }

    /**
     *
     * @param label
     */
    public void setLabel(String label) {
	this.label = label;
    }

    /**
     *
     * @return
     */
    public String getNbTracks() {
	return nbTracks;
    }

    /**
     *
     * @param nbTracks
     */
    public void setNbTracks(String nbTracks) {
	this.nbTracks = nbTracks;
    }

    /**
     *
     * @return
     */
    public String getCoverBig() {
	return coverBig;
    }

    /**
     *
     * @param coverBig
     */
    public void setCoverBig(String coverBig) {
	this.coverBig = coverBig;
    }

    /**
     *
     * @return
     */
    public String getCoverXL() {
	return coverXL;
    }

    /**
     *
     * @param coverXL
     */
    public void setCoverXL(String coverXL) {
	this.coverXL = coverXL;
    }

    /**
     *
     * @return
     */
    public String getNext() {
	return next;
    }

    /**
     *
     * @param next
     */
    public void setNext(String next) {
	this.next = next;
    }

    /**
     *
     * @return
     */
    public Error getError() {
	return error;
    }

    /**
     *
     * @param error
     */
    public void setError(Error error) {
	this.error = error;
    }
}
