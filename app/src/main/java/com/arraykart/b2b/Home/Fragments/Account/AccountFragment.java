package com.arraykart.b2b.Home.Fragments.Account;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import java.text.MessageFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphImageButton;

public class AccountFragment extends Fragment {

    private LinearLayout logoutLL;
    private SharedPreferenceManager sharedPreferenceManager;
    private TextView myProfile;
    private LinearLayout returnLL;
    private LinearLayout privacyLL;
    private LinearLayout termsLL;
    private TextView userGreeting;
    private LinearLayout contactLL;
    private LinearLayout bugLL;
    private LinearLayout kycLL;
    private TextView appVersion;
    private LinearLayout addressLL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        //setup user greeting with name
        userGreeting = view.findViewById(R.id.userGreeting);
        setUserGreeting();

        //logout
        logoutLL = view.findViewById(R.id.logoutLL);
        logoutLL.setOnClickListener(v -> logout());

        //edit profile
        myProfile = view.findViewById(R.id.myProfile);
        myProfile.setOnClickListener(v -> setProfile());

        //set privacy policy
        privacyLL = view.findViewById(R.id.privacyLL);
        privacyLL.setOnClickListener(v -> setPrivacyPolicy());

        //set return policy
        returnLL = view.findViewById(R.id.returnLL);
        returnLL.setOnClickListener(v -> setReturnPolicy());

        //set return policy
        termsLL = view.findViewById(R.id.termsLL);
        termsLL.setOnClickListener(v -> setTermsPolicy());

        //contact us
        contactLL = view.findViewById(R.id.contactLL);
        contactLL.setOnClickListener(v -> setContactUs());

        //report bug
        bugLL = view.findViewById(R.id.bugLL);
        bugLL.setOnClickListener(v -> setReportBug());

        //complete kyc
        kycLL = view.findViewById(R.id.kycLL);
        kycLL.setOnClickListener(v -> setKYC());

        //set app version
        appVersion = view.findViewById(R.id.appVersion);
        setAppVersion();

        //set address
        addressLL = view.findViewById(R.id.addressLL);
        addressLL.setOnClickListener(v -> setAddress());


        return view;
    }

    private void setAddress() {
        Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
        i.putExtra("pageName", "My Address");
        i.putExtra("fragmentName", "address");
        startActivity(i);
    }

    private void setAppVersion() {
        try {
            PackageInfo pInfo = requireActivity().getPackageManager().getPackageInfo(requireActivity().getPackageName(), 0);
            appVersion.setText(MessageFormat.format("App version: {0}", pInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setKYC() {
        Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
        i.putExtra("pageName", "KYC Document");
        i.putExtra("fragmentName", "kyc");
        startActivity(i);
    }

    private void setReportBug() {
        Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
        i.putExtra("pageName", "Report Bug");
        i.putExtra("fragmentName", "bug");
        startActivity(i);
    }

    private void setContactUs() {
        Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
        i.putExtra("pageName", "Contact Us");
        i.putExtra("fragmentName", "contact");
        startActivity(i);
    }

    private void setUserGreeting() {
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
        if(sharedPreferenceManager.checkKey("user")){
            //greet based on time
            userGreeting.setText("Namaskaar, " + sharedPreferenceManager.getString("user") + " Ji!");
        }else {
            userGreeting.setText("Namaskaar, Ji!");
        }
    }

    private void setTermsPolicy() {
        Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
        i.putExtra("pageName", "Terms & Conditions");
        i.putExtra("fragmentName", "terms");
        startActivity(i);
    }

    private void setReturnPolicy() {
        Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
        i.putExtra("pageName", "Return and Replace Policy");
        i.putExtra("fragmentName", "return");
        startActivity(i);
    }

    private void setPrivacyPolicy() {
        Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
        i.putExtra("pageName", "Privacy Policy");
        i.putExtra("fragmentName", "privacy");
        startActivity(i);
    }

    private void setProfile() {
        Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
        i.putExtra("pageName", "My Profile");
        i.putExtra("fragmentName", "profile");
        startActivity(i);
    }

    private void logout() {
        sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
        String token = sharedPreferenceManager.getString("token");
        if(sharedPreferenceManager.getString("token") != null){
            Call<Logout> call = RetrofitClient.getClient().getApi().logout(token);
            call.enqueue(new Callback<Logout>() {
                @Override
                public void onResponse(@NonNull Call<Logout> call, @NonNull Response<Logout> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(requireActivity(), ""+response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    assert response.body() != null;
                    if(!response.body().getSuccess()){
                        Toast.makeText(requireActivity(), "500"+"Internal Server Error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    requireActivity().finish();
                    sharedPreferenceManager.setString("token", null);
                    Intent i = new Intent(requireActivity(), SignUpActivity.class);
                    startActivity(i);
                    Toast.makeText(requireActivity(), "Logged out!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<Logout> call, @NonNull Throwable t) {
                    Toast.makeText(requireActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserGreeting();
    }
}