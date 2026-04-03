## 📊 Viewing SQLite Database in Android App

Since SQLite is an internal database in Android, it cannot be accessed directly like a normal file. However, Android Studio provides powerful tools to inspect and manage your app's database.

---

### 🔍 Method 1: Using Database Inspector (Recommended)

Follow these steps to view live database data:

1. Run your app on an emulator or physical device.
2. Open **App Inspection** from the bottom toolbar in Android Studio.
3. Click on **Database Inspector**.
4. Select your app process (e.g., `com.mocktail.app`).
5. Expand your database (e.g., `mocktail.db`).
6. Open tables like:

   * `TABLE_USERS`
   * `TABLE_ORDERS`
   * `TABLE_ORDER_ITEMS`

📌 The data will appear in a spreadsheet-like format.

#### ⭐ Features:

* **Live Updates**: Enable "Live updates" to see data changes instantly.
* **Edit Data**: Double-click any value to update it in real-time.

---

### 📥 Method 2: Download Database File (Advanced)

#### Step 1: Install SQLite Viewer

Download **DB Browser for SQLite**.

#### Step 2: Extract Database

1. Run your app.
2. Open **Device File Explorer** in Android Studio.
3. Navigate to:

   ```
   data → data → com.mocktail.app → databases
   ```
4. Right-click `mocktail.db` → **Save As**.
5. Also download:

   * `mocktail.db-shm`
   * `mocktail.db-wal`

#### Step 3: Open Database

1. Open DB Browser for SQLite.
2. Click **Open Database**.
3. Select `mocktail.db`.
4. Go to **Browse Data** tab.
5. Choose tables from dropdown.

📌 You can view, edit, and even re-upload the database.

---

### 💡 Notes

* SQLite database is private to the app.
* Use Database Inspector for quick debugging.
* Use DB Browser for deep analysis or backup.

---
