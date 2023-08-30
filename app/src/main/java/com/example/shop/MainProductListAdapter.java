package com.example.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainProductListAdapter extends RecyclerView.Adapter<MainProductListAdapter.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private ArrayList<Product> products;
    private ArrayList<VendorProduct> vendorProducts;
    private ArrayList<Boolean> isPressedBasketButton;
    private ArrayList<Boolean> isPressedFavoriteButton;
    private DatabaseReference firebaseDBRef;
    private BottomSheetDialog dialog;
    private ConstraintLayout constraintLayout;

    public MainProductListAdapter(Context context, ArrayList<VendorProduct> vendorProducts) {

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.vendorProducts = vendorProducts;
        this.dialog = new BottomSheetDialog(context);
        this.products           = new ArrayList<>();
        isPressedBasketButton   = new ArrayList<>();
        isPressedFavoriteButton = new ArrayList<>();
        for(int indexButton = 0; indexButton < vendorProducts.size(); indexButton++) {

            isPressedBasketButton.add(false);
            isPressedFavoriteButton.add(false);
        }
    }

    public MainProductListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.layout_product, parent, false);
        return new MainProductListAdapter.ViewHolder(view);
    }

    public void onBindViewHolder(MainProductListAdapter.ViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

        DatabaseReference refProducts = FirebaseDatabase.getInstance().getReference("ProductList");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot curDatasnapshot : snapshot.getChildren()) {

                    if(curDatasnapshot.getValue(Product.class).getVendorCode()
                              == vendorProducts.get(position).getVendorCode()) {

                        products.add(curDatasnapshot.getValue(Product.class));
                        products.get(position).setAnalogProducts(getAnalogProducts(curDatasnapshot, "analogProducts"));
                        products.get(position).setCharacteristics(getCharacteristics(curDatasnapshot));
                        products.get(position).setCharacteristicKeys(getCharacteristicKeys(curDatasnapshot, "characteristicKeys"));
                    }
                }

