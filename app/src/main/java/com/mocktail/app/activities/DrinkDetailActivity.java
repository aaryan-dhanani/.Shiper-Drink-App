package com.mocktail.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mocktail.app.R;
import com.mocktail.app.models.Drink;
import com.mocktail.app.utils.CartManager;

public class DrinkDetailActivity extends AppCompatActivity {

    private ImageView ivDrinkImage, btnBack, btnDecrease, btnIncrease;
    private TextView tvName, tvPrice, tvDesc, tvRating, tvQuantity;
    private LinearLayout btnAddToCart, llIngredientsList;
    
    private Drink drink;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);

        drink = (Drink) getIntent().getSerializableExtra("drink");
        if (drink == null) {
            finish();
            return;
        }

        initViews();
        populateData();
        setupListeners();
    }

    private void initViews() {
        ivDrinkImage = findViewById(R.id.iv_detail_image);
        tvName = findViewById(R.id.tv_detail_name);
        tvPrice = findViewById(R.id.tv_detail_price);
        tvDesc = findViewById(R.id.tv_detail_desc);
        tvRating = findViewById(R.id.tv_detail_rating);
        llIngredientsList = findViewById(R.id.ll_ingredients_list);
        
        tvQuantity = findViewById(R.id.tv_detail_qty);
        btnBack = findViewById(R.id.btn_back);
        btnDecrease = findViewById(R.id.btn_decrease);
        btnIncrease = findViewById(R.id.btn_increase);
        btnAddToCart = findViewById(R.id.btn_detail_add_cart);
    }

    private void populateData() {
        tvName.setText(drink.getName());
        tvPrice.setText(String.format("$%.2f", drink.getPrice()));
        tvDesc.setText(drink.getDescription());
        tvRating.setText(String.valueOf(drink.getRating()));

        // Load image using Glide
        int imageResId = getResources().getIdentifier(drink.getImageUrl(), "drawable", getPackageName());
        if (imageResId != 0) {
            Glide.with(this).load(imageResId).into(ivDrinkImage);
        } else {
            Glide.with(this).load(R.drawable.drink1_midnight_sunrise).into(ivDrinkImage);
        }

        // Dynamically add ingredients
        String[] ingredientsList = drink.getIngredients().split(",");
        for (String ing : ingredientsList) {
            View view = getLayoutInflater().inflate(R.layout.item_ingredient, llIngredientsList, false);
            TextView tvIng = view.findViewById(R.id.tv_ingredient_name);
            tvIng.setText(ing.trim());
            llIngredientsList.addView(view);
        }
    }

    private void setupListeners() {
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
        
        if (btnIncrease != null) {
            btnIncrease.setOnClickListener(v -> {
                quantity++;
                tvQuantity.setText(String.valueOf(quantity));
                // Option: Update price in real-time or just add
            });
        }

        if (btnDecrease != null) {
            btnDecrease.setOnClickListener(v -> {
                if (quantity > 1) {
                    quantity--;
                    tvQuantity.setText(String.valueOf(quantity));
                }
            });
        }

        if (btnAddToCart != null) {
            btnAddToCart.setOnClickListener(v -> {
                for(int i=0; i<quantity; i++) {
                    CartManager.getInstance().addToCart(drink);
                }
                Toast.makeText(this, "✓ " + quantity + " " + drink.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}
