package com.example.pizzamania;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MenuPagerAdapter extends FragmentStateAdapter {

    private String[] categories = {"pizza", "juice", "dessert"};
    private String[] titles = {"🍕 PIZZAS", "🧃 JUICES", "🍰 DESSERTS"};

    public MenuPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return MenuFragment.newInstance(categories[position]);
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    public String getTitle(int position) {
        return titles[position];
    }
}