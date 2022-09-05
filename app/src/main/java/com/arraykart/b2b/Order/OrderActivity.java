package com.arraykart.b2b.Order;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.arraykart.b2b.Home.HomeActivity;
import com.arraykart.b2b.R;

public class OrderActivity extends AppCompatActivity {
    private TextView cancelOrderProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //to cancel order process and return to cart
        cancelOrderProcess = findViewById(R.id.cancelOrderProcess);
        cancelOrderProcess.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Cancel Order");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.setNegativeButton("Yes", (dialog, which) -> {
                dialog.dismiss();
                finish();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });

    }
}