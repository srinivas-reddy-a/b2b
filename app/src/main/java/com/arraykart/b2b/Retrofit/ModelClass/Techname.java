
package com.arraykart.b2b.Retrofit.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Techname {

    @SerializedName("technical_name")
    @Expose
    private String technicalName;

    public Techname(String technicalName) {
        this.technicalName = technicalName;
    }

    public String getTechnicalName() {
        return technicalName;
    }

    public void setTechnicalName(String technicalName) {
        this.technicalName = technicalName;
    }

}
