package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment1.item.Item;
import com.example.assignment1.item.ItemAdapter;
import com.example.assignment1.user.User;

import java.util.ArrayList;

public class DashBoardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Item> itemList;
    private ItemAdapter adapter;
    private User user;
    private ImageView profile;
    private EditText edtSearch;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        user = (User) getIntent().getSerializableExtra("currentUser");
        itemList = (ArrayList<Item>) getIntent().getSerializableExtra("updatedItemList");

        boolean itemsPassed = itemList instanceof ArrayList && itemList.size() > 0;

        if (itemsPassed == false) {
            itemList = new ArrayList<>();
            addItems();
        }

        edtSearch = findViewById(R.id.searchBar);
        profile = findViewById(R.id.profileButton);
        recyclerView = findViewById(R.id.recyclerView);
        spinner = findViewById(R.id.filter);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ItemAdapter(this, itemList, item -> {
            Toast.makeText(this, item.getTitle() + " added to cart", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DashBoardActivity.this, ItemDetailsActivity.class);
            intent.putExtra("currentItem", item);
            intent.putExtra("currentUser", user);
            intent.putExtra("itemsList", itemList);
            itemDetailsLauncher.launch(intent);
        });

        recyclerView.setAdapter(adapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String selectedType = spinner.getSelectedItem().toString();
                if (selectedType.equals("All")) {
                    filterItems(s.toString());
                } else {
                    filterItemsbox(s.toString(), selectedType);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        profile.setOnClickListener(view -> {
            Intent pIntent = new Intent(this, ProfileActivity.class);
            pIntent.putExtra("currentUser", user);
            pIntent.putExtra("updatedItemList", user.getItems());
            startActivity(pIntent);
        });

        ArrayList<String> filterOptions = new ArrayList<>();
        filterOptions.add("All");
        filterOptions.add("Food");
        filterOptions.add("tools");
        filterOptions.add("toys");
        filterOptions.add("hygiene");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, filterOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedType = filterOptions.get(position);
                String searchQuery = edtSearch.getText().toString();
                if (selectedType.equals("All")) {
                    filterItems(searchQuery);
                } else {
                    filterItemsbox(searchQuery, selectedType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void filterItems(String query) {
        ArrayList<Item> filteredList = new ArrayList<>();
        for (Item item : itemList) {
            boolean hasMatch = item.getTitle().toLowerCase().contains(query.toLowerCase());
            if (hasMatch) {
                filteredList.add(item);
            }
        }
        adapter.setItems(filteredList);
        adapter.notifyDataSetChanged();
    }

    private void filterItemsbox(String query, String type) {
        ArrayList<Item> filteredList = new ArrayList<>();
        for (Item item : itemList) {
            boolean matchesType = item.getType().equals(type);
            boolean matchesQuery = item.getTitle().toLowerCase().contains(query.toLowerCase());
            if (matchesType && matchesQuery) {
                filteredList.add(item);
            }
        }
        adapter.setItems(filteredList);
        adapter.notifyDataSetChanged();
    }

    private final ActivityResultLauncher<Intent> itemDetailsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                boolean hasResult = result.getResultCode() == RESULT_OK;
                boolean hasData = result.getData() instanceof Intent;
                if (hasResult && hasData) {
                    user = (User) result.getData().getSerializableExtra("updatedUser");
                    itemList = (ArrayList<Item>) result.getData().getSerializableExtra("updatedItemList");

                    adapter.setItems(itemList);
                    adapter.notifyDataSetChanged();
                }
            });

    private void addItems() {
        itemList.add(new Item("Felix cat food", "with tasty Salmon and Vegetables", 7.49f, R.drawable.item1, "Food"));
        itemList.add(new Item("Pedigree", "dog food", 29.99f, R.drawable.item2, "Food"));
        itemList.add(new Item("Freshpeet dog food", "Organic dog food", 12.99f,R.drawable.item3, "Food"));
        itemList.add(new Item("Whiskas", "Tuna cats food", 5,R.drawable.item4, "Food"));
        itemList.add(new Item("Pamper", "Adult Deep sea Delights Flavour", 10,R.drawable.item5, "Food"));
        itemList.add(new Item("Green Meadow", "Silicone Pet Grooming Gloves Cat Hair Brush and Grooming Gloves for Dog Bathing Cleaning Hair Removal for Dogs Animal Use Supplies", 3,R.drawable.item6, "tools"));
        itemList.add(new Item("Cat toy", "Cat Toy Feather Ball Funny Cat Toy Star Ball Plus Feather Foam Ball Throwing Interactive Toys Plush Toys Pet Supplies Cat Toy", 5,R.drawable.item8, "toys"));
        itemList.add(new Item("Cat Corner Brush", "Self Cleaning Cat Corner Brush Undercoad Loose Hair Remover Glove Grooming Tools for Cats Kitten Accessories Pet Products", 5,R.drawable.item9, "toys"));
        itemList.add(new Item("Eye yesway", "Pet Tear Stain Remover Powder for Dogs and Cats, Natural Safe, Apply Around, Absorbs, Repels Dry, Non-Rusting, Effective, Non-Irritating", 4,R.drawable.item10, "hygiene"));
        itemList.add(new Item("Dog eye drops", "Pet Eye Wash Drops, Dog Tear Stain Remover, Cat Eye Wash Drops, Effective Cleaning Solution", 5,R.drawable.item11, "hygiene"));
        itemList.add(new Item("Yegbong Dog shampoo", "Pet Tear Stain Remover Powder for Dogs and Cats, Natural Safe, Apply Around, Absorbs, Repels Dry, Non-Rusting, Effective, Non-Irritating", 4,R.drawable.item12, "hygiene"));
        itemList.add(new Item("Pet dry for peets", "Pet Dry Clean Foaming Cleanser, Dog and Cat Bath Gel, Leave-In Deodorant, Shower Gel, Pet Supplies", 6,R.drawable.item13, "hygiene"));
        itemList.add(new Item("Rozino cleaner", "Pet Hair Removal Spray, Cleaner, Static Removal, Detangling Conditioner, Detangling Conditioner, Pet Grooming Accessories,", 6,R.drawable.item14, "hygiene"));

    }

}
