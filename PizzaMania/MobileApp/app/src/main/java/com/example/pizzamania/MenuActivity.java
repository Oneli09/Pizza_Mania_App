package com.example.pizzamania;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PizzaDatabaseHelper dbHelper;
    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 👇 1. Apply theme BEFORE super.onCreate()
        SharedPreferences themePrefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isDark = themePrefs.getBoolean("dark_mode", false);
        if (isDark) {
            setTheme(R.style.Theme_PizzaMania_Dark);
        } else {
            setTheme(R.style.Theme_PizzaMania_Light);
        }

        // 👇 2. Call super.onCreate() ONLY ONCE
        super.onCreate(savedInstanceState);

        // 👇 3. Set content view
        setContentView(R.layout.activity_menu);

        // 👇 4. Auto-login check
        SharedPreferences loginPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        if (!loginPrefs.getBoolean("isLoggedIn", false)) {
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        // Setup ViewPager2
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        MenuPagerAdapter pagerAdapter = new MenuPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Setup TabLayout
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(pagerAdapter.getTitle(position));
        }).attach();

        // Profile Icon
        ImageView imageViewProfile = findViewById(R.id.imageViewProfile);
        imageViewProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, UserAccountActivity.class);
            startActivity(intent);
        });

        // View Cart Button
        Button buttonViewCart = findViewById(R.id.buttonViewCart);
        buttonViewCart.setOnClickListener(v -> {
            if (CartManager.getInstance().getCartItems().isEmpty()) {
                Toast.makeText(MenuActivity.this, "Cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MenuActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        // Cart FAB
        FloatingActionButton fabCart = findViewById(R.id.fabCart);
        fabCart.setOnClickListener(v -> {
            if (CartManager.getInstance().getCartItems().isEmpty()) {
                Toast.makeText(MenuActivity.this, "Cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MenuActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        // Update cart badge
        updateCartBadge();
    }

    private void loadMenuFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM menu ORDER BY category, name", null);

        List<MenuModel> pizzaList = new ArrayList<>();
        List<MenuModel> juiceList = new ArrayList<>();
        List<MenuModel> dessertList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));
                String branch = cursor.getString(cursor.getColumnIndexOrThrow("branch"));
                int stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category")); // 👈 ADDED

                MenuModel item = new MenuModel(id, name, description, price, imageUrl, branch, stock, category); // 👈 FIXED

                switch (category) {
                    case "pizza":
                        pizzaList.add(item);
                        break;
                    case "juice":
                        juiceList.add(item);
                        break;
                    case "dessert":
                        dessertList.add(item);
                        break;
                }
            } while (cursor.moveToNext());
        }

        cursor.close();

        // Create combined list with headers
        List<MenuModel> menuList = new ArrayList<>();

        if (!pizzaList.isEmpty()) {
            menuList.add(new MenuModel(-1, "🍕 PIZZAS", "", 0.0, "", "", 0, "header"));
            menuList.addAll(pizzaList);
        }

        if (!juiceList.isEmpty()) {
            menuList.add(new MenuModel(-2, "🧃 JUICES", "", 0.0, "", "", 0, "header"));
            menuList.addAll(juiceList);
        }

        if (!dessertList.isEmpty()) {
            menuList.add(new MenuModel(-3, "🍰 DESSERTS", "", 0.0, "", "", 0, "header"));
            menuList.addAll(dessertList);
        }

        menuAdapter = new MenuAdapter(menuList, new MenuAdapter.OnPizzaClickListener() {
            @Override
            public void onPizzaClick(MenuModel pizza) {
                updateCartBadge();
            }
        });

        recyclerView.setAdapter(menuAdapter);
    }

    public void updateCartBadge() {

        int count = 0;
        for (CartItem item : CartManager.getInstance().getCartItems()) {
            count += item.getQuantity();
        }
        TextView badge = findViewById(R.id.textViewCartBadge);
        if (count == 0) {
            badge.setVisibility(View.GONE);
        } else {
            badge.setVisibility(View.VISIBLE);
            badge.setText(String.valueOf(count));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }
}