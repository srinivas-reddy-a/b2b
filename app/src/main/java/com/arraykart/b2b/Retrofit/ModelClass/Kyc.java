package com.arraykart.b2b.Retrofit.ModelClass;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Kyc {
    private MultipartBody.Part image;
    private RequestBody type;
    private RequestBody num;

    public Kyc(MultipartBody.Part image, RequestBody type, RequestBody num) {
        this.image = image;
        this.type = type;
        this.num = num;
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

    public RequestBody getNum() {
        return num;
    }

    public void setNum(RequestBody num) {
        this.num = num;
    }
}
