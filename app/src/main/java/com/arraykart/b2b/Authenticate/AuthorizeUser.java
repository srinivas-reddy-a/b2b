package com.arraykart.b2b.Authenticate;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.arraykart.b2b.Loading.LoadingDialog;
import com.arraykart.b2b.Retrofit.ModelClass.AuthorizeToken;
import com.arraykart.b2b.Retrofit.ModelClass.SuccessMessage;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.arraykart.b2b.SignUp.SignUpActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorizeUser {
    private Activity activity;
    private LoadingDialog loadingDialog;
    private SharedPreferenceManager sharedPreferenceManager;


    public AuthorizeUser(Activity activity) {
        this.activity = activity;
        sharedPreferenceManager = new SharedPreferenceManager(activity);
    }

    public void isLoggedIn(){
        if(sharedPreferenceManager.checkKey("token")){
            if(sharedPreferenceManager.getString("token")!=null
                    || !sharedPreferenceManager.getString("token").isEmpty()){
                AuthorizeToken authorizeToken = new AuthorizeToken(sharedPreferenceManager.getString("token"));
                Call<SuccessMessage> call = RetrofitClient.getClient()
                        .getApi().setAuthUser(
                                sharedPreferenceManager.getString("token"),
                                authorizeToken
                        );
//                loadingDialog = new LoadingDialog(activity);
//                loadingDialog.startLoadingDialog();
                call.enqueue(new Callback<SuccessMessage>() {
                    @Override
                    public void onResponse(@NonNull Call<SuccessMessage> call, @NonNull Response<SuccessMessage> response) {
//                        Log.e("token", "before response "+response.code()+ ", "+response.body());
//                        loadingDialog.dismissLoadingDialog();
                        if(!response.isSuccessful()){
                            sharedPreferenceManager.setString("token", "");
                            Intent i = new Intent(activity, SignUpActivity.class);
                            activity.startActivity(i);
                            Toast.makeText(activity, "Sign up first!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        assert response.body() != null;
                        if(!response.body().getSuccess() && response.code()==403){
                            sharedPreferenceManager.setString("token", "");
                            Toast.makeText(activity, "Sign up first!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(activity, SignUpActivity.class);
                            activity.startActivity(i);
                            return;
                        }

                        return;
                    }

                    @Override
                    public void onFailure(@NonNull Call<SuccessMessage> call, @NonNull Throwable t) {
//                        loadingDialog.dismissLoadingDialog();
                        Toast.makeText(activity, "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            else {
                sharedPreferenceManager.setString("token", "");
                Toast.makeText(activity, "Sign up first!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(activity, SignUpActivity.class);
                activity.startActivity(i);
            }
        }
        else {
            sharedPreferenceManager.setString("token", "");
            Toast.makeText(activity, "Sign up first!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(activity, SignUpActivity.class);
            activity.startActivity(i);
        }
    }
}
