package com.example.shop;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int widthWindow;
    String searchProductKey;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        searchProductKey = "";
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        widthWindow = widthWindow();
        FullListProducts fullListProductsFragment = new FullListProducts();
        Bundle bundle = new Bundle();
        bundle.putInt("length_window", widthWindow);
        bundle.putString("search_product", searchProductKey);
        fullListProductsFragment.setArguments(bundle);
        setNewFragment(fullListProductsFragment);

        profileButtonActivate();
    }

    @Override
    protected void onResume() {
        super.onResume();


        Button fullListProductButton = (Button) findViewById(R.id.fullProductListButton);
        Button profileButton = (Button) findViewById(R.id.profile);

        EditText searchProduct = (EditText) findViewById(R.id.searchObject);

        ImageView imageLogo = (ImageView) findViewById(R.id.imageView);
        searchProduct.getLayoutParams().width  = widthWindow - 50;

        FrameLayout mainFrame = (FrameLayout) findViewById(R.id.framelayout);
        mainFrame.getLayoutParams().height = heightWindow() - imageLogo.getLayoutParams().height -
                searchProduct.getLayoutParams().height - 100;
        Log.d("HEIGHT FRAGMENT", mainFrame.getLayoutParams().height + "");

        searchProduct.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                    Log.d("PRESSED", "PRESSED");

                    FullListProducts fullListProductsFragment = new FullListProducts();
                    Bundle bundle = new Bundle();
                    bundle.putInt("length_window", widthWindow);
                    bundle.putString("search_product", searchProduct.getText().toString());
                    searchProductKey = searchProduct.getText().toString();
                    fullListProductsFragment.setArguments(bundle);
                    setNewFragment(fullListProductsFragment);

                    return true;
                }

                return false;
            }
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString("key_save_explore", searchProductKey);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

        searchProductKey = savedInstanceState.getString("key_save_explore");
    }

    private int widthWindow() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        return width;
    }
    private int heightWindow() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        return height;
    }
    private void setNewFragment(Fragment fragment) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout, fragment);
        ft.commit();
    }

    public void profileButtonActivate() {

        ImageView profileButton = (ImageView) findViewById(R.id.profileElement);
        Intent profileActivityIntent = new Intent(this, Profile.class);

        profileButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                startActivity(profileActivityIntent);
            }
        });
    }
}