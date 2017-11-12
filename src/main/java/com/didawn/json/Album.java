package com.didawn.json;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author fabier
 */
public class Album {

    @SerializedName("title")
    private String title;

    @SerializedName("cover_xl")
    private String coverXL;

    @SerializedName("cover_big")
    private String coverBig;

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
}
