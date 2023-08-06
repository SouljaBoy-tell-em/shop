package com.example.shop;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ProductsServerParser {

    private ArrayList<ProductSave> products;

    public ProductsServerParser() {

        products = new ArrayList<>();
    }

    public ArrayList<ProductSave> getProducts() {

        return products;
    }

    public boolean parse(String xmlData, String s) {

        boolean statusParse = true;
        boolean stateProduct = false;
        ProductSave currentProduct = null;
        String currentContent = "";

        boolean flag = false;

        try {

            XmlPullParserFactory xppFactory = XmlPullParserFactory.newInstance();
            xppFactory.setNamespaceAware(true);
            XmlPullParser xpp = xppFactory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {

                String tagName = xpp.getName();
                switch(eventType) {

                    case XmlPullParser.START_TAG:
                        if("product".equalsIgnoreCase(tagName)) {

                            stateProduct = true;
                            currentProduct = new ProductSave();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        currentContent = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(stateProduct) {

                            if("product".equalsIgnoreCase(tagName) && flag == true){

                                products.add(currentProduct);
                                stateProduct = false;
                                flag = false;
                            }

                            else if("product_name".equalsIgnoreCase(tagName) &&
                                    s.regionMatches(true, 0,
                                            currentContent, 0, s.length())) {

                                currentProduct.setName(currentContent);
                                flag = true;
                            }

                            else if("product_price".equalsIgnoreCase(tagName)) {

                                currentProduct.setPrice(Integer.parseInt(currentContent));
                            }

                            else if("product_image".equalsIgnoreCase(tagName)) {

                                currentProduct.setResourceDrawable(currentContent);
                            }
                        }
                        break;

                    default:
                        break;
                }

                eventType = xpp.next();
            }
        } catch (Exception e) {

            statusParse = false;
        }

        return statusParse;
    }
}