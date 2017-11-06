/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.didawn.json;

import com.google.gson.annotations.SerializedName;
import java.util.List;

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

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
