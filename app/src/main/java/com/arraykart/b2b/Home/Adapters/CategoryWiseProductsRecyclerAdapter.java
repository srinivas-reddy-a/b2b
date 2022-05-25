package com.arraykart.b2b.Home.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.Product;

import java.util.List;

public class CategoryWiseProductsRecyclerAdapter extends RecyclerView.Adapter<CategoryWiseProductsRecyclerAdapter.CategoryWiseProductsViewHolder> {
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
        Glide.with(holder.itemView)
                .load(catWiseProducts.get(position).getImage())
//                .centerCrop()
                .placeholder(R.drawable.placeholder)
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
        }
    }
}
