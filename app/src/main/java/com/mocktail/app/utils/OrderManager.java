package com.mocktail.app.utils;

import com.mocktail.app.models.CartItem;
import com.mocktail.app.models.Order;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class OrderManager {
    private static OrderManager instance;
    private List<Order> orders = new ArrayList<>();

    private OrderManager() {}

    public static OrderManager getInstance() {
        if (instance == null) instance = new OrderManager();
        return instance;
    }

    public Order placeOrder(List<CartItem> cartItems, double total) {
        String orderId = "#" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

        List<CartItem> orderItems = new ArrayList<>(cartItems);
        Order order = new Order(orderId, orderItems, total, "Completed", date, time);
        orders.add(0, order); // newest first
        return order;
    }

    public List<Order> getOrders() { return orders; }
}
