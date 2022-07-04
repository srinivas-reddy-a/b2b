package com.arraykart.b2b.ProductDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.Authenticate.LocaleManager;
import com.arraykart.b2b.Home.Adapters.SimilarProductsAdapter;
import com.arraykart.b2b.Home.Adapters.ProductRecyclerAdapter;
import com.arraykart.b2b.Home.Fragments.Account.AccountOptionsActivity;
import com.arraykart.b2b.Products.ProductsListingActivity;
import com.arraykart.b2b.RecyclerViewDecoration.LinePagerIndicatorDecoration;
import com.arraykart.b2b.Retrofit.ModelClass.CategoryWise;
import com.arraykart.b2b.Retrofit.ModelClass.Product;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.bumptech.glide.Glide;
import com.arraykart.b2b.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphCardView;

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
    private TextView volTV;
    private Spinner pdSeedsSpinner;
    private TextView seedsTV;
    private TextView pdPrice;
    private ArrayAdapter<String> adapter;
    private String[] vol;
    private String[] seeds;
    private String[] prices;
    private LinearLayout volLL;
    private LinearLayout seedsLL;
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

    private List<Product> products;
    private List<Product> sendProducts;
    private ArrayList<String> images;
//    contact
    private NeumorphCardView whatsapp;
    private NeumorphCardView phone;
//    product name
    private TextView productName;
    
    //similar products
    private RecyclerView similarRV;
    private SimilarProductsAdapter similarProductsAdapter;

    private int id;
    private TextView similarProductsTv;
    private static final String LANGUAGE = "language";
    private SharedPreferenceManager sharedPreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        sharedPreferenceManager = new SharedPreferenceManager(this);


        checkToken();

        checkLang();

//        get intent extra from calling fragment/activity
//        products = (List<Product>) getIntent().getSerializableExtra("products");
//        or
        Bundle b = this.getIntent().getExtras();
        products = new ArrayList<>();
        products.add((Product) b.getSerializable("products"));

//        set product name
        productName = findViewById(R.id.productName);
        productName.setText(products.get(0).getName());
        id = products.get(0).getId();

        //back
        closeActivity();

        //set contact details
        setContact();

        //set image rv
        setImageRV();

        //offers
//        offers();
//        spinner
        pdVolSpinner = findViewById(R.id.pdVolSpinner);
        setVolSpinner();

        pdSeedsSpinner = findViewById(R.id.pdSeedsSpinner);
        setSeedsSpinner();
        //margin
//        setMargin(0);
        //details
        setDetails();
        //description
