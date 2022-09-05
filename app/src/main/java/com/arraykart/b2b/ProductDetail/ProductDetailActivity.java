package com.arraykart.b2b.ProductDetail;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import android.widget.Button;
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
import com.arraykart.b2b.Home.HomeActivity;
import com.arraykart.b2b.Products.ProductsListingActivity;
import com.arraykart.b2b.RecyclerViewDecoration.LinePagerIndicatorDecoration;
import com.arraykart.b2b.Retrofit.ModelClass.Cart;
import com.arraykart.b2b.Retrofit.ModelClass.CartProductDelete;
import com.arraykart.b2b.Retrofit.ModelClass.CategoryWise;
import com.arraykart.b2b.Retrofit.ModelClass.Product;
import com.arraykart.b2b.Retrofit.ModelClass.SuccessMessage;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.bumptech.glide.Glide;
import com.arraykart.b2b.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
//    private TextView pdPrice;
    private ArrayAdapter<String> adapter;
    private String[] vol;
    private String[] seeds;
    private String[] prices;
    private LinearLayout volLL;
    private LinearLayout seedsLL;
//    margin
    private RecyclerView marginRV;
    private MarginTableRecyclerAdapter marginTableRecyclerAdapter;
//    details
    private ArrayList<String> pdHeading;
    private ArrayList<String> pdDetailDesc;
    private RecyclerView pdDetailsRV;
    private ProductDetailMoreDetailsRecyclerAdapter productDetailMoreDetailsRecyclerAdapter;
//    description
    private TextView pdDesc;

    private List<Product> products;
    private List<Product> similarProducts;
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

    //price and contact
//    private LinearLayout priceLL;
    private TextView priceContact;
    private ConstraintLayout priceContactIconCL;

    //add to cart and quantity
    private Button addToCart;
    private Button goToCart;
//    private LinearLayout quantityLL;
//    private ImageView pdQAdd;
//    private TextView pdQ;
//    private ImageView pdQRemove;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        sharedPreferenceManager = new SharedPreferenceManager(this);

        addToCart = findViewById(R.id.addToCart);
        goToCart = findViewById(R.id.goToCart);


        checkToken();

        //checkLang();

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
        whatsapp = findViewById(R.id.whatsapp);
        phone = findViewById(R.id.phone);
//        priceLL = findViewById(R.id.priceLL);
        priceContact = findViewById(R.id.priceContact);
        priceContactIconCL = findViewById(R.id.priceContactIconCL);
        marginRV = findViewById(R.id.marginRV);
        if(products.get(0).getPrice()==null
                || products.get(0).getPrice().trim().toLowerCase().contains("na")
        ){
//            priceLL.setVisibility(View.GONE);
            marginRV.setVisibility(View.GONE);
            priceContact.setVisibility(View.VISIBLE);
            priceContactIconCL.setVisibility(View.VISIBLE);
            setContact();
        }else {
            priceContact.setVisibility(View.GONE);
            priceContactIconCL.setVisibility(View.GONE);
            marginRV.setVisibility(View.VISIBLE);
//            priceLL.setVisibility(View.VISIBLE);

            //margin
            setMargin(0);
        }

        //set image rv
        setImageRV();

//        offers
        offers();
