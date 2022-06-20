package com.arraykart.b2b.ProductDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arraykart.b2b.R;

import java.util.ArrayList;
import java.util.Objects;

public class MarginTableRecyclerAdapter extends RecyclerView.Adapter<MarginTableRecyclerAdapter.MarginTableViewHolder> {
    private ArrayList<String> packOf;
    private ArrayList<String> pricePerUnit;
    private ArrayList<String> margin;
    private Context context;

    public MarginTableRecyclerAdapter(ArrayList<String> packOf, ArrayList<String> pricePerUnit, ArrayList<String> margin, Context context) {
        this.packOf = packOf;
        this.pricePerUnit = pricePerUnit;
        this.margin = margin;
        this.context = context;
    }

    @NonNull
    @Override
    public MarginTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_product_detail_margin_single_item, parent, false);
        return new MarginTableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarginTableViewHolder holder, int position) {
        holder.pdPackOf.setText(packOf.get(position));
        holder.pdPricePerUnit.setText("₹" + pricePerUnit.get(position));
        holder.pdMargin.setText(margin.get(position) + "%");
        holder.addToCart.setOnClickListener(v -> {
            Objects.requireNonNull(holder).addToCart.setVisibility(View.GONE);
            Objects.requireNonNull(holder).quantityLL.setVisibility(View.VISIBLE);
            Objects.requireNonNull(holder).pdQ.setText("1");
        });
//        if(holder.quantityLL.getVisibility() == View.VISIBLE){
            holder.pdQAdd.setOnClickListener(v -> Objects.requireNonNull(holder).pdQ.setText(String
                            .valueOf(Integer
                                    .parseInt(Objects.requireNonNull(holder)
                                            .pdQ.getText().toString())+1)));
            holder.pdQRemove.setOnClickListener(v -> {
                if(Integer.parseInt(Objects.requireNonNull(holder.pdQ.getText().toString())) > 0){
                    Objects.requireNonNull(holder).pdQ.setText(String
                                    .valueOf(Integer
                                            .parseInt(Objects.requireNonNull(holder)
                                                    .pdQ.getText().toString())-1));
                }
            });
//        }
    }

    @Override
    public int getItemCount() {
        return packOf.size();
    }

    public static class MarginTableViewHolder extends RecyclerView.ViewHolder{
        private TextView pdPackOf;
        private TextView pdPricePerUnit;
        private TextView pdMargin;
        private Button addToCart;
        private LinearLayout quantityLL;
        private ImageView pdQAdd;
        private TextView pdQ;
        private ImageView pdQRemove;

        public MarginTableViewHolder(@NonNull View itemView) {
            super(itemView);
            pdPackOf = itemView.findViewById(R.id.pdPackOf);
            pdPricePerUnit = itemView.findViewById(R.id.pdPricePerUnit);
            pdMargin = itemView.findViewById(R.id.pdMargin);
            addToCart = itemView.findViewById(R.id.addToCart);
            quantityLL = itemView.findViewById(R.id.quantityLL);
            pdQAdd = itemView.findViewById(R.id.pdQAdd);
            pdQ = itemView.findViewById(R.id.pdQ);
            pdQRemove = itemView.findViewById(R.id.pdQRemove);
        }
    }
}
