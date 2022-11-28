
package com.arraykart.b2b.Retrofit.ModelClass;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Allorder {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("products_id")
    @Expose
    private Integer productsId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("volume")
    @Expose
    private String volume;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;
    @SerializedName("is_shipped")
    @Expose
    private Integer isShipped;
    @SerializedName("is_delivered")
    @Expose
    private Integer isDelivered;
    @SerializedName("delivered_on")
    @Expose
    private Object deliveredOn;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("modified_at")
    @Expose
    private String modifiedAt;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("address_id")
    @Expose
    private Integer addressId;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("is_paid")
    @Expose
    private Integer isPaid;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Allorder() {
    }

    /**
     * 
     * @param productsId
     * @param quantity
     * @param orderId
     * @param modifiedAt
     * @param discount
     * @param userId
     * @param addressId
     * @param paymentType
     * @param volume
     * @param isDelivered
     * @param createdAt
     * @param isPaid
     * @param total
     * @param price
     * @param id
     * @param isShipped
     * @param deliveredOn
     * @param deliveryDate
     */
    public Allorder(Integer id, Integer orderId, Integer price, Integer productsId, Integer quantity, String volume, String discount, String deliveryDate, Integer isShipped, Integer isDelivered, Object deliveredOn, String createdAt, String modifiedAt, Integer userId, Integer total, Integer addressId, String paymentType, Integer isPaid) {
        super();
        this.id = id;
        this.orderId = orderId;
        this.price = price;
        this.productsId = productsId;
        this.quantity = quantity;
        this.volume = volume;
        this.discount = discount;
        this.deliveryDate = deliveryDate;
        this.isShipped = isShipped;
        this.isDelivered = isDelivered;
        this.deliveredOn = deliveredOn;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.userId = userId;
        this.total = total;
        this.addressId = addressId;
        this.paymentType = paymentType;
        this.isPaid = isPaid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getProductsId() {
        return productsId;
    }

    public void setProductsId(Integer productsId) {
        this.productsId = productsId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getIsShipped() {
        return isShipped;
    }

    public void setIsShipped(Integer isShipped) {
        this.isShipped = isShipped;
    }

    public Integer getIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(Integer isDelivered) {
        this.isDelivered = isDelivered;
    }

    public Object getDeliveredOn() {
        return deliveredOn;
    }

    public void setDeliveredOn(Object deliveredOn) {
        this.deliveredOn = deliveredOn;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Integer isPaid) {
        this.isPaid = isPaid;
    }

}
