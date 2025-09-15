package com.example.pizzamania;

public class CartItem {
    private MenuModel pizza;
    private int quantity;

    public CartItem(MenuModel pizza, int quantity) {
        this.pizza = pizza;
        this.quantity = quantity;
    }

    public MenuModel getPizza() { return pizza; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalPrice() {
        return pizza.getPrice() * quantity;
    }
}