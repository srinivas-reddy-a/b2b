package com.arraykart.b2b.Home.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arraykart.b2b.ProductDetail.ProductDetailActivity;
import com.bumptech.glide.Glide;
import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.Product;

import java.io.Serializable;
import java.util.List;

public class CategoryWiseProductsRecyclerAdapter extends RecyclerView.Adapter<CategoryWiseProductsRecyclerAdapter.CategoryWiseProductsViewHolder>{
    List<Product> catWiseProducts;
    Activity activity;

    public CategoryWiseProductsRecyclerAdapter(List<Product> catWiseProducts, Activity activity) {
        this.catWiseProducts = catWiseProducts;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CategoryWiseProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_main_fragment_image_with_pname_single_item, parent, false);
        return new CategoryWiseProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryWiseProductsViewHolder holder, int position) {
        String[] images = catWiseProducts.get(position).getImage().split(",");
        Glide.with(holder.itemView)
                .load(new StringBuilder().append("https://arraykartandroid.s3.ap-south-1.amazonaws.com/").append(images[0]).toString())
//                .centerCrop()
                //.placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(holder.neumorphImageView);
        holder.pname.setText(catWiseProducts.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return catWiseProducts.size();
    }

    public class CategoryWiseProductsViewHolder extends RecyclerView.ViewHolder{
        private ImageView neumorphImageView;
        private TextView pname;
        public CategoryWiseProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            neumorphImageView = itemView.findViewById(R.id.neumorphImageView);
            pname = itemView.findViewById(R.id.pname);
            neumorphImageView.setOnClickListener(v -> {
                Bundle b = new Bundle();
//                implement Serializable class in Object class from List<Object> to pass Serializable data
                b.putSerializable("products", (Serializable) catWiseProducts.get(getAdapterPosition()));
                Intent i = new Intent(activity, ProductDetailActivity.class);
//                i.putExtra("products", (Serializable) catWiseProducts);
//                or
                i.putExtras(b);
                activity.startActivity(i);

            });
        }
    }
}
