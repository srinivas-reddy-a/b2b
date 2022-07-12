package com.arraykart.b2b.Home.CropOrCategory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.Authenticate.LocaleManager;
import com.arraykart.b2b.Home.Adapters.AllCategoriesAdapter;
import com.arraykart.b2b.Home.Adapters.AllCropsAdapter;
import com.arraykart.b2b.Home.Fragments.Account.AccountOptionsActivity;
import com.arraykart.b2b.Loading.LoadingDialog;
import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.AllCategories;
import com.arraykart.b2b.Retrofit.ModelClass.AllCrops;
import com.arraykart.b2b.Retrofit.ModelClass.Category;
import com.arraykart.b2b.Retrofit.ModelClass.Crop;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CropOrCategoryListingActivity extends AppCompatActivity {
    private String type;
    private TextView listName;
    private List<Category> allCategories;
    private List<Crop> allCrops;
    private AllCategoriesAdapter allCategoriesAdapter;
    private RecyclerView allRV;
    private AllCropsAdapter allCropsAdapter;
    private TextView total;
    private ImageView backList;
    private LoadingDialog loadingDialog;
    private static final String LANGUAGE = "language";
    private SharedPreferenceManager sharedPreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_or_category_listing);
        sharedPreferenceManager = new SharedPreferenceManager(this);

        loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        //check token

        checkToken();

        //checkLang();

        type = getIntent().getStringExtra("type");
        listName = findViewById(R.id.listName);

        //back
        backList = findViewById(R.id.backList);
        backList.setOnClickListener(v -> finish());

        total = findViewById(R.id.total);
        allRV = findViewById(R.id.allRV);
        allRV.setHasFixedSize(true);
        allRV.setLayoutManager(new GridLayoutManager(CropOrCategoryListingActivity.this, 3));
        switch (type){
            case "category":
                setCategory();
                break;
            case "crop":
                setCrop();
            default:
                break;
        }
    }

    private void checkLang() {
        LocaleManager localeManager = new LocaleManager(CropOrCategoryListingActivity.this);
        if(sharedPreferenceManager.checkKey(LANGUAGE)) {
            localeManager.updateResource(sharedPreferenceManager.getString(LANGUAGE));
        }
    }
    private void checkToken() {
        AuthorizeUser authorizeUser = new AuthorizeUser(this);
        authorizeUser.isLoggedIn();
    }

    private void setCategory() {
        listName.setText("All Categories");

        Call<AllCategories> call = RetrofitClient.getClient().getApi().getAllCategories(null);
        call.enqueue(new Callback<AllCategories>() {
            @Override
            public void onResponse(@NonNull Call<AllCategories> call, @NonNull Response<AllCategories> response) {
                loadingDialog.dismissLoadingDialog();
                if(!response.isSuccessful()){
                    Toast.makeText(CropOrCategoryListingActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                assert response.body() != null;
                if(!response.body().getSuccess()){
                    Toast.makeText(CropOrCategoryListingActivity.this, "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                allCategories = response.body().getCategories();
                total.setText(new StringBuilder().append("Total: ").append(allCategories.size()).toString());
                allCategoriesAdapter = new AllCategoriesAdapter(
                        CropOrCategoryListingActivity.this, allCategories);
                allCategoriesAdapter.setHasStableIds(true);
                allRV.setAdapter(allCategoriesAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<AllCategories> call, @NonNull Throwable t) {
                loadingDialog.dismissLoadingDialog();
                Toast.makeText(CropOrCategoryListingActivity.this, "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCrop() {
        listName.setText("All Crops");
        Call<AllCrops> call = RetrofitClient.getClient().getApi().getCrops(null);
        call.enqueue(new Callback<AllCrops>() {
            @Override
            public void onResponse(@NonNull Call<AllCrops> call, @NonNull Response<AllCrops> response) {
                loadingDialog.dismissLoadingDialog();
                if(!response.isSuccessful()){
                    Toast.makeText(CropOrCategoryListingActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                assert response.body() != null;
                if(!response.body().getSuccess()){
                    Toast.makeText(CropOrCategoryListingActivity.this, "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                allCrops= response.body().getCrops();
                total.setText(new StringBuilder().append("Total: ").append(allCrops.size()).toString());
                allCropsAdapter = new AllCropsAdapter(
                        CropOrCategoryListingActivity.this, allCrops);
                allRV.setAdapter(allCropsAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<AllCrops> call, @NonNull Throwable t) {
                loadingDialog.dismissLoadingDialog();
                Toast.makeText(CropOrCategoryListingActivity.this, "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
            }
        });
    }
}