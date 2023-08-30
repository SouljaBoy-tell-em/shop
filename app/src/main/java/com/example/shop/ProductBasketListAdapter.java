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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductBasketListAdapter extends RecyclerView.Adapter<ProductBasketListAdapter.ViewHolder> {

    private Context context;
    private final LayoutInflater inflater;
    private ArrayList<VendorProduct> vendorProducts;
    private ArrayList<Product> products;
    private ArrayList<Integer> amountProducts;
    private int size;
    private int MAX_PRODUCTS = 10;
    private SQLiteDatabase databaseSQL;
    private Button buyAllProductsButton;
    private DatabaseReference firebaseDBRef;
    private NestedScrollView nestedScrollView;
    public ProductBasketListAdapter(Context context, ArrayList<VendorProduct> vendorProducts,
                                    int size, Button buyAllProductsButton, NestedScrollView nestedScrollView) {

        this.context  = context;
        this.vendorProducts = vendorProducts;
        this.products = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.size     = size;
        this.nestedScrollView = nestedScrollView;
        this.buyAllProductsButton = buyAllProductsButton;
        this.databaseSQL = context.openOrCreateDatabase("basket.db", MODE_PRIVATE, null);
        this.databaseSQL.delete("user_products", null, null);
        this.databaseSQL.execSQL("CREATE TABLE IF NOT EXISTS user_products(vendorCode TEXT, " +
                "                                         amount INTEGER, UNIQUE(vendorCode))");

        this.amountProducts = new ArrayList<>();
        for(int indexAmountProducts = 0; indexAmountProducts < vendorProducts.size();
                                                               indexAmountProducts++) {

            this.amountProducts.add(vendorProducts.get(indexAmountProducts).getAmount());
            databaseSQL.execSQL("INSERT OR IGNORE INTO user_products VALUES" +
                    "('Product-" + vendorProducts.get(indexAmountProducts).getVendorCode() + "', " +
                    + vendorProducts.get(indexAmountProducts).getAmount() + ");");
        }
    }

    public ProductBasketListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.layout_backet_product, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ProductBasketListAdapter.ViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

        firebaseDBRef = FirebaseDatabase.getInstance().getReference("ProductList");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot curSnapshot : snapshot.getChildren()) {

                    if(curSnapshot.getValue(Product.class).getVendorCode() == vendorProducts.get(position).getVendorCode()) {

                        products.add(curSnapshot.getValue(Product.class));
                        createBasketProduct(holder, position);
                    }
                }

                nestedScrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        nestedScrollView.smoothScrollTo(0, buyAllProductsButton.getBottom());
                    }
                }, 100);
                fullSum();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        firebaseDBRef.addValueEventListener(valueEventListener);
    }

    public int getItemCount() {

        return vendorProducts.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView basketProductName,
                basketProductPrice,
                basketProductSize,
                basketProductColor,
                basketOldProductPrice,
                amountProductsTextView,
                returnTextView;
        final ImageView basketProductImage,
                        deleteBusketProductButton;
        final ImageButton productButtonNegative,
                productButtonPositive;
        final Button buyItemButton;
        final ConstraintLayout constraintBasketProduct,
                               mainConstraintBasketProduct;
        ViewHolder(View view) {

            super(view);

            basketProductName           = view.findViewById(R.id.basketProductName);
            basketProductPrice          = view.findViewById(R.id.basketProductPrice);
            basketProductSize           = view.findViewById(R.id.basketProductSize);
            basketProductColor          = view.findViewById(R.id.basketProductColor);
            basketOldProductPrice       = view.findViewById(R.id.basketOldProductPrice);
            amountProductsTextView      = view.findViewById(R.id.amountProductsView);
            basketProductImage          = view.findViewById(R.id.basketProductImage);
            deleteBusketProductButton   = view.findViewById(R.id.deleteBasketProductButton);
            productButtonNegative       = view.findViewById(R.id.productButtonNegative);
            productButtonPositive       = view.findViewById(R.id.productButtonPositive);
            buyItemButton               = view.findViewById(R.id.buyItemButton);
            constraintBasketProduct     = view.findViewById(R.id.constraintBasketProduct);
            mainConstraintBasketProduct = view.findViewById(R.id.mainConstraintBasketProduct);
            returnTextView              = view.findViewById(R.id.returnTextView);
        }
    }
    private void createBasketProduct(ProductBasketListAdapter.ViewHolder holder, int position) {

        try {

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.create_basket_product_anim);
            holder.mainConstraintBasketProduct.startAnimation(animation);
            Product basketProduct = products.get(position);
            holder.constraintBasketProduct.getLayoutParams().width = size - 70;
            holder.basketProductName.setText(basketProduct.getName());
            holder.basketProductSize.setText("Размер: " + basketProduct.getSize());
            holder.basketProductColor.setText("Цвет: " + basketProduct.getColor());
            holder.basketProductPrice.setText(basketProduct.getPrice() + "₽");
            holder.basketOldProductPrice.setText(Html.fromHtml("<s>" + basketProduct.getOldPrice() + "</s>"));
            holder.basketProductImage.getLayoutParams().width  = size / 3;
            holder.basketProductImage.getLayoutParams().height = size / 3;
            holder.amountProductsTextView.setText(vendorProducts.get(position).getAmount() + "");
            holder.returnTextView.setMaxWidth(400);
            Picasso.get().load(basketProduct.getResourceDrawable()).into(holder.basketProductImage);

            holder.productButtonPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(amountProducts.get(position) < MAX_PRODUCTS)
                        updateDBAmount(holder, position, true);
                }
            });

            holder.productButtonNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(amountProducts.get(position) > 1)
                        updateDBAmount(holder, position, false);
                }
            });

            holder.deleteBusketProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteProductThread(holder, basketProduct);
                }
            });

        } catch (Exception e) {

            Log.d("EXCEPTION", e.getMessage());
        }
    }

    private void deleteProductThread(ProductBasketListAdapter.ViewHolder holder, Product product) {

        holder.deleteBusketProductButton.setClickable(false);
        Thread deleteThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                FirebaseDatabase
                        .getInstance()
                        .getReference("Users")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("Basket")
                        .child("Product-" + product.getVendorCode())
                        .removeValue();
            }
        });

        Animation deleteProductAnim = AnimationUtils.loadAnimation
                (context, R.anim.delete_basket_product_anim);
        holder.mainConstraintBasketProduct.startAnimation(deleteProductAnim);
        deleteThread.start();
    }

    private void fullSum() {

        int sum = 0;
        for(int indexProduct = 0; indexProduct < products.size(); indexProduct++)
            sum += vendorProducts.get(indexProduct).getAmount() * products.get(indexProduct).getPrice();
        buyAllProductsButton.setText("КУПИТЬ ВСЁ ЗА " + sum + "₽");
    }

    private void updateDBAmount(@NonNull ProductBasketListAdapter.ViewHolder holder, int position, boolean add) {

        amountProducts.set(position, (add == true) ? amountProducts.get(position) + 1 :
                                                     amountProducts.get(position) - 1);
        holder.amountProductsTextView.setText(amountProducts.get(position) + "");
        vendorProducts.get(position).setAmount(amountProducts.get(position));

        ContentValues cv = new ContentValues();
        cv.put("amount", vendorProducts.get(position).getAmount());
        databaseSQL.update("user_products", cv, "vendorCode=?",
                          new String[]{"Product-" + vendorProducts.get(position).getVendorCode()});
        fullSum();
    }
}
