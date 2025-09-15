package com.example.pizzamania;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzamania.CartManager;
import com.example.pizzamania.CartItem;
import com.example.pizzamania.PizzaDatabaseHelper;
import com.example.pizzamania.SoundManager;

public class CheckoutSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_success);

        Button buttonBackToMenu = findViewById(R.id.buttonBackToMenu);
        buttonBackToMenu.setOnClickListener(v -> {
            // Get current cart items
            List<CartItem> cartItems = CartManager.getInstance().getCartItems();
            double total = CartManager.getInstance().getTotalPrice();

            // Save order to database
            PizzaDatabaseHelper dbHelper = new PizzaDatabaseHelper(this);
            dbHelper.saveOrder(total, cartItems);

            // Clear cart
            CartManager.getInstance().clearCart();

            // Play sound
            SoundManager.playSound(this, R.raw.ding);

            // Vibrate for 500ms
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(500);
            }

            // Go back to menu
            finish();
        });
    }
}