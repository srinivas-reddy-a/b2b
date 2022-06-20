package com.arraykart.b2b.SignUp.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.Home.HomeActivity;
import com.arraykart.b2b.Loading.LoadingDialog;
import com.arraykart.b2b.Retrofit.ModelClass.OTPSignUP;
import com.arraykart.b2b.Retrofit.ModelClass.PhoneNumberSignUP;
import com.arraykart.b2b.Retrofit.ModelClass.SignUp;
import com.arraykart.b2b.Retrofit.ModelClass.Token;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.R;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.arraykart.b2b.SignUp.SmsBroadcastReceiver;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //detect otp
    private  SmsBroadcastReceiver smsBroadcastReceiver;
    private static final int REQ_USER_CONSENT = 200;

    private LoadingDialog loadingDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_otp, container, false);
        if(isAdded()) {
            sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
        }
        //to get data from parent Fragment
        Bundle bundle = this.getArguments();
        assert getArguments() != null;
        number = getArguments().getString("phoneNumber");
        //setphoneNumber
        phoneNumber = view.findViewById(R.id.phoneNumberOTP);
        phoneNumber.setText(getArguments().getString("phoneNumber"));

        otp1 = view.findViewById(R.id.otp1ET);
        otp2 = view.findViewById(R.id.otp2ET);
        otp3 = view.findViewById(R.id.otp3ET);
        otp4 = view.findViewById(R.id.otp4ET);
        otp5 = view.findViewById(R.id.otp5ET);
        otp6 = view.findViewById(R.id.otp6ET);
        submitOTP = view.findViewById(R.id.submitOTP);

        //detect otp
        startSmartUserConsent();

        //send otp
        sendOTP(view);


        resendOTP(view);
        return view;
    }

    private void startSmartUserConsent() {
        if(isAdded()) {
            SmsRetrieverClient client = SmsRetriever.getClient(requireActivity());
            client.startSmsUserConsent(null);
        }

    }

    private void registerBroadcastReceiver(){
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener = new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {
                startActivityForResult(intent , REQ_USER_CONSENT);
            }

            @Override
            public void onFailure() {

            }
        };

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        if(isAdded()) {
            requireActivity().registerReceiver(smsBroadcastReceiver, intentFilter);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_USER_CONSENT){
            if((resultCode == RESULT_OK) && (data!=null)){
                String msg = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(msg);
            }
            else {
                if (isAdded()) {
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(otp1, 0);
                    //setOTP
                    setOTP();
                }
            }
        }
    }

    private void getOtpFromMessage(String msg) {
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(msg);
        if(matcher.find()){
            if(isAdded()) {
                otp1.setText(Character.toString(Objects.requireNonNull(matcher.group(0)).charAt(0)));
                otp2.setText(Character.toString(Objects.requireNonNull(matcher.group(0)).charAt(1)));
                otp3.setText(Character.toString(Objects.requireNonNull(matcher.group(0)).charAt(2)));
                otp4.setText(Character.toString(Objects.requireNonNull(matcher.group(0)).charAt(3)));
                otp5.setText(Character.toString(Objects.requireNonNull(matcher.group(0)).charAt(4)));
                otp6.setText(Character.toString(Objects.requireNonNull(matcher.group(0)).charAt(5)));
                authenticateOTP(matcher.group(0));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isAdded()){
            requireActivity().unregisterReceiver(smsBroadcastReceiver);
        }
    }

    private void sendOTP(View view) {

        assert getArguments() != null;
        PhoneNumberSignUP phoneNumberSignUP = new PhoneNumberSignUP(getArguments().getString("phoneNumber"));
        Call<SignUp> call = RetrofitClient.getClient().getApi().getOTP(phoneNumberSignUP);
        if(isAdded()){
//            loadingDialog = new LoadingDialog(requireActivity());
//            loadingDialog.startLoadingDialog();
            call.enqueue(new Callback<SignUp>() {
                @Override
                public void onResponse(@NonNull Call<SignUp> call, @NonNull Response<SignUp> response) {
//                    loadingDialog.dismissLoadingDialog();
                    if(!response.isSuccessful()){
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "" + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    assert response.body() != null;
                    if(!response.body().getSuccess()){
                        if(isAdded()) {
                            //                    loadingDialog.dismissLoadingDialog();
                            Toast.makeText(requireActivity(), "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SignUp> call, @NonNull Throwable t) {
                    if(isAdded()) {
                        loadingDialog.dismissLoadingDialog();
                        Toast.makeText(requireActivity(), "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void resendOTP(View view) {
        resendOTPTV = view.findViewById(R.id.resendOTP);
        resendOTPTV.setOnClickListener(v -> {
            PhoneNumberSignUP p = new PhoneNumberSignUP(number);
            Call<SignUp> call = RetrofitClient.getClient().getApi().getOTP(p);
            if(isAdded()){
//                    loadingDialog = new LoadingDialog(requireActivity());
//                    loadingDialog.startLoadingDialog();
                call.enqueue(new Callback<SignUp>() {
                    @Override
                    public void onResponse(@NonNull Call<SignUp> call, @NonNull Response<SignUp> response) {
//                            loadingDialog.dismissLoadingDialog();
                        if(!response.isSuccessful()){
                            if(isAdded()) {
                                Toast.makeText(requireActivity(), "" + response.code(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        assert response.body() != null;
                        if(!response.body().getSuccess()){
                            if(isAdded()) {
                                Toast.makeText(requireActivity(), "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "OTP Resent", Toast.LENGTH_SHORT).show();
                        }
                        startSmartUserConsent();
                    }

                    @Override
                    public void onFailure(@NonNull Call<SignUp> call, @NonNull Throwable t) {
                        if(isAdded()) {
                            loadingDialog.dismissLoadingDialog();
                            Toast.makeText(requireActivity(), "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void setOTP() {
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
        otp1.post(() -> otp1.requestFocus());

        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    otp2.post(() -> otp2.requestFocus());
                    otp2.setEnabled(true);
                    o1=s.toString();
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(otp2, 0);
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
                    otp3.post(() -> otp3.requestFocus());
                    otp3.setEnabled(true);
                    o2=s.toString();
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(otp3, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp2.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL
            ){
                otp2.setEnabled(false);
                otp1.requestFocus();
                otp1.setText("");
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(otp1, 0);
            }
            return false;
        });


        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    otp4.post(() -> otp4.requestFocus());
                    otp4.setEnabled(true);
                    o3=s.toString();
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(otp4, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otp3.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL
            ){
                otp3.setEnabled(false);
                otp2.requestFocus();
                otp2.setText("");
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(otp2, 0);
            }
            return false;
        });

        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    otp5.post(() -> otp5.requestFocus());
                    otp5.setEnabled(true);
                    o4=s.toString();
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(otp5, 0);
                    
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otp4.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL
            ){
                otp4.setEnabled(false);
                otp3.requestFocus();
                otp3.setText("");
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(otp3, 0);
            }
            return false;
        });

        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    otp6.post(() -> otp6.requestFocus());
                    otp6.setEnabled(true);
                    o5=s.toString();
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(otp6, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otp5.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL
            ){
                otp5.setEnabled(false);
                otp4.requestFocus();
                otp4.setText("");
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(otp4, 0);
            }
            return false;
        });

        otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    submitOTP.post(() -> submitOTP.requestFocus());
                    submitOTP.setBackgroundResource(R.drawable.round_edges_bg_green);
                    o6=s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otp6.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL
            ){
                otp6.setEnabled(false);
                otp5.requestFocus();
                otp5.setText("");
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(otp5, 0);
            }
            return false;
        });

        submitOTP.setOnClickListener(v -> {
            otp+=o1+o2+o3+o4+o5+o6;
            authenticateOTP(otp);
        });

    }

    private void authenticateOTP(String otp) {
        OTPSignUP otpSignUP = new OTPSignUP(otp, number, sharedPreferenceManager.getBoolean("existingUser"));
        Call<Token> call = RetrofitClient.getClient().getApi()
                .verifyOTP(otpSignUP);
        if(isAdded()){
//            loadingDialog = new LoadingDialog((requireActivity()));
//            loadingDialog.startLoadingDialog();
            call.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
//                    loadingDialog.dismissLoadingDialog();
                    if(!response.isSuccessful()) {
                        if (response.code() == 401) {
                            if(isAdded()) {
                                Toast.makeText(requireActivity(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                                startSmartUserConsent();
                                return;
                            }
                        }
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "" + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if(response.code() == 500)
                        if (isAdded()) {
                            Toast.makeText(requireActivity(), "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    if(isAdded() && response.code() == 200) {
                        assert response.body() != null;
                        if (response.body().getToken() != null) {
                            sharedPreferenceManager.setString("token", response.body().getToken());
                            sharedPreferenceManager.setBoolean("existingUser", true);
                            if (!sharedPreferenceManager.checkKey("firstInstall")) {
                                sharedPreferenceManager.setBoolean("firstInstall", false);
                            }
                            requireActivity().finish();
                            Intent i = new Intent(requireActivity(), HomeActivity.class);
                            startActivity(i);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                    if(isAdded()) {
//                        loadingDialog.dismissLoadingDialog();
                        Toast.makeText(requireActivity(), "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}