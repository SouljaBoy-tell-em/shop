package com.example.shop;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;


public class FullListProducts extends Fragment {

    private SQLiteDatabase dbScreenParams;
    private int widthWindow;

    private FirebaseDatabase firebaseDB;
    private DatabaseReference firebaseDBRef;
    private ArrayList<Product> products;
    private ArrayList<View> productsObj;
    private ArrayList<ConstraintLayout.LayoutParams> productsObjParams;
    private ConstraintLayout mainConstraintProductList;
    private ImageView imageLogo;
    private SearchView productSearchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        products = new ArrayList<>();
        productsObj = new ArrayList<>();
        firebaseDBRef = FirebaseDatabase.getInstance().getReference("ProductList");
        widthWindow = MainActivity.getWidthWindow();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_full_list_products, container, false);
        mainConstraintProductList = view.findViewById(R.id.constraintScrollView);
        imageLogo = view.findViewById(R.id.imageLogo);
        productSearchView = view.findViewById(R.id.searchProduct);
        productSearchView.getLayoutParams().width = widthWindow - 50;

        productSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });

        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot curDataSnapshot : snapshot.getChildren()) {

                    Product product = curDataSnapshot.getValue(Product.class);
                    products.add(product);
                    productsObj.add(createProductObject(product));
                }

                productsObjParams = createProductObjParams();

                int indexProduct = 0;
                for(indexProduct = 0; indexProduct < productsObj.size(); indexProduct++)
                    mainConstraintProductList.addView(productsObj.get(indexProduct),
                                                      productsObjParams.get(indexProduct));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        firebaseDBRef.addValueEventListener(vel);

        return view;
    }
    private View createProductObject(Product product) {

        View productObj = getLayoutInflater().inflate(R.layout.layout_product, null,
                                                                      false);
        productObj.setId(View.generateViewId());
        ImageView productImage = (ImageView) productObj.findViewById(R.id.productImage);
        TextView productName = (TextView)  productObj.findViewById(R.id.productName);
        TextView productPrice = (TextView) productObj.findViewById(R.id.productPrice);

        Picasso.get().load(product.getResourceDrawable()).into(productImage);
        productImage.getLayoutParams().width  = widthWindow / 2 - 50;
        productImage.getLayoutParams().height = widthWindow / 2 - 50;

        productName.setText(product.getName());
        productName.setTextSize(12);
        productPrice.setText(product.getPrice() + " ₽");
        productPrice.setTextSize(12);

        productObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentActivityProduct = new Intent(getActivity(), ProductActivity.class);
                intentActivityProduct.putExtra(Product.class.getSimpleName(), product);
                startActivity(intentActivityProduct);
            }
        });

        return productObj;
    }

    public ArrayList<ConstraintLayout.LayoutParams> createProductObjParams() {

        ArrayList<ConstraintLayout.LayoutParams> productListParams = new ArrayList<>();

        int indentWidth = 100 / 3;
        Log.d("WIDTH", indentWidth + "");

        int  indexProduct = 0;
        for (indexProduct = 0; indexProduct <  productsObj.size(); indexProduct++) {

            ConstraintLayout.LayoutParams cur =
                    new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            productListParams.add(cur);

            if (indexProduct == 0) {

                productListParams.get(indexProduct).topToTop =
                        ConstraintLayout.LayoutParams.PARENT_ID;
                productListParams.get(indexProduct).startToStart =
                        ConstraintLayout.LayoutParams.PARENT_ID;
                productListParams.get(indexProduct).leftMargin = indentWidth;
            }

            else if (indexProduct == 1) {

                productListParams.get(indexProduct).topToTop =
                        ConstraintLayout.LayoutParams.PARENT_ID;
                productListParams.get(indexProduct).endToEnd =
                        ConstraintLayout.LayoutParams.PARENT_ID;
                productListParams.get(indexProduct).rightMargin = indentWidth;
            } else {

                productListParams.get(indexProduct).topToBottom =
                        productsObj.get(indexProduct - 2).getId();
                productListParams.get(indexProduct).startToStart =
                        productsObj.get(indexProduct - 2).getId();
            }
        }

        return productListParams;
    }
}