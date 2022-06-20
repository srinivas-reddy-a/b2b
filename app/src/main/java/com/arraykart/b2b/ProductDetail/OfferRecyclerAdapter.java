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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Objects;

public class OfferRecyclerAdapter extends RecyclerView.Adapter<OfferRecyclerAdapter.OfferViewHolder> {
    private ArrayList<String> offerName;
    private ArrayList<String> offerDesc;
    private ArrayList<String> offerHowToUse;
    private ArrayList<String> offerValidity;
    private BottomSheetDialog bottomSheetDialog;
    private Context context;

    public OfferRecyclerAdapter(ArrayList<String> offerName, ArrayList<String> offerDesc, ArrayList<String> offerHowToUse, ArrayList<String> offerValidity, Context context) {
        this.offerName = offerName;
        this.offerDesc = offerDesc;
        this.offerHowToUse = offerHowToUse;
        this.offerValidity = offerValidity;
        this.context = context;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_pdetail_offer_single_item, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        holder.oName.setText(offerName.get(position));
        holder.oDesc.setText(offerDesc.get(position));
        holder.oMore.setOnClickListener(v -> {
            TextView oName;
            TextView howToUse;
            TextView offValidity;
            ImageView close;
            bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(R.layout.bsd_pdetail_offer);
            oName = bottomSheetDialog.findViewById(R.id.oName);
            howToUse = bottomSheetDialog.findViewById(R.id.howToUse);
            offValidity = bottomSheetDialog.findViewById(R.id.offValidity);
            close = bottomSheetDialog.findViewById(R.id.close);
            assert oName != null;
            oName.setText(offerName.get(Objects.requireNonNull(holder).getAdapterPosition()));
            assert howToUse != null;
            howToUse.setText(offerHowToUse.get(Objects.requireNonNull(holder).getAdapterPosition()));
            assert offValidity != null;
            offValidity.setText(offerValidity.get(Objects.requireNonNull(holder).getAdapterPosition()));
            bottomSheetDialog.show();
            assert close != null;
            close.setOnClickListener(v1 -> bottomSheetDialog.dismiss());
        });
    }
    
    @Override
    public int getItemCount() {
        return offerName.size();
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder{
        private TextView oName;
        private TextView oDesc;
        private TextView oMore;
        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            oName = itemView.findViewById(R.id.offName);
            oDesc = itemView.findViewById(R.id.offDesc);
            oMore = itemView.findViewById(R.id.offMore);
        }
    }
}
