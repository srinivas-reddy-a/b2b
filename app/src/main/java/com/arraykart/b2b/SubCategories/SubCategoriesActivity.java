package com.arraykart.b2b.SubCategories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.CropWiseCategory;
import com.arraykart.b2b.Retrofit.ModelClass.Cwcategory;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SubCategoriesActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ImageView back;
    private List<Cwcategory> cwcategories;
    private Set<String> uniqueCwCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categories);

        Call<CropWiseCategory> call = RetrofitClient.getClient()
                .getApi().getCropWiseCategory(
                        getIntent().getStringExtra("crop"));
        call.enqueue(new Callback<CropWiseCategory>() {
            @Override
            public void onResponse(Call<CropWiseCategory> call, Response<CropWiseCategory> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(SubCategoriesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!response.body().getSuccess()){
                    Toast.makeText(SubCategoriesActivity.this, "500"+"Internal Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                cwcategories = response.body().getCwcategories();
                uniqueCwCategories = new HashSet<>();
                for(int i=0;i<cwcategories.size();i++){
                    uniqueCwCategories.add(cwcategories.get(i).getCategory());
                }
                //settablayout
                tabLayout = findViewById(R.id.tabLayout);
                viewPager2 = findViewById(R.id.vp2);
                //to preload 3 next and 3 previous tabs
                viewPager2.setOffscreenPageLimit(3);
                setDynamicFragmentToTabLayout(
                        uniqueCwCategories.size(),
                        getIntent().getStringExtra("crop"),
                        uniqueCwCategories);
                Toast.makeText(SubCategoriesActivity.this, ""+uniqueCwCategories.size(), Toast.LENGTH_SHORT).show();
                Iterator<String> i = uniqueCwCategories.iterator();
                new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                    viewPager2.setCurrentItem(tab.getPosition());
                    if(i.hasNext()){
                        tab.setText(i.next());
                    }

                }).attach();
            }

            @Override
            public void onFailure(Call<CropWiseCategory> call, Throwable t) {
                Toast.makeText(SubCategoriesActivity.this, "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        //back
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDynamicFragmentToTabLayout(int noOfTabs, String crop, Set<String> uniqueCwCategories) {
        for(int i=0; i<noOfTabs;i++){
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }
        DynamicFragmentAdapter dynamicFragmentAdapter = new DynamicFragmentAdapter(
                getSupportFragmentManager(),
                getLifecycle(),
                tabLayout.getTabCount(),
                crop,
                uniqueCwCategories,
                this);
        viewPager2.setAdapter(dynamicFragmentAdapter);
        viewPager2.setCurrentItem(0);
    }

}