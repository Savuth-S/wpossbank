package com.example.wpossbank.modelos;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wpossbank.database.Database;
import com.example.wpossbank.fragments.Dialogs;

public class Admin extends AppCompatActivity {
    private String id = "1";
    private String email = "carlos@wposs.com";
    private String password = "123456";
    private int balance = 0;
    private int cost = 2_000;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getBalance() { return balance; }
    public void setBalance(int balance) { this.balance = balance; }

    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }

    public void update(Context context, Admin admin){
        Database db = new Database(context);
        db.updateAdmin(admin);
    }

    public void loadData(Context context, @NonNull Admin admin){
        Database db = new Database(context);
        try (Cursor fetch = db.fetchData(
                "1",
                db.getTable("admin"),
                db.getColumn("id"))){
            fetch.moveToFirst();
            admin.setId(fetch.getString(0));
            admin.setEmail(fetch.getString(1));
            admin.setPassword(fetch.getString(2));
            admin.setBalance(fetch.getInt(3));
        }
    }
}
