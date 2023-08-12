package com.example.shop;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static BottomNavigationView bottomNavigationView;
    private MainFragment fullListProductsFragment;
    private ProfileFragment profileFragment;
    private BasketFragment   basketFragment;
    private SQLiteDatabase dataBaseBasket;
    private static int widthWindow;
    private static int heightWindow;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FirebaseDatabase.getInstance().getReference("ProductList").child("Product-7").setValue((new Product("Rainbow Gamepad", 19000, 22000, "44", "orange", "https://firebasestorage.googleapis.com/v0/b/shop-c93db.appspot.com/o/products_image%2Fnewbalance327.png?alt=media&token=41951168-af92-4cb5-8911-95921b724abe", "Product-1")));

        screenParams();
        fullListProductsFragment = new MainFragment();
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

//            case R.id.discountNavigationBar:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.framelayout, fullListProductsFragment)
//                        .commit();
//                return true;

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