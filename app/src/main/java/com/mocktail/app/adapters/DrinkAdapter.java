package com.mocktail.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mocktail.app.R;
import com.mocktail.app.models.Drink;
import com.mocktail.app.activities.DrinkDetailActivity;
import android.content.Intent;
import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

    public interface OnAddToCartListener {
        void onAdd(Drink drink);
    }

    private Context context;
    private List<Drink> drinks;
    private OnAddToCartListener listener;

    public DrinkAdapter(Context context, List<Drink> drinks, OnAddToCartListener listener) {
        this.context = context;
        this.drinks = drinks;
        this.listener = listener;
    }

    public void updateList(List<Drink> newList) {
        this.drinks = newList;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_drink, parent, false);
        return new DrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {
        Drink drink = drinks.get(position);
        holder.tvName.setText(drink.getName());
        holder.tvIngredients.setText(drink.getIngredients());
        holder.tvPrice.setText(String.format("₹%.2f", drink.getPrice()));
        holder.tvRating.setText(String.valueOf(drink.getRating()));

        // Load image using Glide
        int imageResId = context.getResources().getIdentifier(drink.getImageUrl(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            Glide.with(context).load(imageResId).into(holder.ivDrink);
        } else {
            // Fallback just in case
            Glide.with(context).load(R.drawable.drink1_midnight_sunrise).into(holder.ivDrink);
        }

        // Add to cart circle opens details instead of direct add
        holder.btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(context, DrinkDetailActivity.class);
            intent.putExtra("drink", drink);
            context.startActivity(intent);
        });
        
        // Open Detail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DrinkDetailActivity.class);
            intent.putExtra("drink", drink);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return drinks.size(); }

    static class DrinkViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvIngredients, tvPrice, tvRating;
        ImageView ivDrink;
        FrameLayout btnAdd;

        DrinkViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_drink_name);
            tvIngredients = itemView.findViewById(R.id.tv_drink_ingredients);
            tvPrice = itemView.findViewById(R.id.tv_drink_price);
            tvRating = itemView.findViewById(R.id.tv_drink_rating);
            ivDrink = itemView.findViewById(R.id.iv_drink_image);
            btnAdd = itemView.findViewById(R.id.btn_add_to_cart_circle);
        }
    }
}
