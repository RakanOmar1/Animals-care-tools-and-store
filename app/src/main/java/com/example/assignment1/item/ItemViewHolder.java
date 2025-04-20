package com.example.assignment1.item;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment1.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    ImageView itemImage;
    TextView itemTitle, description, itemPrice;
    Button addToCartBtn;

    public ItemViewHolder(View itemView) {
        super(itemView);
        itemImage = itemView.findViewById(R.id.itemImage);
        itemTitle = itemView.findViewById(R.id.itemTitle);
        description = itemView.findViewById(R.id.itemSubtitle);
        itemPrice = itemView.findViewById(R.id.itemPrice);
        addToCartBtn = itemView.findViewById(R.id.addToCartBtn);

        if (itemView.getRootView().getRootView().getId() == R.id.selectedItemsRecyclerView) {
            addToCartBtn.setText("Remove");
        }
    }
}
