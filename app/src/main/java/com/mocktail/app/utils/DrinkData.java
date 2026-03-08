package com.mocktail.app.utils;

import com.mocktail.app.models.Drink;
import java.util.ArrayList;
import java.util.List;

public class DrinkData {

    private static List<Drink> cachedDrinks = null;

    public static List<Drink> getAllDrinks() {
        if (cachedDrinks != null) {
            return cachedDrinks;
        }
        
        List<Drink> drinks = new ArrayList<>();

        drinks.add(new Drink(1, "Midnight Sunrise", "A refreshing citrus drink layered with rich grenadine and topped with sparkling soda for a crisp finish. Perfect for relaxing evenings and golden hour vibes.", 12.50, "drink1_midnight_sunrise", "Mocktail", "Fresh Citrus Blend, Artisanal Grenadine, Sparkling Soda", 120, 20, "", 4.9));
        drinks.add(new Drink(2, "Emerald Detox", "A healthy green drink packed with antioxidants and natural detox ingredients. Fresh apple and lime balance the earthy kale flavor.", 9.00, "drink2_emerald_detox", "Juice", "Kale, Green Apple, Ginger, Lime", 80, 10, "", 4.9));
        drinks.add(new Drink(3, "Velvet Berry", "A smooth berry drink with creamy oat milk. Sweet, refreshing, and perfect for berry lovers.", 10.25, "drink3_velvet_berry", "Smoothie", "Blueberry, Raspberry, Oat Milk", 140, 18, "", 4.7));
        drinks.add(new Drink(4, "Tropical Breeze", "A tropical drink inspired by beach flavors. Sweet mango and pineapple create a refreshing island-style beverage.", 11.00, "drink4_tropical_breeze", "Mocktail", "Pineapple Juice, Coconut Water, Mango", 110, 22, "", 4.8));
        drinks.add(new Drink(5, "Citrus Splash", "A bright citrus drink bursting with fresh orange and lemon flavor, balanced with cooling mint.", 8.75, "drink5_citrus_splash", "Juice", "Orange Juice, Lemon, Mint Leaves", 90, 15, "", 4.6));
        drinks.add(new Drink(6, "Mango Paradise", "A creamy mango smoothie blended with yogurt and natural honey for a tropical sweet taste.", 10.50, "drink6_mango_paradise", "Smoothie", "Mango Pulp, Yogurt, Honey", 160, 25, "", 4.8));
        drinks.add(new Drink(7, "Pink Sunset", "A refreshing pink drink combining sweet strawberry and juicy watermelon with a touch of lime.", 11.25, "drink7_pink_sunset", "Mocktail", "Strawberry, Watermelon, Lime", 100, 19, "", 4.7));
        drinks.add(new Drink(8, "Green Energy", "A nutritious green drink filled with vitamins and natural energy from fresh fruits and greens.", 9.50, "drink8_green_energy", "Juice", "Spinach, Apple, Kiwi", 85, 12, "", 4.6));
        drinks.add(new Drink(9, "Blue Lagoon", "A vibrant blue drink with sweet citrus flavor and sparkling refreshment.", 12.00, "drink9_blue_lagoon", "Mocktail", "Lemon Soda, Blue Curacao Syrup, Ice", 130, 28, "", 4.8));
        drinks.add(new Drink(10, "Chocolate Chill", "A rich chocolate drink blended with creamy milk and topped with smooth ice cream.", 11.75, "drink10_chocolate_chill", "Smoothie", "Cocoa, Milk, Ice Cream", 220, 30, "", 4.7));

        cachedDrinks = drinks;
        return drinks;
    }

    public static List<Drink> searchDrinks(String query) {
        List<Drink> result = new ArrayList<>();
        String lowerQuery = query.toLowerCase().trim();
        for (Drink drink : getAllDrinks()) {
            if (drink.getName().toLowerCase().contains(lowerQuery) ||
                drink.getCategory().toLowerCase().contains(lowerQuery) ||
                drink.getDescription().toLowerCase().contains(lowerQuery)) {
                result.add(drink);
            }
        }
        return result;
    }
}
