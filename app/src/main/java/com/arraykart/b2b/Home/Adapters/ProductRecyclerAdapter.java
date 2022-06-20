package com.arraykart.b2b.Home.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arraykart.b2b.Home.HomeActivity;
import com.arraykart.b2b.Retrofit.ModelClass.Product;
import com.bumptech.glide.Glide;
import com.arraykart.b2b.ProductDetail.ProductDetailActivity;
import com.arraykart.b2b.R;

import java.util.ArrayList;
import java.util.List;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ProductViewHolder> {
    private List<Product> products;
    private Activity activity;

    public ProductRecyclerAdapter(List<Product> products, Activity activity) {
        this.products = products;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from((parent.getContext())).inflate(R.layout.rv_main_category_product_single_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        String[] imgs = products.get(position).getImage().split(",");
        Glide.with(holder.itemView)
                .load(new StringBuilder().append("https://arraykartandroid.s3.ap-south-1.amazonaws.com/").append(imgs[0]).toString())
//                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productIV);
            imageView.setOnClickListener(v -> {
                Intent i =new Intent(activity, ProductDetailActivity.class);
                Bundle b =new Bundle();
                b.putSerializable("products", products.get(getAdapterPosition()));
                i.putExtras(b);
                activity.startActivity(i);
            });
        }
    }
}
