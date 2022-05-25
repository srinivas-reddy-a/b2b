package com.arraykart.b2b.ProductDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arraykart.b2b.R;

import java.util.ArrayList;
import java.util.Objects;

public class ProductDetailMoreDetailsRecyclerAdapter extends RecyclerView.Adapter<ProductDetailMoreDetailsRecyclerAdapter.ProductDetailsMoreDetailsViewHolder> {
    private ArrayList<String> pdHeading;
    private ArrayList<String> pdDetailDesc;
    private Context context;

    public ProductDetailMoreDetailsRecyclerAdapter(ArrayList<String> pdHeading, ArrayList<String> pdDetailDesc, Context context) {
        this.pdHeading = pdHeading;
        this.pdDetailDesc = pdDetailDesc;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductDetailsMoreDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_product_details_more_details_single_item, parent, false);
        return new ProductDetailsMoreDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDetailsMoreDetailsViewHolder holder, int position) {
        holder.pdDetailHeading.setText(pdHeading.get(position));
        holder.pdDetailDesc.setText(pdDetailDesc.get(position));
        holder.pdDetailToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.pdDetailDesc.getVisibility() == View.GONE){
                    Objects.requireNonNull(holder).pdDetailToggle.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                    Objects.requireNonNull(holder).pdDetailDesc.setVisibility(View.VISIBLE);
                }else {
                    Objects.requireNonNull(holder).pdDetailToggle.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    Objects.requireNonNull(holder).pdDetailDesc.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pdHeading.size();
    }

    public class ProductDetailsMoreDetailsViewHolder extends RecyclerView.ViewHolder{
        private TextView pdDetailHeading;
        private TextView pdDetailDesc;
        private ImageView pdDetailToggle;
        public ProductDetailsMoreDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            pdDetailHeading = itemView.findViewById(R.id.pdDetailHeading);
            pdDetailDesc = itemView.findViewById(R.id.pdDetailDesc);
            pdDetailToggle = itemView.findViewById(R.id.pdDetailToggle);
        }
    }
}
