package com.mocktail.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME    = "mocktail.db";
    public static final int    DB_VERSION = 1;

    // ── TABLE: users ───────────────────────────────────────────────────────────
    public static final String TABLE_USERS     = "users";
    public static final String COL_USER_ID     = "id";
    public static final String COL_USER_NAME   = "name";
    public static final String COL_USER_EMAIL  = "email";
    public static final String COL_USER_PASS   = "password";

    // ── TABLE: orders ──────────────────────────────────────────────────────────
    public static final String TABLE_ORDERS       = "orders";
    public static final String COL_ORDER_ID       = "id";           // AUTO row id
    public static final String COL_ORDER_REF      = "order_ref";    // e.g. #ABC12345
    public static final String COL_ORDER_TOTAL    = "total_amount";
    public static final String COL_ORDER_STATUS   = "status";
    public static final String COL_ORDER_DATE     = "order_date";
    public static final String COL_ORDER_TIME     = "order_time";

    // ── TABLE: order_items ─────────────────────────────────────────────────────
    public static final String TABLE_ORDER_ITEMS   = "order_items";
    public static final String COL_ITEM_ID         = "id";
    public static final String COL_ITEM_ORDER_REF  = "order_ref";   // FK → orders.order_ref
    public static final String COL_ITEM_DRINK_ID   = "drink_id";
    public static final String COL_ITEM_DRINK_NAME = "drink_name";
    public static final String COL_ITEM_PRICE      = "unit_price";
    public static final String COL_ITEM_QUANTITY   = "quantity";

    // ── CREATE statements ──────────────────────────────────────────────────────
    private static final String CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
            COL_USER_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_USER_NAME  + " TEXT NOT NULL, " +
            COL_USER_EMAIL + " TEXT NOT NULL UNIQUE, " +
            COL_USER_PASS  + " TEXT NOT NULL);";

    private static final String CREATE_ORDERS =
            "CREATE TABLE " + TABLE_ORDERS + " (" +
            COL_ORDER_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_ORDER_REF    + " TEXT NOT NULL UNIQUE, " +
            COL_ORDER_TOTAL  + " REAL NOT NULL, " +
            COL_ORDER_STATUS + " TEXT NOT NULL, " +
            COL_ORDER_DATE   + " TEXT NOT NULL, " +
            COL_ORDER_TIME   + " TEXT NOT NULL);";

    private static final String CREATE_ORDER_ITEMS =
            "CREATE TABLE " + TABLE_ORDER_ITEMS + " (" +
            COL_ITEM_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_ITEM_ORDER_REF  + " TEXT NOT NULL, " +
            COL_ITEM_DRINK_ID   + " INTEGER NOT NULL, " +
            COL_ITEM_DRINK_NAME + " TEXT NOT NULL, " +
            COL_ITEM_PRICE      + " REAL NOT NULL, " +
            COL_ITEM_QUANTITY   + " INTEGER NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS);
        db.execSQL(CREATE_ORDERS);
        db.execSQL(CREATE_ORDER_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}
