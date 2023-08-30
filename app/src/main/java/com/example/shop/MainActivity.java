package com.example.shop;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static BottomNavigationView bottomNavigationView;
    private MainFragment fullListProductsFragment;
    private ProfileFragment profileFragment;
    private BasketFragment basketFragment;
    private CategoryFragment categoryFragment;
    private SQLiteDatabase dataBaseBasket;
    private static int widthWindow;
    private static int heightWindow;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ArrayList<String> characteristics = new ArrayList<>();
//        characteristics.add("characteristic-1");
//        characteristics.add("characteristic-2");
//        Map<String, ArrayList<String>> map = new HashMap<>();
//        map.put("memory", characteristics);
//        map.put("ram", characteristics);
//        ArrayList<VendorProduct> vendorProducts = new ArrayList<>();
//        vendorProducts.add(new VendorProduct(2, 1));
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("memory");
//        arrayList.add("ram");
//        FirebaseDatabase.getInstance().getReference("ProductList").child("Product-1").setValue(new Product("Xbox Series S white", 27000, 35000, "-",  2131034145, "https://firebasestorage.googleapis.com/v0/b/shop-c93db.appspot.com/o/products_image%2Fxbox_series_s.png?alt=media&token=c02b302b-271f-425e-8a4f-8909f42b6884", 1, 1, vendorProducts, map, arrayList));

        screenParams();
        fullListProductsFragment = new     MainFragment();
        categoryFragment         = new CategoryFragment();
        basketFragment           = new   BasketFragment();
        profileFragment          = new  ProfileFragment();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.mainListNavigationBar);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        dataBaseBasket.close();
    }

    private void screenParams() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size      =                            new Point();
        display.getSize(size);
        widthWindow     = size.x;
        heightWindow    = size.y;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.mainListNavigationBar:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framelayout, fullListProductsFragment)
                        .commit();
                return true;

            case R.id.categoryNavigationBar:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framelayout, categoryFragment)
                        .commit();
                return true;

            case R.id.basketNavigationBar:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framelayout, basketFragment)
                        .commit();
                return true;

            case R.id.profileNavigationBar:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framelayout, profileFragment)
                        .commit();
                return true;
        }

        return false;
    }

    public static int getWidthWindow() {

        return widthWindow;
    }

    public static int getHeightWindow() {

        return heightWindow;
    }

    public static int getHeightBNV() {

        return bottomNavigationView.getHeight();
    }
}