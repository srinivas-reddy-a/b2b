package com.arraykart.b2b.SubCategories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.Authenticate.LocaleManager;
import com.arraykart.b2b.Loading.LoadingDialog;
import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.CropWiseCategory;
import com.arraykart.b2b.Retrofit.ModelClass.Cwcategory;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoriesActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private List<Cwcategory> cwcategories;
    private Set<String> uniqueCwCategories;

    private LoadingDialog loadingDialog;
    private SharedPreferenceManager sharedPreferenceManager;

    private static final String LANGUAGE = "language";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categories);
        sharedPreferenceManager = new SharedPreferenceManager(SubCategoriesActivity.this);

        checkToken();

        checkLang();

        Call<CropWiseCategory> call = RetrofitClient.getClient()
                .getApi().getCropWiseCategory(
                        getIntent().getStringExtra("crop"));
        loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        call.enqueue(new Callback<CropWiseCategory>() {
            @Override
            public void onResponse(@NonNull Call<CropWiseCategory> call, @NonNull Response<CropWiseCategory> response) {
                loadingDialog.dismissLoadingDialog();
                if(!response.isSuccessful()){
                    Toast.makeText(SubCategoriesActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                assert response.body() != null;
                if(!response.body().getSuccess()){
                    Toast.makeText(SubCategoriesActivity.this, "500"+"Internal Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                cwcategories = response.body().getCwcategories();
                uniqueCwCategories = new LinkedHashSet<>();
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
                Iterator<String> i = uniqueCwCategories.iterator();
                new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                    viewPager2.setCurrentItem(tab.getPosition());
                    if(i.hasNext()){
                        tab.setText(i.next());
                    }

                }).attach();
            }

            @Override
            public void onFailure(@NonNull Call<CropWiseCategory> call, @NonNull Throwable t) {
                loadingDialog.dismissLoadingDialog();
                Toast.makeText(SubCategoriesActivity.this, "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
            }
        });


        //back
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());
    }

    private void checkLang() {
        LocaleManager localeManager = new LocaleManager(SubCategoriesActivity.this);
        if(sharedPreferenceManager.checkKey(LANGUAGE)) {
            localeManager.updateResource(sharedPreferenceManager.getString(LANGUAGE));
        }
    }
    private void checkToken() {
        AuthorizeUser authorizeUser = new AuthorizeUser(this);
        authorizeUser.isLoggedIn();
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