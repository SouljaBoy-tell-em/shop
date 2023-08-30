package com.example.shop;

import android.widget.TextView;

public class CategoryItem {

    private String categoryImage;
    private String  categoryText;

    public CategoryItem(){}
    public CategoryItem(String categoryImage, String categoryText) {

        this.categoryImage = categoryImage;
        this.categoryText  =  categoryText;
    }
    public CategoryItem(CategoryItem categoryItem) {
        this.categoryImage = categoryItem.getCategoryImage();
        this.categoryText  = categoryItem.getCategoryText();
    }

    public String getCategoryImage() {return categoryImage;}
    public String getCategoryText() {return categoryText;}
    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }
    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }
}
