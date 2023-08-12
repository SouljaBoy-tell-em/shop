package com.example.shop;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BasketFragment extends Fragment {

    private BottomNavigationView bnv;
    private SQLiteDatabase dbBasketProducts;
    private int sumProductsPrices;
    private Button buyAllProductsButton;
    private DatabaseReference firebaseDBRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bnv = getActivity().findViewById(R.id.bottomNavigationView);
        dbBasketProducts = getActivity().getBaseContext().
                openOrCreateDatabase("basket.db", Context.MODE_PRIVATE, null);
        sumProductsPrices = 0;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        ConstraintLayout mainConstraint = view.findViewById(R.id.mainConstraint);
        buyAllProductsButton = view.findViewById(R.id.buyAllProductsButton);

        Cursor queryBasketProducts = dbBasketProducts.rawQuery("SELECT * FROM products;",
                                                   null);

        ArrayList<Product> basketProducts = new ArrayList<>();
        RecyclerView basketProductsRecyclerView = view.findViewById(R.id.productsBasketListView);
        basketProductsRecyclerView.getLayoutParams().height =
                        MainActivity.getHeightWindow() -
                        MainActivity.getHeightBNV() -
                        buyAllProductsButton.getLayoutParams().height * 3;

        RecyclerView.ItemDecoration indentBasketProductsRecyclerView =
                new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.bottom = 50;
                    }
                };
        basketProductsRecyclerView.addItemDecoration(indentBasketProductsRecyclerView);

        int indexProduct = 1;
        while(queryBasketProducts.moveToNext()) {

            basketProducts.add(new Product(queryBasketProducts.getString(0),
                    queryBasketProducts.getInt(1), 5000,
                    queryBasketProducts.getString(2),
                    queryBasketProducts.getString(3),
                    queryBasketProducts.getString(5), "product-" + indexProduct));
            indexProduct++;
        }

        ProductBasketListAdapter productBasketListAdapter =
                new ProductBasketListAdapter(getActivity(), basketProducts, MainActivity.getWidthWindow(),
                                                                  buyAllProductsButton);
        basketProductsRecyclerView.setAdapter(productBasketListAdapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        dbBasketProducts.close();
    }
}