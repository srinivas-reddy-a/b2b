
package com.arraykart.b2b.Retrofit.ModelClass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllReviews {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

}
