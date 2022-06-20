package com.arraykart.b2b.Home.Fragments.Account;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class KycUploadFragment extends Fragment {
    private ImageView uploadPrev;
    private TextView upload;
    private ImageView uploadPrevDel;
    private static final int SELECT_CAMERA = 100;
    private static final int SELECT_PICTURE = 200;
    private static final int SELECT_FILE_MANAGER = 300;
    private LinearLayout camera;
    private LinearLayout gallery;
    private LinearLayout fileManager;
    private AlertDialog alertDialog;
    private String imageFilePath;
    private TextView gstHead;
    private TextView panHead;
    private LinearLayout gstLL;
    private LinearLayout panLL;
    private EditText docNum;
    private String docNumstr="";
    private Bundle bundle;
    private MultipartBody.Part body;
    private RequestBody type;
    private RequestBody num;
    private TextView submit;
    private ImageView sampleGstIv;
    private ImageView samplePanIv;
    private LinearLayout pdfPrevLL;
    private TextView pdfPrev;
    private ImageView pdfPrevDel;
    private SharedPreferenceManager sharedPreferenceManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_kyc_upload, container, false);
        if(isAdded()) {
            sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
        }
        checkToken();

        bundle = getArguments();
        gstHead = view.findViewById(R.id.gstHead);
        panHead = view.findViewById(R.id.panHead);
        gstLL = view.findViewById(R.id.gstLL);
        panLL = view.findViewById(R.id.panLL);
        docNum = view.findViewById(R.id.docNum);
        sampleGstIv = view.findViewById(R.id.sampleGstIv);
        samplePanIv = view.findViewById(R.id.samplePanIv);
        pdfPrevLL = view.findViewById(R.id.pdfPrevLL);
        pdfPrev = view.findViewById(R.id.pdfPrev);
        pdfPrevDel = view.findViewById(R.id.pdfPrevDel);
        uploadPrevDel = view.findViewById(R.id.uploadPrevDel);

        if(bundle.getString("proof").trim().toLowerCase().contains("gst")){
            gstHead.setVisibility(View.VISIBLE);
            gstLL.setVisibility(View.VISIBLE);
            sampleGstIv.setOnClickListener(v -> {
                if(isAdded()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    View dialogView = LayoutInflater.from(v.getContext())
                            .inflate(R.layout.sample_image_custom_alert_dialog, null);
                    ImageView prevImgAlDia = dialogView.findViewById(R.id.prevImgAlDia);
                    ImageView sampImgDis = dialogView.findViewById(R.id.sampImgDis);
                    builder.setView(dialogView);
                    alertDialog = builder.create();
                    sampImgDis.setOnClickListener(v1 -> alertDialog.dismiss());
                    prevImgAlDia.setImageResource(R.drawable.samplegst);
                    alertDialog.show();
                }
            });
        }else {
            panHead.setVisibility(View.VISIBLE);
            panLL.setVisibility(View.VISIBLE);
            samplePanIv.setOnClickListener(v -> {
                if(isAdded()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    View dialogView = LayoutInflater.from(v.getContext())
                            .inflate(R.layout.sample_image_custom_alert_dialog, null);
                    ImageView prevImgAlDia = dialogView.findViewById(R.id.prevImgAlDia);
                    ImageView sampImgDis = dialogView.findViewById(R.id.sampImgDis);
                    builder.setView(dialogView);
                    alertDialog = builder.create();
                    sampImgDis.setOnClickListener(v1 -> alertDialog.dismiss());
                    prevImgAlDia.setImageResource(R.drawable.samplepan);
                    alertDialog.show();
                }
            });
        }
        upload = view.findViewById(R.id.upload);
        uploadPrev = view.findViewById(R.id.uploadPrev);
        upload.setOnClickListener(v -> handleUpload(v));
        submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(v -> submitDocument());
        return view;
    }

    private void checkToken() {
        if(isAdded()) {
            AuthorizeUser authorizeUser = new AuthorizeUser(requireActivity());
            authorizeUser.isLoggedIn();
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
                Intent intent;
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

//                i.setType("application/pdf");
//                i.setAction(Intent.ACTION_GET_CONTENT);
//                 pass the constant to compare it
//                 with the returned requestCode
//                startActivityForResult(Intent.createChooser(i, "Select File"), SELECT_FILE_MANAGER);

            });
            alertDialog.show();
        }
    }

    // this function is triggered when user
    // selects the image from the imageChooser
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
                        String path = imageFilePath;
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        uploadPrev.setImageBitmap(bitmap);
                        upload.setVisibility(View.GONE);
                        uploadPrevDel.setVisibility(View.VISIBLE);
                        uploadPrevDel.setOnClickListener(v -> {
                            uploadPrev.setImageBitmap(null);
                            uploadPrev.setImageResource(R.drawable.ic_outline_upload_file_54_gray);
                            upload.setVisibility(View.VISIBLE);
                            uploadPrevDel.setVisibility(View.GONE);
                            body = null;
                        });
                        File file= new File(path);
                        RequestBody requestFile = RequestBody
                                .create(MediaType.parse("image/*"), file);

                        body = MultipartBody.Part
                                .createFormData("file", file.getName(), requestFile);
                        type = RequestBody.create(MediaType.parse("multipart/form-data"), bundle.getString("proof"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 200:
                if(resultCode == RESULT_OK){
                    requestMultiplePermissions();
                    Uri fileUri = data.getData();
                    if(isAdded()) {
                        Context context = requireActivity();
                        try {
                            String path = RealPathUtil.getRealPath(context, fileUri);
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            uploadPrev.setImageBitmap(bitmap);
                            upload.setVisibility(View.GONE);
                            uploadPrevDel.setVisibility(View.VISIBLE);
                            uploadPrevDel.setOnClickListener(v -> {
                                uploadPrev.setImageBitmap(null);
                                uploadPrev.setImageResource(R.drawable.ic_outline_upload_file_54_gray);
                                upload.setVisibility(View.VISIBLE);
                                uploadPrevDel.setVisibility(View.GONE);
                                body = null;
                            });
                            File file = new File(path);
                            RequestBody requestFile = RequestBody
                                    .create(MediaType.parse("image/*"), file);

                            body = MultipartBody.Part
                                    .createFormData("file", file.getName(), requestFile);
                            type = RequestBody.create(MediaType.parse("multipart/form-data"), bundle.getString("proof"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case 300:
                if(resultCode == RESULT_OK){
                    requestMultiplePermissions();
                    Uri fileUri = data.getData();
                    if(isAdded()) {
                        Context context = requireActivity();
                        try {
                            String path = getStringPdf(context, fileUri);
//                            Bitmap bitmap = BitmapFactory.decodeFile(path);
//                            uploadPrev.setImageBitmap(bitmap);
                            File file = new File(path);
                            upload.setVisibility(View.GONE);
                            uploadPrev.setVisibility(View.GONE);
                            pdfPrevLL.setVisibility(View.VISIBLE);
                            pdfPrev.setText(file.getName());
                            pdfPrevDel.setOnClickListener(v -> {
                                pdfPrev.setText(null);
                                upload.setVisibility(View.VISIBLE);
                                uploadPrev.setVisibility(View.VISIBLE);
                                pdfPrevLL.setVisibility(View.GONE);
                                body = null;
                            });
                            RequestBody requestFile = RequestBody
                                    .create(MediaType.parse("application/pdf"), file);
                            body = MultipartBody.Part
                                    .createFormData("file", file.getName(), requestFile);
                            type = RequestBody.create(MediaType.parse("multipart/form-data"), bundle.getString("proof"));
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

    private void submitDocument(){
        if(isAdded()){
            docNumstr = String.valueOf(docNum.getText());
            num = RequestBody.create(MediaType.parse("multipart/form-data"), docNumstr);

            if(body==null|| docNumstr==null || num == null || docNumstr.length()<=0){
                if(isAdded()) {
                    Toast.makeText(requireActivity(), "Please fill out all the fields!", Toast.LENGTH_LONG).show();
                }
                return;
            }
            Call<SuccessMessage> call = RetrofitClient.getClient()
                    .getApi().setKyc(
                            sharedPreferenceManager.getString("token"),
                            body,
                            type,
                            num
                    );
            call.enqueue(new Callback<SuccessMessage>() {
                @Override
                public void onResponse(@NonNull Call<SuccessMessage> call, @NonNull Response<SuccessMessage> response) {
                    if(!response.isSuccessful()){
                        if(isAdded()) {
                            Toast.makeText(requireActivity(), "" + response.code(), Toast.LENGTH_SHORT).show();
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
                    if (isAdded()) {
                        sharedPreferenceManager.setString("kycstatus", "BP");//BP -> Business Proof Completed
                        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager()
                                        .beginTransaction();
                        Fragment fragment = new LicenseFragment();
//                        FragmentManager fm = requireActivity().getSupportFragmentManager();
//                        //removing business proof fragment from stack
//                        fm.popBackStack();
//                        fm.popBackStack();
                        fragmentTransaction.addToBackStack("kycupload");
                        fragmentTransaction.replace(R.id.accountContainer, fragment).commit();
                        Toast.makeText(requireActivity(), "Step1 completed successfully!", Toast.LENGTH_SHORT).show();
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
}