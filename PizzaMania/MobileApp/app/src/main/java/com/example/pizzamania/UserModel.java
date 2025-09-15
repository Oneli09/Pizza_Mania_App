package com.example.pizzamania;

public class UserModel {
    private int id;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String password;

    // Constructor to match the database structure
    public UserModel(int id, String fullname, String email, String phone, String address, String password) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }

    // Getters for all fields
    public int getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }
}
