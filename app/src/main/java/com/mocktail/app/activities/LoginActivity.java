package com.mocktail.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.mocktail.app.R;
import com.mocktail.app.database.DatabaseRepository;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private ImageView ivTogglePassword;
    private boolean isPasswordVisible = false;
    private DatabaseRepository db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db         = DatabaseRepository.getInstance(this);
        etEmail    = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        ivTogglePassword = findViewById(R.id.iv_toggle_password_login);

        // 👁 Eye icon — toggle password visibility
        ivTogglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                ivTogglePassword.setAlpha(0.5f);
                isPasswordVisible = false;
            } else {
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                ivTogglePassword.setAlpha(1.0f);
                isPasswordVisible = true;
            }
            etPassword.setSelection(etPassword.getText().length());
        });

        // Log In button
        findViewById(R.id.btn_do_login).setOnClickListener(v -> {
            if (validateLogin()) {
                String email    = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (db.loginUser(email, password)) {
                    // Save logged-in email to SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("mocktail_prefs", MODE_PRIVATE);
                    prefs.edit().putString("logged_email", email).apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "❌ Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Go to Signup
        findViewById(R.id.tv_signup_link).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });

        // Back button
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private boolean validateLogin() {
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            etEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }
}
