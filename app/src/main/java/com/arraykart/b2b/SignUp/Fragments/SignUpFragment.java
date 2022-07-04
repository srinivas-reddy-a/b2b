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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.Home.Fragments.Account.AccountOptionsActivity;
import com.arraykart.b2b.Loading.LoadingDialog;
import com.arraykart.b2b.RecyclerViewDecoration.LinePagerIndicatorDecoration;
import com.arraykart.b2b.Retrofit.ModelClass.AllReviews;
import com.arraykart.b2b.Retrofit.ModelClass.Review;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.R;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.arraykart.b2b.SignUp.SignUpReviewRecyclerAdapter;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpFragment extends Fragment {

    private RecyclerView suReviewRV;
    private List<Review> reviews;
    private ArrayList<String> suReviewImg;
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

    private LoadingDialog loadingDialog;

    //terms and conditions
    private TextView tandc;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //        review
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        otpFragment = new OtpFragment();
        if(isAdded()){
            loadingDialog = new LoadingDialog(requireActivity());
            loadingDialog.startLoadingDialog();
        }

        setReview(view);

        //terms and conditions
        tandc = view.findViewById(R.id.tandc);
        setTandC();

        //signup
        handleSignUp(view);
        //showPhoneNumberHint
        getPhoneNumberHint();

        return view;
    }

    private void setTandC() {
        tandc.setOnClickListener(v -> {
            if(isAdded()) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
                bottomSheetDialog.setContentView(R.layout.fragment_terms_conditions);
                bottomSheetDialog.show();
            }
        });
    }

    private void getPhoneNumberHint() {
        HintRequest hintRequest=new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Credentials.getClient(requireActivity()).getHintPickerIntent(hintRequest);
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
        if(isAdded()) {
            if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == requireActivity().RESULT_OK) {
                Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);
                setPhoneNumber(credentials);
            } else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
                // *** No phone numbers available ***
                Toast.makeText(requireActivity(), "No phone numbers found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setPhoneNumber(Credential credentials) {
        phoneNumber = credentials.getId().substring(3);
        //to send data to child fragment
        Bundle bundle = new Bundle();
        bundle.putString("phoneNumber", phoneNumber);
        otpFragment.setArguments(bundle);
        suPhoneNumber.setText(phoneNumber);
        fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.signUPFragContainer, otpFragment).commit();
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
                            Bundle bundle = new Bundle();
                            bundle.putString("phoneNumber", phoneNumber);
                            otpFragment.setArguments(bundle);
                            suPhoneNumber.setText(phoneNumber);
                            fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.signUPFragContainer, otpFragment).commit();
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
        try {
            Call<AllReviews> call = RetrofitClient.getClient()
                    .getApi().getReviews();
            if(isAdded()){

                call.enqueue(new Callback<AllReviews>() {
                    @Override
                    public void onResponse(Call<AllReviews> call, Response<AllReviews> response) {
                        loadingDialog.dismissLoadingDialog();
                        if (!response.isSuccessful()) {
                            if(isAdded()) {
                                Toast.makeText(requireActivity(), "" + response.code(), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        assert response.body() != null;
                        if (!response.body().getSuccess()) {
                            if(isAdded()) {
                                Toast.makeText(requireActivity(), "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        reviews = response.body().getReviews();
                        suReviewImg = new ArrayList<>(Arrays.asList(response.body().getReviews().get(0).getReviewimg().split(",")));
                        suReview = new ArrayList<>(Arrays.asList(response.body().getReviews().get(0).getReview().split(",")));
                        suReviewCustomer = new ArrayList<>(Arrays.asList(response.body().getReviews().get(0).getReviewCusName().split(",")));

                        if(isAdded()) {
                            linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
                            suReviewRV.setLayoutManager(linearLayoutManager);
                            suReviewRV.setHasFixedSize(true);
                            signUpReviewRecyclerAdapter = new SignUpReviewRecyclerAdapter(
                                    suReviewImg, suReview, suReviewCustomer, requireActivity());
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

                    @Override
                    public void onFailure(Call<AllReviews> call, Throwable t) {
                        if(isAdded()) {
                            loadingDialog.dismissLoadingDialog();
                            Toast.makeText(requireActivity(), "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}