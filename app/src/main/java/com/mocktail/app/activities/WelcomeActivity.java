package com.mocktail.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import com.mocktail.app.R;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ProgressBar pb = findViewById(R.id.pb_loading);
        pb.setProgress(0);

        // Animate progress bar over 2.5 seconds
        new Thread(() -> {
            for (int i = 0; i <= 100; i += 2) {
                try {
                    Thread.sleep(50);
                    pb.setProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(() -> {
                // Since this was previously used for Login/Signup, we transition straight to LoginActivity.
                // The user can login from there or bypass if you build auto-login later.
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
            });
        }).start();
    }
}
