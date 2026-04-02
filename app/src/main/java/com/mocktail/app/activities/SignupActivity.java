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

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private ImageView ivTogglePassword;
    private boolean isPasswordVisible = false;
    private DatabaseRepository db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db                = DatabaseRepository.getInstance(this);
        etName            = findViewById(R.id.et_name);
        etEmail           = findViewById(R.id.et_email);
        etPassword        = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        ivTogglePassword  = findViewById(R.id.iv_toggle_password);

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

        // Sign Up button
        findViewById(R.id.btn_do_signup).setOnClickListener(v -> {
            if (validateSignup()) {
                String name     = etName.getText().toString().trim();
                String email    = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                boolean registered = db.registerUser(name, email, password);
                if (registered) {
                    // Auto-login after successful signup
                    SharedPreferences prefs = getSharedPreferences("mocktail_prefs", MODE_PRIVATE);
                    prefs.edit().putString("logged_email", email).apply();

                    Toast.makeText(this, "✅ Account created! Welcome, " + name + "!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    etEmail.setError("This email is already registered");
                    etEmail.requestFocus();
                    Toast.makeText(this, "❌ Email already exists. Please log in.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Go to Login
        findViewById(R.id.tv_login_link).setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private boolean validateSignup() {
        String name            = etName.getText().toString().trim();
        String email           = etEmail.getText().toString().trim();
        String password        = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Full name is required");
            etName.requestFocus();
            return false;
        }
        if (name.length() < 2) {
            etName.setError("Name must be at least 2 characters");
            etName.requestFocus();
            return false;
        }
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
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Please confirm your password");
            etConfirmPassword.requestFocus();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }
}
