package com.arraykart.b2b.Search.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.arraykart.b2b.Home.Fragments.Account.AccountOptionsActivity;
import com.arraykart.b2b.ProductDetail.ProductDetailActivity;
import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.Product;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.bumptech.glide.Glide;
import java.util.List;

public class SearchPageRecyclerAdapter extends RecyclerView.Adapter<SearchPageRecyclerAdapter.SeacrchPageViewHolder> {
    private ArrayAdapter<String> adapter;
    private Activity activity;
    private List<Product> products;

//    private ArrayList<String> prices;
//    private String[] vol;

    public SearchPageRecyclerAdapter(Activity activity, List<Product> products) {
        this.activity = activity;
        this.products = products;
    }

    @NonNull
    @Override
    public SeacrchPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_main_product_out_of_frame_single_item, parent, false);
        return new SeacrchPageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeacrchPageViewHolder holder, int position) {
        String[] images = products.get(position).getImage().split(",");
        Glide.with(holder.itemView)
                .load(new StringBuilder().append("https://arraykartandroid.s3.ap-south-1.amazonaws.com/").append(images[0]).toString())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(holder.imageView);
        holder.name.setText(products.get(position).getName());
        String[] vol = products.get(position).getVolume().split(",");
        if(
                vol[0]!=null
                        || vol[0].trim().toLowerCase().contains("na")
                        || !vol[0].isEmpty()
        ){
            adapter = new  ArrayAdapter(activity, R.layout.spinner_text_view_single_item, R.id.spinnerText, vol);
        }
        else{
            String[] noOfSeeds = products.get(position).getNumberOfSeedsPacket().split(",");
            adapter = new  ArrayAdapter(activity, R.layout.spinner_text_view_single_item, R.id.spinnerText, noOfSeeds);
        }
        holder.spinnerVol.setAdapter(adapter);
        holder.spinnerVol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    Objects.requireNonNull(holder).price.setText("₹" + prices.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        if(products.get(position).getVolume().equalsIgnoreCase("na")
//                || products.get(position).getVolume().isEmpty()
//                || products.get(position).getVolume().equals(null)
//                || products.get(position).getPrice().equalsIgnoreCase("na")
//                || products.get(position).getPrice().isEmpty()
//                || products.get(position).getPrice().equals(null)){
//            holder.spinnerVol.setVisibility(View.GONE);
//            holder.price.setVisibility(View.GONE);
//            holder.knowPrice.setVisibility(View.VISIBLE);
//            holder.contactLL.setVisibility(View.VISIBLE);
//        }else {
//            //declared and initialized here as price array and volume keeps changing in recyclerview,
//            //due to which all prices and volume will be same
//            String[] vol = products.get(position).getVolume().split(",");
//            adapter = new  ArrayAdapter(activity, R.layout.spinner_text_view_single_item, R.id.spinnerText, vol);
//            holder.spinnerVol.setAdapter(adapter);
//            //declared and initialized here as price array and volume keeps changing in recyclerview,
//            //due to which all prices and volume will be same
//            ArrayList<String> prices = new ArrayList<>(Arrays.asList(products.get(position).getPrice().split(",")));
//            holder.price.setText("₹" + prices.get(0));
//            holder.spinnerVol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    Objects.requireNonNull(holder).price.setText(new StringBuilder().append("₹").append(prices.get(position)).toString());
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class SeacrchPageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView name;
        private TextView price;
        private Spinner spinnerVol;
        private LinearLayout contactLL;
        private TextView knowPrice;
        private ImageView whatsapp;
        private ImageView call;
        private SharedPreferenceManager sharedPreferenceManager;
        public SeacrchPageViewHolder(@NonNull View itemView) {
            super(itemView);
            sharedPreferenceManager = new SharedPreferenceManager(activity);
            imageView = itemView.findViewById(R.id.pimg);
            name = itemView.findViewById(R.id.textView6);
            price = itemView.findViewById(R.id.textView5);
            spinnerVol = itemView.findViewById(R.id.spinner);
            contactLL = itemView.findViewById(R.id.contactLL);
            knowPrice = itemView.findViewById(R.id.knowPrice);
            whatsapp = itemView.findViewById(R.id.whatsapp);
            call = itemView.findViewById(R.id.call);

            imageView.setOnClickListener(v -> {
                Intent i = new Intent(activity, ProductDetailActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("products", products.get(getAdapterPosition()));
                i.putExtras(b);
                activity.startActivity(i);
            });

            whatsapp.setOnClickListener(v -> {
                if(sharedPreferenceManager.checkKey("kycstatus")){
                    if(sharedPreferenceManager.getString("kycstatus").trim().toUpperCase().contains("VF")){
                        try {
                            String vol  = spinnerVol.getSelectedItem().toString();
                            String text = activity.getResources().getString(R.string.whatsapp_price)
                                    + name.getText()
                                    + activity.getResources().getString(R.string.whatsapp_of_volume)
                                    + vol
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
                            i.putExtra("fragmentName", "kyc");
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
                        i.putExtra("fragmentName", "kyc");
                        activity.startActivity(i);
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            });
            call.setOnClickListener(v -> {
                if(sharedPreferenceManager.checkKey("kycstatus")){
                    if(sharedPreferenceManager.getString("kycstatus").trim().toUpperCase().contains("VF")){
                        try{
                            String toNumber = "9311900913";
                            Intent intent = new Intent(Intent.ACTION_DIAL,
                                    Uri.fromParts("tel", toNumber, null));
                            activity.startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setMessage("Complete KYC to order!");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Ok", (dialog, id) -> {
                            dialog.cancel();
                            Intent i = new Intent(activity, AccountOptionsActivity.class);
                            i.putExtra("pageName", "KYC Document");
                            i.putExtra("fragmentName", "kyc");
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
                        i.putExtra("fragmentName", "kyc");
                        activity.startActivity(i);
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
    }
}
