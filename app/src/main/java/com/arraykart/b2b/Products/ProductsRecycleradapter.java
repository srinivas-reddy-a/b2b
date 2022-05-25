package com.arraykart.b2b.Products;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.arraykart.b2b.ProductDetail.ProductDetailActivity;
import com.arraykart.b2b.R;

import java.util.ArrayList;

public class ProductsRecycleradapter extends RecyclerView.Adapter<ProductsRecycleradapter.ProductsViewHolder> {
    private ArrayList<Integer> images;
    private ArrayList<String> names;
    private ArrayList<ArrayList<String>> prices;
    private String[] vol;
    private ArrayAdapter<String> adapter;
    private Activity activity;
//    private String[] priceArray;

    public ProductsRecycleradapter(Activity activity, ArrayList<Integer> images, ArrayList<String> names, ArrayList<ArrayList<String>> prices, String[] vol) {
        this.activity = activity;
        this.images = images;
        this.names = names;
        this.prices = prices;
        this.vol = vol;
        adapter = new  ArrayAdapter(activity, R.layout.spinner_text_view_single_item, R.id.spinnerText, vol);
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_main_product_out_of_frame_single_item, parent, false);

        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(images.get(position))
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ProductDetailActivity.class);
                activity.startActivity(i);
            }
        });
        holder.name.setText(names.get(position));
        holder.price.setText(prices.get(position).get(0));
        holder.spinnerVol.setAdapter(adapter);
        int parentPosition= position;
        holder.spinnerVol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                holder.price.setText(prices.get(parentPosition).get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView name;
        private TextView price;
        private Spinner spinnerVol;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pimg);
            name = itemView.findViewById(R.id.textView6);
            price = itemView.findViewById(R.id.textView5);
            spinnerVol = itemView.findViewById(R.id.spinner);
        }
    }


}
