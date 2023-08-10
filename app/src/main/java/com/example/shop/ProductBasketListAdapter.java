package com.example.shop;

import static android.content.Context.MODE_PRIVATE;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.mbms.MbmsErrors;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductBasketListAdapter extends RecyclerView.Adapter<ProductBasketListAdapter.ViewHolder> {

    private Context context;
    private final LayoutInflater inflater;
    private ArrayList<Product> products;
    private ArrayList<Integer> amountProducts;
    private int size;
    private final int maxAmountProducts = 10;
    private SQLiteDatabase dbBasketProducts;
    private Cursor queryBasketProducts;
    private Button buyAllProductsButton;
    public ProductBasketListAdapter(Context context, ArrayList<Product> products, int size,
                                    Button buyAllProductsButton) {

        this.context  = context;
        this.products = products;
        this.inflater = LayoutInflater.from(context);
        this.size     =     size;

        this.amountProducts = new ArrayList<>();
        int indexProduct = 0;
        for(indexProduct = 0; indexProduct < this.products.size(); indexProduct++)
            this.amountProducts.add(1);

        this.buyAllProductsButton = buyAllProductsButton;
    }

    public ProductBasketListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.layout_backet_product, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ProductBasketListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        InitializeDBBasketProductsSQL();
        InitializeBasketProducts();
        buyAllProductsButton.setText("КУПИТЬ ВСЁ ЗА " + fullSumProducts() + " ₽");

        Product basketProduct = products.get(position);
        queryBasketProducts.moveToPosition(position);

        holder.constraintBasketProduct.getLayoutParams().width = size - 70;
        holder.basketProductName.setText(basketProduct.getName());
        holder.basketProductSize.setText("Размер: " + basketProduct.getSize());
        holder.basketProductColor.setText("Цвет: " + basketProduct.getColor());
        holder.basketProductPrice.setText(basketProduct.getPrice() + "₽");
        holder.basketOldProductPrice.setText(Html.fromHtml("<s>" + "5000₽" + "</s>"));
        holder.basketProductImage.getLayoutParams().width  = size / 3;
        holder.basketProductImage.getLayoutParams().height = size / 3;
        holder.returnTextView.setMaxWidth(400);
        Picasso.get().load(basketProduct.getResourceDrawable()).into(holder.basketProductImage);

        holder.amountProductsTextView.setText(queryBasketProducts.getInt(4) + "");
        updateFirebaseDB();
        holder.productButtonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(amountProducts.get(position) < maxAmountProducts)
                    updateAmountItem(holder, basketProduct,
                            amountProducts.get(position) + 1, position);
            }
        });

        holder.productButtonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(amountProducts.get(position) > 1)
                    updateAmountItem(holder, basketProduct,
                            amountProducts.get(position) - 1, position);
            }
        });

        holder.buyItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rejectionToast(v);
            }
        });

        buyAllProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rejectionToast(v);
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
                       amountProductsTextView,
                       returnTextView;
        final ImageView basketProductImage;
        final ImageButton productButtonNegative,
                          productButtonPositive;
        final Button buyItemButton;
        final ConstraintLayout constraintBasketProduct;

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
            buyItemButton            = view.findViewById(R.id.buyItemButton);
            constraintBasketProduct  = view.findViewById(R.id.constraintBasketProduct);
            returnTextView           = view.findViewById(R.id.returnTextView);
        }
    }
    private int fullSumProducts() {

        int fullSum = 0;

        int indexProduct = 0;
        for(indexProduct = 0; indexProduct < products.size(); indexProduct++)
            fullSum += products.get(indexProduct).getPrice() * amountProducts.get(indexProduct);

        return fullSum;
    }
    private void InitializeBasketProducts() {

        int indexAmountProducts = 0;
        while(queryBasketProducts.moveToNext()) {

            amountProducts.set(indexAmountProducts, queryBasketProducts.getInt(4));
            indexAmountProducts++;
        }
    }

    private void InitializeDBBasketProductsSQL() {

        dbBasketProducts =
                this.context.openOrCreateDatabase("basket.db", MODE_PRIVATE, null);
        queryBasketProducts = dbBasketProducts.rawQuery("SELECT * FROM products;",
                null);
    }
    private void rejectionToast(View view) {

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            Toast.makeText(view.getContext(), "Необходимо авторизоваться, " +
                    "чтобы оплатить заказ.", Toast.LENGTH_LONG).show();
    }
    private void updateAmountItem(ViewHolder holder, Product basketProduct, int addOrRemove,
                                  int position) {

        amountProducts.set(position, addOrRemove);
        holder.amountProductsTextView.setText(amountProducts.get(position) + "");
        ContentValues cv = new ContentValues();
        cv.put("amount", amountProducts.get(position));
        dbBasketProducts.update("products", cv, "name=?", new String[]{basketProduct.getName()});
        buyAllProductsButton.setText("КУПИТЬ ВСЁ ЗА " + fullSumProducts() + " ₽");
        updateFirebaseDB();
    }
    private void updateFirebaseDB() {

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            int indexProduct = 0;
            for(indexProduct = 0; indexProduct < amountProducts.size(); indexProduct++) {

                FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("Basket")
                        .child("product-" + (indexProduct + 1))
                        .setValue(products.get(indexProduct));
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("Users")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("Basket")
                        .child("product-" + (indexProduct + 1))
                        .child("amount")
                        .setValue(amountProducts.get(indexProduct));
            }
        }
    }
}
