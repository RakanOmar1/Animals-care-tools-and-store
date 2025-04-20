package com.example.assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment1.item.Item;
import com.example.assignment1.item.ItemAdapter;
import com.example.assignment1.user.User;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private TextView username;
    private TextView totalPrice;
    private Button confirm;
    private ImageButton btnBack;
    private RecyclerView recyclerView;

    private User currentUser;
    private ArrayList<Item> selectedItemsList;
    private ItemAdapter selectedItemsAdapter;

    private final ActivityResultLauncher<Intent> itemDetailsLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    currentUser = (User) result.getData().getSerializableExtra("updatedUser");
                    selectedItemsList = (ArrayList<Item>) result.getData().getSerializableExtra("updatedItemList");

                    if (currentUser != null) {
                        username.setText(currentUser.getUsername());
                    }

                    selectedItemsAdapter.setItems(selectedItemsList);
                    selectedItemsAdapter.notifyDataSetChanged();
                    updateTotalPrice();
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.username);
        totalPrice = findViewById(R.id.totalPrice);
        confirm = findViewById(R.id.btnConfirmAll);
        ImageButton btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.selectedItemsRecyclerView);

        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        selectedItemsList = (ArrayList<Item>) getIntent().getSerializableExtra("updatedItemList");

        if (selectedItemsList == null || selectedItemsList.isEmpty()) {
            selectedItemsList = loadItemsFromPreferences(); // ✅ load from SharedPreferences
        }

        if (currentUser != null) {
            username.setText(currentUser.getUsername());
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedItemsAdapter = new ItemAdapter(this, selectedItemsList, true, item -> {
            Toast.makeText(this, item.getTitle() + " removed", Toast.LENGTH_SHORT).show();
            int position = selectedItemsList.indexOf(item);
            if (position != -1) {
                selectedItemsList.remove(position);
                selectedItemsAdapter.notifyItemRemoved(position);
                updateTotalPrice();
            }
        });
        recyclerView.setAdapter(selectedItemsAdapter);
        updateTotalPrice();

        confirm.setOnClickListener(e -> {
            selectedItemsList.clear();
            selectedItemsAdapter.notifyDataSetChanged();
            updateTotalPrice();
            Toast.makeText(this, "All items confirmed and removed.", Toast.LENGTH_SHORT).show();

            currentUser.getItems().clear();
            saveItemsToPreferences(selectedItemsList); // ✅ Clear saved data too

            Intent dashboardIntent = new Intent(this, DashBoardActivity.class);
            dashboardIntent.putExtra("currentUser", currentUser);
            startActivity(dashboardIntent);
            finish();
        });

        btnLogout.setOnClickListener(v -> {
            saveItemsToPreferences(selectedItemsList); // ✅ Save items before logout

            Intent loginIntent = new Intent(ProfileActivity.this, MainActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();
        });

        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(ProfileActivity.this, DashBoardActivity.class);
            backIntent.putExtra("currentUser", currentUser);
            startActivity(backIntent);
            finish();
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent backIntent = new Intent(ProfileActivity.this, DashBoardActivity.class);
                backIntent.putExtra("currentUser", currentUser);
                startActivity(backIntent);
                finish();
            }
        });
    }

    private ArrayList<Item> loadItemsFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String savedItems = sharedPreferences.getString("selectedItems", "");

        ArrayList<Item> loadedItems = new ArrayList<>();
        if (!savedItems.isEmpty()) {
            String[] itemStrings = savedItems.split(";");
            for (String itemStr : itemStrings) {
                String[] fields = itemStr.split(",");
                if (fields.length == 6) {
                    int id = Integer.parseInt(fields[0]);
                    String title = fields[1];
                    String description = fields[2];
                    float price = Float.parseFloat(fields[3]);
                    String type = fields[4];
                    int imageRes = Integer.parseInt(fields[5]);


                    loadedItems.add(new Item(id, title, description, price, imageRes, type));
                }
            }
        }

        return loadedItems;
    }

    private void saveItemsToPreferences(ArrayList<Item> selectedItemsList) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        StringBuilder itemsStringBuilder = new StringBuilder();
        for (Item item : selectedItemsList) {
            String itemString = item.getId() + "," + item.getTitle() + "," + item.getDescription() + ","
                    + item.getPrice() + "," + item.getType() + "," + item.getImageResource();

            itemsStringBuilder.append(itemString).append(";");
        }

        editor.putString("selectedItems", itemsStringBuilder.toString());
        editor.apply();
    }

    private void updateTotalPrice() {
        double total = 0;
        for (Item item : selectedItemsList) {
            total += item.getPrice();
        }
        totalPrice.setText(String.format("£%.2f", total));
    }
}
