package com.arraykart.b2b.Home.Fragments.Account;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraykart.b2b.R;


public class TutorialsFragment extends Fragment {

    private ImageView ytOrder;
    private ImageView ytKyc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutorials, container, false);

        ytOrder = view.findViewById(R.id.ytOrder);
        ytOrder.setOnClickListener(v -> {
            if(isAdded()){
                watchYoutubeVideo(requireContext(), "Ol58e792jNg");
            }
        });

        ytKyc = view.findViewById(R.id.ytKyc);
        ytKyc.setOnClickListener(v -> {
            if(isAdded()){
                watchYoutubeVideo(requireContext(), "VHoM2_ecJ0k");
            }
        });

        return view;
    }
    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}