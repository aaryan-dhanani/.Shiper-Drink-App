package com.mocktail.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.mocktail.app.R;
import com.mocktail.app.database.DatabaseRepository;
import com.mocktail.app.models.Order;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // ✅ Load stats from SQLite
        List<Order> orders = DatabaseRepository.getInstance(this).getAllOrders();
        double totalSpent  = 0;
        for (Order o : orders) totalSpent += o.getTotalAmount();

        TextView tvOrderCount = findViewById(R.id.tv_order_count);
        TextView tvTotalSpent = findViewById(R.id.tv_total_spent);
        tvOrderCount.setText(String.valueOf(orders.size()));
        tvTotalSpent.setText(String.format("₹%.2f", totalSpent));

        // Show logged-in user name and email if available
        SharedPreferences prefs = getSharedPreferences("mocktail_prefs", MODE_PRIVATE);
        String email = prefs.getString("logged_email", null);
        if (email != null) {
            String name = DatabaseRepository.getInstance(this).getUserName(email);
            TextView tvName  = findViewById(R.id.tv_profile_name);
            TextView tvEmail = findViewById(R.id.tv_profile_email);
            if (tvName  != null && name  != null) tvName.setText(name);
            if (tvEmail != null) tvEmail.setText(email);
        }

        // Saved Addresses
        findViewById(R.id.btn_saved_addresses).setOnClickListener(v ->
                startActivity(new Intent(this, SavedAddressesActivity.class)));

        // Order History
        findViewById(R.id.btn_order_history).setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));

        // Logout — clear session
        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            getSharedPreferences("mocktail_prefs", MODE_PRIVATE).edit().clear().apply();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Bottom nav
        findViewById(R.id.nav_shop).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class)); finish();
        });
        findViewById(R.id.nav_search).setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("FOCUS_SEARCH", true); startActivity(i); finish();
        });
        findViewById(R.id.nav_history).setOnClickListener(v -> {
            startActivity(new Intent(this, HistoryActivity.class)); finish();
        });
        findViewById(R.id.nav_cart).setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class)); finish();
        });
    }
}
