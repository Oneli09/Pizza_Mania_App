package com.example.pizzamania;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserAccountActivity extends AppCompatActivity {

    private TextView textViewEmail;
    private Button buttonOrderHistory;
    private Button buttonViewBranches;
    private Button buttonToggleOffline;
    private Button buttonToggleDarkMode;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        // Initialize views
        textViewEmail = findViewById(R.id.textViewEmail);
        buttonOrderHistory = findViewById(R.id.buttonOrderHistory);
        buttonViewBranches = findViewById(R.id.buttonViewBranches);
        buttonToggleOffline = findViewById(R.id.buttonToggleOffline);
        buttonToggleDarkMode = findViewById(R.id.buttonToggleDarkMode);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Load user email
        SharedPreferences loginPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String email = loginPrefs.getString("email", "user@example.com");
        textViewEmail.setText("Logged in as: " + email);

        // Order History
        buttonOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(UserAccountActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        // View Branches
        buttonViewBranches.setOnClickListener(v -> {
            Intent intent = new Intent(UserAccountActivity.this, BranchesActivity.class);
            startActivity(intent);
        });

        // Toggle Offline Mode
        buttonToggleOffline.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            boolean isOffline = prefs.getBoolean("offline_mode", false);
            prefs.edit().putBoolean("offline_mode", !isOffline).apply();
            Toast.makeText(this, isOffline ? "Online Mode" : "Offline Mode", Toast.LENGTH_SHORT).show();
        });

        // Toggle Dark Mode
        buttonToggleDarkMode.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            boolean isDark = prefs.getBoolean("dark_mode", false);
            prefs.edit().putBoolean("dark_mode", !isDark).apply();
            Toast.makeText(this, isDark ? "Light Mode" : "Dark Mode", Toast.LENGTH_SHORT).show();
            recreate(); // Restart activity to apply theme
        });

        // Logout
        buttonLogout.setOnClickListener(v -> {
            getSharedPreferences("user_prefs", MODE_PRIVATE).edit().clear().apply();
            Intent intent = new Intent(UserAccountActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}