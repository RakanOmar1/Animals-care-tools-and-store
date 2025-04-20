package com.example.assignment1.item;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment1.DashBoardActivity;
import com.example.assignment1.R;

public class ItemDetailsActivity extends AppCompatActivity {

    ImageView detailImage;
    TextView detailTitle, detailDescription, detailPrice;
    Button addToCartFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailDescription = findViewById(R.id.detailDescription);
        detailPrice = findViewById(R.id.detailPrice);
        addToCartFinal = findViewById(R.id.addToCartFinal);

        // Receive data from intent
        Intent intent = getIntent();
        int imageRes = intent.getIntExtra("image", R.drawable.item4);
        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("description");
        String price = intent.getStringExtra("price");

        // Set the data
        detailImage.setImageResource(imageRes);
        detailTitle.setText(title);
        detailDescription.setText(desc);
        detailPrice.setText(price);

        // Handle button click
        addToCartFinal.setOnClickListener(v -> {
            Intent dashboardIntent = new Intent(ItemDetailsActivity.this, DashBoardActivity.class);
            startActivity(dashboardIntent);
            finish();
        });
    }
}
