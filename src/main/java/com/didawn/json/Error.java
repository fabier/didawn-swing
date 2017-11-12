package com.didawn.json;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author fabier
 */
public class Error {

    @SerializedName("code")
    private int code;

    /**
     *
     * @return
     */
    public int getCode() {
	return code;
    }

    /**
     *
     * @param code
     */
    public void setCode(int code) {
	this.code = code;
    }
}
