package com.mocktail.app.utils;

import com.mocktail.app.models.CartItem;
import com.mocktail.app.models.Drink;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems = new ArrayList<>();

    private CartManager() {}

    public static CartManager getInstance() {
        if (instance == null) instance = new CartManager();
        return instance;
    }

    public void addToCart(Drink drink) {
        for (CartItem item : cartItems) {
            if (item.getDrink().getId() == drink.getId()) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(drink, 1));
    }

    public void removeFromCart(int drinkId) {
        cartItems.removeIf(item -> item.getDrink().getId() == drinkId);
    }

    public void updateQuantity(int drinkId, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getDrink().getId() == drinkId) {
                if (quantity <= 0) { removeFromCart(drinkId); return; }
                item.setQuantity(quantity);
                return;
            }
        }
    }

    public List<CartItem> getCartItems() { return cartItems; }

    public int getTotalItemCount() {
        int count = 0;
        for (CartItem item : cartItems) count += item.getQuantity();
        return count;
    }

    public double getTotalAmount() {
        double total = 0;
        for (CartItem item : cartItems) total += item.getTotalPrice();
        return total;
    }

    public void clearCart() { cartItems.clear(); }
}
