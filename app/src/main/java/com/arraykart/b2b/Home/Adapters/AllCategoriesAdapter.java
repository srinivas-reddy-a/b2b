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

import com.arraykart.b2b.Products.ProductsListingActivity;
import com.arraykart.b2b.Retrofit.ModelClass.Category;
import com.bumptech.glide.Glide;
import com.arraykart.b2b.R;
import com.arraykart.b2b.SubCategories.SubCategoriesActivity;

import java.util.List;

import soup.neumorphism.NeumorphImageView;

public class AllCategoriesAdapter extends  RecyclerView.Adapter<AllCategoriesAdapter.AllCategoriesViewHolder> {
    private List<Category> cat;
    private Activity activity;

    public AllCategoriesAdapter(Activity activity, List<Category> cat) {
        this.cat = cat;
        this.activity = activity;
    }

    @Override
    public long getItemId(int position) {
        return cat.get(position).getId();
    }

    @NonNull
    @Override
    public AllCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_main_all_cat_single_item, parent, false);

        return new AllCategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllCategoriesViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(cat.get(position).getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(holder.imageView);
        holder.textView.setText(""+cat.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return cat.size();
    }

    public class AllCategoriesViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textView;

        public AllCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);;
            imageView = itemView.findViewById(R.id.allCatIV);
            textView = itemView.findViewById(R.id.allCatTV);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, ProductsListingActivity.class);
                    i.putExtra("category", textView.getText());
                    activity.startActivity(i);
                }
            });
        }
    }


}
