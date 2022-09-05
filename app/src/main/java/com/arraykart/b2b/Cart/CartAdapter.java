package com.arraykart.b2b.Cart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arraykart.b2b.Retrofit.ModelClass.Cart;
import com.arraykart.b2b.Retrofit.ModelClass.CartProductDelete;
import com.arraykart.b2b.Retrofit.ModelClass.CartProducts;
import com.arraykart.b2b.Retrofit.ModelClass.SuccessMessage;
import com.arraykart.b2b.Retrofit.RetrofitClient;
import com.arraykart.b2b.SharedPreference.SharedPreferenceManager;
import com.bumptech.glide.Glide;
import com.arraykart.b2b.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphImageButton;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final Context context;
    private ArrayList<CartProducts> cartProducts;

    public CartAdapter(Context context, List<CartProducts> cartProducts) {
        this.context = context;
        this.cartProducts = (ArrayList<CartProducts>) cartProducts;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_cart_out_of_view_single_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(cartProducts.get(holder.getAdapterPosition()).getImage())
                .centerCrop()
                //.placeholder(R.drawable.placeholder)
                .error(R.drawable.imgnotfound)
                .into(holder.pImage);
        holder.pName.setText(cartProducts.get(holder.getAdapterPosition()).getName());
        holder.pVol.setText("Volume: "+cartProducts.get(holder.getAdapterPosition()).getVolume());
        holder.pPrice.setText("₹" + " " + cartProducts.get(holder.getAdapterPosition()).getPrice());
        holder.pQuantity.setText(""+cartProducts.get(holder.getAdapterPosition()).getQuantity());
        holder.pDiscount.setText(cartProducts.get(holder.getAdapterPosition()).getDiscount()+"% Offer!");

    }

    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        private ImageView pImage;
        private TextView pName;
        private TextView pVol;
        private TextView pPrice;
        private TextView pDiscount;
        private EditText pQuantity;
        private NeumorphImageButton qPlus;
        private NeumorphImageButton qMinus;
        private ImageView cartDelete;
        private CartProductDelete cartProductDelete;
        private SharedPreferenceManager sharedPreferenceManager;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            pImage = itemView.findViewById(R.id.cartImage);
            pName = itemView.findViewById(R.id.cartPName);
            pPrice = itemView.findViewById(R.id.cartPPrice);
            pVol = itemView.findViewById(R.id.cartVol);
            pQuantity = itemView.findViewById(R.id.cartPQuantity);
            qPlus = itemView.findViewById(R.id.cartAddButton);
            qMinus = itemView.findViewById(R.id.cartRemoveButton);
            cartDelete = itemView.findViewById(R.id.cartDelete);
            pDiscount = itemView.findViewById(R.id.cartDiscount);

            pQuantity.setImeActionLabel("Ok", KeyEvent.KEYCODE_ENTER);
            pQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        if (s.length() > 0) {
                            if (Integer.parseInt(s.toString()) == 0) {
                                showAlertToRemoveProductFromCart();
                            } else {
                                editProductInCart(s.toString());
                            }
                        }
                    }catch (NumberFormatException e){
                        System.out.println("NumberFormatException: " + e.getMessage());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
//                    pQuantity.setText(s);

                }
            });

            qPlus.setOnClickListener(v -> {
                pQuantity.setText(String.valueOf(Integer
                        .parseInt(pQuantity.getText().toString())+1));
                //TODO:update cart total
            });
            qMinus.setOnClickListener(v -> {
                if(Integer.parseInt(pQuantity.getText().toString()) >1){
                    pQuantity.setText(String.valueOf(Integer
                            .parseInt(pQuantity.getText().toString())-1));
                }else{
                   showAlertToRemoveProductFromCart();
                }
                //TODO:update cart total
            });
            cartDelete.setOnClickListener(v -> {
                showAlertToRemoveProductFromCart();
            });
        }

        private void editProductInCart(String quantity) {
            sharedPreferenceManager  = new SharedPreferenceManager(context);
            Cart cart = new Cart(
                    cartProducts.get(getAdapterPosition()).getId().toString(),
                    cartProducts.get(getAdapterPosition()).getPrice().toString(),
                    cartProducts.get(getAdapterPosition()).getVolume(),
                    quantity,
                    cartProducts.get(getAdapterPosition()).getDiscount().toString(),
                    cartProducts.get(getAdapterPosition()).getCartid().toString()
                    );
            Call<SuccessMessage> call = RetrofitClient.getClient()
                    .getApi().editCart(sharedPreferenceManager.getString("token"), cart);
            call.enqueue(new Callback<SuccessMessage>() {
                @Override
                public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(
                                context,
                                "" + response.code(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

//                    Activity activity = (Activity) context;
//                    if (activity.getFragmentManager() != null) {
//                        FragmentTransaction tr = activity.getFragmentManager().beginTransaction();
//                        Fragment cartFragment = activity.getFragmentManager().findFragmentByTag("cartFragment");
//                        tr.detach(cartFragment);
//                        tr.attach(cartFragment);
//                        tr.commit();
//                    }
                }

                @Override
                public void onFailure(Call<SuccessMessage> call, Throwable t) {
                    Toast.makeText(
                            context,
                            "Please check your internet connection or try again after sometime",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void showAlertToRemoveProductFromCart() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Remove from cart");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("NO", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.setNegativeButton("YES", ((dialog, which) -> {
                // storing the deleted product to restore on undo action
                CartProducts deletedProduct = cartProducts.get(getAdapterPosition());
                // storing the deleted product position to add after undo into the same position
                int position = getAdapterPosition();

                removeProductFromCart(deletedProduct, position);
                dialog.dismiss();
            }));
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        private void undoRemovedProductFromCart(CartProducts deletedProduct, int position) {
            Activity activity=(Activity) context;
            // below line is to display our snackbar with action.
            Snackbar.make(activity.findViewById(android.R.id.content), deletedProduct.getName()+"("+deletedProduct.getVolume()+")", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add to cart database
                    Cart cart = new Cart(
                            deletedProduct.getId().toString(),
                            deletedProduct.getPrice().toString(),
                            deletedProduct.getVolume(),
                            deletedProduct.getQuantity().toString(),
                            deletedProduct.getDiscount().toString(),
                            deletedProduct.getCartid().toString()
                    );
                    sharedPreferenceManager = new SharedPreferenceManager(context);
                    Call<SuccessMessage> call = RetrofitClient.getClient().getApi()
                            .setCart(sharedPreferenceManager.getString("token"), cart);
                    call.enqueue(new Callback<SuccessMessage>() {
                        @Override
                        public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(
                                        context,
                                        "" + response.code(),
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            // adding on click listener to our action of snack bar.
                            // below line is to add our item to array list with a position.
                            cartProducts.add(position, deletedProduct);

                            // below line is to notify item is
                            // added to our adapter class.
                            notifyItemInserted(position);

                        }

                        @Override
                        public void onFailure(Call<SuccessMessage> call, Throwable t) {
                            Toast.makeText(
                                    context,
                                    "Please check your internet connection or try again after sometime",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).show();
        }

        private void removeProductFromCart(CartProducts deletedProduct, int position) {
            cartProductDelete = new CartProductDelete(cartProducts.get(getAdapterPosition()).getId().toString(),
                    cartProducts.get(getAdapterPosition()).getVolume());
            Call<SuccessMessage> call = RetrofitClient.getClient().getApi()
                    .deleteFromCart(sharedPreferenceManager.getString("token"), cartProductDelete);
            call.enqueue(new Callback<SuccessMessage>() {
                @Override
                public void onResponse(Call<SuccessMessage> call, Response<SuccessMessage> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(
                                context,
                                "" + response.code(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cartProducts.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), cartProducts.size());
                    undoRemovedProductFromCart(deletedProduct, position);

                }

                @Override
                public void onFailure(Call<SuccessMessage> call, Throwable t) {
                    Toast.makeText(
                            context,
                            "Please check your internet connection or try again after sometime",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}
