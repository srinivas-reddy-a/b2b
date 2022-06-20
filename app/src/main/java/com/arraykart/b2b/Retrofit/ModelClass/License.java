package com.arraykart.b2b.Retrofit.ModelClass;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class License {
    private MultipartBody.Part image;
    private RequestBody type;
    private RequestBody date;

    public License(MultipartBody.Part image, RequestBody type, RequestBody date) {
        this.image = image;
        this.type = type;
        this.date = date;
    }

    public MultipartBody.Part getImage() {
        return image;
    }

    public void setImage(MultipartBody.Part image) {
        this.image = image;
    }

    public RequestBody getType() {
        return type;
    }

    public void setType(RequestBody type) {
        this.type = type;
    }

    public RequestBody getDate() {
        return date;
    }

    public void setDate(RequestBody date) {
        this.date = date;
    }
}
