package com.didawn.json;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author fabier
 */
public class Alternative {

    @SerializedName("id")
    private String id;

    /**
     *
     * @return
     */
    public String getID() {
	return id;
    }

    /**
     *
     * @param id
     */
    public void setID(String id) {
	this.id = id;
    }
}
