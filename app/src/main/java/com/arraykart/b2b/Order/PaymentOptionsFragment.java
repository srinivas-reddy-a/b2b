package com.arraykart.b2b.Order;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.Rupifi.Body;
import com.arraykart.b2b.Retrofit.ModelClass.Rupifi.CustomerGMV;
import com.arraykart.b2b.Retrofit.ModelClass.Rupifi.EligibilityApiBody;
import com.arraykart.b2b.Retrofit.ModelClass.Rupifi.GmvDatum;
import com.arraykart.b2b.Retrofit.ModelClass.Rupifi.RupifiEligibilityResponse;
import com.arraykart.b2b.Retrofit.ModelClass.Rupifi.RupifiGMVBody;
import com.arraykart.b2b.Retrofit.ModelClass.SuccessMessage;
import com.arraykart.b2b.Retrofit.ModelClass.Token;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentOptionsFragment extends Fragment {

    private TextView step1;
    private ImageView step1Img;
    private TextView step2;
    private ImageView step2Img;
    private View step2View;

    private TextView payByCredit;
    private TextView payByCash;
    private TextView whatCredServ;
    private TextView payCashOnDelivery;

    private SharedPreferenceManager sharedPreferenceManager;
    private String rupifi_access_token;

    //bsd apply
    private WebView rupifiKycWV;
    private TextView rupifiApplyTV;
    private AppCompatButton applyCredit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_options, container, false);

        //change steps color and imgs in mainactivity
        changeSteps();

        //open pay by cash or online options
        payByCash = view.findViewById(R.id.payByOnline);
        payByCash.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.addToBackStack("paymentOptions");
            Fragment paymentFragment = new RazorPayPaymentFragment();
            fragmentTransaction.replace(R.id.orderContainer, paymentFragment).commit();
        });

        //open credit options
        payByCredit = view.findViewById(R.id.payByCredit);
        payByCredit.setOnClickListener(v -> {
            if(isAdded()){
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
                bottomSheetDialog.setContentView(R.layout.bsd_pay_by_credit);
                //bsd apply
                rupifiKycWV = bottomSheetDialog.findViewById(R.id.rupifiKycWV);
                rupifiKycWV.loadUrl("https://stackoverflow.com/questions/71691618/retrofit-onfailure-keeps-triggering-on-connecting-to-localhost");
                rupifiApplyTV = bottomSheetDialog.findViewById(R.id.rupifiApplyTV);
                applyCredit = bottomSheetDialog .findViewById(R.id.applyCredit);
                applyCredit.setOnClickListener(v1 -> {
                    getRupifiActivationUrl();
//                    bottomSheetDialog.dismiss();
//                    Toast.makeText(requireActivity(), "We will notify you!", Toast.LENGTH_SHORT).show();
                });
                ImageView closeBsd = bottomSheetDialog.findViewById(R.id.closeBsd);
                assert closeBsd != null;
                closeBsd.setOnClickListener(v1 -> bottomSheetDialog.dismiss());
                bottomSheetDialog.show();

            }
        });

        //payCashOnDelivery options
        payCashOnDelivery = view.findViewById(R.id.payCashOnDelivery);
        payCashOnDelivery.setOnClickListener(v -> {
            if(isAdded()) {
                Toast.makeText(requireActivity(), "Cash on Delivery", Toast.LENGTH_SHORT).show();
            }
        });

        //questions
        whatCredServ = view.findViewById(R.id.whatCredServ);
        whatCredServ.setOnClickListener(v -> {
            if(isAdded()) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
                bottomSheetDialog.setContentView(R.layout.bsd_arraykart_credit_service);
                ImageView closeBsd = bottomSheetDialog.findViewById(R.id.closeBsd);
                assert closeBsd != null;
                closeBsd.setOnClickListener(v1 -> bottomSheetDialog.dismiss());
                bottomSheetDialog.show();
            }
        });



        return view;
    }

    private void getRupifiActivationUrl() {
        if(isAdded()){
            sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
            String token = sharedPreferenceManager.getString("token");
            Call<Token> call = RetrofitClient.getClient().getApi().getRupifiAccessToken(token);
            call.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    if(!response.isSuccessful()){
                        if(isAdded()) {
                            Toast.makeText(
                                    requireActivity(),
                                    "" + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    rupifi_access_token = response.body().getToken();
                    setRupifiGMVData(token, rupifi_access_token);
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    if(isAdded()) {
                        Toast.makeText(
                                requireActivity(),
                                "Please check your internet connection or try again after sometime",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setRupifiGMVData(String token, String rupifi_access_token) {
        if(isAdded()){
            GmvDatum gmvDatumSep = new GmvDatum("Sep", 10000, 2);
            GmvDatum gmvDatumOct = new GmvDatum("Oct", 25000, 4);
            List<GmvDatum> gmvData = Arrays.asList(gmvDatumSep, gmvDatumOct);
            CustomerGMV customerGMV = new CustomerGMV(
                    "001",
                    "2022-06-10",
                    "9346293027",
                    10,
                    gmvData);
            Body body = new Body(Arrays.asList(customerGMV));
            RupifiGMVBody rupifiGMVBody = new RupifiGMVBody(body, rupifi_access_token);
            Call<SuccessMessage> call = RetrofitClient.getClient().getApi().setRupifiGMVData(
                    token,
                    rupifiGMVBody);
            call.enqueue(new Callback<SuccessMessage>() {
                @Override
                public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                    if(!response.isSuccessful()){
                        if(isAdded()) {
                            Toast.makeText(
                                    requireActivity(),
                                    "" + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    getRupifiEligibilityStatus(token, rupifi_access_token);

                }

                @Override
                public void onFailure(Call<SuccessMessage> call, Throwable t) {
                    if(isAdded()) {
                        Toast.makeText(
                                requireActivity(),
                                "Please check your internet connection or try again after sometime",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void getRupifiEligibilityStatus(String token, String rupifi_access_token) {
        EligibilityApiBody eligibilityApiBody = new EligibilityApiBody(
                "001",
                "9346293027",
                "false",
                rupifi_access_token);
        Call<RupifiEligibilityResponse> call = RetrofitClient.getClient().getApi().getEligibilityData(
                token,
                eligibilityApiBody);
        call.enqueue(new Callback<RupifiEligibilityResponse>() {
            @Override
            public void onResponse(Call<RupifiEligibilityResponse> call, Response<RupifiEligibilityResponse> response) {
                if(!response.isSuccessful()){
                    if(isAdded()) {
                        Toast.makeText(
                                requireActivity(),
                                "" + response.code(),
                                Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                rupifiApplyTV.setVisibility(View.GONE);
                applyCredit.setVisibility(View.GONE);
                rupifiKycWV.loadUrl(response.body().getEligibilityData().getData().getActivationUrl());
            }

            @Override
            public void onFailure(Call<RupifiEligibilityResponse> call, Throwable t) {
                if(isAdded()) {
                    Toast.makeText(
                            requireActivity(),
                            "Please check your internet connection or try again after sometime",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changeSteps() {
        if(isAdded()){
            //making first step checked i.e order summary
            step1Img = requireActivity().findViewById(R.id.step1img);
            Glide.with(requireActivity())
                    .load(R.drawable.ic_baseline_check_circle_24_green)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.imgnotfound)
                    .into(step1Img);
            step1 = requireActivity().findViewById(R.id.step1);
            step1.setTextColor(getResources().getColor(R.color.text));

            step2 = requireActivity().findViewById(R.id.step2);
            step2.setTextColor(getResources().getColor(R.color.heading));
            step2Img = requireActivity().findViewById(R.id.step2img);
            Glide.with(requireActivity())
                    .load(R.drawable.ic_baseline_radio_button_unchecked_24_green)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.imgnotfound)
                    .into(step2Img);
            step2View = requireActivity().findViewById(R.id.step2view);
            step2View.setBackgroundColor(getResources().getColor(R.color.green));
        }
    }
}