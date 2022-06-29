package com.arraykart.b2b.Retrofit.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Address {

    @SerializedName("address_name")
    @Expose
    private String addressName;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("address_line1")
    @Expose
    private String addressLine1;
    @SerializedName("postal_code")
    @Expose
    private Integer postalCode;
    @SerializedName("phone_number")
    @Expose
    private Long phoneNumber;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("gst")
    @Expose
    private String gst;
    @SerializedName("pan")
    @Expose
    private String pan;
    @SerializedName("kyc_status")
    @Expose
    private String kycStatus;

    public Address(String addressName, String addressLine1, Integer postalCode, Long phoneNumber, String city, String state, String gst, String pan) {
        this.addressName = addressName;
        this.addressLine1 = addressLine1;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.state = state;
        this.gst = gst;
        this.pan = pan;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
    }

}