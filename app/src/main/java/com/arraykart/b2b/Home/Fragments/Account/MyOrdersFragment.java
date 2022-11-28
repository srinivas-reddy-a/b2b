package com.arraykart.b2b.Home.Fragments.Account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.AllUserOrders;
import com.arraykart.b2b.Retrofit.ModelClass.Allorder;
import com.arraykart.b2b.Retrofit.ModelClass.SuccessMessage;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersFragment extends Fragment {

    private RecyclerView myOrdersRV;
    private SharedPreferenceManager sharedPreferenceManager;
    private List<Allorder> allorders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        getMyOrders();


        return view;
    }

    private void getMyOrders() {
        if(isAdded()) {
            sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
            Call<AllUserOrders> call = RetrofitClient.getClient().getApi()
                    .getAllOrders(sharedPreferenceManager.getString("token"));
            call.enqueue(new Callback<AllUserOrders>() {
                @Override
                public void onResponse(Call<AllUserOrders> call, Response<AllUserOrders> response) {
                    if(!response.isSuccessful()){
                        if(isAdded()) {
                            Toast.makeText(
                                    requireActivity(),
                                    "" + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    allorders = response.body().getAllorders();

                }

                @Override
                public void onFailure(Call<AllUserOrders> call, Throwable t) {
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
}