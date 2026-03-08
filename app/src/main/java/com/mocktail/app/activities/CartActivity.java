package com.mocktail.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mocktail.app.R;
import com.mocktail.app.adapters.CartAdapter;
import com.mocktail.app.models.Order;
import com.mocktail.app.utils.CartManager;
import com.mocktail.app.utils.OrderManager;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rvCart;
    private TextView tvTotal, tvEmptyCart, tvItemCount;
    private Button btnOrder;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        loadCart();
    }

    private void initViews() {
        rvCart = findViewById(R.id.rv_cart);
        tvTotal = findViewById(R.id.tv_total);
        tvEmptyCart = findViewById(R.id.tv_empty_cart);
        tvItemCount = findViewById(R.id.tv_item_count);
        btnOrder = findViewById(R.id.btn_place_order);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        btnOrder.setOnClickListener(v -> placeOrder());

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
        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        });
    }

    private void loadCart() {
        CartManager cart = CartManager.getInstance();

        if (cart.getCartItems().isEmpty()) {
            rvCart.setVisibility(View.GONE);
            tvEmptyCart.setVisibility(View.VISIBLE);
            btnOrder.setEnabled(false);
            tvTotal.setText("Total: $0.00");
            tvItemCount.setText("0 items");
            return;
        }

        tvEmptyCart.setVisibility(View.GONE);
        rvCart.setVisibility(View.VISIBLE);
        btnOrder.setEnabled(true);

        cartAdapter = new CartAdapter(this, cart.getCartItems(), () -> loadCart());
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        rvCart.setAdapter(cartAdapter);

        tvTotal.setText(String.format("Total: $%.2f", cart.getTotalAmount()));
        tvItemCount.setText(cart.getTotalItemCount() + " items");
    }

    private void placeOrder() {
        CartManager cart = CartManager.getInstance();
        Order order = OrderManager.getInstance().placeOrder(cart.getCartItems(), cart.getTotalAmount());
        cart.clearCart();

        Intent intent = new Intent(this, OrderSuccessActivity.class);
        intent.putExtra("order_id", order.getOrderId());
        intent.putExtra("total", order.getTotalAmount());
        intent.putExtra("summary", order.getSummary());
        startActivity(intent);
        finish();
    }
}
