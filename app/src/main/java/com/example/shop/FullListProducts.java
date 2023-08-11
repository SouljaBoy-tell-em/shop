package com.example.shop;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class FullListProducts extends Fragment {

    private SQLiteDatabase dbScreenParams;
    private int widthWindow;
    private DatabaseReference firebaseDBRef;
    private ArrayList<Product> products;
    private ArrayList<View> productsObj;
    private ArrayList<ConstraintLayout.LayoutParams> productsObjParams;
    private ConstraintLayout mainConstraintProductList;
    private ImageView imageLogo;
    private SearchView productSearchView;
    private boolean isPressed;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isPressed = false;
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
        ImageView addRed = (ImageView) productObj.findViewById(R.id.addRed);
        TextView productName = (TextView)  productObj.findViewById(R.id.productName);
        TextView productPrice = (TextView) productObj.findViewById(R.id.productPrice);
        TextView productOldPrice = (TextView) productObj.findViewById(R.id.productOldPrice);
        ConstraintLayout mainConstraintButtonPrice
                = (ConstraintLayout) productObj.findViewById(R.id.mainConstraintButtonPrice);
//        ConstraintLayout constraintButtonPrice
//                = (ConstraintLayout) productObj.findViewById(R.id.constraintButtonPrice);
        ConstraintLayout inBasketConstraint
                = (ConstraintLayout) productObj.findViewById(R.id.inBasketConstraint);

        Picasso.get().load(product.getResourceDrawable()).into(productImage);
        productImage.getLayoutParams().width  = widthWindow / 2 - 50;
        productImage.getLayoutParams().height = widthWindow / 2 - 50;
        productName.setText(product.getName());
        productPrice.setText(product.getPrice() + " ₽");
        productOldPrice.setText(Html.fromHtml("<s>" + product.getOldPrice() + "</s>"));
        inBasketConstraint.setVisibility(View.INVISIBLE);

        mainConstraintButtonPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isPressed == false) {

//                    ObjectAnimator colorFade = ObjectAnimator.ofObject(constraintButtonPrice, "backgroundColor" /*view attribute name*/, new ArgbEvaluator(), getContext().getResources().getColor(R.color.grey) /*from color*/, getContext().getResources().getColor(R.color.black) /*to color*/);
//                    colorFade.setDuration(250);
//                    colorFade.start();
                    isPressed = true;

                    Animation buttonIncrease = AnimationUtils.loadAnimation(getContext(), R.anim.constraint_button_price_item_anim);
                    mainConstraintButtonPrice.startAnimation(buttonIncrease);

                    inBasketConstraint.setVisibility(View.VISIBLE);
                    productPrice.setVisibility(View.INVISIBLE);
                    productOldPrice.setVisibility(View.INVISIBLE);
                    addRed.setVisibility(View.INVISIBLE);
                    mainConstraintButtonPrice.setBackgroundResource(R.drawable.price_item_product_list_constraint_layout_pressed);

                    return;
                }

                if (isPressed == true) {

                    isPressed = false;

                    Animation buttonIncrease = AnimationUtils.loadAnimation(getContext(), R.anim.constraint_button_price_item_anim);
                    mainConstraintButtonPrice.startAnimation(buttonIncrease);

                    inBasketConstraint.setVisibility(View.INVISIBLE);
                    productPrice.setVisibility(View.VISIBLE);
                    productOldPrice.setVisibility(View.VISIBLE);
                    addRed.setVisibility(View.VISIBLE);
                    mainConstraintButtonPrice.setBackgroundResource(R.drawable.price_item_product_list_constraint_layout);
                }
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