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
}
