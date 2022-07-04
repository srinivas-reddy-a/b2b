package com.arraykart.b2b.SignUp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.arraykart.b2b.Home.HomeActivity;
import com.arraykart.b2b.R;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.arraykart.b2b.SignUp.SignUpActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class LanguageFragment extends Fragment {
    private LinearLayout englishLL;
    private LinearLayout hindiLL;
    private static final String LANGUAGE = "language";
    private Fragment fragment;
    private SharedPreferenceManager sharedPreferenceManager;
    private int i=0;
    private int j=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_language, container, false);
        if(isAdded()){
            sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
        }


//        set language
        englishLL = view.findViewById(R.id.englishLL);
        hindiLL = view.findViewById(R.id.hindiLL);
        setLang();

        return view;
    }

    private void setLang() {
        if(isAdded()){
            englishLL.setOnClickListener(v -> {
                sharedPreferenceManager.setString(LANGUAGE, "en");
//                sharedPreferenceManager.setBoolean("firstinstall", false);
                if(sharedPreferenceManager.checkKey("token")
                        && sharedPreferenceManager.getString("token") != null
                        && !sharedPreferenceManager.getString("token").isEmpty()
                ){
                    requireActivity().finish();
                    Intent i = new Intent(requireActivity(), HomeActivity.class);
                    startActivity(i);
                }else {
                    fragment = new SignUpFragment();
                    FragmentTransaction fragmentTransactionSignUp = requireActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransactionSignUp.replace(R.id.signUPFragContainer, fragment).commit();
                }
            });
            hindiLL.setOnClickListener(v -> {
                sharedPreferenceManager.setString(LANGUAGE, "hi");
//                sharedPreferenceManager.setBoolean("firstinstall", false);
                if(sharedPreferenceManager.checkKey("token")
                        && sharedPreferenceManager.getString("token") != null
                        && !sharedPreferenceManager.getString("token").isEmpty()
                ){
                    requireActivity().finish();
                    Intent i = new Intent(requireActivity(), HomeActivity.class);
                    startActivity(i);
                }else {
                    fragment = new SignUpFragment();
                    FragmentTransaction fragmentTransactionSignUp = requireActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransactionSignUp.replace(R.id.signUPFragContainer, fragment).commit();
                }
            });
        }
    }
}