
package com.arraykart.b2b.Retrofit.ModelClass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AllUserOrders {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("allorders")
    @Expose
    private List<Allorder> allorders = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public AllUserOrders() {
    }

    /**
     * 
     * @param success
     * @param allorders
     */
    public AllUserOrders(Boolean success, List<Allorder> allorders) {
        super();
        this.success = success;
        this.allorders = allorders;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Allorder> getAllorders() {
        return allorders;
    }

    public void setAllorders(List<Allorder> allorders) {
        this.allorders = allorders;
    }

}
