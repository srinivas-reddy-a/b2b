package com.arraykart.b2b.Retrofit.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartProductDelete {
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("volume")
    @Expose
    private String volume;

    public CartProductDelete(String productId, String volume) {
        this.productId = productId;
        this.volume = volume;
    }
}
