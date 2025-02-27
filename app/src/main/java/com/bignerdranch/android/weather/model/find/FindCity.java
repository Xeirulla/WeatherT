
package com.bignerdranch.android.weather.model.find;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FindCity {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("list")
    @Expose
    private java.util.List<Cities> list = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public java.util.List<Cities> getList() {
        return list;
    }

    public void setList(java.util.List<Cities> list) {
        this.list = list;
    }

}
