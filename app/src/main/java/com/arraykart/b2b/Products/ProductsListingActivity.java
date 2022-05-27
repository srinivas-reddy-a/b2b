package com.arraykart.b2b.Products;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class ProductsListingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView back;
    private String category;
    private List<Product> products;
    private ProductsRecycleradapter productsRecycleradapter;
    private TextView totalProducts;
    private TextView categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produts_listing);
        //get products
        getProducts();

        //back
        back = findViewById(R.id.backPD);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getProducts() {
        category = getIntent().getStringExtra("category");
        Call<CategoryWise> call = RetrofitClient.getClient().getApi()
                .getCategoryWise(category,  null);
        call.enqueue(new Callback<CategoryWise>() {
            @Override
            public void onResponse(Call<CategoryWise> call, Response<CategoryWise> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ProductsListingActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!response.body().getSuccess()) {
                    Toast.makeText(ProductsListingActivity.this, "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }

                products = response.body().getProducts();
                categoryName = findViewById(R.id.categoryName);
                totalProducts = findViewById(R.id.totalProducts);
                categoryName.setText(category);
                totalProducts.setText("Total: "+products.size());
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
            public void onFailure(Call<CategoryWise> call, Throwable t) {
                Toast.makeText(ProductsListingActivity.this, "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}