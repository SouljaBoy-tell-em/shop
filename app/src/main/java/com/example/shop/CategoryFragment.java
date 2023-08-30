package com.example.shop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    private DatabaseReference firebaseDBRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);
        parseCategory(view, R.id.categoryRecyclerView, "Category");

        return view;
    }

    private void parseCategory(View view, int idRecyclerView, String branchName) {

        ArrayList<CategoryItem> categoryItems = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(idRecyclerView);
        RecyclerView.LayoutManager doubleManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(doubleManager);

        firebaseDBRef = FirebaseDatabase
                        .getInstance()
                        .getReference(branchName);
        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot curShapshot : snapshot.getChildren()) {

                    categoryItems.add(curShapshot.getValue(CategoryItem.class));
                }

                CategoryFragmentAdapter categoryFragmentAdapter =
                        new CategoryFragmentAdapter(getContext(), categoryItems,
                                                 MainActivity.getWidthWindow());
                recyclerView.setAdapter(categoryFragmentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        firebaseDBRef.addValueEventListener(vel);
    }
}