package com.arraykart.b2b.Retrofit.ModelClass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAddress {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("address")
    @Expose
    private List<Address> address = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

}
