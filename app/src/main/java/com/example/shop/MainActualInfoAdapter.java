package com.example.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActualInfoAdapter extends RecyclerView.Adapter<MainActualInfoAdapter.ViewHolder> {

    private Context context;
    private final LayoutInflater inflater;
    public MainActualInfoAdapter(Context context) {

        this.context  = context;
        this.inflater = LayoutInflater.from(context);
    }

    public MainActualInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.layout_actual_info, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(MainActualInfoAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

    }

    public int getItemCount() {

        return 10;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        final ConstraintLayout post;

        ViewHolder(View view) {
            super(view);

            post = view.findViewById(R.id.post);
        }
    }

}
