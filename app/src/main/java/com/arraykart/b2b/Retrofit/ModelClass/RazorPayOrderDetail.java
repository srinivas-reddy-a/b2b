
package com.arraykart.b2b.Retrofit.ModelClass;


import com.arraykart.b2b.Retrofit.ModelClass.RpOrder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RazorPayOrderDetail {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("rp_order")
    @Expose
    private RpOrder rpOrder;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public RpOrder getRpOrder() {
        return rpOrder;
    }

    public void setRpOrder(RpOrder rpOrder) {
        this.rpOrder = rpOrder;
    }

}
