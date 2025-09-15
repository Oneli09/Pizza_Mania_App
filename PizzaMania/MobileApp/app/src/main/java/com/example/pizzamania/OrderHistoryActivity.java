package com.example.pizzamania;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PizzaDatabaseHelper dbHelper;
    private OrderHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        dbHelper = new PizzaDatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadOrders();
    }

    private void loadOrders() {
        List<OrderModel> orders = dbHelper.getAllOrders();
        if (orders.isEmpty()) {
            Toast.makeText(this, "No orders yet!", Toast.LENGTH_SHORT).show();
        }
        adapter = new OrderHistoryAdapter(orders);
        recyclerView.setAdapter(adapter);
    }
}