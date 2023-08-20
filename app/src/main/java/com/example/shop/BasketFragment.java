package com.example.shop;

import static android.content.Context.MODE_PRIVATE;

import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BasketFragment extends Fragment {

    private BottomNavigationView bnv;
    private int sumProductsPrices;
    private Button buyAllProductsButton;
    private DatabaseReference firebaseDBRef;
    private SQLiteDatabase databaseSQL;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bnv = getActivity().findViewById(R.id.bottomNavigationView);
        sumProductsPrices = 0;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        buyAllProductsButton = view.findViewById(R.id.buyAllProductsButton);
        ArrayList<VendorProduct> basketProducts = new ArrayList<>();
        RecyclerView basketProductsRecyclerView = view.findViewById(R.id.productsBasketRecyclerView);

        RecyclerView.ItemDecoration indentBasketProductsRecyclerView =
                new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.bottom = 50;
                    }
                };
        basketProductsRecyclerView.addItemDecoration(indentBasketProductsRecyclerView);
            firebaseDBRef = FirebaseDatabase
                    .getInstance()
                    .getReference("Users")
                    .child(FirebaseAuth.getInstance().getUid())
                    .child("Basket");

            ValueEventListener vel = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    basketProducts.clear();
                    for (DataSnapshot curDataSnapshot : snapshot.getChildren()) {

                        VendorProduct product = curDataSnapshot.getValue(VendorProduct.class);
                        basketProducts.add(product);
                    }

                    try {

                        ProductBasketListAdapter productBasketListAdapter =
                                new ProductBasketListAdapter(getActivity(), basketProducts,
                                        MainActivity.getWidthWindow(), buyAllProductsButton);
                        basketProductsRecyclerView.setAdapter(productBasketListAdapter);
                    } catch (Exception e) {

                        Log.d("EXCEPTION", e.getMessage());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            };
            firebaseDBRef.addValueEventListener(vel);

            return view;
        }

}