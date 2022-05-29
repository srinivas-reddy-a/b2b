package com.arraykart.b2b.Retrofit.ModelClass;

import com.google.gson.annotations.Expose;

public class AuthorizeToken {

    @Expose
    private String token;

    public AuthorizeToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
