package com.mocktail.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.mocktail.app.R;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.mocktail.app.database.DatabaseRepository;
import com.mocktail.app.models.CartItem;
import com.mocktail.app.models.Drink;
import com.mocktail.app.models.Order;

public class OrderSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        String orderId = getIntent().getStringExtra("order_id");
        double total = getIntent().getDoubleExtra("total", 0.0);
        String summary = getIntent().getStringExtra("summary");

        TextView tvOrderId = findViewById(R.id.tv_order_id);
        TextView tvTotal = findViewById(R.id.tv_success_total);
        TextView tvSummary = findViewById(R.id.tv_success_summary);
        ImageView ivCheck = findViewById(R.id.iv_check);
        LinearLayout llOrderedItems = findViewById(R.id.ll_ordered_items);

        tvOrderId.setText("Order " + orderId);
        tvTotal.setText(String.format("₹%.2f", total));
        tvSummary.setText(summary);

        Order order = DatabaseRepository.getInstance(this).getOrderById(orderId);
        if (order != null && order.getItems() != null && llOrderedItems != null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            for (CartItem item : order.getItems()) {
                View itemView = inflater.inflate(R.layout.item_order_success_drink, llOrderedItems, false);
                Drink drink = item.getDrink();
                
                ImageView ivPhoto = itemView.findViewById(R.id.iv_drink_photo);
                TextView tvName = itemView.findViewById(R.id.tv_drink_name);
                TextView tvDetails = itemView.findViewById(R.id.tv_drink_details);
                TextView tvPriceQty = itemView.findViewById(R.id.tv_drink_price_qty);
                
                tvName.setText(drink.getName());
                tvDetails.setText(drink.getIngredients());
                tvPriceQty.setText(item.getQuantity() + "x  ₹" + String.format("%.2f", drink.getPrice()));
                
                int imageResId = getResources().getIdentifier(drink.getImageUrl(), "drawable", getPackageName());
                if (imageResId != 0) {
                    Glide.with(this).load(imageResId).into(ivPhoto);
                } else {
                    Glide.with(this).load(R.drawable.drink1_midnight_sunrise).into(ivPhoto);
                }
                
                llOrderedItems.addView(itemView);
            }
        }



        // Animate checkmark
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        ivCheck.startAnimation(bounce);

        MaterialButton btnHome = findViewById(R.id.btn_back_home);
        MaterialButton btnHistory = findViewById(R.id.btn_view_history);

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(OrderSuccessActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    // onBackPressed overridden by Dispatcher callback in onCreate
}
