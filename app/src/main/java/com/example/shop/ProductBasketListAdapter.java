package com.example.shop;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductBasketListAdapter extends RecyclerView.Adapter<ProductBasketListAdapter.ViewHolder> {

    private Context context;
    private final LayoutInflater inflater;
    private final List<ProductSave> products;
    private ArrayList<Integer> amountProducts;
    private int size;
    final int maxAmountProducts = 10;
    SQLiteDatabase dbBasketProducts;
    public ProductBasketListAdapter(Context context, List<ProductSave> products, int size, TextView fullSum) {

        this.context  = context;
        this.products = products;
        this.inflater = LayoutInflater.from(context);
        this.size     =     size;

        this.amountProducts = new ArrayList<>();
        int i = 0;
        for(i = 0; i < this.products.size(); i++)
            this.amountProducts.add(1);
    }

    public ProductBasketListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.layout_backet_product, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ProductBasketListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        dbBasketProducts =
                this.context.openOrCreateDatabase("basket.db", MODE_PRIVATE, null);
        Cursor queryBasketProducts = dbBasketProducts.rawQuery("SELECT * FROM products;",
                null);

        int i = 0;
        while(queryBasketProducts.moveToNext()) {

            amountProducts.add(i, queryBasketProducts.getInt(4));
            i++;
        }

        ProductSave basketProduct = products.get(position);
        queryBasketProducts.moveToPosition(position);

        holder.basketProductName.setText(basketProduct.getName());
        holder.basketProductSize.setText("Размер: " + basketProduct.getSize());
        holder.basketProductColor.setText("Цвет: " + basketProduct.getColor());
        holder.basketProductPrice.setText(basketProduct.getPrice() + "₽");
        holder.basketOldProductPrice.setText(Html.fromHtml("<s>" + "5000₽" + "</s>"));
        holder.basketProductImage.getLayoutParams().width  = size / 3;
        holder.basketProductImage.getLayoutParams().height = size / 3;
        Picasso.get().load(basketProduct.getResourceDrawable()).into(holder.basketProductImage);

        holder.amountProductsTextView.setText(queryBasketProducts.getInt(4) + "");
        holder.productButtonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(amountProducts.get(position) < maxAmountProducts) {

                    amountProducts.set(position, amountProducts.get(position) + 1);
                    holder.amountProductsTextView.setText(amountProducts.get(position) + "");
                    ContentValues cv = new ContentValues();
                    cv.put("amount", amountProducts.get(position));
                    dbBasketProducts.update("products", cv, "name=?", new String[]{basketProduct.getName()});
                }
            }
        });

        holder.productButtonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(amountProducts.get(position) > 1) {

                    amountProducts.set(position, amountProducts.get(position) - 1);
                    holder.amountProductsTextView.setText(amountProducts.get(position) + "");
                    ContentValues cv = new ContentValues();
                    cv.put("amount", amountProducts.get(position));
                    dbBasketProducts.update("products", cv, "name=?", new String[]{basketProduct.getName()});
                }
            }
        });

    }


    public int getItemCount() {

        return products.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView basketProductName,
                       basketProductPrice,
                       basketProductSize,
                       basketProductColor,
                       basketOldProductPrice,
                       amountProductsTextView;
        final ImageView basketProductImage;
        final ImageButton productButtonNegative,
                          productButtonPositive;

        ViewHolder(View view) {

            super(view);

            basketProductName        = view.findViewById(R.id.basketProductName);
            basketProductPrice       = view.findViewById(R.id.basketProductPrice);
            basketProductSize        = view.findViewById(R.id.basketProductSize);
            basketProductColor       = view.findViewById(R.id.basketProductColor);
            basketOldProductPrice    = view.findViewById(R.id.basketOldProductPrice);
            amountProductsTextView   = view.findViewById(R.id.amountProductsView);
            basketProductImage       = view.findViewById(R.id.basketProductImage);
            productButtonNegative    = view.findViewById(R.id.productButtonNegative);
            productButtonPositive    = view.findViewById(R.id.productButtonPositive);
        }
    }
}
