package com.example.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;

public class ProductList {

    private List<View> productList;
    private List<Product> products;
    public ProductList() {

        this.productList = new ArrayList<>();
        this.products = new ArrayList<>();
    }
    public ProductList(List<View> productList, List<Product> products) {

        this.productList = productList;
        this.products    =    products;
    }

    public List<View> getProductList() {return this.productList;}
    public List<Product> getProducts() {return this.products;}
    public void setProductList(List<View> productList) {this.productList = productList;}
    public void setProducts(List<Product> products) {this.products = products;}
    public void addElement(View productListElem, Product productsElem) {

        this.productList.add(productListElem);
        this.products.add(productsElem);
    }
}
