package com.example.pizzamania;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;

    // 👇 STEP 3: Listener Setup 👇
    private OnCartUpdateListener onCartUpdateListener;

    public interface OnCartUpdateListener {
        void onCartUpdated();
    }

    public void setOnCartUpdateListener(OnCartUpdateListener listener) {
        this.onCartUpdateListener = listener;
    }
    // 👆 END STEP 3 👆

    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        MenuModel pizza = item.getPizza();

        holder.textViewName.setText(pizza.getName());
        holder.textViewPrice.setText("LKR " + String.format("%.2f", pizza.getPrice()));
        holder.textViewQuantity.setText("Qty: " + item.getQuantity());
        holder.textViewTotal.setText("LKR " + String.format("%.2f", item.getTotalPrice()));

        // Plus Button
        holder.buttonPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
            if (onCartUpdateListener != null) {
                onCartUpdateListener.onCartUpdated();
            }
        });

        // Minus Button
        holder.buttonMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
            } else {
                cartItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItems.size());
            }
            if (onCartUpdateListener != null) {
                onCartUpdateListener.onCartUpdated();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPrice, textViewQuantity, textViewTotal;
        Button buttonPlus, buttonMinus; // 👈 NEW

        CartViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewPizzaName);
            textViewPrice = itemView.findViewById(R.id.textViewPizzaPrice);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewTotal = itemView.findViewById(R.id.textViewTotalPrice);
            buttonPlus = itemView.findViewById(R.id.buttonPlus);     // 👈 NEW
            buttonMinus = itemView.findViewById(R.id.buttonMinus);   // 👈 NEW
        }
    }
}