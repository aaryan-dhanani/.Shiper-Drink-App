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

        tvOrderId.setText("Order " + orderId);
        tvTotal.setText(String.format("$%.2f", total));
        tvSummary.setText(summary);

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
