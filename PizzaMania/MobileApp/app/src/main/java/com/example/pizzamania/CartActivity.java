package com.example.pizzamania;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private TextView textViewTotal;
    private Button buttonCheckout;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        textViewTotal = findViewById(R.id.textViewTotal);
        buttonCheckout = findViewById(R.id.buttonCheckout);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        loadCart();
        updateTotal();

        buttonCheckout.setOnClickListener(v -> {
            if (CartManager.getInstance().getCartItems().isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Go to success screen
            Intent intent = new Intent(CartActivity.this, CheckoutSuccessActivity.class);
            startActivity(intent);
            // Don't finish() yet — let success screen handle cart clearing
        });
    }

    private void loadCart() {
        List<CartItem> cartItems = CartManager.getInstance().getCartItems();
        cartAdapter = new CartAdapter(cartItems);

        // ✅ STEP 5: Listen for cart updates
        cartAdapter.setOnCartUpdateListener(new CartAdapter.OnCartUpdateListener() {
            @Override
            public void onCartUpdated() {
                updateTotal(); // Refresh total when + or - is tapped
                if (CartManager.getInstance().getCartItems().isEmpty()) {
                    Toast.makeText(CartActivity.this, "Cart is now empty!", Toast.LENGTH_SHORT).show();
                    // finish(); // Optional: close cart if empty
                }
            }
        });

        recyclerViewCart.setAdapter(cartAdapter);
    }

    private void updateTotal() {
        double total = CartManager.getInstance().getTotalPrice();
        NumberFormat format = NumberFormat.getCurrencyInstance();
        textViewTotal.setText("Total: " + format.format(total));
    }
}