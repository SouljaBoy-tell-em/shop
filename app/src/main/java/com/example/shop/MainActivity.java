package com.example.shop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private FullListProducts fullListProductsFragment;
    private BasketFragment   basketFragment;
    private SQLiteDatabase dataBaseBasket;

    Product product;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screenParams();
        fullListProductsFragment = new FullListProducts();
        basketFragment           = new   BasketFragment();

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

        Log.d("SIZEX", size.x + "");

        dataBaseBasket =
                getBaseContext().openOrCreateDatabase("screen_params.db",
                                                      MODE_PRIVATE,
                                               null);
        dataBaseBasket.execSQL("CREATE TABLE IF NOT EXISTS" +
                " screen_params (width INTEGER, height INTEGER, UNIQUE(width))");
        dataBaseBasket.execSQL("INSERT OR IGNORE INTO screen_params VALUES" +
                " (" + size.x + ", " + size.y + ")");
        dataBaseBasket.close();
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

//            case R.id.profileNavigationBar:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.framelayout, fullListProductsFragment)
//                        .commit();
//                return true;
        }

        return false;
    }
}