package com.example.assignment1.item;

import java.io.Serializable;

public class Item implements Serializable {

    private int id;
    private String title;
    private String description;
    private float price;
    private String type;

    private int imageResource;

    private static int i = 1;
    public Item(String title, String description, float price, int imageResource, String type) {
        this.description = description;
        this.title = title;
        this.price = price;
        this.imageResource = imageResource;
        this.id = i++;
        this.type = type;

    }


    public Item(int id, String title, String description, float price, int imageResource, String type) {
        this.description = description;
        this.title = title;
        this.price = price;
        this.imageResource = imageResource;
        this.id = id;
        this.type = type;

    }



    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

    public int getId() {
        return id;
    }
}
