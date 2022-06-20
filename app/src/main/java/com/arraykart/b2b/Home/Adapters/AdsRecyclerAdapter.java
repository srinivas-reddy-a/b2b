package com.arraykart.b2b.Home.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.arraykart.b2b.Home.HomeActivity;
import com.arraykart.b2b.R;

import java.util.ArrayList;

import soup.neumorphism.NeumorphCardView;

public class AdsRecyclerAdapter extends RecyclerView.Adapter<AdsRecyclerAdapter.AdsViewHolder> {
    private ArrayList<String> adsCompanyname;
    private ArrayList<String> adsOffer;
    private ArrayList<String> adsOfferExpl;
    private ArrayList<String> adsDate;
    private ArrayList<String> topProductsImages;
    private ArrayList<Integer> bgColors;

    public AdsRecyclerAdapter(ArrayList<String> adsCompanyname, ArrayList<String> adsOffer, ArrayList<String> adsOfferExpl, ArrayList<String> adsDate, ArrayList<String> topProductsImages, ArrayList<Integer> bgColors, HomeActivity activity) {
        this.adsCompanyname = adsCompanyname;
        this.adsOffer = adsOffer;
        this.adsOfferExpl = adsOfferExpl;
        this.adsDate = adsDate;
        this.topProductsImages = topProductsImages;
        this.bgColors = bgColors;
    }

    @NonNull
    @Override
    public AdsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_main_fragment_ads, parent, false);
        return new AdsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdsViewHolder holder, int position) {
        holder.company.setText(adsCompanyname.get(position));
        holder.offer.setText(adsOffer.get(position));
        holder.offerExplanation.setText(adsOfferExpl.get(position));
        holder.date.setText(adsDate.get(position));
        Glide.with(holder.itemView)
                .load(topProductsImages.get(position))
//                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(holder.img);
        //use getResources as setBackgroundColor needs hex value not Resource id
        holder.neumorphCardView.setBackgroundColor(holder.itemView.getResources().getColor(bgColors.get(position)));
    }

    @Override
    public int getItemCount() {
        return adsOffer.size();
    }

    public class AdsViewHolder extends RecyclerView.ViewHolder{
        private TextView company;
        private TextView offer;
        private TextView offerExplanation;
        private TextView date;
        private ImageView img;
        private CardView neumorphCardView;
        public AdsViewHolder(@NonNull View itemView) {
            super(itemView);
            company = itemView.findViewById(R.id.compBrand);
            offer = itemView.findViewById(R.id.textView2);
            offerExplanation = itemView.findViewById(R.id.textView3);
            date = itemView.findViewById(R.id.textView4);
            img = itemView.findViewById(R.id.adimg);
            neumorphCardView = itemView.findViewById(R.id.adsNCV);
        }
    }
}
