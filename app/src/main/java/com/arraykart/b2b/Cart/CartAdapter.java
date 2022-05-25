package com.arraykart.b2b.Cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.arraykart.b2b.R;

import java.util.ArrayList;
import java.util.Objects;

import soup.neumorphism.NeumorphImageButton;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private ArrayList<Integer> cartImgs;
    private ArrayList<String> cartPName;
    private ArrayList<String> cartPVol;
    private ArrayList<String> cartPPrice;
    private ArrayList<String> cartPQuantity;
    private Context context;
    public CartAdapter(Context context, ArrayList<Integer> cartImgs, ArrayList<String> cartPName, ArrayList<String> cartPVol, ArrayList<String> cartPPrice, ArrayList<String> cartPQuantity) {
        this.context = context;
        this.cartImgs = cartImgs;
        this.cartPName = cartPName;
        this.cartPVol = cartPVol;
        this.cartPPrice = cartPPrice;
        this.cartPQuantity = cartPQuantity;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_cart_out_of_view_single_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(cartImgs.get(holder.getAdapterPosition()))
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(holder.pImage);
        holder.pName.setText(cartPName.get(holder.getAdapterPosition()));
        holder.pVol.setText(cartPVol.get(holder.getAdapterPosition()));
        holder.pPrice.setText("₹" + " " + cartPPrice.get(holder.getAdapterPosition()));
        holder.pQuantity.setText(cartPQuantity.get(holder.getAdapterPosition()));
        holder.qPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.pQuantity.setText(String.valueOf(Integer
                        .parseInt(Objects.requireNonNull(holder).pQuantity.getText().toString())+1));
                //TODO:update cart total too and total no of items
            }
        });
        holder.qMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(Objects.requireNonNull(holder).pQuantity.getText().toString()) >0){
                    holder.pQuantity.setText(String.valueOf(Integer
                            .parseInt(Objects.requireNonNull(holder).pQuantity.getText().toString())-1));
                }

                //TODO:update cart total too and total no of items
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartPName.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        private ImageView pImage;
        private TextView pName;
        private TextView pVol;
        private TextView pPrice;
        private TextView pQuantity;
        private NeumorphImageButton qPlus;
        private NeumorphImageButton qMinus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            pImage = itemView.findViewById(R.id.cartImage);
            pName = itemView.findViewById(R.id.cartPName);
            pPrice = itemView.findViewById(R.id.cartPPrice);
            pVol = itemView.findViewById(R.id.cartVol);
            pQuantity = itemView.findViewById(R.id.cartPQuantity);
            qPlus = itemView.findViewById(R.id.cartAddButton);
            qMinus = itemView.findViewById(R.id.cartRemoveButton);

        }
    }
}
