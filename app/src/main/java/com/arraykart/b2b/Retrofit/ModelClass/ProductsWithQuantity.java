package com.arraykart.b2b.Retrofit.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductsWithQuantity {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("products")
    @Expose
    private List<CartProducts> products = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<CartProducts> getProducts() {
        return products;
    }

    public void setProducts(List<CartProducts> products) {
        this.products = products;
    }
}
