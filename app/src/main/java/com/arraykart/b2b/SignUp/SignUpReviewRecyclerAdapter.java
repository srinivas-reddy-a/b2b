package com.arraykart.b2b.SignUp;

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

public class SignUpReviewRecyclerAdapter extends RecyclerView.Adapter<SignUpReviewRecyclerAdapter.SignUpReviewViewHolder> {
    private ArrayList<Integer> suReviewImg;
    private ArrayList<String> suReview;
    private ArrayList<String> suReviewCustomer;
    private Context context;

    public SignUpReviewRecyclerAdapter(ArrayList<Integer> suReviewImg, ArrayList<String> suReview, ArrayList<String> suReviewCustomer, Context context) {
        this.suReviewImg = suReviewImg;
        this.suReview = suReview;
        this.suReviewCustomer = suReviewCustomer;
        this.context = context;
    }

    @NonNull
    @Override
    public SignUpReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_sign_up_reviews_single_item, parent, false);
        return new SignUpReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SignUpReviewViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(suReviewImg.get(position))
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(holder.suReviewImage);
        holder.suReview.setText(" \"" + suReview.get(position) + "\" ");
        holder.suReviewCustomer.setText("- " + suReviewCustomer.get(position));
    }

    @Override
    public int getItemCount() {
        return suReview.size();
    }

    public class SignUpReviewViewHolder extends RecyclerView.ViewHolder{
        private ImageView suReviewImage;
        private TextView suReview;
        private TextView suReviewCustomer;
        public SignUpReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            suReviewImage = itemView.findViewById(R.id.suReviewImage);
            suReview = itemView.findViewById(R.id.suReview);
            suReviewCustomer = itemView.findViewById(R.id.suReviewCustomer);
        }
    }
}
