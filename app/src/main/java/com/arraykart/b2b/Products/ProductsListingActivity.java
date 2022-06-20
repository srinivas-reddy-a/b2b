package com.arraykart.b2b.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.Authenticate.LocaleManager;
import com.arraykart.b2b.Loading.LoadingDialog;
import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.CategoryWise;
import com.arraykart.b2b.Retrofit.ModelClass.Product;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.Search.SearchActivity;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  ProductsListingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView back;
    private String category;
    private List<Product> products;
    private ProductsRecycleradapter productsRecycleradapter;
    private TextView totalProducts;
    private TextView categoryName;

    private LoadingDialog loadingDialog;
    private static final String LANGUAGE = "language";
    private SharedPreferenceManager sharedPreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produts_listing);
        sharedPreferenceManager = new SharedPreferenceManager(ProductsListingActivity.this);

        checkToken();
        //get products

        checkLang();

        loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();

        getProducts();

        //back
        back = findViewById(R.id.backPD);
        back.setOnClickListener(v -> finish());
    }

    private void checkLang() {
        LocaleManager localeManager = new LocaleManager(ProductsListingActivity.this);
        if(sharedPreferenceManager.checkKey(LANGUAGE)) {
            localeManager.updateResource(sharedPreferenceManager.getString(LANGUAGE));
        }
    }

    private void checkToken() {
        AuthorizeUser authorizeUser = new AuthorizeUser(this);
        authorizeUser.isLoggedIn();
    }

    private void getProducts() {
        category = getIntent().getStringExtra("category");
        Call<CategoryWise> call = RetrofitClient.getClient().getApi()
                .getCategoryWise(category,  null);

        call.enqueue(new Callback<CategoryWise>() {
            @Override
            public void onResponse(@NonNull Call<CategoryWise> call, @NonNull Response<CategoryWise> response) {
                loadingDialog.dismissLoadingDialog();
                if (!response.isSuccessful()) {
                    Toast.makeText(ProductsListingActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                assert response.body() != null;
                if (!response.body().getSuccess()) {
                    Toast.makeText(ProductsListingActivity.this, "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }

                products = response.body().getProducts();
                categoryName = findViewById(R.id.categoryName);
                totalProducts = findViewById(R.id.totalProducts);
                categoryName.setText(category);
                totalProducts.setText(new StringBuilder().append("Total: ").append(products.size()).toString());
//                Toast.makeText(ProductsListingActivity.this, ""+products.size(), Toast.LENGTH_SHORT).show();
                productsRecycleradapter = new ProductsRecycleradapter(ProductsListingActivity.this, products);
                //setting this true is generating duplicate products
//                productsRecycleradapter.setHasStableIds(true);
                recyclerView = findViewById(R.id.productsRV);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(ProductsListingActivity.this, 2));
                recyclerView.setAdapter(productsRecycleradapter);
                return;
            }

            @Override
            public void onFailure(@NonNull Call<CategoryWise> call, @NonNull Throwable t) {
                loadingDialog.dismissLoadingDialog();
                Toast.makeText(ProductsListingActivity.this, "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
            }
        });
    }
}