package com.arraykart.b2b.Loading;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.graphics.Color;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.arraykart.b2b.R;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog alertDialog;
    private LottieAnimationView loadingAnimation;
    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }
    public void startLoadingDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(
                activity, R.style.myFullscreenAlertDialogStyle);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.custom_loading, null));
//        false to restrict user from closing the dialog
        builder.setCancelable(false);
        alertDialog = builder.create();
//        to make alert dialog full screen
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.getWindow().setAttributes(lp);
        return;
    }
    public void dismissLoadingDialog(){
        alertDialog.dismiss();
        return;
    }
}
