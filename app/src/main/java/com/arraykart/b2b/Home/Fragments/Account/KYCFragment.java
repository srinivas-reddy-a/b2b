package com.arraykart.b2b.Home.Fragments.Account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.R;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;


public class KYCFragment extends Fragment {

    private TextView gsttv;
    private TextView pantv;
    private Fragment fragment;
    private LinearLayout kycBody;
    private TextView kycStatus;
    private SharedPreferenceManager sharedPreferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_k_y_c, container, false);
        if(isAdded()){
            sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
        }
        checkToken();

        kycBody = view.findViewById(R.id.kycBody);
        kycStatus = view.findViewById(R.id.kycStatus);
        gsttv = view.findViewById(R.id.gsttv);
        pantv = view.findViewById(R.id.pantv);
        checkKycStatus();
        return view;
    }

    private void checkKycStatus() {
        if(isAdded()) {
            if(sharedPreferenceManager.checkKey("kycstatus")){
                if(sharedPreferenceManager.getString("kycstatus").trim().toUpperCase().contains("NV")){
                    kycBody.setVisibility(View.VISIBLE);
                    kycStatus.setVisibility(View.GONE);
                    setGst();
                    setPan();
                }else {
                    kycBody.setVisibility(View.GONE);
                    kycStatus.setVisibility(View.VISIBLE);
                    if(sharedPreferenceManager.getString("kycstatus").trim().toUpperCase().contains("IR")){
                        kycStatus.setText("Your Kyc application is under Review. We will get back to you soon!");
                    }
                    if(sharedPreferenceManager.getString("kycstatus").trim().toUpperCase().contains("VF")){
                        kycStatus.setText("Your Kyc application is Verified. Happy Shopping!");
                    }
                    if(sharedPreferenceManager.getString("kycstatus").trim().toUpperCase().contains("FL")){
                        kycStatus.setText("Your Kyc application is Failed!");
                    }
                }
            }
        }
    }

    private void setPan() {
        pantv.setOnClickListener(v -> {
            if(isAdded()) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                        .beginTransaction();
                fragment = new KycUploadFragment();
                Bundle bundle = new Bundle();
                bundle.putString("proof", "pan");
                fragment.setArguments(bundle);
                transaction.addToBackStack("kyc");
                transaction.replace(R.id.accountContainer, fragment).commit();
            }
        });
    }

    private void setGst() {
        gsttv.setOnClickListener(v -> {
            if(isAdded()) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                        .beginTransaction();
                fragment = new KycUploadFragment();
                Bundle bundle = new Bundle();
                bundle.putString("proof", "gst");
                fragment.setArguments(bundle);
                transaction.addToBackStack("kyc");
                transaction.replace(R.id.accountContainer, fragment).commit();
            }
        });
    }

    private void checkToken() {
        if(isAdded()) {
            AuthorizeUser authorizeUser = new AuthorizeUser(requireActivity());
            authorizeUser.isLoggedIn();
        }
    }
}