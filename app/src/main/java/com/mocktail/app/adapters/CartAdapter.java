package com.mocktail.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mocktail.app.R;
import com.mocktail.app.models.CartItem;
import com.mocktail.app.utils.CartManager;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> items;
    private Runnable onCartChanged;

    public CartAdapter(Context context, List<CartItem> items, Runnable onCartChanged) {
        this.context = context;
        this.items = items;
        this.onCartChanged = onCartChanged;
    }

    @NonNull @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.tvName.setText(item.getDrink().getName());
        holder.tvPrice.setText(String.format("$%.2f", item.getDrink().getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvSubtotal.setText(String.format("$%.2f", item.getTotalPrice()));

        holder.btnPlus.setOnClickListener(v -> {
            CartManager.getInstance().updateQuantity(item.getDrink().getId(), item.getQuantity() + 1);
            onCartChanged.run();
        });

        holder.btnMinus.setOnClickListener(v -> {
            CartManager.getInstance().updateQuantity(item.getDrink().getId(), item.getQuantity() - 1);
            onCartChanged.run();
        });

        holder.btnRemove.setOnClickListener(v -> {
            CartManager.getInstance().removeFromCart(item.getDrink().getId());
            onCartChanged.run();
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity, tvSubtotal;
        ImageButton btnPlus, btnMinus, btnRemove;

        CartViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_cart_name);
            tvPrice = itemView.findViewById(R.id.tv_cart_price);
            tvQuantity = itemView.findViewById(R.id.tv_cart_quantity);
            tvSubtotal = itemView.findViewById(R.id.tv_cart_subtotal);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            btnMinus = itemView.findViewById(R.id.btn_minus);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }
    }
}
