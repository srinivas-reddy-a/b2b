package com.arraykart.b2b.Retrofit.ModelClass;

public class OrderDetail {
    private int order_id;
    private int product_id;
    private int quantity;
    private String volume;
    private int price;
    private int discount;

    public OrderDetail(int order_id, int product_id, int quantity, String volume, int price, int discount) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.volume = volume;
        this.price = price;
        this.discount = discount;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
