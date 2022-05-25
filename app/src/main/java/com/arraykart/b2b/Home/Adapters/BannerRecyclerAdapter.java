package com.arraykart.b2b.Home.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.arraykart.b2b.Home.HomeActivity;
import com.arraykart.b2b.R;

import java.util.ArrayList;

public class BannerRecyclerAdapter extends RecyclerView.Adapter<BannerRecyclerAdapter.BannerViewHolder> {
    private ArrayList<Integer> images;

    public BannerRecyclerAdapter(ArrayList<Integer> images, HomeActivity homeActivity) {
        this.images = images;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from((parent.getContext())).inflate(R.layout.rv_main_banner_single_item, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(images.get(position))
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
