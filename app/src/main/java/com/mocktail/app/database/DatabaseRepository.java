package com.mocktail.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mocktail.app.models.CartItem;
import com.mocktail.app.models.Order;
import com.mocktail.app.utils.DrinkData;
import com.mocktail.app.models.Drink;

import java.util.ArrayList;
import java.util.List;

import static com.mocktail.app.database.DatabaseHelper.*;

/**
 * Single access point for ALL database operations.
 * Usage: DatabaseRepository.getInstance(context).someMethod()
 */
public class DatabaseRepository {

    private static DatabaseRepository instance;
    private final DatabaseHelper dbHelper;

    private DatabaseRepository(Context context) {
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public static synchronized DatabaseRepository getInstance(Context context) {
        if (instance == null) instance = new DatabaseRepository(context);
        return instance;
    }

    // ══════════════════════════════════════════════════════
    //  USER METHODS
    // ══════════════════════════════════════════════════════

    public boolean registerUser(String name, String email, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_NAME,  name.trim());
        cv.put(COL_USER_EMAIL, email.trim().toLowerCase());
        cv.put(COL_USER_PASS,  password);
        long result = db.insert(TABLE_USERS, null, cv);
        db.close();
        
        if (result != -1) {
            try {
                String postData = "action=signup" + 
                                  "&name=" + java.net.URLEncoder.encode(name.trim(), "UTF-8") + 
                                  "&email=" + java.net.URLEncoder.encode(email.trim().toLowerCase(), "UTF-8") + 
                                  "&password=" + java.net.URLEncoder.encode(password, "UTF-8");
                pushToGoogleSheets(postData);
            } catch (Exception e) { e.printStackTrace(); }
        }
        
        return result != -1;  // -1 means duplicate email (UNIQUE constraint)
    }

