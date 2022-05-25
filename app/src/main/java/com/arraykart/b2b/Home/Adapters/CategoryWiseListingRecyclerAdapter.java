package com.arraykart.b2b.Home.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arraykart.b2b.Loading.LoadingDialog;
import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.Category;
import com.arraykart.b2b.Retrofit.ModelClass.CategoryWise;
import com.arraykart.b2b.Retrofit.ModelClass.Product;
import com.arraykart.b2b.Retrofit.RetrofitClient;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryWiseListingRecyclerAdapter extends RecyclerView.Adapter<CategoryWiseListingRecyclerAdapter.CategoryWiseListingViewHolder> {
    private List<Category> allCategory;
    private Activity activity;
    private CategoryWiseProductsRecyclerAdapter categoryWiseProductsRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Product> catWiseProducts;

    //loading
    private LoadingDialog loadingDialog;

    public CategoryWiseListingRecyclerAdapter(List<Category> allCategory, Activity activity) {
        this.allCategory = allCategory;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CategoryWiseListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_main_category_wise_nested_single_item, parent, false);
        return new CategoryWiseListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryWiseListingViewHolder holder, int position) {
        holder.catName.setText(allCategory.get(position).getName());
//        holder.catWiseProducts
        Call<CategoryWise> call = RetrofitClient.getClient().getApi()
                .getCategoryWise(allCategory.get(position).getName(), 6);
        loadingDialog = new LoadingDialog(activity);
//        loadingDialog.startLoadingDialog();
        call.enqueue(new Callback<CategoryWise>() {
            @Override
            public void onResponse(Call<CategoryWise> call, Response<CategoryWise> response) {
//                loadingDialog.dismissLoadingDialog();
                if(!response.isSuccessful()){
                    Toast.makeText(activity, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!response.body().getSuccess()){
                    Toast.makeText(activity, "500"+"Internal Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                catWiseProducts = response.body().getProducts();
                linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                Objects.requireNonNull(holder).catWiseProducts.setLayoutManager(linearLayoutManager);
                categoryWiseProductsRecyclerAdapter = new CategoryWiseProductsRecyclerAdapter(catWiseProducts, activity);
                Objects.requireNonNull(holder).catWiseProducts.setAdapter(categoryWiseProductsRecyclerAdapter);
            }

            @Override
            public void onFailure(Call<CategoryWise> call, Throwable t) {
//                loadingDialog.dismissLoadingDialog();
                Toast.makeText(activity, "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return allCategory.size();
    }

    public class CategoryWiseListingViewHolder extends RecyclerView.ViewHolder{
        private TextView catName;
        private ImageView seeMore;
        private RecyclerView catWiseProducts;
        public CategoryWiseListingViewHolder(@NonNull View itemView) {
            super(itemView);
            catName = itemView.findViewById(R.id.nestedCatName);
            seeMore = itemView.findViewById(R.id.nestedCatMore);
            catWiseProducts = itemView.findViewById(R.id.nestedCatRV);
        }
    }
}
