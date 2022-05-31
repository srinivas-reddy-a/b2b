package com.arraykart.b2b.Authenticate;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.arraykart.b2b.Retrofit.ModelClass.AuthorizeToken;
import com.arraykart.b2b.Retrofit.ModelClass.SuccessMessage;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorizeUser {
    private Activity activity;
    private static Boolean isLogged;

    public AuthorizeUser(Activity activity) {
        this.activity = activity;
        this.isLogged = false;
    }

    public boolean isLoggedIn(){
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(activity);
        if(preferenceManager.checkKey("token")){
            if(preferenceManager.getString("token")!=null
                    || preferenceManager.getString("token").isEmpty()){
                //Log.e("token", preferenceManager.getString("token"));
                AuthorizeToken authorizeToken = new AuthorizeToken(preferenceManager.getString("token"));
                Call<SuccessMessage> call = RetrofitClient.getClient()
                        .getApi().setAuthUser(
                                preferenceManager.getString("token"),
                                authorizeToken
                        );
                call.enqueue(new Callback<SuccessMessage>() {
                    @Override
                    public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(activity, ""+response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!response.body().getSuccess()){
                            Toast.makeText(activity, "500"+"Internal Server Error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        isLogged = true;
                    }

                    @Override
                    public void onFailure(Call<SuccessMessage> call, Throwable t) {
                        Toast.makeText(activity, "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                if(isLogged){
                    return true;
                }
            }
        }
        return false;
    }
}
