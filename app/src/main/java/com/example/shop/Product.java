package com.example.shop;

import java.io.Serializable;

public class Product implements Serializable {

    private String name;
    private int price;
    private int oldPrice;
    private String size;
    private String color;
    private String resourceDrawable;
    private String vendorCode;
    public Product() {}

    public Product(String name, int price, int oldPrice, String size, String color,
                   String resourceDrawable, String vendorCode) {

        this.name             =             name;
        this.price            =            price;
        this.oldPrice         =         oldPrice;
        this.size             =             size;
        this.color            =            color;
        this.resourceDrawable = resourceDrawable;
        this.vendorCode       =       vendorCode;
    }

    public Product(Product product) {

        this.name             = product.getName();
        this.price            = product.getPrice();
        this.size             = product.getSize();
        this.color            = product.getColor();
        this.resourceDrawable = product.getResourceDrawable();
    }
    public void setColor(String color) {this.color = color;}

    public void setName(String name) {this.name = name;}
    public void setOldPrice(int oldPrice) {this.oldPrice = oldPrice;}
    public void setPrice(int price) {this.price = price;}
    public void setSize(String size) {this.size = size;}
    public void setResourceDrawable(String resourceDrawable) {this.resourceDrawable = resourceDrawable;}
    public void setVendorCode(String vendorCode) {this.vendorCode = vendorCode;}
    public String getColor() {return this.color;}
    public String getName()  { return this.name;}
    public int getOldPrice() {return this.oldPrice;}
    public int getPrice() { return this.price;}
    public String getSize() {return this.size;}
    public String getResourceDrawable() {return this.resourceDrawable;}
    public String getVendorCode() {return this.vendorCode;}
}
