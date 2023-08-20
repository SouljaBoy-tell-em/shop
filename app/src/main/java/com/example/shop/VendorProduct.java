package com.example.shop;

public class VendorProduct {

    private String vendorCode;
    private int amount;

    public VendorProduct() {}
    public VendorProduct(String vendorCode, int amount) {

        this.vendorCode = vendorCode;
        this.amount     =     amount;
    }
    public VendorProduct(VendorProduct vendorProduct) {

        this.vendorCode = vendorProduct.getVendorCode();
        this.amount     =     vendorProduct.getAmount();
    }
    public int getAmount() {return this.amount;}
    public String getVendorCode() {return this.vendorCode;}
    public void setAmount(int amount) {this.amount = amount;}
    public void setVendorCode(String vendorCode) {this.vendorCode = vendorCode;}
}
