package com.mocktail.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mocktail.app.R;
import com.mocktail.app.adapters.OrderAdapter;
import com.mocktail.app.database.DatabaseRepository;
import com.mocktail.app.models.Order;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView rvOrders = findViewById(R.id.rv_orders);
        TextView tvEmpty      = findViewById(R.id.tv_empty_history);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Bottom nav
        findViewById(R.id.nav_shop).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class)); finish();
        });
        findViewById(R.id.nav_search).setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("FOCUS_SEARCH", true); startActivity(i); finish();
        });
        findViewById(R.id.nav_history).setOnClickListener(v -> { /* already here */ });
        findViewById(R.id.nav_cart).setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class)); finish();
        });
        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class)); finish();
        });

        // ✅ Load orders from SQLite (persisted across restarts)
        List<Order> orders = DatabaseRepository.getInstance(this).getAllOrders();

        if (orders.isEmpty()) {
            rvOrders.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvOrders.setVisibility(View.VISIBLE);
            rvOrders.setLayoutManager(new LinearLayoutManager(this));
            rvOrders.setAdapter(new OrderAdapter(this, orders));
        }
    }
}
