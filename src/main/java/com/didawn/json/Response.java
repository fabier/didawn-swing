package com.didawn.json;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author fabier
 */
public class Response {

    @SerializedName("results")
    private Results results;

    /**
     *
     * @return
     */
    public Results getResults() {
	return results;
    }

    /**
     *
     * @param results
     */
    public void setResults(Results results) {
	this.results = results;
    }
}
