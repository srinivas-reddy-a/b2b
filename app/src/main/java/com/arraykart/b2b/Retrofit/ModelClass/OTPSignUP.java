package com.arraykart.b2b.Retrofit.ModelClass;

import android.util.Log;

import com.google.gson.annotations.Expose;

public class OTPSignUP {

    @Expose
    private String otp;

    @Expose
    private String  phoneNumber;

    @Expose
    private Boolean existingUser;

    public OTPSignUP(String otp, String phoneNumber, Boolean existingUser) {
        this.otp = otp;
        this.phoneNumber = phoneNumber;
        this.existingUser = existingUser;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getExisitingUser() {
        return existingUser;
    }

    public void setExisitingUser(Boolean existingUser) {
        this.existingUser = existingUser;
    }
}
