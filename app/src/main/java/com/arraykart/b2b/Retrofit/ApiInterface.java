package com.arraykart.b2b.Retrofit;

import com.arraykart.b2b.Retrofit.ModelClass.AllCrops;
import com.arraykart.b2b.Retrofit.ModelClass.CropWiseCategory;
import com.arraykart.b2b.Retrofit.ModelClass.Logout;
import com.arraykart.b2b.Retrofit.ModelClass.OTPSignUP;
import com.arraykart.b2b.Retrofit.ModelClass.PhoneNumberSignUP;
import com.arraykart.b2b.Retrofit.ModelClass.Token;
import com.arraykart.b2b.Retrofit.ModelClass.AllCategories;
import com.arraykart.b2b.Retrofit.ModelClass.CategoryWise;
import com.arraykart.b2b.Retrofit.ModelClass.SignUp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    //all categories
    @GET("/api/product/category/")
    Call<AllCategories> getAllCategories();
    //category wise products
    @GET("/api/product/")
    Call<CategoryWise> getCategoryWise(
            @Query("category") String category,
            @Query("limit") Integer limit);

    //Signup generate otp
    @POST("/api/user/register/")
    Call<SignUp> getOTP(@Body PhoneNumberSignUP phoneNumber);

    //authenticate otp
    @POST("/api/user/register/otp/")
    Call<Token> verifyOTP(@Body OTPSignUP otp);

    //logout
    @POST("/api/user/logout/")
    Call<Logout> logout(@Header("Authorization") String token);

    //all crops
    @GET("/api/crop/")
    Call<AllCrops> getCrops(@Query("limit") Integer limit);

    //crop wise categories
    @GET("/api/crop/{crop}/category")
    Call<CropWiseCategory> getCropWiseCategory(@Path("crop") String crop);

}
