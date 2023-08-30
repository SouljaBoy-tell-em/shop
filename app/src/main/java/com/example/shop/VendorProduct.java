package com.example.shop;

public class VendorProduct {

    private int vendorCode;
    private int amount;

    public VendorProduct() {}
    public VendorProduct(int vendorCode, int amount) {

        this.vendorCode = vendorCode;
        this.amount     =     amount;
    }
    public VendorProduct(VendorProduct vendorProduct) {

        this.vendorCode = vendorProduct.getVendorCode();
        this.amount     =     vendorProduct.getAmount();
    }
    public int getAmount() {return this.amount;}
    public int getVendorCode() {return this.vendorCode;}
    public void setAmount(int amount) {this.amount = amount;}
    public void setVendorCode(int vendorCode) {this.vendorCode = vendorCode;}
}
