package com.arraykart.b2b.Order;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.Cart.CartAdapter;
import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.Address;
import com.arraykart.b2b.Retrofit.ModelClass.CartProducts;
import com.arraykart.b2b.Retrofit.ModelClass.ProductsWithQuantity;
import com.arraykart.b2b.Retrofit.ModelClass.UserAddress;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.bumptech.glide.Glide;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSummaryFragment extends Fragment {
    private RecyclerView cartProductsRV;
    private SharedPreferenceManager sharedPreferenceManager;
    private List<CartProducts> cartProducts;
    private TextView subTotal;
    private AppCompatButton proceedToBuy;
    private CartAdapter cartAdapter;
    private TextView shopNameOS;
    private TextView shopAddress;
    private List<Address> addressList;

    //steps
    private TextView step1;
    private ImageView step1Img;
    private TextView step2;
    private ImageView step2Img;
    private View step2View;
    private TextView step3;
    private ImageView step3Img;
    private View step3View;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_summary, container, false);
        changeSteps();

        cartProductsRV = view.findViewById(R.id.cartProductsRV);
        subTotal = view.findViewById(R.id.subTotal);
        proceedToBuy = view.findViewById(R.id.proceedToBuy);
        shopNameOS = view.findViewById(R.id.shopNameOS);
        shopAddress = view.findViewById(R.id.shopAddress);
        checkToken();
        if(isAdded()) {
            sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
            Call<ProductsWithQuantity> call = RetrofitClient.getClient().getApi()
                    .getCart(sharedPreferenceManager.getString("token"));
            call.enqueue(new Callback<ProductsWithQuantity>() {
                @Override
                public void onResponse(Call<ProductsWithQuantity> call, Response<ProductsWithQuantity> response) {
                    if(!response.isSuccessful()){
                        if(isAdded()) {
                            Toast.makeText(
                                    requireActivity(),
                                    "" + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    cartProducts = response.body().getProducts();
//                    subTotal = view.findViewById(R.id.subTotal);
//                    int total=0;
//                    Iterator<CartProducts> i1=cartProducts.iterator();
//                    while (i1.hasNext()){
//                        total+=(Integer.parseInt(i1.next().getPrice().toString()));
////                    *(Integer.parseInt(i1.next().getQuantity().toString()));
//                    }
                    subTotal.setText("Subtotal : ₹" + " ");
                    proceedToBuy.setText("Proceed to buy("+cartProducts.size()+" items)");
                    proceedToBuy.setOnClickListener(v -> {
                        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager()
                                .beginTransaction();
                        fragmentTransaction.addToBackStack("orderSummary");
                        Fragment paymentOptionsFragment = new PaymentOptionsFragment();
                        fragmentTransaction.replace(R.id.orderContainer, paymentOptionsFragment).commit();
                    });
                    if(isAdded()) {
                        cartProductsRV.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
                    }
                    cartAdapter = new CartAdapter(getContext(), cartProducts);
                    cartProductsRV.setAdapter(cartAdapter);
                }

                @Override
                public void onFailure(Call<ProductsWithQuantity> call, Throwable t) {
                    if(isAdded()) {
                        Toast.makeText(
                                requireActivity(),
                                "Please check your internet connection or try again after sometime",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(isAdded()){
            String token = sharedPreferenceManager.getString("token");
            Call<UserAddress> call = RetrofitClient.getClient().getApi()
                    .getUserAddress(token);
            call.enqueue(new Callback<UserAddress>() {
                @Override
                public void onResponse(Call<UserAddress> call, Response<UserAddress> response) {
                    if(!response.isSuccessful()){
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "" + response.code(), Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    addressList = response.body().getAddress();
                    shopNameOS.setText(addressList.get(0).getAddressName());
                    shopAddress.setText(
                            addressList.get(0).getAddressLine1()+","+"\n"
                            + addressList.get(0).getCity()+","
                            + addressList.get(0).getState()+","
                            + addressList.get(0).getPostalCode()+","+"\n"
                            + addressList.get(0).getPhoneNumber()

                    );
                }

                @Override
                public void onFailure(Call<UserAddress> call, Throwable t) {
                    if(isAdded()) {
                        Toast.makeText(requireActivity(), "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return view;
    }

    private void changeSteps() {
        if(isAdded()){
            //making first step unchecked i.e order summary
            step1Img = requireActivity().findViewById(R.id.step1img);
            Glide.with(requireActivity())
                    .load(R.drawable.ic_baseline_radio_button_unchecked_24_green)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.imgnotfound)
                    .into(step1Img);
            step1 = requireActivity().findViewById(R.id.step1);
            step1.setTextColor(getResources().getColor(R.color.heading));

            //to handle on back pressed
            step2 = requireActivity().findViewById(R.id.step2);
            step2.setTextColor(getResources().getColor(R.color.text));
            step2Img = requireActivity().findViewById(R.id.step2img);
            Glide.with(requireActivity())
                    .load(R.drawable.ic_baseline_radio_button_unchecked_24_gray)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.imgnotfound)
                    .into(step2Img);
            step2View = requireActivity().findViewById(R.id.step2view);
            step2View.setBackgroundColor(getResources().getColor(R.color.gray_A));
        }
    }

    private void checkToken() {
        if(isAdded()) {
            AuthorizeUser authorizeUser = new AuthorizeUser(requireActivity());
            authorizeUser.isLoggedIn();
        }
    }
}