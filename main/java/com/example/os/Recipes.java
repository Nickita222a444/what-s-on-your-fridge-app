package com.example.os;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Recipes extends Fragment {

    private ArrayList<EdaItem> coffeeItems = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.rv_sub_category);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new FavoriteAdapter(coffeeItems, getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        coffeeItems.add(new EdaItem(R.drawable.eda_category, "Latte","0","0"));
        coffeeItems.add(new EdaItem(R.drawable.black_board, "Kapucino","1","0"));

        return root;
    }
}
