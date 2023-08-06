package com.example.shop;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

    int widthWindow;
    SearchView searchProduct;
    List<Product> products;
    List<View> productObjectList;
    List<ConstraintLayout.LayoutParams> productsParams;
    ProductList productList;
    SQLiteDatabase dbScreenParams;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbScreenParams = getActivity().getBaseContext().
                openOrCreateDatabase("screen_params.db", Context.MODE_PRIVATE, null);
        Cursor query = dbScreenParams.rawQuery("SELECT * FROM screen_params;", null);
        query.moveToNext();
        widthWindow = query.getInt(0);

        products = new ArrayList<>();
        productObjectList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View constraintView = inflater.inflate(R.layout.fragment_full_list_products, container,
                                                                            false);
        ConstraintLayout mainConstraintProductList =
                        (ConstraintLayout) constraintView.findViewById(R.id.constraintScrollView);
        ImageView imageLogo = (ImageView) constraintView.findViewById(R.id.imageLogo);

        searchProduct = constraintView.findViewById(R.id.searchProduct);
        searchProduct.getLayoutParams().width =widthWindow - 50;
        searchProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                try {

                    productObjectList.clear();
                    products.clear();
                    productObjectList = InitializeProductList(widthWindow, products, newText);
                } catch (InterruptedException e) {

                    throw new RuntimeException(e);
                }

                productList = new ProductList(productObjectList, products);

                productsParams = CreateInterfaceListActivity(productList, widthWindow);
                mainConstraintProductList.removeAllViewsInLayout();

                int indexProductObject = 0;
                for(indexProductObject = 0; indexProductObject < productObjectList.size();
                                                                     indexProductObject++)
                    mainConstraintProductList.addView(productList.getProductList().get(indexProductObject),
                                                                   productsParams.get(indexProductObject));

                Log.d("SIZE", productObjectList.size() + "");

                return true;
            }
        });

        return constraintView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        dbScreenParams.close();
    }

    public List<View> InitializeProductList(int size, List<Product> products, String s)
                                            throws InterruptedException {

        LayoutInflater lI = getLayoutInflater();

        Thread threadURL = new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    String content = download("https://firebasestorage.googleapis.com/v0/b/shop-c93db.appspot.com/o/list_products.xml?alt=media&token=ef495c8e-8b38-48c9-a16c-a7b0e464d863");
                            ProductsServerParser parser = new ProductsServerParser();
                            if(parser.parse(content, s)) {

                                for(ProductSave product : parser.getProducts())
                                  products.add(new Product(product.getName(), product.getPrice(),
                                                       "XL", "black", product.getResourceDrawable(), lI, size));
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

    public List<ConstraintLayout.LayoutParams> CreateInterfaceListActivity(ProductList productList,
                                                                           int size) {

        List<ConstraintLayout.LayoutParams> productListParams = new ArrayList<>();

        int indentWidth = (size - 2 * productList.getProducts().get(0).imageWidth) / 3;
        Log.d("WIDTH", indentWidth + "");

        int  indexProduct = 0;
        for (indexProduct = 0; indexProduct <  productList.getProductList().size(); indexProduct++) {

            ConstraintLayout.LayoutParams cur =
                    new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            productListParams.add(cur);

            if (indexProduct == 0) {

                productListParams.get(indexProduct).topToTop =
                        ConstraintLayout.LayoutParams.PARENT_ID;
                productListParams.get(indexProduct).startToStart =
                        ConstraintLayout.LayoutParams.PARENT_ID;
                productListParams.get(indexProduct).leftMargin = indentWidth;
            }

            else if (indexProduct == 1) {

                productListParams.get(indexProduct).topToTop =
                        ConstraintLayout.LayoutParams.PARENT_ID;
                productListParams.get(indexProduct).endToEnd =
                        ConstraintLayout.LayoutParams.PARENT_ID;
                productListParams.get(indexProduct).rightMargin = indentWidth;
            } else {

                productListParams.get(indexProduct).topToBottom =
                        productList.getProductList().get(indexProduct - 2).getId();
                productListParams.get(indexProduct).startToStart =
                        productList.getProductList().get(indexProduct - 2).getId();
            }
        }

        return productListParams;
    }
}