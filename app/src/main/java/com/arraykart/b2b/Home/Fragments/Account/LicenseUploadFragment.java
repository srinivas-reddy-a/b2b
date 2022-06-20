package com.arraykart.b2b.Home.Fragments.Account;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.BuildConfig;
import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.SuccessMessage;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LicenseUploadFragment extends Fragment {
    private LinearLayout pesticideLL;
    private LinearLayout fertilizerLL;
    private LinearLayout seedsLL;
    private Bundle bundle;
    private ConstraintLayout pUpload;
    private ConstraintLayout fUpload;
    private ConstraintLayout sUpload;
    private ImageView uploadPrevPest;
    private ImageView uploadPrevFret;
    private ImageView uploadPrevSeed;
    private RelativeLayout imgRLPest;
    private RelativeLayout uploadRLPest;
    private RelativeLayout imgRLFert;
    private RelativeLayout uploadRLFert;
    private RelativeLayout imgRLSeed;
    private RelativeLayout uploadRLSeed;
    private LinearLayout delPestPrev;
    private LinearLayout delFertPrev;
    private LinearLayout delSeedPrev;
    private LinearLayout pdfPrevPestLL;
    private TextView pdfPrevPest;
    private ImageView pdfPrevPestDel;
    private LinearLayout pdfPrevFertLL;
    private TextView pdfPrevFert;
    private ImageView pdfPrevFertDel;
    private LinearLayout pdfPrevSeedLL;
    private TextView pdfPrevSeed;
    private ImageView pdfPrevSeedDel;
    private TextView pestDate;
    private TextView fertDate;
    private TextView seedDate;
    private String pestDateStr;
    private String fertDateStr;
    private String seedDateStr;
    private Boolean pestUpClick = false;
    private Boolean fertUpClick = false;
    private Boolean seedUpClick = false;
    private Boolean flag = true;

    private TextView finish;


    private LinearLayout camera;
    private LinearLayout gallery;
    private LinearLayout fileManager;
    private AlertDialog alertDialog;
    private String imageFilePath;
    private static final int SELECT_CAMERA = 100;
    private static final int SELECT_PICTURE = 200;
    private static final int SELECT_FILE_MANAGER = 300;

    private MultipartBody.Part bodyPest;
    private RequestBody typePest;
    private RequestBody datePest;
    private MultipartBody.Part bodyFert;
    private RequestBody typeFert;
    private RequestBody dateFert;
    private MultipartBody.Part bodySeed;
    private RequestBody typeSeed;
    private RequestBody dateSeed;
    private Uri fileUriPest;
    private String pathPest;
    private File filePest;
    private RequestBody requestFilePest;
    private Uri fileUriFert;
    private String pathFert;
    private File fileFert;
    private RequestBody requestFileFert;
    private Uri fileUriSeed;
    private String pathSeed;
    private File fileSeed;
    private RequestBody requestFileSeed;

    private CalendarView calendarView;
    private SharedPreferenceManager sharedPreferenceManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_license_upload, container, false);
        if(isAdded()) {
            sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
        }

        checkToken();

        pesticideLL = view.findViewById(R.id.pesticideLL);
        fertilizerLL = view.findViewById(R.id.fertilizerLL);
        seedsLL = view.findViewById(R.id.seedsLL);
        bundle = this.getArguments();
        setLL(view);
