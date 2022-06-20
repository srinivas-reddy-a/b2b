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

import com.arraykart.b2b.Loading.LoadingDialog;
import com.arraykart.b2b.Retrofit.ModelClass.CategoryWise;
import com.arraykart.b2b.Retrofit.ModelClass.Cwcategory;
import com.arraykart.b2b.Retrofit.ModelClass.Product;
import com.arraykart.b2b.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


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
