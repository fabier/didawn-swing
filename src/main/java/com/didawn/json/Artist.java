/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.didawn.json;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author fabier
 */
public class Artist {

    @SerializedName("name")
    private String name;

    @SerializedName("ART_NAME")
    private String artistName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
