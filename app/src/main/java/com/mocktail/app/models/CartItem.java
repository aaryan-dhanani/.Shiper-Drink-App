package com.mocktail.app.models;

public class CartItem {
    private Drink drink;
    private int quantity;

    public CartItem(Drink drink, int quantity) {
        this.drink = drink;
        this.quantity = quantity;
    }

    public Drink getDrink() { return drink; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTotalPrice() { return drink.getPrice() * quantity; }
}
