package com.arraykart.b2b.Home.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//
//import com.arraykart.b2b.Cart.CartAdapter;
import com.arraykart.b2b.Authenticate.AuthorizeUser;
import com.arraykart.b2b.Cart.CartAdapter;
import com.arraykart.b2b.Home.Fragments.Account.AccountOptionsActivity;
import com.arraykart.b2b.Home.Fragments.Account.KycUploadFragment;
import com.arraykart.b2b.Order.OrderActivity;
import com.arraykart.b2b.R;
import com.arraykart.b2b.Retrofit.ModelClass.Cart;
import com.arraykart.b2b.Retrofit.ModelClass.CartEstimate;
import com.arraykart.b2b.Retrofit.ModelClass.CartProductDelete;
import com.arraykart.b2b.Retrofit.ModelClass.CartProducts;
import com.arraykart.b2b.Retrofit.ModelClass.ProductsWithQuantity;
import com.arraykart.b2b.Retrofit.ModelClass.SuccessMessage;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {

    private ArrayList<String> cartPQuantity;
    private RecyclerView cartRV;
    private CartAdapter cartAdapter;
    private TextView cartTotal;
    private TextView cartPlaceOrder;
    private SharedPreferenceManager sharedPreferenceManager;
    private List<CartProducts> cartProducts;
    private Boolean undoCheck = false;
    private CartProductDelete cartProductDelete;
    private Button getEstimateButton;
    private LinearLayout estimateLL;
    private LinearLayout placeOrderLL;
    private TextView rejectEstimate;
    private TextView getBackToYouTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        checkToken();
        if(isAdded()) {
            sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
            Call<ProductsWithQuantity> call = RetrofitClient.getClient().getApi()
                    .getCart(sharedPreferenceManager.getString("token"));
            call.enqueue(new Callback<ProductsWithQuantity>() {
                @Override
                public void onResponse(Call<ProductsWithQuantity> call, Response<ProductsWithQuantity> response) {
                    if(!response.isSuccessful()){
                        if(isAdded()) {
                            Toast.makeText(
                                    requireActivity(),
                                    "Cart Empty!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    cartProducts = response.body().getProducts();
//                    cartTotal = view.findViewById(R.id.cartTotal);
//                    int total=0;
//                    Iterator<CartProducts> i1=cartProducts.iterator();
//                    while (i1.hasNext()){
//                        total+=(Integer.parseInt(i1.next().getPrice().toString()));
////                    *(Integer.parseInt(i1.next().getQuantity().toString()));
//                    }
//                    cartTotal.setText("Subtotal : ₹" + " " + total);
                    cartPlaceOrder = view.findViewById(R.id.cartPlaceOrder);

                    cartRV = view.findViewById(R.id.cartRV);
                    if(isAdded()) {
                        cartRV.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
                    }
                    cartAdapter = new CartAdapter(getContext(), cartProducts);
                    cartRV.setAdapter(cartAdapter);

                    //based on estimate requested on not toggle visibily in cart
                    //get estimate controls
                    estimateLL = view.findViewById(R.id.estimateLL);
                    placeOrderLL = view.findViewById(R.id.placeOrderLL);
                    getEstimateButton = view.findViewById(R.id.getEstimateButton);
                    getBackToYouTV = view.findViewById(R.id.getBackToYouTV);
                    rejectEstimate = view.findViewById(R.id.rejectEstimate);
                    if(cartProducts.get(0).getEstimate().equalsIgnoreCase("default")){
                        estimateLL.setVisibility(View.VISIBLE);
                        cartRV.setVisibility(View.VISIBLE);
                        getBackToYouTV.setVisibility(View.GONE);
                        if(cartProducts.size()>0) {
                            getEstimateButton.setOnClickListener(view1 -> {
                                CartEstimate cartEstimate = new CartEstimate("request");
                                if (isAdded()) {
                                    sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
                                    Call<SuccessMessage> call1 = RetrofitClient.getClient().getApi()
                                            .updateCartEstimate(sharedPreferenceManager.getString("token"), cartEstimate);
                                    call1.enqueue(new Callback<SuccessMessage>() {
                                        @Override
                                        public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                                            if (!response.isSuccessful()) {
                                                if (isAdded()) {
                                                    Toast.makeText(
                                                            requireActivity(),
                                                            "" + response.code(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                return;
                                            }
                                            estimateLL.setVisibility(View.GONE);
                                            cartRV.setVisibility(View.GONE);
                                            getBackToYouTV.setVisibility(View.VISIBLE);
                                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity());
                                            builder.setMessage("You will get a Estimate Shortly!");
                                            builder.setCancelable(true);
                                            builder.setPositiveButton("Ok", (dialog, id) -> {
                                                dialog.cancel();
                                            });
                                            androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                        }

                                        @Override
                                        public void onFailure(Call<SuccessMessage> call, Throwable t) {
                                            if (isAdded()) {
                                                Toast.makeText(
                                                        requireActivity(),
                                                        "Please check your internet connection or try again after sometime",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }else{
                            if(isAdded()) {
                                Toast.makeText(requireActivity(), "Cart Empty!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else if(cartProducts.get(0).getEstimate().equalsIgnoreCase("request")){
                        estimateLL.setVisibility(View.GONE);
                        cartRV.setVisibility(View.GONE);
                        getBackToYouTV.setVisibility(View.VISIBLE);
                    }else{
                        estimateLL.setVisibility(View.GONE);
                        cartRV.setVisibility(View.VISIBLE);
                        getBackToYouTV.setVisibility(View.GONE);
                        placeOrderLL.setVisibility(View.VISIBLE);
                        rejectEstimate.setOnClickListener(view1 -> {
                            Call<SuccessMessage> call1 = RetrofitClient.getClient().getApi()
                                    .clearCart(sharedPreferenceManager.getString("token"));
                            call1.enqueue(new Callback<SuccessMessage>() {
                                @Override
                                public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                                    if(isAdded()) {
                                        if (!response.isSuccessful()) {
                                            Toast.makeText(
                                                    requireActivity(),
                                                    "" + response.code(),
                                                    Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    estimateLL.setVisibility(View.VISIBLE);
                                    cartRV.setVisibility(View.VISIBLE);
                                    getBackToYouTV.setVisibility(View.GONE);
                                    placeOrderLL.setVisibility(View.GONE);
                                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity());
                                    builder.setMessage("We are sorry that you did not like our Estimate!");
                                    builder.setCancelable(true);
                                    builder.setPositiveButton("Continue Shopping", (dialog, id) -> {
                                        dialog.cancel();
                                    });
                                    androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                    if(isAdded()) {
                                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                                                .beginTransaction();
                                        Fragment fragment = new CartFragment();
                                        transaction.replace(R.id.homeContainer, fragment).commit();
                                    }
                                }

                                @Override
                                public void onFailure(Call<SuccessMessage> call, Throwable t) {
                                    if(isAdded()) {
                                        Toast.makeText(
                                                requireActivity(),
                                                "Please check your internet connection or try again after sometime",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        });
                        cartPlaceOrder.setText("Place Order("+ cartProducts.size()+")");
                        cartPlaceOrder.setOnClickListener(v -> {

                            estimateLL.setVisibility(View.VISIBLE);
                            cartRV.setVisibility(View.VISIBLE);
                            getBackToYouTV.setVisibility(View.GONE);
                            placeOrderLL.setVisibility(View.GONE);
                            if(isAdded()) {
                                Intent i = new Intent(requireActivity(), OrderActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("cartProducts", (Serializable) cartProducts);
                                i.putExtras(bundle);
                                startActivity(i);
                            }
//                            Call<SuccessMessage> call1 = RetrofitClient.getClient().getApi()
//                                    .clearCart(sharedPreferenceManager.getString("token"));
//                            call1.enqueue(new Callback<SuccessMessage>() {
//                                @Override
//                                public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
//                                    if(isAdded()) {
//                                        if (!response.isSuccessful()) {
//                                            Toast.makeText(
//                                                    requireActivity(),
//                                                    "" + response.code(),
//                                                    Toast.LENGTH_SHORT).show();
//                                            return;
//                                        }
//                                    }
//                                    estimateLL.setVisibility(View.VISIBLE);
//                                    cartRV.setVisibility(View.VISIBLE);
//                                    getBackToYouTV.setVisibility(View.GONE);
//                                    placeOrderLL.setVisibility(View.GONE);
//                                    if(isAdded()) {
//                                        Intent i = new Intent(requireActivity(), OrderActivity.class);
//                                        Bundle bundle = new Bundle();
//                                        bundle.putSerializable("cartProducts", (Serializable) cartProducts);
//                                        i.putExtras(bundle);
//                                        startActivity(i);
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<SuccessMessage> call, Throwable t) {
//                                    if(isAdded()) {
//                                        Toast.makeText(
//                                                requireActivity(),
//                                                "Please check your internet connection or try again after sometime",
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });

                        });
                    }




                    // on below line we are creating a method to create item touch helper
                    // method for adding swipe to delete functionality.
                    // in this we are specifying drag direction and position to right
                    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                        @Override
                        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                            // this method is called
                            // when the item is moved.
                            return false;
                        }

                        @Override
                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                            if(isAdded()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                                builder.setTitle("Remove from cart");
                                builder.setMessage("Are you sure?");
                                builder.setPositiveButton("NO", (dialog, which) -> {
                                    dialog.dismiss();
                                    // This will make the swiped out view animate back into it's original position.
                                    cartAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                });
                                builder.setNegativeButton("YES", ((dialog, which) -> {
                                    // storing the deleted product to restore on undo action
                                    CartProducts deletedProduct = cartProducts.get(viewHolder.getAdapterPosition());

                                    // storing the deleted product position to add after undo into the same position
                                    int position = viewHolder.getAdapterPosition();
                                    cartProducts.remove(viewHolder.getAdapterPosition());

                                    // below line is to notify our item is removed from adapter.
                                    cartAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                                    //delete from database
                                    if (isAdded()) {
                                        sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
                                        cartProductDelete = new CartProductDelete(deletedProduct.getId().toString(),
                                                deletedProduct.getVolume());
                                        Call<SuccessMessage> call = RetrofitClient.getClient().getApi()
                                                .deleteFromCart(sharedPreferenceManager.getString("token"), cartProductDelete);
                                        call.enqueue(new Callback<SuccessMessage>() {
                                            @Override
                                            public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                                                if (!response.isSuccessful()) {
                                                    if (isAdded()) {
                                                        Toast.makeText(
                                                                requireActivity(),
                                                                "" + response.code(),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                    return;
                                                }
                                                // below line is to display our snackbar with action.
                                                Snackbar.make(cartRV, deletedProduct.getName() + "(" + deletedProduct.getVolume() + ")", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        //add to cart database
                                                        Cart cart = new Cart(
                                                                deletedProduct.getId().toString(),
//                                                                deletedProduct.getPrice().toString(),
                                                                deletedProduct.getVolume(),
                                                                deletedProduct.getQuantity().toString()
//                                                                deletedProduct.getDiscount().toString(),
//                                                                deletedProduct.getCartid().toString()
                                                        );
                                                        if (isAdded()) {
                                                            sharedPreferenceManager = new SharedPreferenceManager(requireActivity());
                                                            Call<SuccessMessage> call = RetrofitClient.getClient().getApi()
                                                                    .setCart(sharedPreferenceManager.getString("token"), cart);
                                                            call.enqueue(new Callback<SuccessMessage>() {
                                                                @Override
                                                                public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                                                                    if (!response.isSuccessful()) {
                                                                        if (isAdded()) {
                                                                            Toast.makeText(
                                                                                    requireActivity(),
                                                                                    "" + response.code(),
                                                                                    Toast.LENGTH_SHORT).show();
                                                                            return;
                                                                        }
                                                                    }
                                                                    // adding on click listener to our action of snack bar.
                                                                    // below line is to add our item to array list with a position.
                                                                    cartProducts.add(position, deletedProduct);

                                                                    // below line is to notify item is
                                                                    // added to our adapter class.
                                                                    cartAdapter.notifyItemInserted(position);

                                                                }

                                                                @Override
                                                                public void onFailure(Call<SuccessMessage> call, Throwable t) {
                                                                    if (isAdded()) {
                                                                        Toast.makeText(
                                                                                requireActivity(),
                                                                                "Please check your internet connection or try again after sometime",
                                                                                Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });


                                                        }

                                                    }
                                                }).show();
                                            }

                                            @Override
                                            public void onFailure(Call<SuccessMessage> call, Throwable t) {
                                                if (isAdded()) {
                                                    Toast.makeText(
                                                            requireActivity(),
                                                            "Please check your internet connection or try again after sometime",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                    dialog.dismiss();
                                }));
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }
                        // at last we are adding this
                        // to our recycler view.
                    }).attachToRecyclerView(cartRV);

                }

                @Override
                public void onFailure(Call<ProductsWithQuantity> call, Throwable t) {
                    if(isAdded()) {
                        Toast.makeText(
                                requireActivity(),
                                "Please check your internet connection or try again after sometime",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        return view;
    }

    private void checkToken() {
        if(isAdded()) {
            AuthorizeUser authorizeUser = new AuthorizeUser(requireActivity());
            authorizeUser.isLoggedIn();
        }
    }

}