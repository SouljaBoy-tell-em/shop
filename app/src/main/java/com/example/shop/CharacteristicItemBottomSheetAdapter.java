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
import java.util.Map;
import java.util.Objects;

public class CharacteristicItemBottomSheetAdapter extends RecyclerView.Adapter<CharacteristicItemBottomSheetAdapter.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private String key;
    private Map<String, ArrayList<String>> params;
    public CharacteristicItemBottomSheetAdapter(Context context, String key,
                                                Map<String, ArrayList<String>> params) {

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.key = key;
        this.params = params;
    }

    public CharacteristicItemBottomSheetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.layout_characteristic_item, parent, false);
        return new CharacteristicItemBottomSheetAdapter.ViewHolder(view);
    }

    public void onBindViewHolder(CharacteristicItemBottomSheetAdapter.ViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

    }

    @Override
    public int getItemCount() {
        return params.get(key).size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView characteristicItemTextView;
        ViewHolder(View view) {
            super(view);
            characteristicItemTextView = view.findViewById(R.id.itemTextView);
        }
    }
}
