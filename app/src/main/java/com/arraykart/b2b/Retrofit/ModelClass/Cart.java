package com.arraykart.b2b.Retrofit.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cart {


    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("product_id")
    @Expose
    private String productId;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("volume")
    @Expose
    private String volume;

    @SerializedName("quantity")
    @Expose
    private String quantity;

    @SerializedName("discount")
    @Expose
    private String discount;

    public Cart(String productId, String price, String volume, String quantity, String discount, String id) {
        this.productId = productId;
        this.price = price;
        this.volume = volume;
        this.quantity = quantity;
        this.discount = discount;
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
