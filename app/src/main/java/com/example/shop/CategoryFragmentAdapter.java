package com.example.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryFragmentAdapter extends RecyclerView.Adapter<CategoryFragmentAdapter.ViewHolder> {

    private Context context;
    private final LayoutInflater inflater;
    private ArrayList<CategoryItem> categoryItems;
    private int size;
    public CategoryFragmentAdapter(Context context, ArrayList<CategoryItem> categoryItems, int size) {

        this.context       = context;
        this.categoryItems = categoryItems;
        this.inflater      = LayoutInflater.from(context);
        this.size          = size;
    }

    public CategoryFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.layout_category, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(CategoryFragmentAdapter.ViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {
        CategoryItem categoryItem = categoryItems.get(position);
        Picasso.get().load(categoryItem.getCategoryImage()).into(holder.categoryImage);
        holder.categoryCardView.getLayoutParams().width = size / 2 - 40;
        holder.categoryCardView.getLayoutParams().height = size / 2 - 40;
        holder.categoryText.setText(categoryItem.getCategoryText());

        holder.constraintCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public int getItemCount() {

        return categoryItems.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        final ConstraintLayout constraintCategory;
        final CardView categoryCardView;
        final TextView categoryText;
        final ImageView categoryImage;

        ViewHolder(View view) {

            super(view);
            constraintCategory = view.findViewById(R.id.constraintCategory);
            categoryCardView   = view.findViewById(R.id.categoryCardView);
            categoryImage      = view.findViewById(R.id.categoryImage);
            categoryText       = view.findViewById(R.id.categoryText);
        }
    }
}
