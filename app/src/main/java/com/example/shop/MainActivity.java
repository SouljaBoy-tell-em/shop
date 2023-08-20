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
import android.view.Display;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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

        //FirebaseDatabase.getInstance().getReference("Category").child("Category-2").setValue((new CategoryItem("https://firebasestorage.googleapis.com/v0/b/shop-c93db.appspot.com/o/category%2Fcategory.JPG?alt=media&token=92b0041c-3ee0-4c7d-b47d-3526eb617bb2", "Обувь 2")));

        screenParams();
        fullListProductsFragment = new     MainFragment();
        categoryFragment         = new CategoryFragment();
        basketFragment           = new   BasketFragment();
        profileFragment          = new  ProfileFragment();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.profileNavigationBar);
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