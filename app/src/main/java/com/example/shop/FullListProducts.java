package com.example.shop;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;


public class FullListProducts extends Fragment {

    EditText searchProduct;
    int size;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchProduct = (EditText) getActivity().findViewById(R.id.searchObject);
        Button button = getActivity().findViewById(R.id.fullProductListButton);
        size = this.getArguments().getInt("length_window");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View constraintView = inflater.inflate(R.layout.fragment_full_list_products, container, false);
        ConstraintLayout mainConstraintProductList =
                (ConstraintLayout) constraintView.findViewById(R.id.mainConstraintList);

        List<Product> products = new ArrayList<>();
        List<View> productObjectList = new ArrayList<>();

        try {

            productObjectList = InitializeProductList(size, products);
        } catch (InterruptedException e) {

            throw new RuntimeException(e);
        }


        ProductList productList = new ProductList(productObjectList, products);
        ProductList partProductList = new ProductList();
        List<View> saveObjects = new ArrayList<>();

        String save = "" + this.getArguments().getString("search_product");
        Log.d("SAVE", save + "");

        for(int i = 0; i < productObjectList.size(); i++) {

            if(save.regionMatches(true, 0,
                    products.get(i).getName(), 0, save.length()))
                saveObjects.add(productObjectList.get(i));
        }


        partProductList.setProductList(saveObjects);
        List<ConstraintLayout.LayoutParams> productsParams =
                CreateInterfaceListActivity(partProductList, size);

        int i = 0;
        for(i = 0; i < saveObjects.size(); i++)
            mainConstraintProductList.addView(partProductList.getProductList().get(i), productsParams.get(i));

        return constraintView;
    }


    public List<View> InitializeProductList(int size, List<Product> products) throws InterruptedException {

        LayoutInflater lI = getLayoutInflater();

        Thread threadURL = new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    String content = download("https://firebasestorage.googleapis.com/v0/b/shop-c93db.appspot.com/o/list_products.xml?alt=media&token=5816e1c0-db47-48c7-ae15-d5d89e9c21dc");

                            ProductsServerParser parser = new ProductsServerParser();
                            if(parser.parse(content)) {

                                for(ProductSave product : parser.getProducts())
                                  products.add(new Product(product.getName(), product.getPrice(),
                                                       product.getResourceDrawable(), lI, size));
                            }
                } catch (Exception e) {

                    Log.d("EXCEPTION", e.getMessage().toString());
                }
            }
        });

        threadURL.start();
        threadURL.join();

        List<View> productsList = new ArrayList<>();

        int indexProduct = 0;
        for(indexProduct = 0; indexProduct < products.size(); indexProduct++)
            productsList.add(products.get(indexProduct).CreateProduct());

        for(indexProduct = 0; indexProduct < products.size(); indexProduct++) {

            final int indexProductSave = indexProduct;
            productsList.get(indexProduct).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intentActivityProduct = new Intent(getActivity(), ProductActivity.class);
                    ProductSave productSave = new ProductSave(products.get(indexProductSave));
                    intentActivityProduct.putExtra(ProductSave.class.getSimpleName(), productSave);
                    intentActivityProduct.putExtra("full_screen_size", size);

                    startActivity(intentActivityProduct);
                }
            });
        }

        return productsList;
    }


    private String download(String urlPath) throws IOException {

        StringBuilder xmlResult = new StringBuilder();
        BufferedReader reader = null;
        InputStream stream = null;
        HttpsURLConnection connection = null;

        try {

            URL url = new URL(urlPath);
            connection = (HttpsURLConnection) url.openConnection();
            stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            String line;

            while ((line=reader.readLine()) != null)
                xmlResult.append(line);

            return xmlResult.toString();
        } finally {

            if (reader != null)
                reader.close();

            if (stream != null)
                stream.close();

            if (connection != null)
                connection.disconnect();
        }
    }

    public List<ConstraintLayout.LayoutParams> CreateInterfaceListActivity(ProductList productList, int size) {

        List<ConstraintLayout.LayoutParams> productListParams = new ArrayList<>();

        ImageView imageView = getActivity().findViewById(R.id.productImage);

        int indexProduct = 0;
        for (indexProduct = 0; indexProduct <  productList.getProductList().size(); indexProduct++) {

            ConstraintLayout.LayoutParams cur =
                    new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            productListParams.add(cur);

            if (indexProduct == 0) {

                productListParams.get(indexProduct).topToTop =
                        ConstraintLayout.LayoutParams.PARENT_ID;
                productListParams.get(indexProduct).leftToLeft =
                        ConstraintLayout.LayoutParams.PARENT_ID;
                productListParams.get(indexProduct).setMarginStart(33);
            } else if (indexProduct == 1) {

                productListParams.get(indexProduct).topToTop =
                        ConstraintLayout.LayoutParams.PARENT_ID;
                productListParams.get(indexProduct).leftToRight =
                        productList.getProductList().get(indexProduct - 1).getId();
                productListParams.get(indexProduct).setMarginStart(33);
            } else if (indexProduct % 2 == 0) {

                productListParams.get(indexProduct).topToBottom =
                        productList.getProductList().get(indexProduct - 2).getId();
                productListParams.get(indexProduct).leftToLeft =
                        ConstraintLayout.LayoutParams.PARENT_ID;
                productListParams.get(indexProduct).setMarginStart(33);
            } else if (indexProduct % 2 != 0) {

                productListParams.get(indexProduct).topToBottom =
                        productList.getProductList().get(indexProduct - 2).getId();
                productListParams.get(indexProduct).leftToRight =
                        productList.getProductList().get(indexProduct - 1).getId();
                productListParams.get(indexProduct).setMarginStart(33);
            }
        }

        return productListParams;
    }

}