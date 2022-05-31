package com.arraykart.b2b.SubCategories;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.CategoryWise;
import com.arraykart.b2b.Retrofit.ModelClass.Product;
import com.arraykart.b2b.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DynamicFragment extends Fragment {
    private RecyclerView dynamicRV;
    private ProductAdapter productAdapter;
    private List<Product> products;

    public static Fragment newInstance() {
        return new DynamicFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dynamic, container, false);

        //set productsrv
        setProductRV(view);
        return view;
    }

    private void setProductRV(View view) {
        dynamicRV = view.findViewById(R.id.dynamicRV);
        dynamicRV.setHasFixedSize(true);
        if(isAdded()) {
            dynamicRV.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        }
        Bundle bundle = this.getArguments();
        String crop = bundle.getString("crop");
        String category = bundle.getString("category");
        Call<CategoryWise> call = RetrofitClient.getClient().getApi()
                .getCropWiseCategoryWiseProduct(crop, category);
        call.enqueue(new Callback<CategoryWise>() {
            @Override
            public void onResponse(Call<CategoryWise> call, Response<CategoryWise> response) {
                if(!response.isSuccessful()){
                    if(isAdded()) {
                        Toast.makeText(requireActivity(), response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(!response.body().getSuccess()){
                    if(isAdded()) {
                        Toast.makeText(requireActivity(), "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                products = response.body().getProducts();
                if(isAdded()) {
                    productAdapter = new ProductAdapter(requireActivity(), products);
                    //setting this true is generating duplicate products
//              productAdapter.setHasStableIds(true);
                    dynamicRV.setAdapter(productAdapter);
                }
            }

            @Override
            public void onFailure(Call<CategoryWise> call, Throwable t) {
                if(isAdded()) {
                    Toast.makeText(requireActivity(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

}