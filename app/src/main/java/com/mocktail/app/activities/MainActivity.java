package com.mocktail.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mocktail.app.R;
import com.mocktail.app.adapters.DrinkAdapter;
import com.mocktail.app.models.Drink;
import com.mocktail.app.utils.CartManager;
import com.mocktail.app.utils.DrinkData;
import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvDrinks;
    private EditText etSearch;
    private TextView tvResultCount;
    private DrinkAdapter drinkAdapter;
    private LinearLayout llCategories;
    
    private String currentCategory = "All";
    private List<TextView> categoryChips = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupCategories();
        setupRecyclerView();
        setupSearch();

        if (getIntent().getBooleanExtra("FOCUS_SEARCH", false)) {
            etSearch.requestFocus();
        }
    }

    private void initViews() {
        rvDrinks = findViewById(R.id.rv_drinks);
        etSearch = findViewById(R.id.et_search);
        tvResultCount = findViewById(R.id.tv_result_count);
        llCategories = findViewById(R.id.ll_categories);

        // "View All" — clear search and reset to All category
        findViewById(R.id.tv_view_all).setOnClickListener(v -> {
            etSearch.setText("");
            currentCategory = "All";
            updateChipStyles();
            filterDrinks();
        });

        // Bottom nav click handlers
        findViewById(R.id.nav_shop).setOnClickListener(v -> {
            // Curren screen - scroll to top
            if (rvDrinks != null) rvDrinks.smoothScrollToPosition(0);
        });
        findViewById(R.id.nav_search).setOnClickListener(v -> {
            etSearch.requestFocus();
        });
        findViewById(R.id.nav_history).setOnClickListener(v -> {
            startActivity(new Intent(this, HistoryActivity.class));
            finish();
        });
        findViewById(R.id.nav_cart).setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
            finish();
        });
        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        });
        
        // Top profile icon
        findViewById(R.id.iv_profile_top).setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });
    }

    private void setupRecyclerView() {
        List<Drink> drinks = DrinkData.getAllDrinks();
        drinkAdapter = new DrinkAdapter(this, drinks, drink -> {
            CartManager.getInstance().addToCart(drink);
            showAddedToast(drink.getName());
        });
        rvDrinks.setLayoutManager(new LinearLayoutManager(this));
        rvDrinks.setAdapter(drinkAdapter);
        if (tvResultCount != null) {
            tvResultCount.setText(drinks.size() + " mocktails available");
        }
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterDrinks();
            }
        });
    }

    private void setupCategories() {
        String[] categories = {"All", "Mocktail", "Juice", "Smoothie"};
        for (String cat : categories) {
            TextView chip = new TextView(this);
            chip.setText(cat);
            chip.setTextSize(14f);
            
            LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 24, 0);
            chip.setLayoutParams(params);
            
            // set padding to make it a distinct chip
            chip.setPadding(48, 20, 48, 20);

            chip.setOnClickListener(v -> {
                currentCategory = cat;
                updateChipStyles();
                filterDrinks();
            });

            llCategories.addView(chip);
            categoryChips.add(chip);
        }
        updateChipStyles();
    }

    private void updateChipStyles() {
        for (TextView chip : categoryChips) {
            boolean isSelected = chip.getText().toString().equals(currentCategory);
            chip.setBackgroundResource(isSelected ? R.drawable.bg_chip_yellow_selected : R.drawable.bg_chip_dark_unselected);
            chip.setTextColor(getResources().getColor(isSelected ? android.R.color.black : android.R.color.white));
        }
    }

    private void filterDrinks() {
        String query = etSearch.getText().toString().toLowerCase().trim();
        List<Drink> filtered;
        
        if (query.isEmpty() && currentCategory.equals("All")) {
            filtered = DrinkData.getAllDrinks();
        } else {
            filtered = new ArrayList<>();
            for (Drink d : DrinkData.getAllDrinks()) {
                boolean matchesCategory = currentCategory.equals("All") || d.getCategory().equals(currentCategory);
                boolean matchesQuery = query.isEmpty() || 
                    d.getName().toLowerCase().contains(query) || 
                    d.getDescription().toLowerCase().contains(query);
                    
                if (matchesCategory && matchesQuery) {
                    filtered.add(d);
                }
            }
        }
        
        drinkAdapter.updateList(filtered);
        if (tvResultCount != null) {
            tvResultCount.setText(filtered.size() + " mocktails found");
        }
    }

    private void showAddedToast(String name) {
        android.widget.Toast.makeText(this, "✓ " + name + " added to cart!", android.widget.Toast.LENGTH_SHORT).show();
    }
}
