package com.arraykart.b2b.Home.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arraykart.b2b.Home.HomeActivity;
import com.bumptech.glide.Glide;
import com.arraykart.b2b.ProductDetail.ProductDetailActivity;
import com.arraykart.b2b.R;

import java.util.ArrayList;

public class TopProductsRecyclerAdapter extends RecyclerView.Adapter<TopProductsRecyclerAdapter.TopProductsViewHolder> {
    private ArrayList<Integer> topProductsImages;
    private ArrayList<String> topProductsNames;
    private Activity activity;
    public TopProductsRecyclerAdapter(ArrayList<Integer> topProductsImages, ArrayList<String> topProductsNames, HomeActivity activity) {
        this.topProductsImages = topProductsImages;
        this.topProductsNames = topProductsNames;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TopProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_main_fragment_image_with_pname_single_item, parent, false);
        return new TopProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopProductsViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(topProductsImages.get(position))
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(holder.neumorphImageView);
        holder.neumorphImageView .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(activity, ProductDetailActivity.class);
                activity.startActivity(i);

            }
        });
        holder.textView.setText(topProductsNames.get(position));
    }

    @Override
    public int getItemCount() {
        return topProductsImages.size();
    }

    public class TopProductsViewHolder extends RecyclerView.ViewHolder{
        private ImageView neumorphImageView;
        private TextView textView;
        public TopProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            neumorphImageView = itemView.findViewById(R.id.neumorphImageView);
            textView = itemView.findViewById(R.id.pname);
        }
    }
}
