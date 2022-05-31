package com.arraykart.b2b.Home.TechnicalName;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.arraykart.b2b.Products.ProductsListingActivity;
import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.CategoryWise;
import com.arraykart.b2b.Retrofit.ModelClass.Product;
import com.arraykart.b2b.Retrofit.ModelClass.Techname;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TechNameRecyclerAdapter extends RecyclerView.Adapter<TechNameRecyclerAdapter.TechNameViewHolder>{
    private List<String> technames;
    private Activity activity;

    public TechNameRecyclerAdapter(List<String> technames, Activity activity) {
        this.technames = technames;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TechNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_tech_name_single_item, parent, false);
        return new TechNameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TechNameViewHolder holder, int position) {
        if(!technames.get(position).trim().toLowerCase().contains("na")) {
            holder.techNameTV.setText(technames.get(position));
            holder.techNameCL.setVisibility(View.VISIBLE);
            //need to set manually else they wont recycle
            holder.techNameCL.setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        }else{
            holder.techNameCL.setVisibility(View.GONE);
            //need to set manually else they wont recycle
            holder.techNameCL.setLayoutParams(new RecyclerView.LayoutParams(0,0));

        }
    }

    @Override
    public int getItemCount() {
        return technames.size();
    }

    public class TechNameViewHolder extends RecyclerView.ViewHolder{
        private TextView techNameTV;
        private ConstraintLayout techNameCL;

        public TechNameViewHolder(@NonNull View itemView) {
            super(itemView);
            techNameTV = itemView.findViewById(R.id.techNameTV);
            techNameCL = itemView.findViewById(R.id.techNameCL);
            techNameCL.setOnClickListener(v -> {

                //using category wise as same syntax
                Bundle b = new Bundle();
                b.putString("techname", techNameTV.getText().toString());
                AppCompatActivity appCompatActivity = (AppCompatActivity) itemView.getContext();
                Fragment fragment = new TechnicalWiseProductFragment();
                fragment.setArguments(b);
                appCompatActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.techNameContainer, fragment).commit();

            });
        }
    }
}
