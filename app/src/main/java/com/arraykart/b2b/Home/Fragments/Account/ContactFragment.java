package com.arraykart.b2b.Home.Fragments.Account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraykart.b2b.R;

import soup.neumorphism.NeumorphCardView;


public class ContactFragment extends Fragment {
    private NeumorphCardView whatsappNCV;
    private NeumorphCardView phoneNCV;
    private NeumorphCardView mailNCV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        //send whatsapp message
        setWhatsapp(view);

        //dial phone number
        setPhone(view);

        //send mail
        setMail(view);



        return view;
    }

    private void setMail(View view) {
        mailNCV = view.findViewById(R.id.mailNCV);
        mailNCV.setOnClickListener(v -> {
            try{
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"yourfriends@arraykart.com"});
                //need this to prompts email client only
                email.setType("message/rfc822");
                if(isAdded()) {
                    requireActivity().startActivity(email);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    private void setPhone(View view) {
        phoneNCV = view.findViewById(R.id.phoneNCV);
        phoneNCV.setOnClickListener(v -> {
            try{
                String toNumber = "9311900913";
                Intent intent = new Intent(Intent.ACTION_DIAL,
                        Uri.fromParts("tel", toNumber, null));
                if(isAdded()) {
                    requireActivity().startActivity(intent);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    private void setWhatsapp(View view) {
        whatsappNCV = view.findViewById(R.id.whatsappNCV);
        whatsappNCV.setOnClickListener(v -> {
            try {
                String text = "Hi Arraykart!";

                String toNumber = "9311900913";


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                if(isAdded()) {
                    requireActivity().startActivity(intent);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}