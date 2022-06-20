package com.arraykart.b2b.Home.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.Home.HomeActivity;
import com.arraykart.b2b.R;


public class WalletFragment extends Fragment {
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        checkToken();

//        ActivityCompat.requestPermissions(requireActivity(), new String[]
//                {Manifest.permission.READ_SMS},
//                PackageManager.PERMISSION_GRANTED);
//        textView = view.findViewById(R.id.sms);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                READ_SMS(textView);
//            }
//        });
        return view;
    }

    private void checkToken() {
        if(isAdded()) {
            AuthorizeUser authorizeUser = new AuthorizeUser(requireActivity());
            authorizeUser.isLoggedIn();
        }
    }

//    public void READ_SMS(View view){
//        Cursor cursor = HomeActivity.getContextOfApplication().getContentResolver()
//                .query(Uri.parse("content://sms"), null, null, null, null);
//
//        int i=0;
//        String x="Allo";
//        if(cursor.moveToFirst()){
//            String msg = cursor.getString(12);
//            do{
//                if(cursor.getString(12).contains("INR") & cursor.getString(12).contains("credit"))
//                {
//                    int index = cursor.getString(12).indexOf("INR");
//                    if(index!=-1 & index+4<cursor.getString(12).length()){
//                        int j = index+4;
//                        char c = cursor.getString(12).charAt(j);
//                        String inr ="";
//                        while(!Character.isWhitespace(c)){
//                            inr+=c;
//                            j++;
//                            c = cursor.getString(12).charAt(j);
//                        }
//                        String[] y = extractInt(cursor.getString(12)).split(" ");
//                        int m = (int) Double.parseDouble(inr);
////                        while(m!=0){
//                            m=m%10;
////                        }
//                        x=x+cursor.getString(12)+"\n"+y[0]
//                                +"  "+ inr+"  "+ m +"\n";
//                    }
//
//                }
//                i++;
//                cursor.moveToNext();
//            }while (cursor.moveToNext());
//        }
//        textView.setText(x);
//        cursor.close();
//    }
//    static String extractInt(String str)
//    {
//        // Replacing every non-digit number
//        // with a space(" ")
//        str = str.replaceAll("[^\\d]", " ");
//
//        // Remove extra spaces from the beginning
//        // and the ending of the string
//        str = str.trim();
//
//        // Replace all the consecutive white
//        // spaces with a single space
//        str = str.replaceAll(" +", " ");
//
//        if (str.equals(""))
//            return "-1";
//
//        return str;
//    }

}