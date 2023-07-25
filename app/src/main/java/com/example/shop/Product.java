package com.example.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;

public class Product {

    private String          name;
    private double         price;
    private String resourceDrawable;
    private LayoutInflater layoutInflater;
    private int size;
    public Product(String name, double price, String resourceDrawable, LayoutInflater layoutInflater,
                   int size) {

        this.name             =             name;
        this.price            =            price;
        this.resourceDrawable = resourceDrawable;
        this.layoutInflater   =   layoutInflater;
        this.size        =                  size;
    }

    public Product() {}

    public View CreateProduct() {

        View product = this.layoutInflater.inflate(R.layout.layout_product, null, false);
        product.setId(View.generateViewId());
        ImageView imageView = (ImageView) product.findViewById(R.id.productImage);
        TextView productName = (TextView)  product.findViewById(R.id.productName);
        TextView productPrice = (TextView) product.findViewById(R.id.productPrice);

        Picasso.get().load(this.resourceDrawable).into(imageView);
        imageView.getLayoutParams().width  = this.size / 2 - 50;
        imageView.getLayoutParams().height = this.size / 2 - 50;

        productName.setText(this.name);
        productName.setTextSize(20);

        productPrice.setText((int)this.price + " ₽");
        productPrice.setTextSize(16);

        return product;
    }

    public String getName()  { return this.name;}
    public double getPrice() { return this.price;}
    public String getResourceDrawable() {return this.resourceDrawable;}
    public void setName(String name) {this.name = name;}
    public void setPrice(double price) {this.price = price;}
    public void setResourceDrawable(String resourceDrawable) {this.resourceDrawable = resourceDrawable;}
}