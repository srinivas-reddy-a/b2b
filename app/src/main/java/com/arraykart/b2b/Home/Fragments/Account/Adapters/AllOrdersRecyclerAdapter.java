package com.arraykart.b2b.Home.Fragments.Account.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.Allorder;
import com.bumptech.glide.Glide;

import java.util.List;

public class AllOrdersRecyclerAdapter extends RecyclerView.Adapter<AllOrdersRecyclerAdapter.AllOrdersViewHolder> {
    private List<Allorder> allorders;
    private Activity activity;

    public AllOrdersRecyclerAdapter(List<Allorder> allorders, Activity activity) {
        this.allorders = allorders;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AllOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_account_all_orders_single_item, parent, false);

        return new AllOrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllOrdersViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load("https://arraykartandroid.s3.ap-south-1.amazonaws.com/"+allorders.get(position).getId())
//                .centerCrop()
//                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(holder.productIV);
        holder.productNameTV.setText(allorders.get(position).getProductsId());
        holder.delDateTV.setText(allorders.get(position).getDeliveryDate());
    }

    @Override
    public int getItemCount() {
        return allorders.size();
    }


    public class AllOrdersViewHolder extends RecyclerView.ViewHolder{
        private ImageView productIV;
        private TextView productNameTV;
        private TextView delDateTV;
        public AllOrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            productIV = itemView.findViewById(R.id.productOrderIV);
            productNameTV = itemView.findViewById(R.id.productNameTV);
            delDateTV = itemView.findViewById(R.id.delDateTV);
        }
    }
}
