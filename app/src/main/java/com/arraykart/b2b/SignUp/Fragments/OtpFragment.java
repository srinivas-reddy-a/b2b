package com.arraykart.b2b.SignUp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.Home.HomeActivity;
import com.arraykart.b2b.Retrofit.ModelClass.OTPSignUP;
import com.arraykart.b2b.Retrofit.ModelClass.PhoneNumberSignUP;
import com.arraykart.b2b.Retrofit.ModelClass.SignUp;
import com.arraykart.b2b.Retrofit.ModelClass.Token;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.R;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpFragment extends Fragment {
    private EditText otp1;
    private EditText otp2;
    private EditText otp3;
    private EditText otp4;
    private EditText otp5;
    private EditText otp6;
    private TextView submitOTP;
    private String otp = "";
    private TextView phoneNumber;
    private String number="";
    private TextView resendOTPTV;

    //to sumup otp strings
    private String o1;
    private String o2;
    private String o3;
    private String o4;
    private String o5;
    private String o6;

    //shared preferences
    private SharedPreferenceManager sharedPreferenceManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_otp, container, false);
        //to get data from parent Fragment
        Bundle bundle = this.getArguments();
        number = getArguments().getString("phoneNumber");
        //setphoneNumber
        phoneNumber = view.findViewById(R.id.phoneNumberOTP);
        phoneNumber.setText(getArguments().getString("phoneNumber"));
        //setOTP
        setOTP(view);
        resendOTP(view);
        return view;
    }

    private void resendOTP(View view) {
        resendOTPTV = view.findViewById(R.id.resendOTP);
        resendOTPTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNumberSignUP p = new PhoneNumberSignUP(number);
                Call<SignUp> call = RetrofitClient.getClient().getApi().getOTP(p);
                call.enqueue(new Callback<SignUp>() {
                    @Override
                    public void onResponse(Call<SignUp> call, Response<SignUp> response) {
                        if(!response.isSuccessful()){
                            if(isAdded()) {
                                Toast.makeText(requireActivity(), "" + response.code(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        if(!response.body().getSuccess()){
                            if(isAdded()) {
                                Toast.makeText(requireActivity(), "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "OTP Resent", Toast.LENGTH_SHORT).show();
                        }
                        setOTP(view);
                    }

                    @Override
                    public void onFailure(Call<SignUp> call, Throwable t) {
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void setOTP(View view) {
        otp1 = view.findViewById(R.id.otp1ET);
        otp2 = view.findViewById(R.id.otp2ET);
        otp3 = view.findViewById(R.id.otp3ET);
        otp4 = view.findViewById(R.id.otp4ET);
        otp5 = view.findViewById(R.id.otp5ET);
        otp6 = view.findViewById(R.id.otp6ET);
        submitOTP = view.findViewById(R.id.submitOTP);
        submitOTP.setBackgroundResource(R.drawable.round_edges_gray_bg);
        otp1.requestFocus();
        otp1.setEnabled(true);
        otp1.setText("");
        otp2.setText("");
        otp3.setText("");
        otp4.setText("");
        otp5.setText("");
        otp6.setText("");
        otp="";
        otp1.post(new Runnable() {
            @Override
            public void run() {
                otp1.requestFocus();
            }
        });

        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    otp2.post(new Runnable() {
                        @Override
                        public void run() {
                            otp2.requestFocus();
                        }
                    });
                    otp2.setEnabled(true);
                    o1=s.toString();
                    
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    otp3.post(new Runnable() {
                        @Override
                        public void run() {
                            otp3.requestFocus();

                        }
                    });
                    otp3.setEnabled(true);
                    o2=s.toString();
                    
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    otp4.post(new Runnable() {
                        @Override
                        public void run() {
                            otp4.requestFocus();
                        }
                    });

                    otp4.setEnabled(true);
                    o3=s.toString();
                    
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    otp5.post(new Runnable() {
                        @Override
                        public void run() {
                            otp5.requestFocus();
                        }
                    });

                    otp5.setEnabled(true);
                    o4=s.toString();
                    
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    otp6.post(new Runnable() {
                        @Override
                        public void run() {
                            otp6.requestFocus();
                        }
                    });
                    otp6.setEnabled(true);
                    o5=s.toString();
                    
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    submitOTP.post(new Runnable() {
                        @Override
                        public void run() {
                            submitOTP.requestFocus();
                        }
                    });
                    submitOTP.setBackgroundResource(R.drawable.round_edges_bg_green);
                    o6=s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        submitOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp+=o1+o2+o3+o4+o5+o6;
                otp6.setEnabled(false);
                authenticateOTP(otp, view);
            }
        });

    }

    private void authenticateOTP(String otp, View view) {
        sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
        OTPSignUP otpSignUP = new OTPSignUP(otp, number, sharedPreferenceManager.getBoolean("existingUser"));
        Call<Token> call = RetrofitClient.getClient().getApi()
                .verifyOTP(otpSignUP);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(!response.isSuccessful()) {
                    if (response.code() == 401) {
                        Toast.makeText(requireActivity(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                        setOTP(view);
                        return;
                    }
                    Toast.makeText(requireActivity(), ""+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!response.body().getSuccess() && response.code() == 500){
                    Toast.makeText(requireActivity(), "500"+"Internal Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(response.isSuccessful() && response.code() == 200 && response.body().getToken()!=null){
                    sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
                    sharedPreferenceManager.setString("token", response.body().getToken());
                    sharedPreferenceManager.setBoolean("existingUser", true);
                    if(!sharedPreferenceManager.checkKey("firstInstall")){
                        sharedPreferenceManager.setBoolean("firstInstall", false);
                    }
                    requireActivity().finish();
                    Intent i = new Intent(requireActivity(), HomeActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(requireActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}