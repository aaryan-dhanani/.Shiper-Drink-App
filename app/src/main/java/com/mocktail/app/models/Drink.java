package com.mocktail.app.models;

import java.io.Serializable;

public class Drink implements Serializable {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl; // drawable resource name
    private String category;
    
    // New fields
    private String ingredients;
    private int calories;
    private int sugar;
    private String flavorProfile;
    private boolean isFavorite;
    private double rating;

    public Drink(int id, String name, String description, double price, String imageUrl, String category, String ingredients, int calories, int sugar, String flavorProfile, double rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.ingredients = ingredients;
        this.calories = calories;
        this.sugar = sugar;
        this.flavorProfile = flavorProfile;
        this.rating = rating;
        this.isFavorite = false;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }
    
    public String getIngredients() { return ingredients; }
    public int getCalories() { return calories; }
    public int getSugar() { return sugar; }
    public String getFlavorProfile() { return flavorProfile; }
    
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean isFavorite) { this.isFavorite = isFavorite; }
    public double getRating() { return rating; }
}
