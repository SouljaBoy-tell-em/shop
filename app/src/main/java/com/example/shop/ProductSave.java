package com.example.shop;

import java.io.Serializable;

public class ProductSave implements Serializable {

    private String name;
    private int price;
    private String size;
    private String color;
    private String resourceDrawable;

    public ProductSave() {}
    public ProductSave(Product product) {

        this.name             =             product.getName();
        this.price            =            product.getPrice();
        this.size             =             product.getSize();
        this.color            =            product.getColor();
        this.resourceDrawable = product.getResourceDrawable();
    }

    public ProductSave(String name, int price, String size, String color, String resourceDrawable) {

        this.name             =             name;
        this.price            =            price;
        this.size             =             size;
        this.color            =            color;
        this.resourceDrawable = resourceDrawable;
    }

    public void setName(String name)   {this.name = name;}
    public void setPrice(int price) {this.price = price;}
    public void setSize(String size) {this.size = size;}
    public void setColor(String color) {this.color = color;}
    public void setResourceDrawable(String resourceDrawable) {this.resourceDrawable = resourceDrawable;}
    public String getName()  { return this.name;}
    public int getPrice() { return this.price;}
    public String getSize() {return this.size;}
    public String getColor() {return this.color;}
    public String getResourceDrawable() {return this.resourceDrawable;}
}
