package com.arraykart.b2b.Home.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arraykart.b2b.Home.HomeActivity;
import com.arraykart.b2b.R;

import java.util.ArrayList;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.CategoryViewHolder> {
    private ArrayList<String> categories;

    public CategoriesRecyclerAdapter(ArrayList<String> categories, HomeActivity homeActivity) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_main_categories_single_item, parent,false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.textView.setText(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
//        private NeumorphCardView neumorphCardView;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_cat);
//            neumorphCardView = itemView.findViewById(R.id.ncv);
//            neumorphCardView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if(event.getAction() ==MotionEvent.ACTION_DOWN ){
//                        neumorphCardView.setShapeType(1);
//                    }
//                    else if(event.getAction() == MotionEvent.ACTION_UP){
//                        neumorphCardView.setShapeType(0);
//                    }
//                    return true;
//                }
//            });
        }
    }

}
