package com.example.pizzamania;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuFragment extends Fragment {

    private String category;
    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;

    public static MenuFragment newInstance(String category) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString("category", category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString("category");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadMenuItems();

        return view;
    }

    private void loadMenuItems() {
        PizzaDatabaseHelper dbHelper = new PizzaDatabaseHelper(requireContext());
        List<MenuModel> menuList = dbHelper.getMenuItemsByCategory(category);

        menuAdapter = new MenuAdapter(menuList, pizza -> {
            CartManager.getInstance().addItem(pizza);
            if (getActivity() instanceof MenuActivity) {
                ((MenuActivity) getActivity()).updateCartBadge();
            }
        });

        recyclerView.setAdapter(menuAdapter);
    }
}