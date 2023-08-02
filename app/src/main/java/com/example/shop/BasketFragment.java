package com.example.shop;

import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class BasketFragment extends Fragment {

    int windowWidth;
    int windowHeight;
    int bottomNavigationHeight;
    SQLiteDatabase dbBasketProducts;
    SQLiteDatabase dbScreenParams;
    TextView sumProductsPricesTextView;
    int sumProductsPrices;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        sumProductsPrices = 0;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        ConstraintLayout mainConstraint = view.findViewById(R.id.mainConstraint);
        sumProductsPricesTextView = view.findViewById(R.id.sumProductsPricesTextView);

        dbBasketProducts = getActivity().getBaseContext().
                openOrCreateDatabase("basket.db", Context.MODE_PRIVATE, null);
        Cursor queryBasketProducts = dbBasketProducts.rawQuery("SELECT * FROM products;",
                                                   null);

        dbScreenParams = getActivity().getBaseContext().
                openOrCreateDatabase("screen_params.db", Context.MODE_PRIVATE, null);
        Cursor queryScreenParams = dbScreenParams.rawQuery("SELECT * FROM screen_params;",
                                               null);
        Cursor queryHeightNavigationBar = dbScreenParams.rawQuery("SELECT * FROM navigation_bar_params;",
                null);

        queryScreenParams.moveToNext();
        windowWidth            = queryScreenParams.getInt(0);
        windowHeight           = queryScreenParams.getInt(1);

        queryHeightNavigationBar.moveToNext();
        bottomNavigationHeight = queryHeightNavigationBar.getInt(0);

        ArrayList<ProductSave> basketProducts = new ArrayList<>();
        RecyclerView basketProductsRecyclerView = view.findViewById(R.id.productsBasketListView);
        basketProductsRecyclerView.getLayoutParams().height = windowHeight -
                        sumProductsPricesTextView.getLayoutParams().height -
                        bottomNavigationHeight - 50;


        while(queryBasketProducts.moveToNext()) {

            basketProducts.add(new ProductSave(queryBasketProducts.getString(0),
                    queryBasketProducts.getInt(1),
                    queryBasketProducts.getString(2),
                    queryBasketProducts.getString(3),
                    queryBasketProducts.getString(5)));

            Log.d("AMOUNT", queryBasketProducts.getInt(4) + "");
            sumProductsPrices += queryBasketProducts.getInt(4) * queryBasketProducts.getInt(1);
        }

        ProductBasketListAdapter productBasketListAdapter =
                new ProductBasketListAdapter(getActivity(), basketProducts, windowWidth, sumProductsPricesTextView);
        basketProductsRecyclerView.setAdapter(productBasketListAdapter);
        sumProductsPricesTextView.append(" " + sumProductsPrices + "₽");

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        dbBasketProducts.close();
        dbScreenParams.close();
    }
}