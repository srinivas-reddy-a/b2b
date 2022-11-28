package com.arraykart.b2b.Order;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.RazorPayOrder;
import com.arraykart.b2b.Retrofit.ModelClass.RazorPayOrderDetail;
import com.arraykart.b2b.Retrofit.ModelClass.RpOrder;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.bumptech.glide.Glide;
import com.razorpay.Checkout;
import com.razorpay.PayloadHelper;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RazorPayPaymentFragment extends Fragment  {

    private TextView step1;
    private ImageView step1Img;
    private TextView step2;
    private ImageView step2Img;
    private View step2View;
    private TextView step3;
    private ImageView step3Img;
    private View step3View;
    private SharedPreferenceManager sharedPreferenceManager;
    private RpOrder rpOrder;
//    razorpay
//    private final String TAG = requireActivity().class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        //change steps color and imgs in mainactivity
        changeSteps();

        //get user token from shared preferences
        sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
        String token = sharedPreferenceManager.getString("token");
        String amount = "10000";
        String receiptId = "akrcpt001";
        String note = "Customer placing order";
        String phoneNumber = "9346293027";
        String email = "test@arraykart.com";

//        to know if razorpay sdk version is outdated
//        only works in debug mode and not in the release
        if(isAdded()) {
            Checkout.sdkCheckIntegration(requireActivity());
        }

        if(isAdded()) {
            createOrderOnRazorPay(token, amount, receiptId, note, phoneNumber, email);
        }
        return view;
    }

    private void createOrderOnRazorPay(String token, String amount, String receiptId, String note,
                                       String phoneNumber, String email) {
        RazorPayOrder razorPayOrder = new RazorPayOrder(amount, receiptId, note);
        Call<RazorPayOrderDetail> call = RetrofitClient.getClient().getApi()
                .setRpOrder(token, razorPayOrder);
        call.enqueue(new Callback<RazorPayOrderDetail>() {
            @Override
            public void onResponse(Call<RazorPayOrderDetail> call, Response<RazorPayOrderDetail> response) {
                if(!response.isSuccessful()){
                    if(isAdded()) {
                        Toast.makeText(
                                requireActivity(),
                                "" + response.code(),
                                Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                rpOrder = response.body().getRpOrder();
                startPayment(rpOrder.getId(), amount, phoneNumber, email);
            }

            @Override
            public void onFailure(Call<RazorPayOrderDetail> call, Throwable t) {
                if(isAdded()) {
                    Toast.makeText(
                            requireActivity(),
                            "Please check your internet connection or try again after sometime",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void startPayment(String razorPayOrderId, String amount, String phoneNumber, String email) {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
//        6qr6MvEW90nZKmFgKl1kgUP2 - live key
//        rzp_test_Xi9Zwp0uAJKix4 - test key

        checkout.setKeyID("rzp_test_Xi9Zwp0uAJKix4");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.company_icon_green);

        /**
         * Reference to current activity
         */

        final Activity activity = requireActivity();

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Arraykart");
//            options.put("description", "Reference No. #123456");
            options.put("image", "https://arraykartandroid.s3.ap-south-1.amazonaws.com/assets/logos/company_icon_green.png");
            options.put("order_id", razorPayOrderId);
            options.put("theme.color", "#00D66C");
            options.put("currency", "INR");
            options.put("amount", amount); //pass amount in currency subunits(paise for rupees)
            options.put("prefill.email", email);
            options.put("prefill.contact",phoneNumber);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
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