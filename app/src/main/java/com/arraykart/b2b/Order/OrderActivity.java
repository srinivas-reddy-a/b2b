package com.arraykart.b2b.Order;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.Home.Fragments.Account.AccountOptionsActivity;
import com.arraykart.b2b.Home.HomeActivity;
import com.arraykart.b2b.ProductDetail.ProductDetailActivity;
import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.Order;
import com.arraykart.b2b.Retrofit.ModelClass.SuccessMessage;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity implements PaymentResultWithDataListener {
    private TextView cancelOrderProcess;
    private SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment orderSummaryFragment = new OrderSummaryFragment();
        fragmentTransaction.replace(R.id.orderContainer, orderSummaryFragment).commit();

        // razorpay takes time to load components
        // hence preload before you checkout and pay
        /**
         * Preload payment resources
         */
        Checkout.preload(OrderActivity.this);



        //to cancel order process and return to cart
        cancelOrderProcess = findViewById(R.id.cancelOrderProcess);
        cancelOrderProcess.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Cancel Order");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.setNegativeButton("Yes", (dialog, which) -> {
                dialog.dismiss();
                finish();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        sharedPreferenceManager = new SharedPreferenceManager(this);
        Call<SuccessMessage> call = RetrofitClient.getClient().getApi()
                        .clearCart(sharedPreferenceManager.getString("token"));
        call.enqueue(new Callback<SuccessMessage>() {
            @Override
            public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(
                            OrderActivity.this,
                            "" + response.code(),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
//                Order order = new Order()
//                Call<SuccessMessage> call1 = RetrofitClient.getClient().getApi()
//                        .setOrder(sharedPreferenceManager.getString("token"));

                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                builder.setMessage("Your Order has been Placed!");
                builder.setCancelable(true);
                builder.setPositiveButton("Track", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finish();
                    Intent intent = new Intent(OrderActivity.this, AccountOptionsActivity.class);
                    intent.putExtra("pageName", "My Orders");
                    intent.putExtra("fragmentName", "myorders");
                    startActivity(intent);
                }));
                builder.setNegativeButton("Dismiss", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finish();
                }));
            }

            @Override
            public void onFailure(Call<SuccessMessage> call, Throwable t) {
                Toast.makeText(
                        OrderActivity.this,
                        "Please check your internet connection or try again after sometime",
                        Toast.LENGTH_SHORT).show();
            }
        });
//        Toast.makeText(OrderActivity.this, "success trx: "+s,
//                Toast.LENGTH_LONG).show();
//        Log.e("tag", "success trx: "+s + "\n"+paymentData.getPaymentId()+ "\n"
//        +paymentData.getOrderId()+ "\n"+paymentData.getSignature());
//        Checkout.clearUserData(OrderActivity.this);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(OrderActivity.this, "failed due to: "+s, Toast.LENGTH_SHORT).show();
        Checkout.clearUserData(OrderActivity.this);
    }

}