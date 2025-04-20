package com.example.assignment1.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment1.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private List<Item> itemList;
    private Context context;
    private OnItemClickListener listener;

    // Flag to indicate if this is the "Selected Items" view (for removal)
    private boolean isSelectedItemsList = false;

    // Constructor for regular usage (Add to Cart)
    public ItemAdapter(Context context, List<Item> itemList, OnItemClickListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

    // Constructor for selected items view (Remove)
    public ItemAdapter(Context context, List<Item> itemList, boolean isSelectedItemsList, OnItemClickListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.isSelectedItemsList = isSelectedItemsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);

        holder.itemImage.setImageResource(item.getImageResource());
        holder.itemTitle.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.itemPrice.setText(String.format("£%.2f", item.getPrice()));

        // Update button behavior based on adapter mode
        holder.addToCartBtn.setVisibility(View.VISIBLE);
        holder.addToCartBtn.setText(isSelectedItemsList ? "Remove" : "Add to Cart");
        holder.addToCartBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<Item> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public void removeItem(Item item) {
        int position = itemList.indexOf(item);
        if (position != -1) {
            itemList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
