package com.example.shop;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class MainFragment extends Fragment{

    private final int DELAY_POST = 3000;
    private final int DELAY_CHANGE = 500;
    private int widthWindow;
    private int currentPage = 0;
    private boolean isCurrentPage = false;
    private ArrayList<Post> posts;
    private static DatabaseReference firebaseDBRef;
    private static SQLiteDatabase databaseSQL;
    private static BottomSheetDialog dialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            updateDatabaseSQL(getContext());
            widthWindow = MainActivity.getWidthWindow();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            parseInfoBlock(view, R.id.topInfoBlock, "ActualInfo");
            parseProductBlock(view, R.id.mainRecyclerView, "ActualProducts");
            parseProductBlock(view, R.id.discountProductRecyclerView, "DiscountProducts");
        }

        return view;
    }

    private void flipping(ProgressBar progressBar, ViewPager2 pagerActualInfo) {

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {

                pagerActualInfo.setCurrentItem(currentPage++, true);
                if(currentPage == posts.size() - 1)
                    isCurrentPage = true;
            }
        };

        pagerActualInfo.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                pagerActualInfo.setCurrentItem(position, true);
                currentPage = position;
                progressBarAnimation(progressBar, pagerActualInfo);
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                handler.post(update);
                if(isCurrentPage == true) {

                    isCurrentPage = false;
                    currentPage = 0;
                }
            }
        }, DELAY_CHANGE, DELAY_POST);
    }
    private void parseInfoBlock(View view, int idViewPager2, String branchName) {

        posts = new ArrayList<>();
        firebaseDBRef = FirebaseDatabase
                .getInstance()
                .getReference(branchName)
                .child("actual_info_top_main");
        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot curDataSnapshot : snapshot.getChildren()) {

                    Post post = curDataSnapshot.getValue(Post.class);
                    posts.add(post);
                }

                ViewPager2 pagerActualInfo = view.findViewById(idViewPager2);
                PageActualInfoAdapter pageAdapter = new PageActualInfoAdapter(getActivity(), posts);
                pagerActualInfo.setAdapter(pageAdapter);
                ProgressBar progressBar = view.findViewById(R.id.progressBar);
                flipping(progressBar, pagerActualInfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        firebaseDBRef.addValueEventListener(vel);
    }
    private static void parseProductBlock(View view, int idRecyclerView, String branchName) {

        RecyclerView recyclerView = view.findViewById(idRecyclerView);
        ArrayList<VendorProduct> products = new ArrayList<>();
        RecyclerView.LayoutManager doubleManager
                = new GridLayoutManager(recyclerView.getContext(), 2);
        recyclerView.setLayoutManager(doubleManager);

        firebaseDBRef = FirebaseDatabase.getInstance().getReference(branchName);
        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot curDataSnapshot : snapshot.getChildren()) {

                    VendorProduct product = curDataSnapshot.getValue(VendorProduct.class);
                    products.add(product);
                }

                MainProductListAdapter adapter =
                        new MainProductListAdapter(recyclerView.getContext(), products);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        firebaseDBRef.addValueEventListener(vel);
    }

    private void progressBarAnimation(ProgressBar progressBar, ViewPager2 pagerActualInfo) {

        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar,
                "progress", progressBar.getProgress(),
                currentPage * 100 / (posts.size() - 1));
        animation.setDuration(200);
        animation.setAutoCancel(true);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    public static void updateDatabaseSQL(Context context) {

        databaseSQL = context.openOrCreateDatabase("basket.db", MODE_PRIVATE, null);
        databaseSQL.execSQL("CREATE TABLE IF NOT EXISTS user_products(vendorCode TEXT, " +
                "                                           amount INTEGER, UNIQUE(vendorCode))");
        Cursor query = databaseSQL.rawQuery("SELECT * FROM user_products;", null);

        while(query.moveToNext()) {

            FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(FirebaseAuth.getInstance().getUid())
                    .child("Basket")
                    .child(query.getString(0))
                    .child("amount")
                    .setValue(query.getInt(1));
        }
    }
}