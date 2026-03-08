package com.mocktail.app.models;

import java.util.List;

public class Order {
    private String orderId;
    private List<CartItem> items;
    private double totalAmount;
    private String status;
    private String date;
    private String time;

    public Order(String orderId, List<CartItem> items, double totalAmount, String status, String date, String time) {
        this.orderId = orderId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.date = date;
        this.time = time;
    }

    public String getOrderId() { return orderId; }
    public List<CartItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getDate() { return date; }
    public String getTime() { return time; }

    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        for (CartItem item : items) {
            sb.append(item.getDrink().getName())
              .append(" x").append(item.getQuantity()).append(", ");
        }
        if (sb.length() > 2) sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
