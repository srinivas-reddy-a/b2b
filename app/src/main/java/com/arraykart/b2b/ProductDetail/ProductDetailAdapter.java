package com.arraykart.b2b.ProductDetail;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
                //.placeholder(R.drawable.placeholder)
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
            imageView.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater layoutInflater = activity.getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.custom_full_screen_image_product_detail, null);
                builder.setView(view);
                builder.setCancelable(true);
                AlertDialog alertDialog = builder.create();

                //        to make alert dialog full screen
                ImageView close = view.findViewById(R.id.closeFullImg);
                close.setOnClickListener(v1 -> alertDialog.dismiss());
                ImageView fullimg = view.findViewById(R.id.fullimg);
                Glide.with(itemView)
                        .load(imageView.getDrawable())
                        //.placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(fullimg);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(alertDialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.getWindow().setAttributes(lp);
                alertDialog.show();
                alertDialog.getWindow().setAttributes(lp);
            });
        }
    }
}
