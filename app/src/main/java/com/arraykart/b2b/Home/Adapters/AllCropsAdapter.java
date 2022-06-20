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

import com.arraykart.b2b.Retrofit.ModelClass.Category;
import com.arraykart.b2b.Retrofit.ModelClass.Crop;
import com.bumptech.glide.Glide;
import com.arraykart.b2b.R;
import com.arraykart.b2b.SubCategories.SubCategoriesActivity;

import java.util.List;

import soup.neumorphism.NeumorphImageView;

public class AllCropsAdapter extends  RecyclerView.Adapter<AllCropsAdapter.AllCropsViewHolder> {
    private List<Crop> crop;
    private Activity activity;

    public AllCropsAdapter(Activity activity, List<Crop> crop) {
        this.crop = crop;
        this.activity = activity;
    }


    @NonNull
    @Override
    public AllCropsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_main_all_cat_single_item, parent, false);
        return new AllCropsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllCropsViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(crop.get(position).getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(holder.imageView);
//        holder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(activity, SubCategoriesActivity.class);
//                activity.startActivity(i);
//
//            }
//        });
        holder.textView.setText(""+crop.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return crop.size();
    }

    public class AllCropsViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textView;

        public AllCropsViewHolder(@NonNull View itemView) {
            super(itemView);;
            imageView = itemView.findViewById(R.id.allCatIV);
            textView = itemView.findViewById(R.id.allCatTV);
            imageView.setOnClickListener(v -> {
                Intent i = new Intent(activity, SubCategoriesActivity.class);
                i.putExtra("crop", textView.getText());
                activity.startActivity(i);
            });
        }
    }


}
