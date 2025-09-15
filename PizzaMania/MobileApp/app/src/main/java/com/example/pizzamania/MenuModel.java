package com.example.pizzamania;

public class MenuModel {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String branch;
    private int stock;
    private String category; // 👈 ADDED

    // 👇 UPDATED CONSTRUCTOR
    public MenuModel(int id, String name, String description, double price, String imageUrl, String branch, int stock, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.branch = branch;
        this.stock = stock;
        this.category = category;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getBranch() { return branch; }
    public int getStock() { return stock; }
    public String getCategory() { return category; } // 👈 ADDED
}