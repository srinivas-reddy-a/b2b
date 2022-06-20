package com.arraykart.b2b.Authenticate;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.arraykart.b2b.Retrofit.ModelClass.KycStatus;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Kyc {
    private Activity activity;

    public Kyc(Activity activity) {
        this.activity = activity;
    }
    private SharedPreferenceManager sharedPreferenceManager;

    public void kycStatus(){
        sharedPreferenceManager = new SharedPreferenceManager(activity);
            Call<KycStatus> call = RetrofitClient.getClient()
                    .getApi().getKycStatus(sharedPreferenceManager.getString("token"));
            call.enqueue(new Callback<KycStatus>() {
                @Override
                public void onResponse(@NonNull Call<KycStatus> call, @NonNull Response<KycStatus> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(activity, "" + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    assert response.body() != null;
                    if(!response.body().getSuccess()){
                        Toast.makeText(activity, "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sharedPreferenceManager.setString("kycstatus", response.body().getKycStatus());
                }

                @Override
                public void onFailure(@NonNull Call<KycStatus> call, @NonNull Throwable t) {
                    Toast.makeText(activity, "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
                }
            });
    }
}
