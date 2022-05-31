
package com.arraykart.b2b.Retrofit.ModelClass;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Review {

    @SerializedName("reviewimg")
    @Expose
    private String reviewimg;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("review_cus_name")
    @Expose
    private String reviewCusName;

    public String getReviewimg() {
        return reviewimg;
    }

    public void setReviewimg(String reviewimg) {
        this.reviewimg = reviewimg;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewCusName() {
        return reviewCusName;
    }

    public void setReviewCusName(String reviewCusName) {
        this.reviewCusName = reviewCusName;
    }

}
