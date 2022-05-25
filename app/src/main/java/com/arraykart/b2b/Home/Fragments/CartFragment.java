package com.arraykart.b2b.Home.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arraykart.b2b.Cart.CartAdapter;
import com.arraykart.b2b.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class CartFragment extends Fragment {

    private ArrayList<Integer> cartImgs;
    private ArrayList<String> cartPName;
    private ArrayList<String> cartPVol;
    private ArrayList<String> cartPPrice;
    private ArrayList<String> cartPQuantity;
    private RecyclerView cartRV;
    private CartAdapter cartAdapter;
    private TextView cartTotal;
    private TextView cartQuantity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        populateArray();
        cartRV = view.findViewById(R.id.cartRV);
        cartRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        cartAdapter = new CartAdapter(getContext(), cartImgs, cartPName, cartPVol, cartPPrice, cartPQuantity);
        cartRV.setAdapter(cartAdapter);

        cartTotal = view.findViewById(R.id.cartTotal);
        int total=0;
        Iterator<String> i1=cartPPrice.iterator();
        Iterator<String> i2=cartPQuantity.iterator();
        while (i1.hasNext() && i2.hasNext()){
            total+=(Integer.parseInt(i1.next()))*(Integer.parseInt(i2.next()));
        }
        cartTotal.setText("Subtotal : ₹" + " " + total);
        cartQuantity = view.findViewById(R.id.cartQuantity);
        cartQuantity.setText("Proceed To Buy(" + cartPName.size() + " items)");
        return view;
    }
    public void populateArray(){
        cartImgs = new ArrayList<>(Arrays.asList(R.drawable.tp0, R.drawable.tp1, R.drawable.tp2, R.drawable.tp3));
        cartPName = new ArrayList<>(Arrays.asList("Maggie", "Oil", "Soap", "Bisci"));
        cartPVol = new ArrayList<>(Arrays.asList("100ml", "1liter", "1case", "10kgs"));
        cartPPrice = new ArrayList<>(Arrays.asList("100", "5000", "15000", "7000"));
        cartPQuantity = new ArrayList<>(Arrays.asList("3", "1", "1", "10"));
    }
}