package com.example.shop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainFragment extends Fragment {

    private int widthWindow;
    private DatabaseReference firebaseDBRef;
    private ArrayList<Product> actualProducts;
    private ArrayList<Product> discountProducts;
    private SearchView productSearchView;
    private RecyclerView actualProductRecyclerView;
    private RecyclerView discountProductRecyclerView;
    private MainProductListAdapter adapter;
    private RecyclerView mainActualProductsRecyclerView;
    private MainActualInfoAdapter mainActualProductsRecyclerViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actualProducts = new ArrayList<>();
        discountProducts = new ArrayList<>();
        widthWindow = MainActivity.getWidthWindow();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        productSearchView = view.findViewById(R.id.searchProduct);
        actualProductRecyclerView = view.findViewById(R.id.mainRecyclerView);
        discountProductRecyclerView = view.findViewById(R.id.discountProductRecyclerView);
        mainActualProductsRecyclerView = view.findViewById(R.id.actualProductsRecyclerView);
        productSearchView.getLayoutParams().width = widthWindow - 50;

        RecyclerView.LayoutManager actualProductsRecyclerViewManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                true);
        mainActualProductsRecyclerViewAdapter = new MainActualInfoAdapter(getContext());
        mainActualProductsRecyclerView.setLayoutManager(actualProductsRecyclerViewManager);
        mainActualProductsRecyclerView.setAdapter(mainActualProductsRecyclerViewAdapter);
        parseActualProducts();
        parseDiscountProducts();

        return view;
    }

    private void parseActualProducts() {

        RecyclerView.LayoutManager doubleManagerActualProductRecyclerView
                = new GridLayoutManager(getContext(), 2);
        actualProductRecyclerView.setLayoutManager(doubleManagerActualProductRecyclerView);

        firebaseDBRef = FirebaseDatabase.getInstance().getReference("ProductList");
        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot curDataSnapshot : snapshot.getChildren()) {

                    Product product = curDataSnapshot.getValue(Product.class);
                    actualProducts.add(product);
                }

                adapter = new MainProductListAdapter(getContext(), actualProducts);
                actualProductRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        firebaseDBRef.addValueEventListener(vel);
    }

    private void parseDiscountProducts() {

        RecyclerView.LayoutManager doubleManagerDiscountProductRecyclerView
                = new GridLayoutManager(getContext(), 2);
        discountProductRecyclerView.setLayoutManager(doubleManagerDiscountProductRecyclerView);

        firebaseDBRef = FirebaseDatabase.getInstance().getReference("DiscountProducts");
        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot curDataSnapshot : snapshot.getChildren()) {

                    Product product = curDataSnapshot.getValue(Product.class);
                    discountProducts.add(product);
                }

                adapter = new MainProductListAdapter(getContext(), discountProducts);
                discountProductRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        firebaseDBRef.addValueEventListener(vel);
    }
}