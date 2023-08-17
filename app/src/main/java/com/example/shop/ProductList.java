package com.example.shop;

import java.util.ArrayList;

public class ProductList {

    private ArrayList<Product> products;
    public ProductList(ArrayList<Product> products) {

        this.products = new ArrayList<>();
        this.products = products;
    }

    public ArrayList<Product> getProducts() {

        return products;
    }
}
