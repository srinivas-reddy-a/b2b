package com.arraykart.b2b.Home.Fragments.Account;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.Logout;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.arraykart.b2b.SignUp.SignUpActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphImageButton;

public class AccountFragment extends Fragment {

    private LinearLayout logoutLL;
    private SharedPreferenceManager sharedPreferenceManager;
    private TextView myProfile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        //logout
        logoutLL = view.findViewById(R.id.logoutLL);
        logoutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        //edit profile
        myProfile = view.findViewById(R.id.myProfile);
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfile();
            }
        });


        return view;
    }

    private void setProfile() {
        Intent i = new Intent(getActivity(), AccountOptionsActivity.class);
        i.putExtra("pageName", "My Profile");
        i.putExtra("fragmentName", "profile");
        startActivity(i);
    }

    private void logout() {
        sharedPreferenceManager = new SharedPreferenceManager(getActivity());
        String token = sharedPreferenceManager.getString("token");
        if(sharedPreferenceManager.getString("token") != null){
            Call<Logout> call = RetrofitClient.getClient().getApi().logout(token);
            call.enqueue(new Callback<Logout>() {
                @Override
                public void onResponse(Call<Logout> call, Response<Logout> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(getActivity(), ""+response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!response.body().getSuccess()){
                        Toast.makeText(getActivity(), "500"+"Internal Server Error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getActivity().finish();
                    sharedPreferenceManager.setString("token", null);
                    Intent i = new Intent(getActivity(), SignUpActivity.class);
                    startActivity(i);
                    Toast.makeText(getActivity(), "Logged out!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Logout> call, Throwable t) {
                    Toast.makeText(getActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}