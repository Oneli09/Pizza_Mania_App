package com.example.pizzamania;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PizzaDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pizza_mania.db";
    // 👇 UPDATED: Version increased to trigger onUpgrade() for schema changes
    private static final int DATABASE_VERSION = 4;

    public PizzaDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Menu Table
        String CREATE_MENU_TABLE = "CREATE TABLE menu (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "description TEXT, " +
                "price REAL, " +
                "image_url TEXT, " +
                "branch TEXT, " +
                "stock INTEGER, " +
                "category TEXT)";
        db.execSQL(CREATE_MENU_TABLE);

        // Create Orders Table
        String CREATE_ORDERS_TABLE = "CREATE TABLE orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp INTEGER, " +
                "total REAL)";
        db.execSQL(CREATE_ORDERS_TABLE);

        // Create Order Items Table
        String CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE order_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_id INTEGER, " +
                "pizza_id INTEGER, " +
                "quantity INTEGER, " +
                "FOREIGN KEY(order_id) REFERENCES orders(id))";
        db.execSQL(CREATE_ORDER_ITEMS_TABLE);

        // Create Users Table
        String CREATE_USERS_TABLE = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fullname TEXT, " +
                "email TEXT UNIQUE, " +
                "phone TEXT, " +
                "address TEXT, " +
                "password TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Insert sample data
        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        String[] items = {
                // Pizzas
                "INSERT INTO menu (name, description, price, image_url, branch, stock, category) VALUES ('Margherita', 'Classic cheese and tomato', 12.99, 'https://example.com/margherita.jpg', 'Colombo', 10, 'pizza')",
                "INSERT INTO menu (name, description, price, image_url, branch, stock, category) VALUES ('Pepperoni', 'Spicy pepperoni slices', 14.99, 'https://example.com/pepperoni.jpg', 'Galle', 5, 'pizza')",
                "INSERT INTO menu (name, description, price, image_url, branch, stock, category) VALUES ('Veggie Supreme', 'Loaded with fresh veggies', 13.99, 'https://example.com/veggie.jpg', 'Colombo', 8, 'pizza')",
                // Juices
                "INSERT INTO menu (name, description, price, image_url, branch, stock, category) VALUES ('Orange Juice', 'Freshly squeezed', 4.99, 'https://example.com/orange_juice.jpg', 'Colombo', 20, 'juice')",
                "INSERT INTO menu (name, description, price, image_url, branch, stock, category) VALUES ('Mango Lassi', 'Sweet yogurt drink', 5.99, 'https://example.com/mango_lassi.jpg', 'Galle', 15, 'juice')",
                "INSERT INTO menu (name, description, price, image_url, branch, stock, category) VALUES ('Apple Juice', 'Crisp and refreshing', 4.49, 'https://example.com/apple_juice.jpg', 'Colombo', 18, 'juice')",
                // Desserts
                "INSERT INTO menu (name, description, price, image_url, branch, stock, category) VALUES ('Chocolate Lava Cake', 'Warm chocolate center', 6.99, 'https://example.com/lava_cake.jpg', 'Colombo', 12, 'dessert')",
                "INSERT INTO menu (name, description, price, image_url, branch, stock, category) VALUES ('Tiramisu', 'Classic Italian dessert', 7.49, 'https://example.com/tiramisu.jpg', 'Galle', 10, 'dessert')",
                "INSERT INTO menu (name, description, price, image_url, branch, stock, category) VALUES ('Cheesecake', 'Creamy New York style', 6.49, 'https://example.com/cheesecake.jpg', 'Colombo', 14, 'dessert')",
                // 👇 ADDED: New category for "Sides"
                "INSERT INTO menu (name, description, price, image_url, branch, stock, category) VALUES ('Garlic Bread', 'Toasted with garlic butter', 3.99, 'https://example.com/garlic_bread.jpg', 'Colombo', 25, 'sides')",
                "INSERT INTO menu (name, description, price, image_url, branch, stock, category) VALUES ('Chicken Wings', 'Spicy and tangy wings', 8.99, 'https://example.com/chicken_wings.jpg', 'Galle', 15, 'sides')"
        };
        for (String sql : items) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This simple approach drops and recreates tables on version change.
        db.execSQL("DROP TABLE IF EXISTS menu");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS order_items");
        onCreate(db);
    }

    // Register new user with all details
    public boolean registerUser(String fullname, String email, String phone, String address, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullname", fullname);
        values.put("email", email);
        values.put("phone", phone);
        values.put("address", address);
        values.put("password", password);
        long result = db.insert("users", null, values);
        db.close();
        return result != -1;
    }

    // Validate login credentials
    public boolean validateLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    // Save order to database
    public long saveOrder(double total, List<CartItem> items) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues orderValues = new ContentValues();
        orderValues.put("timestamp", System.currentTimeMillis());
        orderValues.put("total", total);
        long orderId = db.insert("orders", null, orderValues);

        if (orderId != -1) {
            for (CartItem item : items) {
                ContentValues itemValues = new ContentValues();
                itemValues.put("order_id", orderId);
                itemValues.put("pizza_id", item.getPizza().getId());
                itemValues.put("quantity", item.getQuantity());
                db.insert("order_items", null, itemValues);
            }
        }
        db.close();
        return orderId;
    }

    // Get all orders
    public List<OrderModel> getAllOrders() {
        List<OrderModel> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor orderCursor = db.rawQuery("SELECT * FROM orders ORDER BY timestamp DESC", null);
        while (orderCursor.moveToNext()) {
            int id = orderCursor.getInt(orderCursor.getColumnIndexOrThrow("id"));
            long timestamp = orderCursor.getLong(orderCursor.getColumnIndexOrThrow("timestamp"));
            double total = orderCursor.getDouble(orderCursor.getColumnIndexOrThrow("total"));

            List<CartItem> items = getOrderItems(db, id);

            orders.add(new OrderModel(id, timestamp, total, items));
        }
        orderCursor.close();
        db.close();
        return orders;
    }

    // Helper: Get items for one order
    private List<CartItem> getOrderItems(SQLiteDatabase db, int orderId) {
        List<CartItem> items = new ArrayList<>();
        Cursor itemCursor = db.rawQuery("SELECT * FROM order_items WHERE order_id = ?", new String[]{String.valueOf(orderId)});
        while (itemCursor.moveToNext()) {
            int pizzaId = itemCursor.getInt(itemCursor.getColumnIndexOrThrow("pizza_id"));
            int quantity = itemCursor.getInt(itemCursor.getColumnIndexOrThrow("quantity"));

            Cursor pizzaCursor = db.rawQuery("SELECT * FROM menu WHERE id = ?", new String[]{String.valueOf(pizzaId)});
            if (pizzaCursor.moveToFirst()) {
                int id = pizzaCursor.getInt(pizzaCursor.getColumnIndexOrThrow("id"));
                String name = pizzaCursor.getString(pizzaCursor.getColumnIndexOrThrow("name"));
                String description = pizzaCursor.getString(pizzaCursor.getColumnIndexOrThrow("description"));
                double price = pizzaCursor.getDouble(pizzaCursor.getColumnIndexOrThrow("price"));
                String imageUrl = pizzaCursor.getString(pizzaCursor.getColumnIndexOrThrow("image_url"));
                String branch = pizzaCursor.getString(pizzaCursor.getColumnIndexOrThrow("branch"));
                int stock = pizzaCursor.getInt(pizzaCursor.getColumnIndexOrThrow("stock"));
                String category = pizzaCursor.getString(pizzaCursor.getColumnIndexOrThrow("category"));

                MenuModel pizza = new MenuModel(id, name, description, price, imageUrl, branch, stock, category);
                items.add(new CartItem(pizza, quantity));
            }
            pizzaCursor.close();
        }
        itemCursor.close();
        return items;
    }

    // Get items by category
    public List<MenuModel> getMenuItemsByCategory(String category) {
        List<MenuModel> menuList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM menu WHERE category = ? ORDER BY name", new String[]{category});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));
                String branch = cursor.getString(cursor.getColumnIndexOrThrow("branch"));
                int stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock"));
                String itemCategory = cursor.getString(cursor.getColumnIndexOrThrow("category"));

                menuList.add(new MenuModel(id, name, description, price, imageUrl, branch, stock, itemCategory));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return menuList;
    }
}
