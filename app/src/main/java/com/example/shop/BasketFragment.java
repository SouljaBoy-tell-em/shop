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

import java.util.ArrayList;

public class BasketFragment extends Fragment {

    BottomNavigationView bnv;
    int windowWidth;
    int windowHeight;
    int bottomNavigationHeight;
    SQLiteDatabase dbBasketProducts;
    SQLiteDatabase dbScreenParams;
    int sumProductsPrices;
    Button buyAllProductsButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bnv = getActivity().findViewById(R.id.bottomNavigationView);
        dbBasketProducts = getActivity().getBaseContext().
                openOrCreateDatabase("basket.db", Context.MODE_PRIVATE, null);
        dbScreenParams = getActivity().getBaseContext().
                openOrCreateDatabase("screen_params.db", Context.MODE_PRIVATE, null);
        dbScreenParams.execSQL("CREATE TABLE IF NOT EXISTS" +
                " navigation_bar_params (height INTEGER, UNIQUE(height))");
        dbScreenParams.execSQL("INSERT OR IGNORE INTO navigation_bar_params VALUES" +
                " (" + bnv.getHeight() + ")");

        sumProductsPrices = 0;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        ConstraintLayout mainConstraint = view.findViewById(R.id.mainConstraint);
        buyAllProductsButton = view.findViewById(R.id.buyAllProductsButton);
        buyAllProductsButton.setText("КУПИТЬ ВСЁ за 15000 ₽");

        Cursor queryBasketProducts = dbBasketProducts.rawQuery("SELECT * FROM products;",
                                                   null);
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
                                                    bottomNavigationHeight -
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

        while(queryBasketProducts.moveToNext())
            basketProducts.add(new ProductSave(queryBasketProducts.getString(0),
                    queryBasketProducts.getInt(1),
                    queryBasketProducts.getString(2),
                    queryBasketProducts.getString(3),
                    queryBasketProducts.getString(5)));

        ProductBasketListAdapter productBasketListAdapter =
                new ProductBasketListAdapter(getActivity(), basketProducts, windowWidth,
                                                                  buyAllProductsButton);
        basketProductsRecyclerView.setAdapter(productBasketListAdapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        dbBasketProducts.close();
        dbScreenParams.close();
    }
}