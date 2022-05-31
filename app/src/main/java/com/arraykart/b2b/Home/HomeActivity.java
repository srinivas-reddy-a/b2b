package com.arraykart.b2b.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.Authenticate.ConnectionReceiver;
import com.arraykart.b2b.Home.Fragments.CartFragment;
import com.arraykart.b2b.Home.Fragments.ScrollFragment;
import com.arraykart.b2b.Home.Fragments.WalletFragment;
import com.arraykart.b2b.Search.SearchActivity;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.arraykart.b2b.SignUp.SignUpActivity;
import com.bumptech.glide.Glide;
import com.arraykart.b2b.Home.Fragments.Account.AccountFragment;
import com.arraykart.b2b.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.NeumorphImageButton;

public class HomeActivity extends AppCompatActivity implements ConnectionReceiver.ReceiverListener {


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
    private LocationManager locationManager;
    private LocationListener locationListener;
    private com.google.android.gms.location.LocationRequest locationRequest;

    double lat, lon;
    private TextView pincode;

    //header and footer
    private NeumorphCardView footer;
    private ConstraintLayout header;

    //shared preferences
    private SharedPreferenceManager sharedPreferenceManager;

    //toggle animation
    private LinearLayout iconLL;
    private LottieAnimationView leavesAnimation;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        if(sharedPreferenceManager.getString("token") == null){
//            Intent i =
//        }

        //check netork connectivity
        checkConnection();
        //check token
        //java.lang.IllegalStateException: FragmentManager has been destroyed error
        //while calling following function
//        checkToken();
        //user location
        sharedPreferenceManager = new SharedPreferenceManager(this);
//        if(!sharedPreferenceManager.checkKey("GPS")){
        //set location
        pincode = findViewById(R.id.pincode);
            checkPermission();
//        }
//        getUserLocation();

        //for getContentResolver to work in wallet fragment
        contextOfApplication = getApplicationContext();


        //toggle animation on icon click
        toggleAnimation();


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
        pincode = findViewById(R.id.pincode);
        neumorphCardView = findViewById(R.id.neumorphCardView);
        editText = findViewById(R.id.search);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(i)    ;
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
                pincode.setVisibility(View.VISIBLE);
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
                pincode.setVisibility(View.GONE);
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
                pincode.setVisibility(View.GONE);
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
                pincode.setVisibility(View.GONE);
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.homeContainer, accountFragment).commit();
            }
            //return true as motion up will occur only after motion down
            return true;
        });

        //all categories from api
    }

    private void toggleAnimation() {
        iconLL = findViewById(R.id.iconLL);
        leavesAnimation = findViewById(R.id.leavesAnimation);
        iconLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leavesAnimation.setVisibility(View.VISIBLE);
                leavesAnimation.playAnimation();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        leavesAnimation.setVisibility(View.GONE);
                        leavesAnimation.pauseAnimation();
                    }
                },2000);
            }
        });
    }

    private void checkPermission(){
        locationRequest = com.google.android.gms.location.LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        //Log.e("loc", "version");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if(isGPSEnabled()){
                    LocationServices.getFusedLocationProviderClient(HomeActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
                                    LocationServices.getFusedLocationProviderClient(HomeActivity.this)
                                            .removeLocationUpdates(this);
                                    if(locationResult != null && locationResult.getLocations().size() > 0){
                                        int index = locationResult.getLocations().size() - 1 ;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();
//                                        sharedPreferenceManager.setString("GPS","gps");
                                        float[] results = new float[1];
                                        //Log.e("loc", ""+latitude);
                                        //Log.e("loc", ""+longitude);
                                        List<Address> addresses;
                                        Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
                                        try {
                                            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                            String address = addresses.get(0).getAddressLine(0);
                                            String city = addresses.get(0).getLocality();
                                            String state = addresses.get(0).getAdminArea();
                                            String country = addresses.get(0).getCountryName();
                                            String postalCode = addresses.get(0).getPostalCode();
                                            String knownName = addresses.get(0).getFeatureName();
                                            pincode.setText(String.valueOf(postalCode));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
//                                        Location.distanceBetween(latitude, longitude, 27.8899595, 78.0989536, results);
//                                        float distanceInMeters = results[0];
//                                        boolean isWithinRange = distanceInMeters < 30000;
//
//
//                                        if (isWithinRange) {
//
//                                        }else {
//                                            alert("we are not servicing in your area we will reach you soon");
//                                        }
                                    }
                                }
                            }, Looper.getMainLooper());
                }else {
                    turnOnGPS();
//                    sharedPreferenceManager.setString("GPS","gps");
                }
            }else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }
    }

    private void turnOnGPS() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
//                    Toast.makeText(HomeActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();
                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                                resolvableApiException.startResolutionForResult(
                                        HomeActivity.this,2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }


    private Boolean isGPSEnabled(){
        LocationManager locationManager = null;
        boolean isEnabled =false;
        if(locationManager == null){
            locationManager =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        }
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled ;

    }

    private void checkToken() {
        AuthorizeUser authorizeUser = new AuthorizeUser(this);
        if(!authorizeUser.isLoggedIn()){
            SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(this);
            sharedPreferenceManager.setString("token", null);
            Intent i = new Intent(this, SignUpActivity.class);
            finish();
            startActivity(i);
        }
    }

    private boolean checkConnection() {
        // initialize intent filter
        IntentFilter intentFilter = new IntentFilter();

        // add action
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");

        // register receiver
        registerReceiver(new ConnectionReceiver(), intentFilter);

        // Initialize listener
        ConnectionReceiver.Listener = this;

        // Initialize connectivity manager
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        // get connection status
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        // display snack bar
        showAlertDialogue(isConnected);
        return isConnected;
    }

    private void showAlertDialogue(boolean isConnected) {
        // check condition
        if (isConnected) {

        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("No Internet!");
            builder1.setCancelable(true);

            builder1.setPositiveButton("Ok",
                    (dialog, id) -> dialog.cancel());

//            builder1.setNegativeButton(
//                    "No",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        // call method
        checkConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // call method
        checkConnection();
    }
//
//    private void getUserLocation() {
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(@NonNull Location location) {
//                lat = location.getLatitude();
//                lon = location.getLongitude();
////                Toast.makeText(HomeActivity.this, "lan:"+String.valueOf(lat)+" lon:"+String.valueOf(lon), Toast.LENGTH_SHORT).show();
////                //Log.e("lat: ", String.valueOf(lat));
////                //Log.e("lon: ", String.valueOf(lon));
//                pincode.setText("lat"+lat);
//                final Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
//
//            }
//            @Override
//            public void onProviderEnabled(@NonNull String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(@NonNull String provider) {
//
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//        };
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//        != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }else {
//            locationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    checkPermission();

                }else {

                    turnOnGPS();
                }
            }
        }
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        showAlertDialogue(isConnected);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                checkPermission();
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}