//        spinner
        pdVolSpinner = findViewById(R.id.pdVolSpinner);
        setVolSpinner();

        pdSeedsSpinner = findViewById(R.id.pdSeedsSpinner);
        setSeedsSpinner();


        //details
        setDetails();

        //add to cart and set quantity and check cart products
        addToCart();

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

    private void addToCart() {
        checkCart();
//        quantityLL = findViewById(R.id.quantityLL);
//        pdQAdd = findViewById(R.id.pdQAdd);
//        pdQ = findViewById(R.id.pdQ);
//        pdQRemove = findViewById(R.id.pdQRemove);
        addToCart.setOnClickListener(v -> {
            addToCart.setVisibility(View.GONE);
            goToCart.setVisibility(View.VISIBLE);
//            quantityLL.setVisibility(View.VISIBLE);
//            pdQ.setText("1");
            Cart cart = new Cart(
                    products.get(0).getId().toString(),
                    products.get(0).getPrice().split(",")[0],
                    pdVolSpinner.getSelectedItem().toString(),
                    "1",
                    products.get(0).getDiscount().split(",")[0],
                    null
                    );
            sharedPreferenceManager = new SharedPreferenceManager(ProductDetailActivity.this);
            Call<SuccessMessage> call = RetrofitClient.getClient().getApi()
                    .setCart(sharedPreferenceManager.getString("token"),cart);
            call.enqueue(new Callback<SuccessMessage>() {
                @Override
                public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(
                                ProductDetailActivity.this,
                                "" + response.code(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Create a snackbar
                    Snackbar snackbar
                            = Snackbar
                            .make(
                                    findViewById(android.R.id.content),
                                    "Product added to cart",
                                    Snackbar.LENGTH_LONG)
                            .setAction(
                                    "Go to Cart",
                                    view -> {
                                        Intent i = new Intent(ProductDetailActivity.this, HomeActivity.class);
                                        i.putExtra("fragment", "cart");
                                        i.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                    });
                    snackbar.show();
                }

                @Override
                public void onFailure(Call<SuccessMessage> call, Throwable t) {
                    Toast.makeText(
                            ProductDetailActivity.this,
                            "Please check your internet connection or try again after sometime",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
        goToCart.setOnClickListener(v -> {
            Intent i = new Intent(ProductDetailActivity.this, HomeActivity.class);
            i.putExtra("fragment", "cart");
            i.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });
//        if(quantityLL.getVisibility() == View.VISIBLE){
//        pdQAdd.setOnClickListener(v -> pdQ.setText(String
//                .valueOf(Integer
//                        .parseInt(pdQ.getText().toString())+1)));
//        pdQRemove.setOnClickListener(v -> {
//            if(Integer.parseInt(Objects.requireNonNull(pdQ.getText().toString())) > 1){
//                pdQ.setText(String
//                        .valueOf(Integer
//                                .parseInt(pdQ.getText().toString())-1));
//            }else{
//                addToCart.setVisibility(View.VISIBLE);
//                quantityLL.setVisibility(View.GONE);
//            }
//        });
//        }
    }

    private void checkCart() {
        sharedPreferenceManager = new SharedPreferenceManager(ProductDetailActivity.this);
        CartProductDelete cartProductDelete = new CartProductDelete(
                products.get(0).getId().toString(), pdVolSpinner.getSelectedItem().toString());
        Call<SuccessMessage> call = RetrofitClient.getClient().getApi()
                .checkCart(sharedPreferenceManager.getString("token"), cartProductDelete);
        call.enqueue(new Callback<SuccessMessage>() {
            @Override
            public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                if(!response.isSuccessful()){
                    addToCart.setVisibility(View.VISIBLE);
                    goToCart.setVisibility(View.GONE);
//                    Toast.makeText(
//                            ProductDetailActivity.this,
//                            "" + response.code(),
//                            Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    addToCart.setVisibility(View.GONE);
                    goToCart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<SuccessMessage> call, Throwable t) {
                Toast.makeText(
                        ProductDetailActivity.this,
                        "Please check your internet connection or try again after sometime",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkLang() {
        LocaleManager localeManager = new LocaleManager(ProductDetailActivity.this);
        if(sharedPreferenceManager.checkKey(LANGUAGE)) {
            localeManager.updateResource(sharedPreferenceManager.getString(LANGUAGE));
        }
    }

    private void setSimilarProductsCrop() {
        similarRV.setHasFixedSize(true);
        similarRV.setLayoutManager(new LinearLayoutManager(
                ProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        Call<CategoryWise> call = RetrofitClient.getClient().getApi()
                .getSearch(products.get(0).getTargetFieldCrops(), null);
//                loadingDialog = new LoadingDialog(ProductDetailActivity.this);
//                loadingDialog.startLoadingDialog();
        call.enqueue(new Callback<CategoryWise>() {
            @Override
            public void onResponse(@NonNull Call<CategoryWise> call, @NonNull Response<CategoryWise> response) {
//                        loadingDialog.dismissLoadingDialog();
                if(!response.isSuccessful()){
                    Toast.makeText(
                            ProductDetailActivity.this,
                            "" + response.code(),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                assert response.body() != null;
                if(!response.body().getSuccess()){
                    Toast.makeText(
                            ProductDetailActivity.this,
                            "500" + "Internal Server Error",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                similarProducts = response.body().getProducts();
                if(similarProducts.size()>0) {
                    for(int i=0; i<similarProducts.size(); i++){
                        if(similarProducts.get(i).getId()!=id) {
                            if(similarProducts.get(i).getTechnicalName()!=null
                                    && !similarProducts.get(i)
                                    .getTechnicalName().trim().toLowerCase().contains("na")) {
                                sendProducts.add(similarProducts.get(i));
                            }
                        }
                    }
                    if(sendProducts.size()>0){
                        similarProductsAdapter = new SimilarProductsAdapter(
                                sendProducts, id, true, ProductDetailActivity.this);
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
                Toast.makeText(
                        ProductDetailActivity.this,
                        "Please check your internet connection or try again after sometime",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSimilarProductsTech() {
        similarRV.setHasFixedSize(true);
        similarRV.setLayoutManager(new LinearLayoutManager(
                ProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        Call<CategoryWise> call = RetrofitClient.getClient().getApi()
                .getSearch(products.get(0).getTechnicalName(), 8);
//                loadingDialog = new LoadingDialog(ProductDetailActivity.this);
//                loadingDialog.startLoadingDialog();
        call.enqueue(new Callback<CategoryWise>() {
            @Override
            public void onResponse(@NonNull Call<CategoryWise> call, @NonNull Response<CategoryWise> response) {
//                        loadingDialog.dismissLoadingDialog();
                if(!response.isSuccessful()){
                        Toast.makeText(
                                ProductDetailActivity.this,
                                "" + response.code(),
                                Toast.LENGTH_SHORT).show();
                        return;
                }
                assert response.body() != null;
                if(!response.body().getSuccess()){
                        Toast.makeText(
                                ProductDetailActivity.this,
                                "500" + "Internal Server Error",
                                Toast.LENGTH_SHORT).show();
                        return;
                }
                similarProducts = response.body().getProducts();
                if(similarProducts.size()>0) {
                        for(int i=0; i<similarProducts.size(); i++){
                            if(similarProducts.get(i).getId()!=id) {
                                sendProducts.add(similarProducts.get(i));
                            }
                        }
                        if(sendProducts.size()>0){
                            similarProductsAdapter = new SimilarProductsAdapter(
                                    sendProducts, id, false, ProductDetailActivity.this);
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
                    Toast.makeText(
                            ProductDetailActivity.this,
                            "Please check your internet connection or try again after sometime",
                            Toast.LENGTH_SHORT).show();
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
        linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, true);
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
        adapter = new ArrayAdapter<>(
                this, R.layout.spinner_text_view_single_item, R.id.spinnerText, seeds);
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
        linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        pdDetailsRV = findViewById(R.id.pdDetailsRV);
        pdDetailsRV.setLayoutManager(linearLayoutManager);
        productDetailMoreDetailsRecyclerAdapter = new ProductDetailMoreDetailsRecyclerAdapter(
                pdHeading, pdDetailDesc, this);
        pdDetailsRV.setAdapter(productDetailMoreDetailsRecyclerAdapter);
    }

    private void setMargin(int position) {
        String[] noOfCases = products.get(0).getNoOfCases().toString().split(",");
        String[] pricePerUnit = products.get(0).getPrice().split(",");
        String[] discount = products.get(0).getDiscount().split(",");

        linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        marginRV.setLayoutManager(linearLayoutManager);
        marginTableRecyclerAdapter = new MarginTableRecyclerAdapter(
                new ArrayList<>(Arrays.asList(noOfCases)),
                new ArrayList<>(Arrays.asList(pricePerUnit)),
                new ArrayList<>(Arrays.asList(discount)), ProductDetailActivity.this);
        marginRV.setAdapter(marginTableRecyclerAdapter);
    }

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
        prices = products.get(0).getPrice().split(",");
        adapter = new ArrayAdapter<>(this, R.layout.spinner_text_view_single_item, R.id.spinnerText, vol);
//        pdPrice = findViewById(R.id.pdPrice);
        pdVolSpinner.setAdapter(adapter);
//        pdPrice.setText("₹ " + prices[0]);
        pdVolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                pdPrice.setText("₹" + prices[0]);
                checkCart();
                setMargin(position);
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
                            //.placeholder(R.drawable.placeholder)
                            .error(R.drawable.imgnotfound)
                            .into(offerToggle);
                }else {
                    offersRV.setVisibility(View.VISIBLE);
                    Glide.with(ProductDetailActivity.this)
                            .load(R.drawable.ic_baseline_keyboard_arrow_up_24)
                            .centerCrop()
                            //.placeholder(R.drawable.placeholder)
                            .error(R.drawable.imgnotfound)
                            .into(offerToggle);
                }
            }
        });

    }

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