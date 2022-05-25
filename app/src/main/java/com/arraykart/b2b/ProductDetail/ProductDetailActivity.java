package com.arraykart.b2b.ProductDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.arraykart.b2b.RecyclerViewDecoration.LinePagerIndicatorDecoration;
import com.bumptech.glide.Glide;
import com.arraykart.b2b.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ProductDetailActivity extends AppCompatActivity {
    private ArrayList<Integer> pimg;
    private RecyclerView pDetailrv;
    private ProductDetailAdapter productDetailAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ImageView backPD;
//    offers
    private RecyclerView offersRV;
    private ArrayList<String> offerName;
    private ArrayList<String> offerDesc;
    private ArrayList<String> offerHowToUse;
    private ArrayList<String> offerValidity;
    private OfferRecyclerAdapter offerRecyclerAdapter;
    private ImageView offerToggle;
//    spinner
    private Spinner pdVolSpinner;
    private TextView pdPrice;
    private ArrayAdapter<String> adapter;
    private String[] vol;
    private ArrayList<String> prices;
//    margin
    private RecyclerView marginRV;
    private ArrayList<ArrayList<String>> packOf;
    private ArrayList<ArrayList<String>> pricePerUnit;
    private ArrayList<ArrayList<String>> margin;
    private MarginTableRecyclerAdapter marginTableRecyclerAdapter;
//    details
    private ArrayList<String> pdHeading;
    private ArrayList<String> pdDetailDesc;
    private RecyclerView pdDetailsRV;
    private ProductDetailMoreDetailsRecyclerAdapter productDetailMoreDetailsRecyclerAdapter;
//    description
    private TextView pdDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        closeActivity();
        pDetailrv = findViewById(R.id.pDetailImg);
//        pDetailrv.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        pDetailrv.setLayoutManager(linearLayoutManager);
        pDetailrv.addItemDecoration(new LinePagerIndicatorDecoration());
        fillArray();
        productDetailAdapter = new ProductDetailAdapter(this, pimg);
        pDetailrv.setAdapter(productDetailAdapter);
        pDetailrv.smoothScrollToPosition(0);
        imgScroll();
        //offers
        offers();
        //spinner
        setSpinner();
        //margin
        setMargin(0);
        //details
        setDetails();
        //description
        pdDesc = findViewById(R.id.pdDesc);
        pdDesc.setText("egowuygefoufwgyoeuyfgwfuygewfouywgfoweuyfgwoufygewoufygwouygyiouyiuy");
    }

    private void setDetails() {
        pdHeading = new ArrayList<>(Arrays.asList("Heading1", "Heading2", "Heading3", "Heading4"));
        pdDetailDesc = new ArrayList<>(Arrays
                .asList("Before you post, search the site to make sure your question hasn’t been answered.",
                        "Before you post, search the site to make sure your question hasn’t been answered.",
                        "Before you post, search the site to make sure your question hasn’t been answered.",
                        "Before you post, search the site to make sure your question hasn’t been answered."));
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pdDetailsRV = findViewById(R.id.pdDetailsRV);
        pdDetailsRV.setLayoutManager(linearLayoutManager);
        productDetailMoreDetailsRecyclerAdapter = new ProductDetailMoreDetailsRecyclerAdapter(
                pdHeading, pdDetailDesc, this);
        pdDetailsRV.setAdapter(productDetailMoreDetailsRecyclerAdapter);
    }

    private void setMargin(int position) {
        ArrayList<String> p1 = new ArrayList<>(Arrays.asList("6", "10", "25"));
        ArrayList<String> p2 = new ArrayList<>(Arrays.asList("7", "15", "35"));
        ArrayList<String> p3 = new ArrayList<>(Arrays.asList("8", "25", "45"));
        ArrayList<String> p4 = new ArrayList<>(Arrays.asList("9", "35", "55"));
        packOf = new ArrayList<>(Arrays.asList(p1, p2, p3, p4));

        ArrayList<String> pp1 = new ArrayList<>(Arrays.asList("6", "10", "25"));
        ArrayList<String> pp2 = new ArrayList<>(Arrays.asList("7", "15", "35"));
        ArrayList<String> pp3 = new ArrayList<>(Arrays.asList("8", "25", "45"));
        ArrayList<String> pp4 = new ArrayList<>(Arrays.asList("9", "35", "55"));
        pricePerUnit = new ArrayList<>(Arrays.asList(pp1, pp2, pp3, pp4));

        ArrayList<String> m1 = new ArrayList<>(Arrays.asList("12.12", "24.00", "52.00"));
        ArrayList<String> m2 = new ArrayList<>(Arrays.asList("13.13", "25.00", "53.00"));
        ArrayList<String> m3 = new ArrayList<>(Arrays.asList("14.14", "26.00", "54.00"));
        ArrayList<String> m4 = new ArrayList<>(Arrays.asList("15.15", "27.00", "55.00"));
        margin = new ArrayList<>(Arrays.asList(m1, m2, m3, m4));

        marginRV = findViewById(R.id.marginRV);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        marginRV.setLayoutManager(linearLayoutManager);
        marginTableRecyclerAdapter = new MarginTableRecyclerAdapter(packOf.get(position), pricePerUnit.get(position), margin.get(position), ProductDetailActivity.this);
        marginRV.setAdapter(marginTableRecyclerAdapter);
    }

    private void setSpinner() {
        pdVolSpinner = findViewById(R.id.pdVolSpinner);
        vol = new String[]{"100ml", "500ml", "1liter","10liter"};
        prices = new ArrayList<>(Arrays.asList("199", "299", "499", "9999"));
        adapter = new ArrayAdapter<>(this, R.layout.spinner_text_view_single_item, R.id.spinnerText, vol);
        pdPrice = findViewById(R.id.pdPrice);
        pdVolSpinner.setAdapter(adapter);
        pdPrice.setText("₹ " + prices.get(0));
        pdVolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pdPrice.setText("₹ " + prices.get(position));
                setMargin(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void closeActivity() {
        backPD = findViewById(R.id.backPD);
        backPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void offers() {
        offerName = new ArrayList<>(Arrays.asList("Bank Offer", "Bank Offer", "Bank Offer"));
        offerDesc = new ArrayList<>(Arrays.asList(
                "Flat INR 3000 Instant Discount on ICICI Bank Credit Cards (excluding Amazon Pay ICICI Credit Card) Credit Card Transactions. Minimum purchase value INR 5000",
                "Flat INR 3000 Instant Discount on ICICI Bank Credit Cards (excluding Amazon Pay ICICI Credit Card) Credit Card Transactions. Minimum purchase value INR 5000",
                "Flat INR 3000 Instant Discount on ICICI Bank Credit Cards (excluding Amazon Pay ICICI Credit Card) Credit Card Transactions. Minimum purchase value INR 5000"));
        offerHowToUse = new ArrayList<>(Arrays.asList(
                " Once you've collected the reward, place a successful Amazon.in order between 01-May-2022 12:00:00 AM to 31-May-2022 11:59:59 PM of minimum order value ₹500." +
                        " Once you've collected the reward, place a successful Amazon.in order between 01-May-2022 12:00:00 AM to 31-May-2022 11:59:59 PM of minimum order value ₹500.",
                " Once you've collected the reward, place a successful Amazon.in order between 01-May-2022 12:00:00 AM to 31-May-2022 11:59:59 PM of minimum order value ₹500." +
                        " Once you've collected the reward, place a successful Amazon.in order between 01-May-2022 12:00:00 AM to 31-May-2022 11:59:59 PM of minimum order value ₹500.",
                " Once you've collected the reward, place a successful Amazon.in order between 01-May-2022 12:00:00 AM to 31-May-2022 11:59:59 PM of minimum order value ₹500." +
                        " Once you've collected the reward, place a successful Amazon.in order between 01-May-2022 12:00:00 AM to 31-May-2022 11:59:59 PM of minimum order value ₹500."
        ));
        offerValidity = new ArrayList<>(Arrays.asList(
                "Valid Until 13-13-2022. Only on products",
                "Valid Until 13-13-2022. Only on products",
                "Valid Until 13-13-2022. Only on products"
        ));
        offersRV = findViewById(R.id.offersRV);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        offersRV.setLayoutManager(linearLayoutManager);
        offerRecyclerAdapter = new OfferRecyclerAdapter(offerName, offerDesc, offerHowToUse, offerValidity, this);
        offersRV.setAdapter(offerRecyclerAdapter);
        offerToggle = findViewById(R.id.offerToggle);
        offerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(offersRV.getVisibility() == View.VISIBLE){
                    offersRV.setVisibility(View.GONE);
                    Glide.with(ProductDetailActivity.this)
                            .load(R.drawable.ic_baseline_keyboard_arrow_down_24)
                            .centerCrop()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.imgnotfound)
                            .into(offerToggle);
                }else {
                    offersRV.setVisibility(View.VISIBLE);
                    Glide.with(ProductDetailActivity.this)
                            .load(R.drawable.ic_baseline_keyboard_arrow_up_24)
                            .centerCrop()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.imgnotfound)
                            .into(offerToggle);
                }
            }
        });

    }

    private void imgScroll() {
        final int interval_time = 2000;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                if(count<pimg.size()){
                    pDetailrv.smoothScrollToPosition(count++);
                    handler.postDelayed(this, interval_time);
                    if(count==pimg.size()){
                        count=0;
                    }
                }
            }
        };
        handler.postDelayed(runnable, interval_time);

    }

    private void fillArray() {
        pimg = new ArrayList<Integer>(Arrays.
                asList(R.drawable.tp0, R.drawable.tp1, R.drawable.tp2, R.drawable.tp3));
    }
}