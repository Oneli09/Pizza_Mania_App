package com.example.pizzamania;

import java.util.List;

public class OrderModel {
    private int id;
    private long timestamp; // when order was placed
    private double total;
    private List<CartItem> items; // what was ordered

    public OrderModel(int id, long timestamp, double total, List<CartItem> items) {
        this.id = id;
        this.timestamp = timestamp;
        this.total = total;
        this.items = items;
    }

    // Getters
    public int getId() { return id; }
    public long getTimestamp() { return timestamp; }
    public double getTotal() { return total; }
    public List<CartItem> getItems() { return items; }
}