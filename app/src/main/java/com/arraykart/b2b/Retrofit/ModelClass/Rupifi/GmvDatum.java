
package com.arraykart.b2b.Retrofit.ModelClass.Rupifi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GmvDatum {

    @SerializedName("monthYear")
    @Expose
    private String monthYear;
    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("count")
    @Expose
    private Integer count;

    /**
     * No args constructor for use in serialization
     * 
     */
    public GmvDatum() {
    }

    /**
     * 
     * @param monthYear
     * @param count
     * @param value
     */
    public GmvDatum(String monthYear, Integer value, Integer count) {
        super();
        this.monthYear = monthYear;
        this.value = value;
        this.count = count;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
