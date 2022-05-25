
package com.arraykart.b2b.Retrofit.ModelClass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CropWiseCategory {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("cwcategories")
    @Expose
    private List<Cwcategory> cwcategories = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Cwcategory> getCwcategories() {
        return cwcategories;
    }

    public void setCwcategories(List<Cwcategory> cwcategories) {
        this.cwcategories = cwcategories;
    }

}
