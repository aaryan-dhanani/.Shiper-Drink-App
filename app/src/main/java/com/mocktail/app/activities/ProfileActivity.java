package com.mocktail.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.mocktail.app.R;
import com.mocktail.app.utils.OrderManager;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        TextView tvOrderCount = findViewById(R.id.tv_order_count);
        TextView tvTotalSpent = findViewById(R.id.tv_total_spent);

        var orders = OrderManager.getInstance().getOrders();
        double totalSpent = 0;
        for (var order : orders) totalSpent += order.getTotalAmount();

        tvOrderCount.setText(String.valueOf(orders.size()));
        tvTotalSpent.setText(String.format("$%.2f", totalSpent));

        // Saved Addresses menu click -> open SavedAddressesActivity
        findViewById(R.id.btn_saved_addresses).setOnClickListener(v -> {
            startActivity(new Intent(this, SavedAddressesActivity.class));
        });

        // Bottom nav click handlers
        findViewById(R.id.nav_shop).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        findViewById(R.id.nav_search).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("FOCUS_SEARCH", true);
            startActivity(intent);
            finish();
        });
        findViewById(R.id.nav_cart).setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
            finish();
        });
    }
}