//                Collections.sort(products, new Comparator<Product>() {
//                    @Override
//                    public int compare(Product o1, Product o2) {
//
//                        return o1.getName().compareTo(o2.getName());
//                    }
//                });

                parseInfo(holder, isPressedBasketButton,   "Basket",   position);
                parseInfo(holder, isPressedFavoriteButton, "Favorite", position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        refProducts.addValueEventListener(valueEventListener);
    }

    @Override
    public int getItemCount() {
        return vendorProducts.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView productName,
                       productOldPrice,
                       productPrice;
        final ImageView productImage,
                        addRed,
                        whiteCart,
                        favoriteIcon,
                        favoriteIconPressed;
        final ConstraintLayout mainConstraintButtonPrice,
                               inBasketConstraint,
                               layoutProduct,
                               favoriteConstraint;

        ViewHolder(View view) {
            super(view);

            productName               = view.findViewById(R.id.productName);
            productOldPrice           = view.findViewById(R.id.productOldPrice);
            productPrice              = view.findViewById(R.id.productPrice);
            productImage              = view.findViewById(R.id.productImage);
            addRed                    = view.findViewById(R.id.addRed);
            whiteCart                 = view.findViewById(R.id.whiteCart);
            favoriteIcon              = view.findViewById(R.id.favouriteIcon);
            favoriteIconPressed       = view.findViewById(R.id.favouriteIconPressed);
            mainConstraintButtonPrice = view.findViewById(R.id.mainConstraintButtonPrice);
            inBasketConstraint        = view.findViewById(R.id.inBasketConstraint);
            layoutProduct             = view.findViewById(R.id.layoutProduct);
            favoriteConstraint        = view.findViewById(R.id.favouriteConstraint);
        }
    }

    private void acceptFavoriteProducts(MainProductListAdapter.ViewHolder holder, int position) {

        if(isPressedFavoriteButton.get(position) == true) {

            holder.favoriteIconPressed.setVisibility(View.VISIBLE);
            holder.favoriteIcon.setVisibility(View.INVISIBLE);
        }
    }

    private void acceptSimilarProducts(MainProductListAdapter.ViewHolder holder, int position) {

        if(isPressedBasketButton.get(position) == true) {

            holder.inBasketConstraint.setVisibility(View.VISIBLE);
            updateButtonAdd(holder, View.INVISIBLE,
                    R.drawable.price_item_product_list_constraint_layout_pressed);
        }
    }

    private void cancelFavoriteProducts(MainProductListAdapter.ViewHolder holder, int position) {

        FirebaseDatabase
                .getInstance()
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("Favorite")
                .child("Product-" + products.get(position).getVendorCode())
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if(task.getResult().getValue() == null) {

                            holder.favoriteIconPressed.setVisibility(View.INVISIBLE);
                            holder.favoriteIcon.setVisibility(View.VISIBLE);
                            isPressedFavoriteButton.set(position, false);
                        }
                    }
                });
    }

    private void cancelSimilarProducts(MainProductListAdapter.ViewHolder holder, int position) {

        FirebaseDatabase
                .getInstance()
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("Basket")
                .child("Product-" + products.get(position).getVendorCode())
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if(task.getResult().getValue() == null) {

                            holder.inBasketConstraint.setVisibility(View.INVISIBLE);
                            updateButtonAdd(holder, View.VISIBLE,
                                    R.drawable.price_item_product_list_constraint_layout);
                            isPressedBasketButton.set(position, false);
                        }
                    }
                });
    }

    private void checkInFirebase(int position, String branchName) {

        FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child(branchName)
                .child("Product-" + products.get(position).getVendorCode())
                .setValue(products.get(position).getParent());
    }

    private void createBottomSheet(int position, MainProductListAdapter.ViewHolder holder) {

        View view = inflater.inflate(R.layout.bottom_sheet_product_layout, null,
                                                                  false);
        createBottomSheetProduct(view, position);
        createBottomSheetAnalogProducts(view, position);
        createBottomSheetCharacteristics(view.findViewById(R.id.characteristicsConstraint), position);

        dialog.dismiss();
        dialog.setContentView(view);
    }

    private void createBottomSheetAnalogProducts(View view, int position) {

        RecyclerView colorCaseRecyclerView = view.findViewById(R.id.analogProductsRecyclerView);
        RecyclerView.LayoutManager horizontalManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        colorCaseRecyclerView.setLayoutManager(horizontalManager);
        ColorCaseAdapter colorCaseAdapter =
                new ColorCaseAdapter(context, products.get(position).getAnalogProducts());
        colorCaseRecyclerView.setAdapter(colorCaseAdapter);
    }

    private void createBottomSheetCharacteristics(ConstraintLayout constraintLayout,
                                                  int position) {

        RecyclerView characteristicRecyclerView = new RecyclerView(context);
        RecyclerView.LayoutManager horizontalManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        characteristicRecyclerView.setLayoutManager(horizontalManager);
        CharacteristicBottomSheetAdapter characteristicBottomSheetAdapter =
                new CharacteristicBottomSheetAdapter(context, products.get(position));
        characteristicRecyclerView.setAdapter(characteristicBottomSheetAdapter);
        constraintLayout.addView(characteristicRecyclerView);
    }

    private void createBottomSheetProduct(View view, int position) {

        ImageView productImage = view.findViewById(R.id.productImage);
        TextView productName = view.findViewById(R.id.productName);
        TextView productPrice = view.findViewById(R.id.productPrice);
        TextView productOldPrice = view.findViewById(R.id.productOldPrice);
        TextView productSummary = view.findViewById(R.id.productSummary);
        productImage.getLayoutParams().width  = MainActivity.getWidthWindow() - 100;
        productImage.getLayoutParams().height = MainActivity.getWidthWindow() - 100;
        Picasso.get().load(products.get(position).getResourceDrawable()).into(productImage);
        productName.setText(products.get(position).getName());
        productPrice.setText(products.get(position).getPrice() + " ₽");
        productOldPrice.setText(Html.fromHtml("<s>" + products.get(position).getOldPrice() + "</s>"));
        productSummary.getLayoutParams().width = view.findViewById(R.id.appCompatButton).getWidth();
    }

    private void createProductHolder(MainProductListAdapter.ViewHolder holder,
                          @SuppressLint("RecyclerView") int position) {

        Product product = products.get(position);
        Picasso.get().load(product.getResourceDrawable()).into(holder.productImage);
        holder.productImage.getLayoutParams().width  = MainActivity.getWidthWindow() / 2 - 50;
        holder.productImage.getLayoutParams().height = MainActivity.getWidthWindow() / 2 - 50;
        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice() + " ₽");
        holder.productOldPrice.setText(Html.fromHtml("<s>" + product.getOldPrice() + "</s>"));
        holder.inBasketConstraint.setVisibility(View.INVISIBLE);
        holder.favoriteIconPressed.setVisibility(View.INVISIBLE);

        acceptFavoriteProducts(holder, position);
        acceptSimilarProducts(holder,  position);
        cancelFavoriteProducts(holder, position);
        cancelSimilarProducts(holder,  position);

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createBottomSheet(position, holder);
                dialog.show();
            }
        });

        holder.mainConstraintButtonPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isPressedBasketButton.get(position) == false) {

                    isPressedBasketButton.set(position, true);
                    checkInFirebase(position, "Basket");

                    holder.inBasketConstraint.setVisibility(View.VISIBLE);
                    updateButtonAddAnimation(holder);
                    updateButtonAdd(holder, View.INVISIBLE,
                            R.drawable.price_item_product_list_constraint_layout_pressed);

                    return;
                }

                if (isPressedBasketButton.get(position) == true) {

                    isPressedBasketButton.set(position, false);
                    removeFromFirebase(position, "Basket");

                    holder.inBasketConstraint.setVisibility(View.INVISIBLE);
                    updateButtonAddAnimation(holder);
                    updateButtonAdd(holder, View.VISIBLE,
                            R.drawable.price_item_product_list_constraint_layout);
                }
            }
        });

        holder.favoriteConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isPressedFavoriteButton.get(position) == false) {

                    isPressedFavoriteButton.set(position, true);
                    checkInFirebase(position, "Favorite");

                    updateButtonFavoriteAnimation(holder, R.anim.favorite_button_pressed_anim);
                    holder.favoriteIconPressed.setVisibility(View.VISIBLE);
                    holder.favoriteIcon.setVisibility(View.INVISIBLE);

                    return;
                }

                if (isPressedFavoriteButton.get(position) == true) {

                    isPressedFavoriteButton.set(position, false);
                    removeFromFirebase(position, "Favorite");

                    updateButtonFavoriteAnimation(holder, R.anim.favorite_button_repressed_anim);
                    holder.favoriteIconPressed.setVisibility(View.INVISIBLE);
                    holder.favoriteIcon.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private ArrayList<VendorProduct> getAnalogProducts(DataSnapshot curDatasnapshot,
                                                                  String branchName) {

        ArrayList<VendorProduct> vendorProducts = new ArrayList<VendorProduct>();
        FirebaseDatabase
                .getInstance()
                .getReference("ProductList")
                .child("Product-" + curDatasnapshot.getValue(Product.class).getVendorCode())
                .child(branchName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot curSnapshot : snapshot.getChildren()) {

                            vendorProducts.add(curSnapshot.getValue(VendorProduct.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

        return vendorProducts;
    }
    private ArrayList<String> getCharacteristicKeys(DataSnapshot curDatasnapshot,
                                                       String branchName) {

        ArrayList<String> vendorProducts = new ArrayList<String>();
        FirebaseDatabase
                .getInstance()
                .getReference("ProductList")
                .child("Product-" + curDatasnapshot.getValue(Product.class).getVendorCode())
                .child(branchName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot curSnapshot : snapshot.getChildren()) {

                            vendorProducts.add(curSnapshot.getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

        return vendorProducts;
    }
    private Map<String, ArrayList<String>> getCharacteristics(DataSnapshot parentSnapshot) {

        Map<String, ArrayList<String>> characteristics = new HashMap<>();
        FirebaseDatabase
                .getInstance()
                .getReference("ProductList")
                .child("Product-" + parentSnapshot.getValue(Product.class).getVendorCode())
                .child("characteristics")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot curSnapshot : snapshot.getChildren()) {

                            characteristics.put((String) curSnapshot.getKey(), (ArrayList<String>) curSnapshot.getValue());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

        return characteristics;
    }
    private void parseInfo(MainProductListAdapter.ViewHolder holder, ArrayList<Boolean> isPressed,
                                                                    String parseIndex, int position) {

        firebaseDBRef = FirebaseDatabase
                .getInstance()
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child(parseIndex);
        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot curSnapshot : snapshot.getChildren()) {

                    Product product = curSnapshot.getValue(Product.class);
                    if(product.getVendorCode() == products.get(position).getVendorCode())
                        isPressed.set(position, true);
                }

                createProductHolder(holder, position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        firebaseDBRef.addValueEventListener(vel);
    }

    private void removeFromFirebase(int position, String branchName) {

        FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child(branchName)
                .child("Product-" + products.get(position).getVendorCode())
                .removeValue();
    }

    private void updateButtonAdd(MainProductListAdapter.ViewHolder holder, int status,
                                 int backgroundResourceButton) {

        holder.productPrice.setVisibility(status);
        holder.productOldPrice.setVisibility(status);
        holder.addRed.setVisibility(status);
        holder.mainConstraintButtonPrice.setBackgroundResource(backgroundResourceButton);
    }

    private void updateButtonAddAnimation(MainProductListAdapter.ViewHolder holder) {

            Animation buttonPressed = AnimationUtils.loadAnimation
                    (context, R.anim.constraint_button_price_item_anim);
            holder.mainConstraintButtonPrice.startAnimation(buttonPressed);
    }

    private void updateButtonFavoriteAnimation(MainProductListAdapter.ViewHolder holder, int anim) {

        Animation favoriteButtonAnim =
                AnimationUtils.loadAnimation(context, anim);
        holder.favoriteIconPressed.startAnimation(favoriteButtonAnim);
    }
}
