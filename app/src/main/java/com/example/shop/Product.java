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

    private String name;
    private int price;
    private String size;
    private String color;
    private String resourceDrawable;
    private LayoutInflater layoutInflater;
    private int sizeScreen;
    public int imageWidth;
    public Product(String name, int price, String size, String color, String resourceDrawable, LayoutInflater layoutInflater,
                   int sizeScreen) {

        this.name             =             name;
        this.price            =            price;
        this.size             =             size;
        this.color            =            color;
        this.resourceDrawable = resourceDrawable;
        this.layoutInflater   =   layoutInflater;
        this.sizeScreen       =       sizeScreen;
    }

    public Product() {}

    public View CreateProduct() {

        View product = this.layoutInflater.inflate(R.layout.layout_product, null, false);
        product.setId(View.generateViewId());
        ImageView imageView = (ImageView) product.findViewById(R.id.productImage);
        TextView productName = (TextView)  product.findViewById(R.id.productName);
        TextView productPrice = (TextView) product.findViewById(R.id.productPrice);

        Picasso.get().load(this.resourceDrawable).into(imageView);
        imageView.getLayoutParams().width  = this.sizeScreen / 2 - 50;
        imageView.getLayoutParams().height = this.sizeScreen / 2 - 50;
        this.imageWidth = imageView.getLayoutParams().width;

        productName.setText(this.name);
        productName.setTextSize(12);
        productPrice.setText(this.price + " ₽");
        productPrice.setTextSize(12);

        return product;
    }
    public String getName()  { return this.name;}
    public int getPrice() { return this.price;}
    public String getSize() {return this.size;}
    public String getColor() {return this.color;}
    public String getResourceDrawable() {return this.resourceDrawable;}
    public void setName(String name) {this.name = name;}
    public void setPrice(int price) {this.price = price;}
    public void setSize(String size) {this.size = size;}
    public void setColor(String color) {this.color = color;}
    public void setResourceDrawable(String resourceDrawable) {this.resourceDrawable = resourceDrawable;}
}