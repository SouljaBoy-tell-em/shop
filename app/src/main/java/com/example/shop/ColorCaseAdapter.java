package com.example.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class ColorCaseAdapter extends RecyclerView.Adapter<ColorCaseAdapter.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private ArrayList<VendorProduct> vendorProducts;
    private DatabaseReference firebaseDBRef;

    public ColorCaseAdapter(Context context, ArrayList<VendorProduct> vendorProducts) {

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.vendorProducts = vendorProducts;
    }

    public ColorCaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.color_button_layout, parent, false);
        return new ColorCaseAdapter.ViewHolder(view);
    }

    public void onBindViewHolder(ColorCaseAdapter.ViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

        FirebaseDatabase
                .getInstance()
                .getReference("ProductList")
                .child("Product-" + vendorProducts.get(position).getVendorCode())
                .child("color")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot snapshot) {

                        holder.colorButton.setBackgroundResource(snapshot.getValue(Integer.class));
                    }
                });
    }

    @Override
    public int getItemCount() {
        return vendorProducts.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        final ImageButton colorButton;
        ViewHolder(View view) {
            super(view);
            colorButton = view.findViewById(R.id.colorButton);
        }
    }
}
