
package com.arraykart.b2b.Retrofit.ModelClass.Rupifi;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Body {

    @SerializedName("customerGMVList")
    @Expose
    private List<CustomerGMV> customerGMVList = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Body() {
    }

    /**
     * 
     * @param customerGMVList
     */
    public Body(List<CustomerGMV> customerGMVList) {
        super();
        this.customerGMVList = customerGMVList;
    }

    public List<CustomerGMV> getCustomerGMVList() {
        return customerGMVList;
    }

    public void setCustomerGMVList(List<CustomerGMV> customerGMVList) {
        this.customerGMVList = customerGMVList;
    }

}