//        submit data
        finish = view.findViewById(R.id.finish);
        finish.setOnClickListener(v -> {
            flag = true;
            if(bundle.getString("fert").trim().toLowerCase().contains("true")){
                if(bodyFert==null
                        && fertDateStr ==null
                        && dateFert==null
                ){
                    if(isAdded()) {
                        Toast.makeText(requireActivity(), "Please fill out all the fields!", Toast.LENGTH_LONG).show();
                    }
                    flag = false;
                    return;
                }
            }
            if(bundle.getString("seed").trim().toLowerCase().contains("true")){
                if(bodySeed == null
                        && seedDateStr == null
                        && dateSeed == null
                ){
                    if(isAdded()) {
                        Toast.makeText(requireActivity(), "Please fill out all the fields!", Toast.LENGTH_LONG).show();
                    }
                    flag = false;
                    return;

                }
            }
            if(bundle.getString("pest").trim().toLowerCase().contains("true")){
                if(bodyPest==null
                        && pestDateStr ==null
                        && datePest==null
                ){
                    if(isAdded()) {
                        Toast.makeText(requireActivity(), "Please fill out all the fields!", Toast.LENGTH_LONG).show();
                    }
                    flag = false;
                    return;
                }
            }
            if(flag) {
                setData();
            }
        });

        return view;
    }

    private void setData() {
        if(bundle.getString("pest").trim().toLowerCase().contains("true")){
            if(bodyPest!=null
                    && pestDateStr!=null
                    && !pestDateStr.isEmpty()
                    && datePest != null
            ){
                if(bundle.getString("fert").trim().toLowerCase().contains("true")){
                    if(bodyFert==null
                            && fertDateStr ==null
                            && dateFert==null
                    ){
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "Please fill out all the fields!", Toast.LENGTH_LONG).show();
                        }
                        return;
                    }
                }
                if(bundle.getString("seed").trim().toLowerCase().contains("true")){
                    if(bodySeed == null
                            && seedDateStr == null
                            && dateSeed == null
                    ){
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "Please fill out all the fields!", Toast.LENGTH_LONG).show();
                        }
                        return;

                    }
                }
                if(isAdded()) {
                    Call<SuccessMessage> call = RetrofitClient.getClient()
                            .getApi().setLicense(sharedPreferenceManager.getString("token"),
                                    bodyPest, typePest, datePest);
                    call.enqueue(new Callback<SuccessMessage>() {
                        @Override
                        public void onResponse(@NonNull Call<SuccessMessage> call, @NonNull Response<SuccessMessage> response) {
                            if(!response.isSuccessful()){
                                if(isAdded()) {
                                    Toast.makeText(requireActivity(), "" + response.code()+response.message(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            assert response.body() != null;
                            if(!response.body().getSuccess()){
                                if(isAdded()) {
                                    Toast.makeText(requireActivity(), "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            if(isAdded()){
                                Toast.makeText(requireActivity(),
                                        "Kyc completed, We will get back to you soon!",
                                        Toast.LENGTH_SHORT).show();
                                if(sharedPreferenceManager.checkKey("kycstatus")){
                                    sharedPreferenceManager.setString("kycstatus", "IR");
                                }
                                requireActivity().finish();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<SuccessMessage> call, @NonNull Throwable t) {
                            if(isAdded()) {
                                Toast.makeText(requireActivity(), "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }else{
                if(isAdded()) {
                    Toast.makeText(requireActivity(), "Please fill out all the fields!", Toast.LENGTH_LONG).show();
                }
            }
        }
        if(bundle.getString("fert").trim().toLowerCase().contains("true")){
            if(bodyFert!=null
                    && fertDateStr!=null
                    && !fertDateStr.isEmpty()
                    && dateFert!=null
            ){
                if(bundle.getString("pest").trim().toLowerCase().contains("true")){
                    if(bodyPest==null
                            && pestDateStr ==null
                            && datePest==null
                    ){
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "Please fill out all the fields!", Toast.LENGTH_LONG).show();
                        }
                        return;
                    }
                }
                if(bundle.getString("seed").trim().toLowerCase().contains("true")){
                    if(bodySeed == null
                            && seedDateStr == null
                            && dateSeed == null
                    ){
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "Please fill out all the fields!", Toast.LENGTH_LONG).show();
                        }

                        return;
                    }
                }
                if(isAdded()) {
                    Call<SuccessMessage> call = RetrofitClient.getClient()
                            .getApi().setLicense(sharedPreferenceManager.getString("token"),
                                    bodyFert, typeFert, dateFert);
                    call.enqueue(new Callback<SuccessMessage>() {
                        @Override
                        public void onResponse(@NonNull Call<SuccessMessage> call, @NonNull Response<SuccessMessage> response) {
                            if(!response.isSuccessful()){
                                if(isAdded()) {
                                    Toast.makeText(requireActivity(), "" + response.code()+response.message(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            assert response.body() != null;
                            if(!response.body().getSuccess()){
                                if(isAdded()) {
                                    Toast.makeText(requireActivity(), "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            if(isAdded()){
                                Toast.makeText(requireActivity(),
                                        "Kyc completed, We will get back to you soon!",
                                        Toast.LENGTH_SHORT).show();
                                if(sharedPreferenceManager.checkKey("kycstatus")){
                                    sharedPreferenceManager.setString("kycstatus", "IR");
                                }
                                requireActivity().finish();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<SuccessMessage> call, @NonNull Throwable t) {
                            if(isAdded()) {
                                Toast.makeText(requireActivity(), "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }else{
                if(isAdded()) {
                    Toast.makeText(requireActivity(), "Please fill out all the fields!", Toast.LENGTH_LONG).show();
                }
            }
        }
        if(bundle.getString("seed").trim().toLowerCase().contains("true")){
            if(bodySeed!=null
                    && seedDateStr!=null
                    && dateSeed!= null
            ){
                if(bundle.getString("pest").trim().toLowerCase().contains("true")){
                    if(bodyPest==null
                            && pestDateStr ==null
                            && datePest==null
                    ){
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "Please fill out all the fields!", Toast.LENGTH_LONG).show();
                        }
                        return;
                    }
                }
                if(bundle.getString("fert").trim().toLowerCase().contains("true")){
                    if(bodyFert == null
                            && fertDateStr == null
                    ) {
                        assert fertDateStr != null;
                        if (fertDateStr.isEmpty()
                                && dateFert == null) {
                            if (isAdded()) {
                                Toast.makeText(requireActivity(), "Please fill out all the fields!", Toast.LENGTH_LONG).show();
                            }
                            return;
                        }
                    }
                }
                if(isAdded()) {
                    Call<SuccessMessage> call = RetrofitClient.getClient()
                            .getApi().setLicense(sharedPreferenceManager.getString("token"),
                                    bodySeed, typeSeed, dateSeed);
                    call.enqueue(new Callback<SuccessMessage>() {
                        @Override
                        public void onResponse(@NonNull Call<SuccessMessage> call, @NonNull Response<SuccessMessage> response) {
                            if(!response.isSuccessful()){
                                if(isAdded()) {
                                    Toast.makeText(requireActivity(), "" + response.code()+response.message(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            assert response.body() != null;
                            if(!response.body().getSuccess()){
                                if(isAdded()) {
                                    Toast.makeText(requireActivity(), "500" + "Internal Server Error", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            if(isAdded()){
                                Toast.makeText(requireActivity(),
                                        "Kyc completed, We will get back to you soon!",
                                        Toast.LENGTH_SHORT).show();
                                if(sharedPreferenceManager.checkKey("kycstatus")){
                                    sharedPreferenceManager.setString("kycstatus", "IR");
                                }
                                requireActivity().finish();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<SuccessMessage> call, @NonNull Throwable t) {
                            if(isAdded()) {
                                Toast.makeText(requireActivity(), "Please check your internet connection or try again after sometime", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }else{
                if(isAdded()) {
                    Toast.makeText(requireActivity(), "Please fill out all the fields!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void setLL(View view) {
        if(bundle.getString("pest").trim().toLowerCase().contains("true")){
            pesticideLL.setVisibility(View.VISIBLE);
            uploadPrevPest = view.findViewById(R.id.addCLPrev);
            imgRLPest = view.findViewById(R.id.imgRLPest);
            uploadRLPest = view.findViewById(R.id.uploadRLPest);
            delPestPrev = view.findViewById(R.id.delPestPrev);
            pestDate = view.findViewById(R.id.pestDate);
            pdfPrevPestLL = view.findViewById(R.id.pdfPrevPestLL);
            pdfPrevPest = view.findViewById(R.id.pdfPrevPest);
            pdfPrevPestDel = view.findViewById(R.id.pdfPrevPestDel);
            getPestData(view);
        }else{
            pesticideLL.setVisibility(View.GONE);
        }
        if(bundle.getString("fert").trim().toLowerCase().contains("true")){
            fertilizerLL.setVisibility(View.VISIBLE);
            uploadPrevFret = view.findViewById(R.id.addCLFerPrev);
            imgRLFert = view.findViewById(R.id.imgRLFert);
            uploadRLFert = view.findViewById(R.id.uploadRLFert);
            delFertPrev = view.findViewById(R.id.delFertPrev);
            fertDate = view.findViewById(R.id.fertDate);
            pdfPrevFertLL = view.findViewById(R.id.pdfPrevFertLL);
            pdfPrevFert = view.findViewById(R.id.pdfPrevFert);
            pdfPrevFertDel = view.findViewById(R.id.pdfPrevFertDel);
            getFertData(view);
        }else{
            fertilizerLL.setVisibility(View.GONE);
        }
        if(bundle.getString("seed").trim().toLowerCase().contains("true")){
            seedsLL.setVisibility(View.VISIBLE);
            uploadPrevSeed = view.findViewById(R.id.addCLSePrev);
            imgRLSeed = view.findViewById(R.id.imgRLSeed);
            uploadRLSeed = view.findViewById(R.id.uploadRLSeed);
            delSeedPrev = view.findViewById(R.id.delSeedPrev);
            seedDate = view.findViewById(R.id.seedDate);
            pdfPrevSeedLL = view.findViewById(R.id.pdfPrevSeedLL);
            pdfPrevSeed = view.findViewById(R.id.pdfPrevSeed);
            pdfPrevSeedDel = view.findViewById(R.id.pdfPrevSeedDel);
            getSeedData(view);
        }else{
            seedsLL.setVisibility(View.GONE);
        }
    }

    private void getSeedData(View view) {
        sUpload = view.findViewById(R.id.sUpload);
        seedDate.setOnClickListener(v -> {
            if(isAdded()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
//            ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_date_alert_dialog, null);
                calendarView = dialogView.findViewById(R.id.calendarView);
                Spinner yearSpinner = dialogView.findViewById(R.id.yearSpinner);
                Calendar calendar = Calendar.getInstance();
                String[] years = new String[100];
                for(int i=0; i<50; i++){
                    years[i] = String.valueOf(calendar.get(Calendar.YEAR)+i);
                }
                ArrayAdapter adapter = new ArrayAdapter(requireActivity(), R.layout.spinner_year_text_view_single_item, R.id.yearSpinnerText, years);
                builder.setView(dialogView);
                alertDialog = builder.create();
                yearSpinner.setAdapter(adapter);
                yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        calendar.set(Integer.parseInt(years[position]), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                        long Date = calendar.getTime().getTime();
                        calendarView.setDate(Date);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
                    seedDateStr = dayOfMonth + "/" + month + "/" + year;
                    seedDate.setText(dayOfMonth + " / " + month + " / " + year);
                    dateSeed = RequestBody.create(MediaType.parse("multipart/form-data"), seedDateStr);
                    alertDialog.dismiss();
                });
                alertDialog.show();
            }
        });
        sUpload.setOnClickListener(v -> {
            seedUpClick = true;
            fertUpClick = false;
            pestUpClick = false;
            handleUpload(v);
        });
    }

    private void getFertData(View view) {
        fUpload = view.findViewById(R.id.fUpload);
        fertDate.setOnClickListener(v -> {
            if(isAdded()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
//            ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_date_alert_dialog, null);
                calendarView = dialogView.findViewById(R.id.calendarView);
                Spinner yearSpinner = dialogView.findViewById(R.id.yearSpinner);
                Calendar calendar = Calendar.getInstance();
                String[] years = new String[100];
                for(int i=0; i<100; i++){
                    years[i] = String.valueOf(calendar.get(Calendar.YEAR)+i);
                }
                ArrayAdapter adapter = new ArrayAdapter(requireActivity(), R.layout.spinner_year_text_view_single_item, R.id.yearSpinnerText, years);
                builder.setView(dialogView);
                alertDialog = builder.create();
                yearSpinner.setAdapter(adapter);
                yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        calendar.set(Integer.parseInt(years[position]), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                        long Date = calendar.getTime().getTime();
                        calendarView.setDate(Date);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
                    fertDateStr = dayOfMonth + "/" + month + "/" + year;
                    fertDate.setText(dayOfMonth + " / " + month + " / " + year);
                    dateFert= RequestBody.create(MediaType.parse("multipart/form-data"), fertDateStr);
                    alertDialog.dismiss();
                });
                alertDialog.show();
            }
        });
        fUpload.setOnClickListener(v -> {
            fertUpClick = true;
            pestUpClick = false;
            seedUpClick = false;
            handleUpload(v);
        });
    }

    private void getPestData(View view) {
        pUpload = view.findViewById(R.id.pUpload);
        pestDate.setOnClickListener(v -> {
            if(isAdded()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
//            ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_date_alert_dialog, null);
                calendarView = dialogView.findViewById(R.id.calendarView);
                Spinner yearSpinner = dialogView.findViewById(R.id.yearSpinner);
                Calendar calendar = Calendar.getInstance();
                String[] years = new String[100];
                for(int i=0; i<100; i++){
                    years[i] = String.valueOf(calendar.get(Calendar.YEAR)+i);
                }
                ArrayAdapter adapter = new ArrayAdapter(requireActivity(), R.layout.spinner_year_text_view_single_item, R.id.yearSpinnerText, years);
                builder.setView(dialogView);
                alertDialog = builder.create();
                yearSpinner.setAdapter(adapter);
                yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        calendar.set(Integer.parseInt(years[position]), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                        long Date = calendar.getTime().getTime();
                        calendarView.setDate(Date);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
                    pestDateStr = dayOfMonth + "/" + month + "/" + year;
                    pestDate.setText(dayOfMonth + " / " + month + " / " + year);
                    datePest = RequestBody.create(MediaType.parse("multipart/form-data"), pestDateStr);
                    alertDialog.dismiss();
                });
                alertDialog.show();
            }
        });
        pUpload.setOnClickListener(v -> {
            pestUpClick = true;
            fertUpClick = false;
            seedUpClick = false;
            handleUpload(v);
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 100:
                if(resultCode == RESULT_OK){
                    requestMultiplePermissions();
//                    Uri fileUri = data.getData();
                    //don't compare the data to null,
                    // it will always come as  null because we are providing a file URI,
                    // so load with the imageFilePath we obtained before opening the cameraIntent
                    try {
                        if(pestUpClick){
                            pathPest = imageFilePath;
                            Bitmap bitmap = BitmapFactory.decodeFile(pathPest);
                            uploadPrevPest.setImageBitmap(bitmap);
                            imgRLPest.setVisibility(View.VISIBLE);
                            uploadRLPest.setVisibility(View.GONE);
                            delPestPrev.setOnClickListener(v -> {
                                uploadPrevPest.setImageBitmap(null);
                                imgRLPest.setVisibility(View.GONE);
                                uploadRLPest.setVisibility(View.VISIBLE);
                                bodyPest = null;
                            });
                            filePest= new File(pathPest);
                            requestFilePest = RequestBody
                                    .create(MediaType.parse("image/*"), filePest);

                            bodyPest = MultipartBody.Part
                                    .createFormData("file", filePest.getName(), requestFilePest);
                            typePest = RequestBody.create(MediaType.parse("multipart/form-data"), "pest");
                        }
                        if(fertUpClick){
                            pathFert = imageFilePath;
                            Bitmap bitmap = BitmapFactory.decodeFile(pathFert);
                            uploadPrevFret.setImageBitmap(bitmap);
                            imgRLFert.setVisibility(View.VISIBLE);
                            uploadRLFert.setVisibility(View.GONE);
                            delFertPrev.setOnClickListener(v -> {
                                uploadPrevFret.setImageBitmap(null);
                                imgRLFert.setVisibility(View.GONE);
                                uploadRLFert.setVisibility(View.VISIBLE);
                                bodyFert = null;
                            });
                            fileFert= new File(pathFert);
                            requestFileFert = RequestBody
                                    .create(MediaType.parse("image/*"), fileFert);

                            bodyFert = MultipartBody.Part
                                    .createFormData("file", fileFert.getName(), requestFileFert);
                            typeFert = RequestBody.create(MediaType.parse("multipart/form-data"), "fert");
                        }
                        if(seedUpClick){
                            pathSeed = imageFilePath;
                            Bitmap bitmap = BitmapFactory.decodeFile(pathSeed);
                            uploadPrevSeed.setImageBitmap(bitmap);
                            imgRLSeed.setVisibility(View.VISIBLE);
                            uploadRLSeed.setVisibility(View.GONE);
                            delSeedPrev.setOnClickListener(v -> {
                                uploadPrevSeed.setImageBitmap(null);
                                imgRLSeed.setVisibility(View.GONE);
                                uploadRLSeed.setVisibility(View.VISIBLE);
                                bodySeed = null;
                            });
                            fileSeed= new File(pathSeed);
                            requestFileSeed = RequestBody
                                    .create(MediaType.parse("image/*"), fileSeed);

                            bodySeed = MultipartBody.Part
                                    .createFormData("file", fileSeed.getName(), requestFileSeed);
                            typeSeed = RequestBody.create(MediaType.parse("multipart/form-data"), "seed");

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 200:
                if(resultCode == RESULT_OK){
                    requestMultiplePermissions();
                    if(isAdded()) {
                        Context context = requireActivity();
                        try {

                            if (pestUpClick) {
                                fileUriPest = data.getData();
                                pathPest = RealPathUtil.getRealPath(context, fileUriPest);
                                Bitmap bitmap = BitmapFactory.decodeFile(pathPest);
                                uploadPrevPest.setImageBitmap(bitmap);
                                imgRLPest.setVisibility(View.VISIBLE);
                                uploadRLPest.setVisibility(View.GONE);
                                delPestPrev.setOnClickListener(v -> {
                                    uploadPrevPest.setImageBitmap(null);
                                    imgRLPest.setVisibility(View.GONE);
                                    uploadRLPest.setVisibility(View.VISIBLE);
                                    bodyPest = null;
                                });
                                filePest= new File(pathPest);
                                requestFilePest = RequestBody
                                        .create(MediaType.parse("image/*"), filePest);

                                bodyPest = MultipartBody.Part
                                        .createFormData("file", filePest.getName(), requestFilePest);
                                typePest = RequestBody.create(MediaType.parse("multipart/form-data"), "pest");
                            }
                            if (fertUpClick) {
                                fileUriFert = data.getData();
                                pathFert = RealPathUtil.getRealPath(context, fileUriFert);
                                Bitmap bitmap = BitmapFactory.decodeFile(pathFert);
                                uploadPrevFret.setImageBitmap(bitmap);
                                imgRLFert.setVisibility(View.VISIBLE);
                                uploadRLFert.setVisibility(View.GONE);
                                delFertPrev.setOnClickListener(v -> {
                                    uploadPrevFret.setImageBitmap(null);
                                    imgRLFert.setVisibility(View.GONE);
                                    uploadRLFert.setVisibility(View.VISIBLE);
                                    bodyFert = null;
                                });
                                fileFert = new File(pathFert);
                                requestFileFert = RequestBody
                                        .create(MediaType.parse("image/*"), fileFert);

                                bodyFert = MultipartBody.Part
                                        .createFormData("file", fileFert.getName(), requestFileFert);
                                typeFert = RequestBody.create(MediaType.parse("multipart/form-data"), "fert");
                            }
                            if (seedUpClick) {
                                fileUriSeed = data.getData();
                                pathSeed = RealPathUtil.getRealPath(context, fileUriSeed);
                                Bitmap bitmap = BitmapFactory.decodeFile(pathSeed);
                                uploadPrevSeed.setImageBitmap(bitmap);
                                imgRLSeed.setVisibility(View.VISIBLE);
                                uploadRLSeed.setVisibility(View.GONE);
                                delSeedPrev.setOnClickListener(v -> {
                                    uploadPrevSeed.setImageBitmap(null);
                                    imgRLSeed.setVisibility(View.GONE);
                                    uploadRLSeed.setVisibility(View.VISIBLE);
                                    bodySeed = null;
                                });
                                fileSeed = new File(pathSeed);
                                requestFileSeed = RequestBody
                                        .create(MediaType.parse("image/*"), fileSeed);

                                bodySeed = MultipartBody.Part
                                        .createFormData("file", fileSeed.getName(), requestFileSeed);
                                typeSeed = RequestBody.create(MediaType.parse("multipart/form-data"), "seed");

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case 300:
                if(resultCode == RESULT_OK){
                    requestMultiplePermissions();

                    if(isAdded()) {
                        Context context = requireActivity();
                        try {
//                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            if (pestUpClick) {
                                Log.e("pest", "allo");
                                fileUriPest = data.getData();
                                pathPest = getStringPdf(context, fileUriPest);
//                                uploadPrevPest.setImageBitmap(bitmap);
//                                imgRLPest.setVisibility(View.VISIBLE);
//                                uploadRLPest.setVisibility(View.GONE);
//                                delPestPrev.setOnClickListener(v -> {
//                                    uploadPrevPest.setImageBitmap(null);
//                                    imgRLPest.setVisibility(View.GONE);
//                                    uploadRLPest.setVisibility(View.VISIBLE);
//                                });
                                filePest = new File(pathPest);
                                uploadRLPest.setVisibility(View.GONE);
                                pdfPrevPestLL.setVisibility(View.VISIBLE);
                                pdfPrevPest.setText(filePest.getName());
                                pdfPrevPestDel.setOnClickListener(v -> {
                                    uploadRLPest.setVisibility(View.VISIBLE);
                                    pdfPrevPestLL.setVisibility(View.GONE);
                                    pdfPrevPest.setText(null);
                                    bodyPest = null;
                                });
                                requestFilePest = RequestBody
                                        .create(MediaType.parse("application/pdf"), filePest);

                                bodyPest = MultipartBody.Part
                                        .createFormData("file", filePest.getName(), requestFilePest);
                                typePest = RequestBody.create(MediaType.parse("multipart/form-data"), "pest");
                            }
                            if (fertUpClick) {
                                fileUriFert = data.getData();
                                pathFert = getStringPdf(context, fileUriFert);

//                                uploadPrevFret.setImageBitmap(bitmap);
//                                imgRLFert.setVisibility(View.VISIBLE);
//                                uploadRLFert.setVisibility(View.GONE);
//                                delFertPrev.setOnClickListener(v -> {
//                                    uploadPrevFret.setImageBitmap(null);
//                                    imgRLFert.setVisibility(View.GONE);
//                                    uploadRLFert.setVisibility(View.VISIBLE);
//                                });
                                fileFert = new File(pathFert);
                                uploadRLFert.setVisibility(View.GONE);
                                pdfPrevFertLL.setVisibility(View.VISIBLE);
                                pdfPrevFert.setText(fileFert.getName());
                                pdfPrevFertDel.setOnClickListener(v -> {
                                    uploadRLFert.setVisibility(View.VISIBLE);
                                    pdfPrevFertLL.setVisibility(View.GONE);
                                    pdfPrevFert.setText(null);
                                    bodyFert = null;
                                });
                                requestFileFert = RequestBody
                                        .create(MediaType.parse("application/pdf"), fileFert);

                                bodyFert = MultipartBody.Part
                                        .createFormData("file", fileFert.getName(), requestFileFert);
                                typeFert = RequestBody.create(MediaType.parse("multipart/form-data"), "fert");
                            }
                            if (seedUpClick) {
                                fileUriSeed = data.getData();
                                pathSeed = getStringPdf(context, fileUriSeed);
//                                uploadPrevSeed.setImageBitmap(bitmap);
//                                imgRLSeed.setVisibility(View.VISIBLE);
//                                uploadRLSeed.setVisibility(View.GONE);
//                                delSeedPrev.setOnClickListener(v -> {
//                                    uploadPrevSeed.setImageBitmap(null);
//                                    imgRLSeed.setVisibility(View.GONE);
//                                    uploadRLSeed.setVisibility(View.VISIBLE);
//                                });
                                fileSeed = new File(pathSeed);
                                uploadRLSeed.setVisibility(View.GONE);
                                pdfPrevSeedLL.setVisibility(View.VISIBLE);
                                pdfPrevSeed.setText(fileSeed.getName());
                                pdfPrevSeedDel.setOnClickListener(v -> {
                                    uploadRLSeed.setVisibility(View.VISIBLE);
                                    pdfPrevSeedLL.setVisibility(View.GONE);
                                    pdfPrevSeed.setText(null);
                                    bodySeed = null;
                                });
                                requestFileSeed = RequestBody
                                        .create(MediaType.parse("application/pdf"), fileSeed);

                                bodySeed = MultipartBody.Part
                                        .createFormData("file", fileSeed.getName(), requestFileSeed);
                                typeSeed = RequestBody.create(MediaType.parse("multipart/form-data"), "seed");

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    public String getStringPdf (Context context, Uri uri){
        {

            final String id = DocumentsContract.getDocumentId(uri);

            if (id != null && id.startsWith("raw:")) {
                return id.substring(4);
            }

            String[] contentUriPrefixesToTry = new String[]{
                    "content://downloads/public_downloads",
                    "content://downloads/my_downloads",
                    "content://downloads/all_downloads",
            };

            for (String contentUriPrefix : contentUriPrefixesToTry) {
                try {
                    assert id != null;
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    String path = FileUtils.getDataColumn(context, contentUri, null, null);
                    if (path != null) {
                        return path;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
            String fileName = FileUtils.getFileName(context, uri);
            File cacheDir = FileUtils.getDocumentCacheDir(context);
            File file = FileUtils.generateFileName(fileName, cacheDir);
            String destinationPath = null;
            if (file != null) {
                destinationPath = file.getAbsolutePath();
                FileUtils.saveFileFromUri(context, uri, destinationPath);
            }
            return destinationPath;
        }
    }

    private void  requestMultiplePermissions(){
        if(isAdded()) {
            Dexter.withActivity(requireActivity())
                    .withPermissions(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
//                            Toast.makeText(requireActivity(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings

                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).
                    withErrorListener(error -> {
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "Some Error! ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .onSameThread()
                    .check();
        }
    }

    private void handleUpload(View v) {
        if(isAdded()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
//            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.file_custom_alert_dialog, null);
            camera = dialogView.findViewById(R.id.cameraLL);
            gallery = dialogView.findViewById(R.id.galleryLL);
            fileManager = dialogView.findViewById(R.id.fileLL);
            builder.setView(dialogView);
            alertDialog = builder.create();
            camera.setOnClickListener(v1 -> {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(isAdded()) {
                    if (takePicture.resolveActivity(requireActivity().getPackageManager()) != null) {
                        //Create a file to store the image
                        File photoFile = null;
                        try {
                            if(isAdded()) {
                                photoFile = createImageFile();
                            }
                        } catch (IOException e) {
                            // Error occurred while creating the File
                            e.printStackTrace();
                        }
                        if (photoFile != null) {
                            if(isAdded()) {
                                Uri photoURI = FileProvider.getUriForFile(requireActivity(),
                                        BuildConfig.APPLICATION_ID + ".provider", photoFile);
                                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePicture, SELECT_CAMERA);
                            }
                        }
                    }
                }
                alertDialog.dismiss();
            });
            gallery.setOnClickListener(v1 -> {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, SELECT_PICTURE);
                alertDialog.dismiss();
            });
            fileManager.setOnClickListener(v1 -> {
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(intent, SELECT_FILE_MANAGER);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("application/pdf");
                    startActivityForResult(intent, SELECT_FILE_MANAGER);
                }
                alertDialog.dismiss();
            });
            alertDialog.show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void checkToken() {
        if(isAdded()) {
            AuthorizeUser authorizeUser = new AuthorizeUser(requireActivity());
            authorizeUser.isLoggedIn();
        }
    }
}