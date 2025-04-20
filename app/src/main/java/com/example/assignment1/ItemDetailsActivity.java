package com.example.assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment1.item.Item;
import com.example.assignment1.user.User;

import java.util.ArrayList;

public class ItemDetailsActivity extends AppCompatActivity {

    private Item selectedItem;
    private User user;
    private ArrayList<Item> itemsList;

    private ImageView detailImage;
    private TextView detailTitle, detailDescription, detailPrice;
    private Button addToCartFinal;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "AppPrefs";
    private static final String USER_ITEMS_PREFIX = "user_items_";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        // Retrieve data from intent
        selectedItem = (Item) getIntent().getSerializableExtra("currentItem");
        user = (User) getIntent().getSerializableExtra("currentUser");
        itemsList = (ArrayList<Item>) getIntent().getSerializableExtra("itemsList");

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Link UI elements
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailDescription = findViewById(R.id.detailDescription);
        detailPrice = findViewById(R.id.detailPrice);
        addToCartFinal = findViewById(R.id.addToCartFinal);

        // Set UI data
        if (selectedItem != null) {
            detailImage.setImageResource(selectedItem.getImageResource());
            detailTitle.setText(selectedItem.getTitle());
            detailDescription.setText(selectedItem.getDescription());
            detailPrice.setText(String.format("$%.2f", selectedItem.getPrice()));
        }

        // Add to cart logic
        addToCartFinal.setOnClickListener(v -> {
            user.getItems().add(selectedItem);
            saveUserItems(user.getUsername(), user.getItems());

            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedUser", user);
            resultIntent.putExtra("updatedItemList", itemsList);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void saveUserItems(String username, ArrayList<Item> items) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear previous items
        int index = 0;
        while (sharedPreferences.contains(USER_ITEMS_PREFIX + username + "_" + index + "_title")) {
            editor.remove(USER_ITEMS_PREFIX + username + "_" + index + "_title");
            editor.remove(USER_ITEMS_PREFIX + username + "_" + index + "_desc");
            editor.remove(USER_ITEMS_PREFIX + username + "_" + index + "_price");
            editor.remove(USER_ITEMS_PREFIX + username + "_" + index + "_img");
            editor.remove(USER_ITEMS_PREFIX + username + "_" + index + "_type");
            index++;
        }

        // Save new items
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            editor.putString(USER_ITEMS_PREFIX + username + "_" + i + "_title", item.getTitle());
            editor.putString(USER_ITEMS_PREFIX + username + "_" + i + "_desc", item.getDescription());
            editor.putFloat(USER_ITEMS_PREFIX + username + "_" + i + "_price", item.getPrice());
            editor.putInt(USER_ITEMS_PREFIX + username + "_" + i + "_img", item.getImageResource());
            editor.putString(USER_ITEMS_PREFIX + username + "_" + i + "_type", item.getType());
        }

        editor.apply();
    }
}
