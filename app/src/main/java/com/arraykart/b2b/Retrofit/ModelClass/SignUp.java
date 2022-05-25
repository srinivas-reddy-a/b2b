
package com.arraykart.b2b.Retrofit.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUp {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("existingUser")
    @Expose
    private Boolean existingUser;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getExistingUser() {
        return existingUser;
    }

    public void setExistingUser(Boolean existingUser) {
        this.existingUser = existingUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
