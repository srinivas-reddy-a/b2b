package com.arraykart.b2b.SignUp.Fragments;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arraykart.b2b.RecyclerViewDecoration.LinePagerIndicatorDecoration;
import com.arraykart.b2b.Retrofit.ModelClass.PhoneNumberSignUP;
import com.arraykart.b2b.Retrofit.ModelClass.SignUp;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.R;
import com.arraykart.b2b.SignUp.SignUpReviewRecyclerAdapter;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpFragment extends Fragment {

    private RecyclerView suReviewRV;
    private ArrayList<Integer> suReviewImg;
    private ArrayList<String> suReview;
    private ArrayList<String> suReviewCustomer;
    private LinearLayoutManager linearLayoutManager;
    private SignUpReviewRecyclerAdapter signUpReviewRecyclerAdapter;

    //    signup
    private EditText suPhoneNumber;
    private ImageView suSignUpImg;
    private static final int CREDENTIAL_PICKER_REQUEST =120 ;
    private Fragment otpFragment;
    private String phoneNumber;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //        review
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        setReview(view);
        //signup
        handleSignUp(view);
        //showPhoneNumberHint
        getPhoneNumberHint();

        return view;
    }

    private void getPhoneNumberHint() {
        HintRequest hintRequest=new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Credentials.getClient(getActivity()).getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(
                    intent.getIntentSender(),
                    CREDENTIAL_PICKER_REQUEST,
                    null,
                    0,
                    0,
                    0,
                    new Bundle());
        }catch (IntentSender.SendIntentException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == getActivity().RESULT_OK){
            Credential credentials =data.getParcelableExtra(Credential.EXTRA_KEY);
            setPhoneNumber(credentials);
        } else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE)
        {
            // *** No phone numbers available ***
            Toast.makeText(getActivity(), "No phone numbers found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setPhoneNumber(Credential credentials) {
        phoneNumber = credentials.getId().substring(3);
        otpFragment = new OtpFragment();
        //to send data to child fragment
        Bundle bundle = new Bundle();
        bundle.putString("phoneNumber", phoneNumber);
        otpFragment.setArguments(bundle);
        suPhoneNumber.setText(phoneNumber);
        PhoneNumberSignUP phoneNumberSignUP = new PhoneNumberSignUP(phoneNumber);
        Call<SignUp> call = RetrofitClient.getClient().getApi().getOTP(phoneNumberSignUP);
        call.enqueue(new Callback<SignUp>() {
            @Override
            public void onResponse(Call<SignUp> call, Response<SignUp> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getActivity(), ""+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!response.body().getSuccess()){
                    Toast.makeText(getActivity(), "500"+"Internal Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.signUPFragContainer, otpFragment).commit();
            }

            @Override
            public void onFailure(Call<SignUp> call, Throwable t) {
                Toast.makeText(getActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSignUp(View view) {
        suPhoneNumber = view.findViewById(R.id.suPhoneNumber);
        suSignUpImg = view.findViewById(R.id.suSignUpImg);
        suPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=10){
                    if(s.length()>10){
                        phoneNumber = s.toString().substring(2);
                    }else {
                        phoneNumber = s.toString();
                    }
                    suSignUpImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    suSignUpImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PhoneNumberSignUP p = new PhoneNumberSignUP(phoneNumber);
                            Call<SignUp> call = RetrofitClient.getClient().getApi().getOTP(p);
                            call.enqueue(new Callback<SignUp>() {
                                @Override
                                public void onResponse(Call<SignUp> call, Response<SignUp> response) {
                                    if(!response.isSuccessful()){
                                        Toast.makeText(getActivity(), ""+response.code(), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if(!response.body().getSuccess()){
                                        Toast.makeText(getActivity(), "500"+"Internal Server Error", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    otpFragment = new OtpFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("phoneNumber", phoneNumber);
                                    otpFragment.setArguments(bundle);
                                    //as it is a nested fragment one should call getParentFragment()
                                    try {
                                        fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.signUPFragContainer, otpFragment).commit();
                                    }catch (NullPointerException e){
                                        e.printStackTrace();
                                    }

                                    Toast.makeText(getActivity(), phoneNumber, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<SignUp> call, Throwable t) {
                                    Toast.makeText(getActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }else{
                    suSignUpImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setReview(View view) {
        suReviewRV = view.findViewById(R.id.suReviewRV);
        suReviewImg = new ArrayList<>(Arrays.asList(R.drawable.banner0, R.drawable.banner1, R.drawable.banner2, R.drawable.banner3));
        suReview = new ArrayList<>(Arrays.asList(
                "Again, the best way to do this is to ask. Send an email, ask in person, or add links to your website that make it easy for customers to leave their opinion.",
                "Again, the best way to do this is to ask. Send an email, ask in person, or add links to your website that make it easy for customers to leave their opinion.",
                "Again, the best way to do this is to ask. Send an email, ask in person, or add links to your website that make it easy for customers to leave their opinion.",
                "Again, the best way to do this is to ask. Send an email, ask in person, or add links to your website that make it easy for customers to leave their opinion."));
        suReviewCustomer = new ArrayList<>(Arrays.asList(
                "Panda",
                "Panda",
                "Panda",
                "Panda"
        ));
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        suReviewRV.setLayoutManager(linearLayoutManager);
        suReviewRV.setHasFixedSize(true);
        signUpReviewRecyclerAdapter = new SignUpReviewRecyclerAdapter(
                suReviewImg, suReview, suReviewCustomer, getActivity());
        suReviewRV.setAdapter(signUpReviewRecyclerAdapter);
        suReviewRV.addItemDecoration(new LinePagerIndicatorDecoration());
        suReviewRV.smoothScrollToPosition(0);
        //to auto scroll
        final int interval_time = 2000;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                if(count<suReviewImg.size()){
                    suReviewRV.smoothScrollToPosition(count++);
                    handler.postDelayed(this, interval_time);
                    if(count==suReviewImg.size()){
                        count=0;
                    }
                }
            }
        };
        handler.postDelayed(runnable, interval_time);
    }
}