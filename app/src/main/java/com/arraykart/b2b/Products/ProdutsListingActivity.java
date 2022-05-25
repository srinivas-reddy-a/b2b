package com.arraykart.b2b.Products;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.arraykart.b2b.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ProdutsListingActivity extends AppCompatActivity {
    private ArrayList<Integer> images;
    private ArrayList<String> names;
    private ArrayList<ArrayList<String>> prices;
    private ArrayList<String> pricesArray1;
    private ArrayList<String> pricesArray2;
    private ArrayList<String> pricesArray3;
    private ArrayList<String> pricesArray4;
    private String[] vol;
    private RecyclerView recyclerView;
    private ProductsRecycleradapter productsRecycleradapter;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produts_listing);
        populateArray();
        recyclerView= findViewById(R.id.productsRV);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productsRecycleradapter = new ProductsRecycleradapter(this, images, names, prices, vol);
        recyclerView.setAdapter(productsRecycleradapter);
        back = findViewById(R.id.backPD);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    public void populateArray(){
        images = new ArrayList<>(Arrays.asList(R.drawable.tp0, R.drawable.tp1, R.drawable.tp2, R.drawable.tp3, R.drawable.tp0, R.drawable.tp1, R.drawable.tp2, R.drawable.tp3, R.drawable.tp0, R.drawable.tp1, R.drawable.tp2, R.drawable.tp3));
        names = new ArrayList<>(Arrays.asList("Product1", "Product2", "Product3", "Product4", "Product1", "Product2", "Product3", "Product4", "Product1", "Product2", "Product3", "Product4"));
        prices = new ArrayList<ArrayList<String>>();
        pricesArray1 = new ArrayList<>(Arrays.asList("₹99", "₹999", "₹19", "₹179", "₹99", "₹199", "₹19", "₹179", "₹99", "₹199", "₹19", "₹179"));
        pricesArray2 = new ArrayList<>(Arrays.asList("₹299", "₹899", "₹19", "₹179", "₹99", "₹199", "₹19", "₹179", "₹99", "₹199", "₹19", "₹179"));
        pricesArray3 = new ArrayList<>(Arrays.asList("₹399", "₹399", "₹19", "₹179", "₹99", "₹199", "₹19", "₹179", "₹99", "₹199", "₹19", "₹179"));
        pricesArray4 = new ArrayList<>(Arrays.asList("₹699", "₹799", "₹19", "₹179", "₹99", "₹199", "₹19", "₹179", "₹99", "₹199", "₹19", "₹179"));
        prices.add(pricesArray1);
        prices.add(pricesArray2);
        prices.add(pricesArray3);
        prices.add(pricesArray4);
        prices.add(pricesArray1);
        prices.add(pricesArray2);
        prices.add(pricesArray3);
        prices.add(pricesArray4);
        prices.add(pricesArray1);
        prices.add(pricesArray2);
        prices.add(pricesArray3);
        prices.add(pricesArray4);
        vol = new String[]{"100ml", "500ml", "1liter","10liter"};
    }
}