package com.arraykart.b2b.SubCategories;

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

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.SampleViewHolder>{
    private ArrayList<Integer> images;
    private ArrayList<String> names;
    private ArrayList<ArrayList<String>> prices;
    private ArrayAdapter<String> adapter;
    private String[] vol;
    private Activity activity;
    public SampleAdapter(Activity activity, ArrayList<Integer> images, ArrayList<String> names, ArrayList<ArrayList<String>> prices, String[] vol) {
        this.images = images;
        this.names = names;
        this.prices = prices;
        this.vol = vol;
        this.activity =activity;
        adapter = new ArrayAdapter(activity, R.layout.spinner_text_view_single_item, R.id.spinnerText, this.vol);
    }

    @NonNull
    @Override
    public SampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_main_product_out_of_frame_single_item, parent, false);
        return new SampleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SampleViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(images.get(position))
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
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

    public class SampleViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView name;
        private TextView price;
        private Spinner spinnerVol;
        public SampleViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.pimg);
            name = itemView.findViewById(R.id.textView6);
            price = itemView.findViewById(R.id.textView5);
            spinnerVol = itemView.findViewById(R.id.spinner);
        }
    }
}