//        pdDesc = findViewById(R.id.pdDesc);
//        pdDesc.setText("egowuygefoufwgyoeuyfgwfuygewfouywgfoweuyfgwoufygewoufygwouygyiouyiuy");
        
        //set similar products
        sendProducts = new ArrayList<>();
        similarRV = findViewById(R.id.similarRV);
        if(products.get(0).getTechnicalName()!=null
                && !products.get(0).getTechnicalName().trim().toLowerCase().contains("na")
        ){
            //change products data here instead of changing in adapter
            setSimilarProductsTech();
        }else {
            //change products data here instead of changing in adapter
            setSimilarProductsCrop();
        }

    }

    private void checkLang() {
        LocaleManager localeManager = new LocaleManager(ProductDetailActivity.this);
        if(sharedPreferenceManager.checkKey(LANGUAGE)) {
            localeManager.updateResource(sharedPreferenceManager.getString(LANGUAGE));
        }
    }

    private void setSimilarProductsCrop() {
        similarRV.setHasFixedSize(true);
        similarRV.setLayoutManager(new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        Call<CategoryWise> call = RetrofitClient.getClient().getApi()
                .getSearch(products.get(0).getTargetFieldCrops(), null);
//                loadingDialog = new LoadingDialog(ProductDetailActivity.this);
//                loadingDialog.startLoadingDialog();
        call.enqueue(new Callback<CategoryWise>() {
            @Override
            public void onResponse(@NonNull Call<CategoryWise> call, @NonNull Response<CategoryWise> response) {
//                        loadingDialog.dismissLoadingDialog();
                if(!response.isSuccessful()){
                    Toast.makeText(ProductDetailActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                assert response.body() != null;
                if(!response.body().getSuccess()){
                    Toast.makeText(ProductDetailActivity.this, "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                products = response.body().getProducts();
                if(products.size()>0) {
                    for(int i=0; i<products.size(); i++){
                        if(products.get(i).getId()!=id) {
                            if(products.get(i).getTechnicalName()!=null
                                    && !products.get(i).getTechnicalName().trim().toLowerCase().contains("na")) {
                                sendProducts.add(products.get(i));
                            }
                        }
                    }
                    if(sendProducts.size()>0){
                        similarProductsAdapter = new SimilarProductsAdapter(sendProducts, id, true, ProductDetailActivity.this);
                        similarRV.setAdapter(similarProductsAdapter);
                    }else {
                        similarProductsTv = findViewById(R.id.similarProductsTv);
                        similarProductsTv.setVisibility(View.GONE);
                    }
                }else {
                    similarProductsTv = findViewById(R.id.similarProductsTv);
                    similarProductsTv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryWise> call, @NonNull Throwable t) {
//                            loadingDialog.dismissLoadingDialog();
                Toast.makeText(ProductDetailActivity.this, "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSimilarProductsTech() {
        similarRV.setHasFixedSize(true);
        similarRV.setLayoutManager(new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        Call<CategoryWise> call = RetrofitClient.getClient().getApi()
                .getSearch(products.get(0).getTechnicalName(), 8);
//                loadingDialog = new LoadingDialog(ProductDetailActivity.this);
//                loadingDialog.startLoadingDialog();
        call.enqueue(new Callback<CategoryWise>() {
            @Override
            public void onResponse(@NonNull Call<CategoryWise> call, @NonNull Response<CategoryWise> response) {
//                        loadingDialog.dismissLoadingDialog();
                if(!response.isSuccessful()){
                        Toast.makeText(ProductDetailActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                }
                assert response.body() != null;
                if(!response.body().getSuccess()){
                        Toast.makeText(ProductDetailActivity.this, "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                        return;
                }
                products = response.body().getProducts();
                if(products.size()>0) {
                        for(int i=0; i<products.size(); i++){
                            if(products.get(i).getId()!=id) {
                                sendProducts.add(products.get(i));
                            }
                        }
                        if(sendProducts.size()>0){
                            similarProductsAdapter = new SimilarProductsAdapter(sendProducts, id, false, ProductDetailActivity.this);
                            similarRV.setAdapter(similarProductsAdapter);
                        }else {
                            similarProductsTv = findViewById(R.id.similarProductsTv);
                            similarProductsTv.setVisibility(View.GONE);
                        }

//                    similarRV.smoothScrollToPosition(sendProducts.size()-1);
                }else {
                    similarProductsTv = findViewById(R.id.similarProductsTv);
                    similarProductsTv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryWise> call, @NonNull Throwable t) {
//                            loadingDialog.dismissLoadingDialog();
                    Toast.makeText(ProductDetailActivity.this, "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkToken() {
        AuthorizeUser authorizeUser = new AuthorizeUser(this);
        authorizeUser.isLoggedIn();
    }

    private void setImageRV() {
        pDetailrv = findViewById(R.id.pDetailImg);
        pDetailrv.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        pDetailrv.setLayoutManager(linearLayoutManager);
        pDetailrv.addItemDecoration(new LinePagerIndicatorDecoration());
        productDetailAdapter = new ProductDetailAdapter(products, this);
        pDetailrv.setAdapter(productDetailAdapter);
        pDetailrv.smoothScrollToPosition(0);
        images = new ArrayList<>(Arrays.asList(products.get(0).getImage().split(",")));
        imgScroll();
    }

    private void setSeedsSpinner() {
        seedsTV = findViewById(R.id.seeds);
        seedsLL = findViewById(R.id.seedsLL);
        seeds = products.get(0).getNumberOfSeedsPacket().split(",");
        if(
                seeds[0].isEmpty()
                || seeds[0] == null
                || seeds[0].trim().toLowerCase().contains("na")
        ){
            pdSeedsSpinner.setVisibility(View.GONE);
            seedsTV.setVisibility(View.GONE);
            seedsLL.setVisibility(View.GONE);
            return;
        }
        seedsLL.setVisibility(View.VISIBLE);
        pdSeedsSpinner.setVisibility(View.VISIBLE);
        seedsTV.setVisibility(View.VISIBLE);
//        prices = products.get(0).getPrice().split(",");
        adapter = new ArrayAdapter<>(this, R.layout.spinner_text_view_single_item, R.id.spinnerText, seeds);
//        pdPrice = findViewById(R.id.pdPrice);
        pdSeedsSpinner.setAdapter(adapter);
//        pdPrice.setText("₹ " + prices[0]);
        pdSeedsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                pdPrice.setText("₹" + prices[0]);
//                setMargin(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setContact() {
        whatsapp = findViewById(R.id.whatsapp);
        phone = findViewById(R.id.phone);
        whatsapp.setOnClickListener(v -> {
            if(sharedPreferenceManager.checkKey("kycstatus")){
                if(!sharedPreferenceManager.getString("kycstatus").trim().toUpperCase().contains("NV")){
                    try {
                        String vol;
                        String[] seeds = products.get(0).getNumberOfSeedsPacket().split(",");
                        if(
                                !seeds[0].isEmpty()
                                        && seeds[0] != null
                                        && !seeds[0].trim().toLowerCase().contains("na")
                        ){
                            vol = pdSeedsSpinner.getSelectedItem().toString();
                        }else {
                            vol = pdVolSpinner.getSelectedItem().toString();
                        }
                        String text = getResources().getString(R.string.whatsapp_price)
                                + products.get(0).getName()
                                + getResources().getString(R.string.whatsapp_of_volume)
                                + vol
                                + ".";

                        String toNumber = "9311900913";


                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                        startActivity(intent);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.whatsapp_complete_kyc));
                    builder.setCancelable(true);
                    builder.setPositiveButton("Ok", (dialog, id) -> {
                        dialog.cancel();
                        Intent i = new Intent(ProductDetailActivity.this, AccountOptionsActivity.class);
                        i.putExtra("pageName", "KYC Document");
                        i.putExtra("fragmentName", "address");
                        startActivity(i);
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Complete KYC to order!");
                builder.setCancelable(true);
                builder.setPositiveButton("Ok", (dialog, id) -> {
                    dialog.cancel();
                    Intent i = new Intent(this, AccountOptionsActivity.class);
                    i.putExtra("pageName", "KYC Document");
                    i.putExtra("fragmentName", "address");
                    startActivity(i);
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });
        phone.setOnClickListener(v -> {
            if(sharedPreferenceManager.checkKey("kycstatus")){
                if(!sharedPreferenceManager.getString("kycstatus").trim().toUpperCase().contains("NV")){
                    try{
                        String toNumber = "9311900913";
                        Intent intent = new Intent(Intent.ACTION_DIAL,
                                Uri.fromParts("tel", toNumber, null));
                        startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Complete KYC to order!");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Ok", (dialog, id) -> {
                        dialog.cancel();
                        Intent i = new Intent(this, AccountOptionsActivity.class);
                        i.putExtra("pageName", "KYC Document");
                        i.putExtra("fragmentName", "address");
                        startActivity(i);
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Complete KYC to order!");
                builder.setCancelable(true);
                builder.setPositiveButton("Ok", (dialog, id) -> {
                    dialog.cancel();
                    Intent i = new Intent(this, AccountOptionsActivity.class);
                    i.putExtra("pageName", "KYC Document");
                    i.putExtra("fragmentName", "address");
                    startActivity(i);
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });
    }

    private void setDetails() {
        pdDetailDesc = new ArrayList<>(Arrays.asList(
                products.get(0).getTechnicalName(),
                products.get(0).getBrand(),
                products.get(0).getCategory(),
                products.get(0).getDosage()
        ));
        pdHeading = new ArrayList<>(Arrays
                .asList(
                        "Technical Name",
                        "Brand",
                        "Category",
                        "Dosage"
                ));
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pdDetailsRV = findViewById(R.id.pdDetailsRV);
        pdDetailsRV.setLayoutManager(linearLayoutManager);
        productDetailMoreDetailsRecyclerAdapter = new ProductDetailMoreDetailsRecyclerAdapter(
                pdHeading, pdDetailDesc, this);
        pdDetailsRV.setAdapter(productDetailMoreDetailsRecyclerAdapter);
    }

//    private void setMargin(int position) {
//        ArrayList<String> p1 = new ArrayList<>(Arrays.asList("6", "10", "25"));
//        ArrayList<String> p2 = new ArrayList<>(Arrays.asList("7", "15", "35"));
//        ArrayList<String> p3 = new ArrayList<>(Arrays.asList("8", "25", "45"));
//        ArrayList<String> p4 = new ArrayList<>(Arrays.asList("9", "35", "55"));
//        packOf = new ArrayList<>(Arrays.asList(p1, p2, p3, p4));
//
//        ArrayList<String> pp1 = new ArrayList<>(Arrays.asList("6", "10", "25"));
//        ArrayList<String> pp2 = new ArrayList<>(Arrays.asList("7", "15", "35"));
//        ArrayList<String> pp3 = new ArrayList<>(Arrays.asList("8", "25", "45"));
//        ArrayList<String> pp4 = new ArrayList<>(Arrays.asList("9", "35", "55"));
//        pricePerUnit = new ArrayList<>(Arrays.asList(pp1, pp2, pp3, pp4));
//
//        ArrayList<String> m1 = new ArrayList<>(Arrays.asList("12.12", "24.00", "52.00"));
//        ArrayList<String> m2 = new ArrayList<>(Arrays.asList("13.13", "25.00", "53.00"));
//        ArrayList<String> m3 = new ArrayList<>(Arrays.asList("14.14", "26.00", "54.00"));
//        ArrayList<String> m4 = new ArrayList<>(Arrays.asList("15.15", "27.00", "55.00"));
//        margin = new ArrayList<>(Arrays.asList(m1, m2, m3, m4));
//
//        marginRV = findViewById(R.id.marginRV);
//        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        marginRV.setLayoutManager(linearLayoutManager);
//        marginTableRecyclerAdapter = new MarginTableRecyclerAdapter(packOf.get(position), pricePerUnit.get(position), margin.get(position), ProductDetailActivity.this);
//        marginRV.setAdapter(marginTableRecyclerAdapter);
//    }

    private void setVolSpinner() {
        volTV = findViewById(R.id.vol);
        volLL = findViewById(R.id.volLL);
        vol = products.get(0).getVolume().split(",");
        //Log.e("vol", "out");
        if(
                vol[0].isEmpty()
                || vol[0] == null
                || vol[0].trim().toLowerCase().contains("na")
        ){
            //Log.e("vol", "in"+products.get(0).getVolume());
            pdVolSpinner.setVisibility(View.GONE);
            volTV.setVisibility(View.GONE);
            volLL.setVisibility(View.GONE);
            return;
        }
        //Log.e("vol", "visible"+vol[0]);
        volLL.setVisibility(View.VISIBLE);
        pdVolSpinner.setVisibility(View.VISIBLE);
        volTV.setVisibility(View.VISIBLE);
//        prices = products.get(0).getPrice().split(",");
        adapter = new ArrayAdapter<>(this, R.layout.spinner_text_view_single_item, R.id.spinnerText, vol);
//        pdPrice = findViewById(R.id.pdPrice);
        pdVolSpinner.setAdapter(adapter);
//        pdPrice.setText("₹ " + prices[0]);
        pdVolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                pdPrice.setText("₹" + prices[0]);
//                setMargin(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void closeActivity() {
        backPD = findViewById(R.id.backPD);
        backPD.setOnClickListener(v -> finish());

    }

//    private void offers() {
//        offerName = new ArrayList<>(Arrays.asList("Bank Offer", "Bank Offer", "Bank Offer"));
//        offerDesc = new ArrayList<>(Arrays.asList(
//                "Flat INR 3000 Instant Discount on ICICI Bank Credit Cards (excluding Amazon Pay ICICI Credit Card) Credit Card Transactions. Minimum purchase value INR 5000",
//                "Flat INR 3000 Instant Discount on ICICI Bank Credit Cards (excluding Amazon Pay ICICI Credit Card) Credit Card Transactions. Minimum purchase value INR 5000",
//                "Flat INR 3000 Instant Discount on ICICI Bank Credit Cards (excluding Amazon Pay ICICI Credit Card) Credit Card Transactions. Minimum purchase value INR 5000"));
//        offerHowToUse = new ArrayList<>(Arrays.asList(
//                " Once you've collected the reward, place a successful Amazon.in order between 01-May-2022 12:00:00 AM to 31-May-2022 11:59:59 PM of minimum order value ₹500." +
//                        " Once you've collected the reward, place a successful Amazon.in order between 01-May-2022 12:00:00 AM to 31-May-2022 11:59:59 PM of minimum order value ₹500.",
//                " Once you've collected the reward, place a successful Amazon.in order between 01-May-2022 12:00:00 AM to 31-May-2022 11:59:59 PM of minimum order value ₹500." +
//                        " Once you've collected the reward, place a successful Amazon.in order between 01-May-2022 12:00:00 AM to 31-May-2022 11:59:59 PM of minimum order value ₹500.",
//                " Once you've collected the reward, place a successful Amazon.in order between 01-May-2022 12:00:00 AM to 31-May-2022 11:59:59 PM of minimum order value ₹500." +
//                        " Once you've collected the reward, place a successful Amazon.in order between 01-May-2022 12:00:00 AM to 31-May-2022 11:59:59 PM of minimum order value ₹500."
//        ));
//        offerValidity = new ArrayList<>(Arrays.asList(
//                "Valid Until 13-13-2022. Only on products",
//                "Valid Until 13-13-2022. Only on products",
//                "Valid Until 13-13-2022. Only on products"
//        ));
//        offersRV = findViewById(R.id.offersRV);
//        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        offersRV.setLayoutManager(linearLayoutManager);
//        offerRecyclerAdapter = new OfferRecyclerAdapter(offerName, offerDesc, offerHowToUse, offerValidity, this);
//        offersRV.setAdapter(offerRecyclerAdapter);
//        offerToggle = findViewById(R.id.offerToggle);
//        offerToggle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(offersRV.getVisibility() == View.VISIBLE){
//                    offersRV.setVisibility(View.GONE);
//                    Glide.with(ProductDetailActivity.this)
//                            .load(R.drawable.ic_baseline_keyboard_arrow_down_24)
//                            .centerCrop()
//                            .placeholder(R.drawable.placeholder)
//                            .error(R.drawable.imgnotfound)
//                            .into(offerToggle);
//                }else {
//                    offersRV.setVisibility(View.VISIBLE);
//                    Glide.with(ProductDetailActivity.this)
//                            .load(R.drawable.ic_baseline_keyboard_arrow_up_24)
//                            .centerCrop()
//                            .placeholder(R.drawable.placeholder)
//                            .error(R.drawable.imgnotfound)
//                            .into(offerToggle);
//                }
//            }
//        });
//
//    }

    private void imgScroll() {
        images = new ArrayList<>(Arrays.asList(products.get(0).getImage().split(",")));
        final int interval_time = 2000;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                if (count < images.size()) {
                    pDetailrv.smoothScrollToPosition(count++);
                    handler.postDelayed(this, interval_time);
                    if (count == images.size()) {
                        count = 0;
                    }
                } else {
                    return;
                }
            }
        };
        handler.postDelayed(runnable, interval_time);

    }
}