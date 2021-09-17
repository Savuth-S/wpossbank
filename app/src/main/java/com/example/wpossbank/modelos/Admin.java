package com.example.wpossbank.modelos;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

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

    public String getObjectId(Context context){
        Database db = new Database(context);

        try (Cursor fetch = db.fetchData(
                "1",
                db.getTable("admin"),
                db.getColumn("id"))) {
            fetch.moveToFirst();
            if (fetch.getCount() > 0) {
                return fetch.getString(1);
            }else{
                return "";
            }
        }
    }

    public void update(Context context){
        Database db = new Database(context);
        Cursor fetch = db.fetchData("1",db.getTable("admin"),db.getColumn("id"));
        Admin existingAdmin = new Admin();

        if (fetch.getCount() > 0){
            fetch.moveToFirst();
            existingAdmin.setId(fetch.getString(0));
            existingAdmin.setEmail(fetch.getString(2));
            existingAdmin.setPassword(fetch.getString(3));
            existingAdmin.setBalance(fetch.getInt(4));

            if(!getEmail().equals(existingAdmin.getEmail())){
                Log.d("UPDATE","New email found for admin, current email="+existingAdmin.getEmail()+" new email="+getEmail());
                existingAdmin.setEmail(getEmail());
            }

            if(!getPassword().equals(existingAdmin.getPassword())){
                Log.d("UPDATE","New password found for admin, current password="+existingAdmin.getPassword()+" new password="+getPassword());
                existingAdmin.setPassword(getPassword());
            }

            int balance = existingAdmin.getBalance();
            existingAdmin.setBalance(balance + getBalance());

            db.updateAdmin(existingAdmin);
        }else{
            Log.e("UPDATE","Couldn't find existing admin data, sending default values instead");
            db.updateAdmin(existingAdmin);
        }
    }

    public void loadData(Context context, @NonNull Admin admin){
        Database db = new Database(context);
        try (Cursor fetch = db.fetchData(
                "1",
                db.getTable("admin"),
                db.getColumn("id"))){
            fetch.moveToFirst();
            admin.setId(fetch.getString(0));
            admin.setEmail(fetch.getString(2));
            admin.setPassword(fetch.getString(3));
            admin.setBalance(fetch.getInt(4));
        }
    }
}
