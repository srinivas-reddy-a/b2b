package com.arraykart.b2b.Retrofit.ModelClass;

import com.google.gson.annotations.Expose;

public class PhoneNumberSignUP {

    @Expose
    private String phoneNumber;

    public PhoneNumberSignUP(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
