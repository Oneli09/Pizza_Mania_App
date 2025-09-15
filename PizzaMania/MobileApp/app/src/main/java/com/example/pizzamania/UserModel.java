package com.example.pizzamania;

public class UserModel {
    private String email;
    private String password;

    public UserModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}