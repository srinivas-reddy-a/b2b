package com.arraykart.b2b.Home.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.arraykart.b2b.Home.Fragments.Account.AccountOptionsActivity;
import com.arraykart.b2b.Retrofit.ModelClass.Ad;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.bumptech.glide.Glide;
import com.arraykart.b2b.Home.HomeActivity;
import com.arraykart.b2b.R;

import java.util.ArrayList;
import java.util.List;

public class BannerRecyclerAdapter extends RecyclerView.Adapter<BannerRecyclerAdapter.BannerViewHolder> {
    private List<Ad> ads;
    private Activity activity;

    public BannerRecyclerAdapter(List<Ad> ads, Activity activity) {
        this.ads = ads;
        this.activity = activity;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from((parent.getContext())).inflate(R.layout.rv_main_banner_single_item, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        String[] images = ads.get(0).getAds().split(",");
        Glide.with(holder.itemView)
                .load("https://arraykartandroid.s3.ap-south-1.amazonaws.com/"+images[position])
//                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return ads.get(0).getAds().split(",").length;
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private SharedPreferenceManager sharedPreferenceManager;
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            imageView.setOnClickListener(v -> {
                try {
                    sharedPreferenceManager = new SharedPreferenceManager(activity);
                    if(sharedPreferenceManager.checkKey("kycstatus")){
                        if(!sharedPreferenceManager.getString("kycstatus").trim().toUpperCase().contains("NV")){
                            try {
                                String text = "Hi Arraykart, I want to know regarding the offer of "
                                        + ads.get(0).getProduct().split(",")[getAdapterPosition()]
                                        + ".";

                                String toNumber = "9311900913";


                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                                activity.startActivity(intent);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setMessage(activity.getResources().getString(R.string.whatsapp_complete_kyc));
                            builder.setCancelable(true);
                            builder.setPositiveButton("Ok", (dialog, id) -> {
                                dialog.cancel();
                                Intent i = new Intent(activity, AccountOptionsActivity.class);
                                i.putExtra("pageName", "KYC Document");
                                i.putExtra("fragmentName", "address");
                                activity.startActivity(i);
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setMessage("Complete KYC to order!");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Ok", (dialog, id) -> {
                            dialog.cancel();
                            Intent i = new Intent(activity, AccountOptionsActivity.class);
                            i.putExtra("pageName", "KYC Document");
                            i.putExtra("fragmentName", "address");
                            activity.startActivity(i);
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            });
        }
    }
}
