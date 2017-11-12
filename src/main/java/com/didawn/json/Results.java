package com.didawn.json;

import static java.util.Collections.unmodifiableList;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author fabier
 */
public class Results {

    @SerializedName("tracks")
    private List<Track> tracks;

    @SerializedName("data")
    private List<Data> data;

    @SerializedName("count")
    private int count;

    /**
     *
     * @return
     */
    public List<Track> getTracks() {
	return unmodifiableList(tracks);
    }

    /**
     *
     * @param tracks
     */
    public void setTracks(List<Track> tracks) {
	this.tracks = tracks;
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
    public int getCount() {
	return count;
    }

    /**
     *
     * @param count
     */
    public void setCount(int count) {
	this.count = count;
    }
}
