package com.example.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class MainProductListAdapter extends RecyclerView.Adapter<MainProductListAdapter.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private ArrayList<Product> products;
    private ArrayList<Boolean> isPressed;
    private DatabaseReference firebaseDBRef;

    public MainProductListAdapter(Context context, ArrayList<Product> products) {

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.products = products;

        isPressed = new ArrayList<>();
        for(int indexIsPressed = 0; indexIsPressed < products.size(); indexIsPressed++)
            isPressed.add(false);
    }

    public MainProductListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.layout_product, parent, false);
        return new MainProductListAdapter.ViewHolder(view);
    }

    public void onBindViewHolder(MainProductListAdapter.ViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

        firebaseDBRef = FirebaseDatabase
                        .getInstance()
                        .getReference("Users")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("Basket");
        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot curSnapshot : snapshot.getChildren()) {

                    Product product = curSnapshot.getValue(Product.class);
                        if(Objects.equals(product.getVendorCode(), products.get(position).getVendorCode()))
                            isPressed.set(position, true);
                }

                createProduct(holder, position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        firebaseDBRef.addValueEventListener(vel);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView productName,
                       productOldPrice,
                       productPrice;
        final ImageView productImage,
                        addRed,
                        whiteCart;
        final ConstraintLayout mainConstraintButtonPrice,
                               inBasketConstraint,
                               layoutProduct;

        ViewHolder(View view) {
            super(view);

            productName               = view.findViewById(R.id.productName);
            productOldPrice           = view.findViewById(R.id.productOldPrice);
            productPrice              = view.findViewById(R.id.productPrice);
            productImage              = view.findViewById(R.id.productImage);
            addRed                    = view.findViewById(R.id.addRed);
            whiteCart                 = view.findViewById(R.id.whiteCart);
            mainConstraintButtonPrice = view.findViewById(R.id.mainConstraintButtonPrice);
            inBasketConstraint        = view.findViewById(R.id.inBasketConstraint);
            layoutProduct             = view.findViewById(R.id.layoutProduct);
        }
    }

    private void createProduct(MainProductListAdapter.ViewHolder holder,
                          @SuppressLint("RecyclerView") int position) {

        Product product = products.get(position);
        Picasso.get().load(product.getResourceDrawable()).into(holder.productImage);
        holder.productImage.getLayoutParams().width  = MainActivity.getWidthWindow() / 2 - 50;
        holder.productImage.getLayoutParams().height = MainActivity.getWidthWindow() / 2 - 50;
        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice() + " ₽");
        holder.productOldPrice.setText(Html.fromHtml("<s>" + product.getOldPrice() + "</s>"));
        holder.inBasketConstraint.setVisibility(View.INVISIBLE);

        if(isPressed.get(position) == true) {

            holder.inBasketConstraint.setVisibility(View.VISIBLE);
            updateButtonAdd(holder, View.INVISIBLE,
                    R.drawable.price_item_product_list_constraint_layout_pressed);
        }

        holder.mainConstraintButtonPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isPressed.get(position) == false) {

                    isPressed.set(position, true);
                    FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("Basket")
                                    .child("Product-" + (position + 1))
                                    .setValue(products.get(position));

                    holder.inBasketConstraint.setVisibility(View.VISIBLE);
                    updateButtonAddAnimation(holder);
                    updateButtonAdd(holder, View.INVISIBLE,
                            R.drawable.price_item_product_list_constraint_layout_pressed);

                    return;
                }

                if (isPressed.get(position) == true) {

                    isPressed.set(position, false);
                    FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child("Basket")
                            .child("Product-" + (position + 1))
                            .removeValue();

                    holder.inBasketConstraint.setVisibility(View.INVISIBLE);
                    updateButtonAddAnimation(holder);
                    updateButtonAdd(holder, View.VISIBLE,
                            R.drawable.price_item_product_list_constraint_layout);
                }
            }
        });
    }

    private void updateButtonAdd(MainProductListAdapter.ViewHolder holder, int status,
                                 int backgroundResourceButton) {

        holder.productPrice.setVisibility(status);
        holder.productOldPrice.setVisibility(status);
        holder.addRed.setVisibility(status);
        holder.mainConstraintButtonPrice.setBackgroundResource(backgroundResourceButton);
    }

    private void updateButtonAddAnimation(MainProductListAdapter.ViewHolder holder) {

        Animation buttonPressed = AnimationUtils.loadAnimation
                (context, R.anim.constraint_button_price_item_anim);
        holder.mainConstraintButtonPrice.startAnimation(buttonPressed);
    }
}
