package com.arraykart.b2b.ProductDetail;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arraykart.b2b.Retrofit.ModelClass.Product;
import com.bumptech.glide.Glide;
import com.arraykart.b2b.R;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.ProductDetailViewHolder> {
    private List<Product> products;
    private Activity activity;
    private String[] images;

    public ProductDetailAdapter(List<Product> products, Activity activity) {
        this.products = products;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ProductDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_single_image_view, parent, false);
        return new ProductDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDetailViewHolder holder, int position) {
        images = products.get(0).getImage().split(",");
        Glide.with(holder.itemView)
                .load(new StringBuilder().append("https://arraykartandroid.s3.ap-south-1.amazonaws.com/").append(images[position]).toString())
//                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return products.get(0).getImage().split(",").length;
    }

    public class ProductDetailViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public ProductDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rv_img);
        }
    }
}
