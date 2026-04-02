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
import com.mocktail.app.database.DatabaseRepository;
import com.mocktail.app.models.Order;
import com.mocktail.app.utils.CartManager;
import com.mocktail.app.utils.OrderManager;

public class CartActivity extends AppCompatActivity {

    private static final double DELIVERY_FEE = 8.00;

    private RecyclerView rvCart;
    private TextView tvTotal, tvEmptyCart, tvItemCount, tvSubtotal, tvDeliveryFee;
    private Button btnOrder;
    private CartAdapter cartAdapter;
    private DatabaseRepository db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        db = DatabaseRepository.getInstance(this);
        initViews();
        loadCart();
    }

    private void initViews() {
        rvCart       = findViewById(R.id.rv_cart);
        tvTotal      = findViewById(R.id.tv_total);
        tvEmptyCart  = findViewById(R.id.tv_empty_cart);
        tvItemCount  = findViewById(R.id.tv_item_count);
        tvSubtotal   = findViewById(R.id.tv_subtotal);
        tvDeliveryFee = findViewById(R.id.tv_delivery_fee);
        btnOrder     = findViewById(R.id.btn_place_order);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        btnOrder.setOnClickListener(v -> placeOrder());

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
        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class)); finish();
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
            if (tvSubtotal != null) tvSubtotal.setText("$0.00");
            if (tvDeliveryFee != null) tvDeliveryFee.setText("$0.00");
            return;
        }
        tvEmptyCart.setVisibility(View.GONE);
        rvCart.setVisibility(View.VISIBLE);
        btnOrder.setEnabled(true);
        cartAdapter = new CartAdapter(this, cart.getCartItems(), () -> loadCart());
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        rvCart.setAdapter(cartAdapter);

        double subtotal = cart.getTotalAmount();
        double total    = subtotal + DELIVERY_FEE;
        if (tvSubtotal != null) tvSubtotal.setText(String.format("$%.2f", subtotal));
        if (tvDeliveryFee != null) tvDeliveryFee.setText(String.format("$%.2f", DELIVERY_FEE));
        tvTotal.setText(String.format("Total: $%.2f", total));
        tvItemCount.setText(cart.getTotalItemCount() + " items");
    }

    private void placeOrder() {
        CartManager cart = CartManager.getInstance();
        Order order      = OrderManager.getInstance().placeOrder(cart.getCartItems(), cart.getTotalAmount());

        // ✅ Persist order to SQLite
        db.saveOrder(order);

        cart.clearCart();

        Intent intent = new Intent(this, OrderSuccessActivity.class);
        intent.putExtra("order_id", order.getOrderId());
        intent.putExtra("total",    order.getTotalAmount());
        intent.putExtra("summary",  order.getSummary());
        startActivity(intent);
        finish();
    }
}
