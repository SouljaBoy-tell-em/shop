package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ProductSave productSave =
                (ProductSave) getIntent().getExtras().getSerializable(ProductSave.class.getSimpleName());

        ConstraintLayout product = new ConstraintLayout(this);

        ImageView productImage = new ImageView(this);
        int imageSize = getIntent().getExtras().getInt("full_screen_size") - 30;
        productImage.setId(View.generateViewId());
        ConstraintLayout.LayoutParams productImageParams =
                new ConstraintLayout.LayoutParams(imageSize, imageSize);

        productImageParams.topToTop   = ConstraintLayout.LayoutParams.PARENT_ID;
        productImageParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        productImageParams.setMargins(15, 30, 15,0);
        Picasso.get().load(productSave.getResourceDrawable()).into(productImage);

        TextView productName = new TextView(this);
        productName.setId(View.generateViewId());
        productName.setText(productSave.getName());
        productName.setTextSize(35);
        ConstraintLayout.LayoutParams productNameParams =
                new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                  ViewGroup.LayoutParams.WRAP_CONTENT);
        productNameParams.topToBottom = productImage.getId();
        productNameParams.leftToLeft  = ConstraintLayout.LayoutParams.PARENT_ID;

        Button productButton = new Button(this);
        productButton.setText("GET NOW");
        productButton.getBackground().setColorFilter(0xFFFF8000, PorterDuff.Mode.MULTIPLY);

        ConstraintLayout.LayoutParams productButtonParams =
                new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                  ViewGroup.LayoutParams.WRAP_CONTENT);
        productButtonParams.topToBottom = productName.getId();
        productButtonParams.leftToLeft  = ConstraintLayout.LayoutParams.PARENT_ID;

        product.addView(productImage, productImageParams);
        product.addView(productName, productNameParams);
        product.addView(productButton, productButtonParams);

        setContentView(product);
    }
}