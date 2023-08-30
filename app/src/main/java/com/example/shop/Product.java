package com.example.shop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Product extends VendorProduct implements Serializable {

    private String name;
    private int price;
    private int oldPrice;
    private String size;
    private int color;
    private String resourceDrawable;
    private ArrayList<VendorProduct> analogProducts;
    private ArrayList<String> characteristicKeys;
    private Map<String, ArrayList<String>> characteristics;
    public Product() {}
    public Product(VendorProduct vendorProduct) {

        super(vendorProduct);
        this.analogProducts     = new ArrayList<>();
        this.characteristics    = new HashMap<>();
        this.characteristicKeys = new ArrayList<>();
    }
    public Product(String name, int price, int oldPrice, String size, int color,
                   String resourceDrawable, int vendorCode, int amount,
                   ArrayList<VendorProduct> analogProducts,
                   Map<String, ArrayList<String>> characteristics,
                   ArrayList<String> characteristicKeys) {

        super(vendorCode, amount);
        this.name               =               name;
        this.price              =              price;
        this.oldPrice           =           oldPrice;
        this.size               =               size;
        this.color              =              color;
        this.resourceDrawable   =   resourceDrawable;
        this.analogProducts     =     analogProducts;
        this.characteristics    =    characteristics;
        this.characteristicKeys = characteristicKeys;
    }

    public Product(Product product) {

        super(product.getVendorCode(), product.getAmount());
        this.name               = product.getName();
        this.price              = product.getPrice();
        this.oldPrice           = product.getOldPrice();
        this.size               = product.getSize();
        this.color              = product.getColor();
        this.resourceDrawable   = product.getResourceDrawable();
        this.analogProducts     = product.getAnalogProducts();
        this.characteristics    = product.getCharacteristics();
        this.characteristicKeys = product.getCharacteristicKeys();
    }

    public ArrayList<VendorProduct> getAnalogProducts() {
        return this.analogProducts;
    }
    public ArrayList<String> getCharacteristicKeys() {
        return this.characteristicKeys;
    }
    public Map<String, ArrayList<String>> getCharacteristics() {
        return this.characteristics;
    }
    public int getColor() {
        return this.color;
    }
    public String getName()  {
        return this.name;
    }
    public int getOldPrice() {
        return this.oldPrice;
    }
    public int getPrice() {
        return this.price;
    }
    public String getSize() {
        return this.size;
    }
    public String getResourceDrawable() {
        return this.resourceDrawable;
    }
    public void setAnalogProducts(ArrayList<VendorProduct> analogProducts) {
        this.analogProducts = analogProducts;
    }
    public void setCharacteristicKeys(ArrayList<String> characteristicKeys) {
        this.characteristicKeys = characteristicKeys;
    }
    public void setCharacteristics(Map<String, ArrayList<String>> characteristics) {
        this.characteristics = characteristics;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public void setResourceDrawable(String resourceDrawable) {
        this.resourceDrawable = resourceDrawable;
    }
    public VendorProduct getParent() {
        return new VendorProduct(this.getVendorCode(), this.getAmount());
    }
}
