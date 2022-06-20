package com.arraykart.b2b.Home.Fragments.Account;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.Loading.LoadingDialog;
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
    private LinearLayout abouLL;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        if(isAdded()){
            sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
        }
        checkToken();

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

        //set about us
        abouLL = view.findViewById(R.id.abouLL);
        abouLL.setOnClickListener(v -> setAbout());


        return view;
    }

    private void checkToken() {
        if(isAdded()) {
            AuthorizeUser authorizeUser = new AuthorizeUser(requireActivity());
            authorizeUser.isLoggedIn();
        }
    }

    private void setAbout() {
        if(isAdded()) {
            Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
            i.putExtra("pageName", "About Us");
            i.putExtra("fragmentName", "about");
            startActivity(i);
        }
    }

    private void setAddress() {
        if(isAdded()) {
            Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
            i.putExtra("pageName", "My Address");
            i.putExtra("fragmentName", "address");
            startActivity(i);
        }
    }



    private void setAppVersion() {
        try {
            if(isAdded()) {
                PackageInfo pInfo = requireActivity().getPackageManager().getPackageInfo(requireActivity().getPackageName(), 0);
                appVersion.setText(MessageFormat.format("App version: {0}", pInfo.versionName));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setKYC() {
        if(isAdded()) {
            Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
            i.putExtra("pageName", "KYC Document");
            i.putExtra("fragmentName", "kyc");
            startActivity(i);
        }
    }

    private void setReportBug() {
        if(isAdded()) {
            Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
            i.putExtra("pageName", "Report Bug");
            i.putExtra("fragmentName", "bug");
            startActivity(i);
        }
    }

    private void setContactUs() {
        if(isAdded()) {
            Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
            i.putExtra("pageName", "Contact Us");
            i.putExtra("fragmentName", "contact");
            startActivity(i);
        }
    }

    private void setUserGreeting() {
        if(isAdded()) {
            if (sharedPreferenceManager.checkKey("user")) {
                //greet based on time
                userGreeting.setText("नमस्कार, " + sharedPreferenceManager.getString("user") + " जी!");
            } else {
                userGreeting.setText("नमस्कार, जी!");
            }
        }
    }

    private void setTermsPolicy() {
        if(isAdded()) {
            Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
            i.putExtra("pageName", "Terms & Conditions");
            i.putExtra("fragmentName", "terms");
            startActivity(i);
        }
    }

    private void setReturnPolicy() {
        if(isAdded()) {
            Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
            i.putExtra("pageName", "Return and Replace Policy");
            i.putExtra("fragmentName", "return");
            startActivity(i);
        }
    }

    private void setPrivacyPolicy() {
        if(isAdded()) {
            Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
            i.putExtra("pageName", "Privacy Policy");
            i.putExtra("fragmentName", "privacy");
            startActivity(i);
        }
    }

    private void setProfile() {
        if(isAdded()) {
            Intent i = new Intent(requireActivity(), AccountOptionsActivity.class);
            i.putExtra("pageName", "My Profile");
            i.putExtra("fragmentName", "profile");
            startActivity(i);
        }
    }

    private void logout() {
        if(isAdded()) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(requireActivity());
                     //                             🧐
                    //                               |
            //                                       v
            builder1.setMessage("Are you sure? \uD83E\uDDD0");
            builder1.setCancelable(true);

            builder1.setPositiveButton("No",
                    (dialog, id) -> dialog.cancel());

            builder1.setNegativeButton(
                    "Yes",
                    (dialog, id) -> {
                        String token = sharedPreferenceManager.getString("token");
                        if (sharedPreferenceManager.getString("token") != null) {
                            Call<Logout> call = RetrofitClient.getClient().getApi().logout(token);
                            if(isAdded()) {
                                loadingDialog = new LoadingDialog(requireActivity());
                                loadingDialog.startLoadingDialog();
                                call.enqueue(new Callback<Logout>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Logout> call, @NonNull Response<Logout> response) {
                                        loadingDialog.dismissLoadingDialog();
                                        if (!response.isSuccessful()) {
                                            if (isAdded()) {
                                                Toast.makeText(requireActivity(), "" + response.code(), Toast.LENGTH_SHORT).show();
                                            }
                                            return;
                                        }
                                        assert response.body() != null;
                                        if (!response.body().getSuccess()) {
                                            if (isAdded()) {
                                                Toast.makeText(requireActivity(), "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                                            }
                                            return;
                                        }
                                        requireActivity().finish();
                                        sharedPreferenceManager.setString("token", null);
                                        if (isAdded()) {
                                            Intent i = new Intent(requireActivity(), SignUpActivity.class);
                                            startActivity(i);
                                            Toast.makeText(requireActivity(), "Logged out!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<Logout> call, @NonNull Throwable t) {
                                        if (isAdded()) {
                                            loadingDialog.dismissLoadingDialog();
                                            Toast.makeText(requireActivity(), "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserGreeting();
    }
}