package com.didawn.json;

import static java.util.Collections.unmodifiableList;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author fabier
 */
public class Datas {

    @SerializedName("data")
    private List<Data> data;

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
}
