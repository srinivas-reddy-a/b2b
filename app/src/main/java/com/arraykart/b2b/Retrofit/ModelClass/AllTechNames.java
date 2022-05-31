
package com.arraykart.b2b.Retrofit.ModelClass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AllTechNames {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("technames")
    @Expose
    private List<Techname> technames = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Techname> getTechnames() {
        return technames;
    }

    public void setTechnames(List<Techname> technames) {
        this.technames = technames;
    }

}
