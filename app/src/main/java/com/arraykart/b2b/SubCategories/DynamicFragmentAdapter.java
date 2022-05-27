package com.arraykart.b2b.SubCategories;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.arraykart.b2b.Retrofit.ModelClass.CategoryWise;
import com.arraykart.b2b.Retrofit.ModelClass.Cwcategory;
import com.arraykart.b2b.Retrofit.ModelClass.Product;
import com.arraykart.b2b.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DynamicFragmentAdapter extends FragmentStateAdapter {
    private int mNumOfTabs;
    private String crop;
    private ArrayList<String> category;
    private List<Product> products;
    private Activity activity;
    private static ArrayList<Product> productAL = new ArrayList<>();

    public DynamicFragmentAdapter(
            @NonNull FragmentManager fragmentManager,
            @NonNull Lifecycle lifecycle,
            int NumOfTabs,
            String crop,
            Set category,
            Activity activity) {
        super(fragmentManager, lifecycle);
        this.mNumOfTabs = NumOfTabs;
        this.crop = crop;
        //to pass set through intent, it must be casted to list
        String[] objects = new String[category.size()];
        category.toArray(objects);
        this.category = new ArrayList<>(Arrays.asList(objects));
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
//        Call<CategoryWise> call = RetrofitClient.getClient().getApi()
//                .getCropWiseCategoryWiseProduct(crop, category.get(position));
//        call.enqueue(new Callback<CategoryWise>() {
//            @Override
//            public void onResponse(Call<CategoryWise> call, Response<CategoryWise> response) {
//                if(!response.isSuccessful()){
//                    Toast.makeText(activity, response.code(), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(!response.body().getSuccess()){
//                    Toast.makeText(activity, "500"+"Internal Server Error", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                productAL.clear();
//                products = response.body().getProducts();
//                productAL.addAll(products);
//            }
//
//            @Override
//            public void onFailure(Call<CategoryWise> call, Throwable t) {
//                Toast.makeText(activity, "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//        });
        Bundle b = new Bundle();
//        b.putSerializable("product",productAL);
        b.putString("category", category.get(position));
        b.putInt("position", position);
        b.putString("crop", crop);
        Fragment fragment = DynamicFragment.newInstance();
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public int getItemCount() {
        return mNumOfTabs;
    }

}
