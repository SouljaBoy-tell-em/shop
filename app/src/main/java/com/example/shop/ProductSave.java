package com.example.shop;

import java.io.Serializable;

public class ProductSave implements Serializable {

    private String name;
    private double price;
    private String resourceDrawable;

    public ProductSave() {}
    public ProductSave(Product product) {

        this.name             =             product.getName();
        this.price            =            product.getPrice();
        this.resourceDrawable = product.getResourceDrawable();
    }

    public void setName(String name)   {this.name = name;}
    public void setPrice(double price) {this.price = price;}
    public void setResourceDrawable(String resourceDrawable) {this.resourceDrawable = resourceDrawable;}

    public String getName()  { return this.name;}
    public double getPrice() { return this.price;}
    public String getResourceDrawable() {return this.resourceDrawable;}
}
