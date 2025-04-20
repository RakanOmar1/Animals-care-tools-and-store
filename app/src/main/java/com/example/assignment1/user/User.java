package com.example.assignment1.user;

import com.example.assignment1.item.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class User implements Serializable {
    private String username;
    private String password;

    private double balance;

    private ArrayList<Item> items;
    // Default constructor
    public User() {
        this.username = "";
        this.password = "";
        this.items = new ArrayList<>();
    }

    // Parameterized constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.items = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", items=" + items +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return username.equals(user.username) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

}
