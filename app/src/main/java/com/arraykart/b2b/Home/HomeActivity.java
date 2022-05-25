package com.arraykart.b2b.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.arraykart.b2b.Home.Fragments.CartFragment;
import com.arraykart.b2b.Home.Fragments.ScrollFragment;
import com.arraykart.b2b.Home.Fragments.WalletFragment;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.bumptech.glide.Glide;
import com.arraykart.b2b.Home.Fragments.AccountFragment;
import com.arraykart.b2b.R;

import java.util.Locale;

import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.NeumorphImageButton;

public class HomeActivity extends AppCompatActivity {


    private Fragment scrollFragment;
    private Fragment walletFragment;
    private Fragment cartFragment;
    private Fragment accountFragment;
    private NeumorphImageButton home;
    private NeumorphImageButton wallet;
    private NeumorphImageButton cart;
    private NeumorphImageButton account;
    private ImageView imageView;
    private ImageView icon;
    private NeumorphCardView neumorphCardView;

    private EditText editText;
    //for getContentResolver to work in wallet fragment
    private static Context contextOfApplication;
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }


    //userLocation
    LocationManager locationManager;
    LocationListener locationListener;
    double lat, lon;
    private TextView pincode;

    //header and footer
    private NeumorphCardView footer;
    private ConstraintLayout header;

    //shared preferences
    private SharedPreferenceManager sharedPreferenceManager;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        if(sharedPreferenceManager.getString("token") == null){
//            Intent i =
//        }
        //user location
        getUserLocation();

        //for getContentResolver to work in wallet fragment
        contextOfApplication = getApplicationContext();

        scrollFragment = new ScrollFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.homeContainer, scrollFragment).commit();
        imageView = findViewById(R.id.homeCompanyName);
        Glide.with(this)
                .load(R.drawable.company_name_green)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(imageView);
        icon = findViewById(R.id.homeCompanyicon);
        Glide.with(this)
                .load(R.drawable.company_icon_green)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(icon);
        neumorphCardView = findViewById(R.id.neumorphCardView);
        editText = findViewById(R.id.search);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(HomeActivity.this, SearchActivity.class);
//                startActivity(i)    ;
            }
        });

//        animations for hints in search
//        final int interval_time=2000;
//        searchOptions = new ArrayList<>(Arrays.asList("Search Foods!", "Search Maggie!", "Search Chocolates!", "Search Biscuits!"));
//        Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            int count=0;
//            @Override
//            public void run() {
//                if(count<searchOptions.size()){
//                    editText.setHint(searchOptions.get(count++));
//                    handler.postDelayed(this, interval_time);
//                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
//                    editText.startAnimation(animation);
//                }
//                if(count==searchOptions.size()){
//                    count=0;
//                }
//            }
//        };
//        handler.postDelayed(runnable, interval_time);

        home = findViewById(R.id.home);
        scrollFragment = getSupportFragmentManager().findFragmentByTag("homeFragment");
        Glide.with(this)
                .load(R.drawable.ic_outline_home_24_green)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(home);
        home.setOnTouchListener((v, event) -> {
            if(scrollFragment!=null && scrollFragment.isVisible()){
//                Toast.makeText(this, "visible", Toast.LENGTH_SHORT).show();
            }else{
//                Toast.makeText(this, "notvisible", Toast.LENGTH_SHORT).show();
            }
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                home.setShapeType(1);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                home.setShapeType(0);
                Glide.with(this)
                        .load(R.drawable.ic_outline_account_circle_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(account);
                Glide.with(this)
                        .load(R.drawable.ic_outline_account_balance_wallet_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(wallet);
                Glide.with(this)
                        .load(R.drawable.ic_outline_home_24_green)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(home);
                Glide.with(this)
                        .load(R.drawable.ic_outline_shopping_cart_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(cart);
                scrollFragment = new ScrollFragment();
                FragmentTransaction fragmentTransaction14 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction14.replace(R.id.homeContainer, scrollFragment).commit();
                neumorphCardView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                icon.setVisibility(View.VISIBLE);
            }
            //return true as motion up will occur only after motion down
            return true;
        });
        wallet = findViewById(R.id.wallet);
        Glide.with(this)
                .load(R.drawable.ic_outline_account_balance_wallet_24)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(wallet);
        wallet.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                wallet.setShapeType(1);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                wallet.setShapeType(0);
                if(walletFragment!=null && walletFragment.isVisible()){
//                    Toast.makeText(this, "visible", Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(this, "notvisible", Toast.LENGTH_SHORT).show();
                }
                Glide.with(this)
                        .load(R.drawable.ic_outline_account_circle_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(account);
                Glide.with(this)
                        .load(R.drawable.ic_outline_account_balance_wallet_24_green)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(wallet);
                Glide.with(this)
                        .load(R.drawable.ic_outline_home_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(home);
                Glide.with(this)
                        .load(R.drawable.ic_outline_shopping_cart_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(cart);
                walletFragment = new WalletFragment();
                neumorphCardView.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                icon.setVisibility(View.GONE);
                FragmentTransaction fragmentTransaction13 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction13.replace(R.id.homeContainer, walletFragment).commit();
            }
            //return true as motion up will occur only after motion down
            return true;
        });
        cart = findViewById(R.id.cart);
        Glide.with(this)
                .load(R.drawable.ic_outline_shopping_cart_24)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(cart);
        cart.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                cart.setShapeType(1);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                cart.setShapeType(0);
                Glide.with(this)
                        .load(R.drawable.ic_outline_account_circle_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(account);
                Glide.with(this)
                        .load(R.drawable.ic_outline_account_balance_wallet_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(wallet);
                Glide.with(this)
                        .load(R.drawable.ic_outline_home_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(home);
                Glide.with(this)
                        .load(R.drawable.ic_outline_shopping_cart_24_green)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(cart);
                cartFragment = new CartFragment();
                neumorphCardView.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                icon.setVisibility(View.GONE);
                FragmentTransaction fragmentTransaction12 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction12.replace(R.id.homeContainer,cartFragment).commit();
            }
            //return true as motion up will occur only after motion down
            return true;
        });
        account = findViewById(R.id.account);
        Glide.with(this)
                .load(R.drawable.ic_outline_account_circle_24)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(account);
        account.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                account.setShapeType(1);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                account.setShapeType(0);
                Glide.with(this)
                        .load(R.drawable.ic_outline_account_circle_24_green)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(account);
                Glide.with(this)
                        .load(R.drawable.ic_outline_account_balance_wallet_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(wallet);
                Glide.with(this)
                        .load(R.drawable.ic_outline_home_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(home);
                Glide.with(this)
                        .load(R.drawable.ic_outline_shopping_cart_24)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imgnotfound)
                        .into(cart);
                accountFragment = new AccountFragment();
                neumorphCardView.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                icon.setVisibility(View.GONE);
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.homeContainer, accountFragment).commit();
            }
            //return true as motion up will occur only after motion down
            return true;
        });

        //all categories from api
    }

    private void getUserLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
//                Toast.makeText(HomeActivity.this, "lan:"+String.valueOf(lat)+" lon:"+String.valueOf(lon), Toast.LENGTH_SHORT).show();
//                Log.e("lat: ", String.valueOf(lat));
//                Log.e("lon: ", String.valueOf(lon));
                pincode = findViewById(R.id.pincode);
                pincode.setText("lat"+lat);
                final Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());

            }
            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==1 && permissions.length > 0 && ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }
    }
}














