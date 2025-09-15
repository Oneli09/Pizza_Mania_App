package com.example.pizzamania;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuModel> menuList;
    private OnPizzaClickListener listener;

    public interface OnPizzaClickListener {
        void onPizzaClick(MenuModel pizza);
    }

    public MenuAdapter(List<MenuModel> menuList, OnPizzaClickListener listener) {
        this.menuList = menuList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pizza, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuModel pizza = menuList.get(position);
        holder.textViewName.setText(pizza.getName());
        holder.textViewPrice.setText("LKR " + String.format("%.2f", pizza.getPrice()));
        holder.textViewStock.setText("Stock: " + pizza.getStock());

        holder.buttonOrder.setOnClickListener(v -> {
            CartManager.getInstance().addItem(pizza);
            Toast.makeText(v.getContext(), pizza.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            SoundManager.playSound(v.getContext(), R.raw.pop);
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPrice, textViewStock;
        Button buttonOrder;

        MenuViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewPizzaName);
            textViewPrice = itemView.findViewById(R.id.textViewPizzaPrice);
            textViewStock = itemView.findViewById(R.id.textViewStock);
            buttonOrder = itemView.findViewById(R.id.buttonOrder);
        }
    }
}