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

public class CharacteristicBottomSheetAdapter extends RecyclerView.Adapter<CharacteristicBottomSheetAdapter.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private Product product;
    public CharacteristicBottomSheetAdapter(Context context, Product product) {

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.product = product;
    }

    public CharacteristicBottomSheetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.layout_characteristic, parent, false);
        return new CharacteristicBottomSheetAdapter.ViewHolder(view);
    }

    public void onBindViewHolder(CharacteristicBottomSheetAdapter.ViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

        holder.characteristicTextView.setText(product.getCharacteristicKeys().get(position));

        CharacteristicItemBottomSheetAdapter characteristicItemBottomSheetAdapter
                = new CharacteristicItemBottomSheetAdapter(context,
                product.getCharacteristicKeys().get(position), product.getCharacteristics());
        RecyclerView.LayoutManager horizontalManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.characteristicRecyclerView.setLayoutManager(horizontalManager);
        holder.characteristicRecyclerView.setAdapter(characteristicItemBottomSheetAdapter);
    }

    @Override
    public int getItemCount() {
        return product.getCharacteristics().size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView characteristicTextView;
        final RecyclerView characteristicRecyclerView;
        ViewHolder(View view) {
            super(view);

            characteristicTextView = view.findViewById(R.id.characteristicTextView);
            characteristicRecyclerView = view.findViewById(R.id.characteristicRecyclerView);
        }
    }
}