    private void pushToGoogleSheets(String postData) {
        new Thread(() -> {
            try {
                java.net.URL url = new java.net.URL("https://script.google.com/macros/s/AKfycbzgbbMOCA4NBqO0gFKxHhgtG76umY0Mf4eO7UyQfL2-2J9-QPCzp0GamroUX_EcPg_B/exec");
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                java.io.OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes("UTF-8"));
                os.flush();
                os.close();
                int responseCode = conn.getResponseCode(); // Execute the request
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /** Returns true if email + password match a record in the DB. */
    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_USER_EMAIL + "=? AND " + COL_USER_PASS + "=?",
                new String[]{email.trim().toLowerCase(), password},
                null, null, null
        );
        boolean found = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return found;
    }

    /** Returns the display name for a logged-in email, or null. */
    public String getUserName(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_USER_NAME},
                COL_USER_EMAIL + "=?",
                new String[]{email.trim().toLowerCase()},
                null, null, null
        );
        String name = null;
        if (cursor.moveToFirst()) name = cursor.getString(0);
        cursor.close();
        db.close();
        return name;
    }

    // ══════════════════════════════════════════════════════
    //  ORDER METHODS
    // ══════════════════════════════════════════════════════

    /** Save a full order (header + all items) in one transaction. */
    public void saveOrder(Order order) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            // Insert order header
            ContentValues ov = new ContentValues();
            ov.put(COL_ORDER_REF,    order.getOrderId());
            ov.put(COL_ORDER_TOTAL,  order.getTotalAmount());
            ov.put(COL_ORDER_STATUS, order.getStatus());
            ov.put(COL_ORDER_DATE,   order.getDate());
            ov.put(COL_ORDER_TIME,   order.getTime());
            db.insert(TABLE_ORDERS, null, ov);

            // Insert each item
            for (CartItem item : order.getItems()) {
                ContentValues iv = new ContentValues();
                iv.put(COL_ITEM_ORDER_REF,  order.getOrderId());
                iv.put(COL_ITEM_DRINK_ID,   item.getDrink().getId());
                iv.put(COL_ITEM_DRINK_NAME, item.getDrink().getName());
                iv.put(COL_ITEM_PRICE,      item.getDrink().getPrice());
                iv.put(COL_ITEM_QUANTITY,   item.getQuantity());
                db.insert(TABLE_ORDER_ITEMS, null, iv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        // --- Push Order details to Google Sheets ---
        try {
            String orderData = "action=order" +
                    "&orderId=" + java.net.URLEncoder.encode(order.getOrderId(), "UTF-8") +
                    "&email=" + java.net.URLEncoder.encode("In-App Customer", "UTF-8") +
                    "&total=" + order.getTotalAmount() +
                    "&status=" + java.net.URLEncoder.encode(order.getStatus(), "UTF-8");
            pushToGoogleSheets(orderData);

            for (CartItem item : order.getItems()) {
                String itemData = "action=order_item" +
                        "&orderId=" + java.net.URLEncoder.encode(order.getOrderId(), "UTF-8") +
                        "&drinkName=" + java.net.URLEncoder.encode(item.getDrink().getName(), "UTF-8") +
                        "&quantity=" + item.getQuantity() +
                        "&price=" + item.getDrink().getPrice();
                pushToGoogleSheets(itemData);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    /** Load all orders from DB (newest first), with their items. */
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Get all drink objects for lookup
        List<Drink> allDrinks = DrinkData.getAllDrinks();

        Cursor oc = db.query(TABLE_ORDERS, null, null, null, null, null,
                COL_ORDER_ID + " DESC");

        if (oc.moveToFirst()) {
            do {
                String ref    = oc.getString(oc.getColumnIndexOrThrow(COL_ORDER_REF));
                double total  = oc.getDouble(oc.getColumnIndexOrThrow(COL_ORDER_TOTAL));
                String status = oc.getString(oc.getColumnIndexOrThrow(COL_ORDER_STATUS));
                String date   = oc.getString(oc.getColumnIndexOrThrow(COL_ORDER_DATE));
                String time   = oc.getString(oc.getColumnIndexOrThrow(COL_ORDER_TIME));

                // Load items for this order
                List<CartItem> items = getItemsForOrder(db, ref, allDrinks);
                orders.add(new Order(ref, items, total, status, date, time));
            } while (oc.moveToNext());
        }
        oc.close();
        db.close();
        return orders;
    }

    /** Get a specific order by ID */
    public Order getOrderById(String orderId) {
        Order order = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Drink> allDrinks = DrinkData.getAllDrinks();

        Cursor oc = db.query(TABLE_ORDERS, null, COL_ORDER_REF + "=?", new String[]{orderId}, null, null, null);
        if (oc.moveToFirst()) {
            double total  = oc.getDouble(oc.getColumnIndexOrThrow(COL_ORDER_TOTAL));
            String status = oc.getString(oc.getColumnIndexOrThrow(COL_ORDER_STATUS));
            String date   = oc.getString(oc.getColumnIndexOrThrow(COL_ORDER_DATE));
            String time   = oc.getString(oc.getColumnIndexOrThrow(COL_ORDER_TIME));
            List<CartItem> items = getItemsForOrder(db, orderId, allDrinks);
            order = new Order(orderId, items, total, status, date, time);
        }
        oc.close();
        db.close();
        return order;
    }

    /** Helper: load CartItems for a specific order ref from an already-open DB. */
    private List<CartItem> getItemsForOrder(SQLiteDatabase db, String orderRef, List<Drink> allDrinks) {
        List<CartItem> items = new ArrayList<>();
        Cursor ic = db.query(
                TABLE_ORDER_ITEMS, null,
                COL_ITEM_ORDER_REF + "=?",
                new String[]{orderRef},
                null, null, null
        );
        if (ic.moveToFirst()) {
            do {
                int drinkId  = ic.getInt(ic.getColumnIndexOrThrow(COL_ITEM_DRINK_ID));
                int quantity = ic.getInt(ic.getColumnIndexOrThrow(COL_ITEM_QUANTITY));

                // Find matching Drink object
                Drink drink = null;
                for (Drink d : allDrinks) {
                    if (d.getId() == drinkId) { drink = d; break; }
                }
                if (drink != null) items.add(new CartItem(drink, quantity));
            } while (ic.moveToNext());
        }
        ic.close();
        return items;
    }
}
