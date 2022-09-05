package com.arraykart.b2b.Retrofit;

import com.arraykart.b2b.Retrofit.ModelClass.Address;
import com.arraykart.b2b.Retrofit.ModelClass.AllCrops;
import com.arraykart.b2b.Retrofit.ModelClass.AllReviews;
import com.arraykart.b2b.Retrofit.ModelClass.AllTechNames;
import com.arraykart.b2b.Retrofit.ModelClass.AuthorizeToken;
import com.arraykart.b2b.Retrofit.ModelClass.BugReport;
import com.arraykart.b2b.Retrofit.ModelClass.Cart;
import com.arraykart.b2b.Retrofit.ModelClass.CartProductDelete;
import com.arraykart.b2b.Retrofit.ModelClass.CartProducts;
import com.arraykart.b2b.Retrofit.ModelClass.CropWiseCategory;
import com.arraykart.b2b.Retrofit.ModelClass.Kyc;
import com.arraykart.b2b.Retrofit.ModelClass.KycStatus;
import com.arraykart.b2b.Retrofit.ModelClass.Logout;
import com.arraykart.b2b.Retrofit.ModelClass.MetaData;
import com.arraykart.b2b.Retrofit.ModelClass.OTPSignUP;
import com.arraykart.b2b.Retrofit.ModelClass.PhoneNumberSignUP;
import com.arraykart.b2b.Retrofit.ModelClass.Product;
import com.arraykart.b2b.Retrofit.ModelClass.ProductsWithQuantity;
import com.arraykart.b2b.Retrofit.ModelClass.SuccessMessage;
import com.arraykart.b2b.Retrofit.ModelClass.Techname;
import com.arraykart.b2b.Retrofit.ModelClass.Token;
import com.arraykart.b2b.Retrofit.ModelClass.AllCategories;
import com.arraykart.b2b.Retrofit.ModelClass.CategoryWise;
import com.arraykart.b2b.Retrofit.ModelClass.SignUp;
import com.arraykart.b2b.Retrofit.ModelClass.User;
import com.arraykart.b2b.Retrofit.ModelClass.UserAddress;
import com.arraykart.b2b.Retrofit.ModelClass.UserProfile;
import com.arraykart.b2b.Retrofit.ModelClass.UserProfileUpdate;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    //all categories
    @GET("/api/product/category/")
    Call<AllCategories> getAllCategories(
            @Query("limit") Integer limit
    );
    //category wise products
    @GET("/api/product/")
    Call<CategoryWise> getCategoryWise(
            @Query("category") String category,
            @Query("limit") Integer limit);

    //search products
    @GET("/api/product/")
    Call<CategoryWise> getSearch(
            @Query("search") String category,
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

    //cropwise Category wise products
    @GET("/api/crop/{crop}/category/{category}/product/")
    Call<CategoryWise> getCropWiseCategoryWiseProduct(
            @Path("crop") String crop,
            @Path("category") String category
    );

    //get user profile
    @GET("/api/user/")
    Call<UserProfile> getUserProfile(
            @Header("Authorization") String token);

    //set/edit user profile
    @PUT("/api/user/")
    Call<UserProfileUpdate> setUserProfile(
            @Header("Authorization") String token,
            @Body User user);

    //report bug
    @POST("api/bug")
    Call<SuccessMessage> setReportBug(
            @Header("Authorization") String token,
            @Body BugReport bugReport
            );

    //get address
    @GET("/api/user/address")
    Call<UserAddress> getUserAddress(
            @Header("Authorization") String token
            );

    //add address
    @POST("/api/user/address")
    Call<UserAddress> setUserAddress(
            @Header("Authorization") String token,
            @Body Address address
    );

    //edit address
    @PUT("/api/user/address")
    Call<UserAddress> putUserAddress(
            @Header("Authorization") String token,
            @Body Address address
    );

    //check if user is logged in one device and authenticate token
    @POST("/api/user/auth/")
    Call<SuccessMessage> setAuthUser(
            @Header("Authorization") String token,
            @Body AuthorizeToken authorizeToken
            );

    //get top products
    //category wise products has same format
    @GET("/api/product/")
    Call<CategoryWise> setTopProducts(
            @Query("top") Boolean top,
            @Query("limit") Integer limit
    );

    //get freq products
    //category wise products has same format
    @GET("/api/product/")
    Call<CategoryWise> setFreqProducts(
            @Query("freq") Boolean top,
            @Query("limit") Integer limit
    );

    //get ads
    @GET("/api/meta/")
    Call<MetaData> getAds(@Header("Authorization") String token);

    //get technical names
    @GET("/api/techname/")
    Call<AllTechNames> getTechNames(@Header("Authorization") String token);

    //get technical name wise products
    @POST("/api/techname/product/")
    Call<CategoryWise> getTechNameProducts(
            @Header("Authorization") String token,
            @Body Techname techname
    );

    //get reviews for sign up activity
    @GET("/api/meta/review/")
    Call<AllReviews> getReviews();

    //upload kyc docs
    @Multipart
    @PUT("/api/kyc/")
    Call<SuccessMessage> setKyc(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file,
            @Part("type") RequestBody type,
            @Part("num") RequestBody num
    );

    //upload license
    @Multipart
    @PUT("/api/kyc/license/")
    Call<SuccessMessage> setLicense(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file,
            @Part("type") RequestBody type,
            @Part("date") RequestBody date
    );

    @GET("/api/kyc/")
    Call<KycStatus> getKycStatus(@Header("Authorization") String token);

    //cart
    @POST("api/cart/")
    Call<SuccessMessage> setCart(
            @Header("Authorization") String token,
            @Body Cart cart
            );

    @GET("/api/cart/")
    Call<ProductsWithQuantity> getCart(@Header("Authorization") String token);

    @HTTP(method = "DELETE", path = "/api/cart/", hasBody = true)
    Call<SuccessMessage> deleteFromCart(@Header("Authorization") String token,
                                        @Body CartProductDelete cartProductDelete);

    //using cartproduct delete modal class as it has same signnature that
    //is required to check cart status
    @POST("/api/cart/status/")
    Call<SuccessMessage> checkCart(@Header("Authorization") String token,
                                   @Body CartProductDelete cartProductDelete);

    //to update product quantity and price and discount in cart

    @PUT("/api/cart/")
    Call<SuccessMessage> editCart(@Header("Authorization") String token,
                                  @Body Cart cart);

}









