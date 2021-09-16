package com.example.wpossbank.modelos;

import android.content.Context;
import android.database.Cursor;

import com.example.wpossbank.database.Database;

public class User {
    private final Context context;
    private final SharedPreference sp;
    private final Database db;

    private String id = "0";
    private String pin = "1234";
    private String cc = "1234";
    private int balance = 0;
    private String name = "Usuario Usuario";

    public User(Context context){
        this.context = context;
        sp = new SharedPreference(context);
        db = new Database(context);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPin() { return pin; }

    public void setPin(String pin) { this.pin = pin; }
    public String getCc() { return cc; }

    public void setCc(String cc) { this.cc = cc; }
    public int getBalance() { return balance; }

    public void setBalance(int balance) { this.balance = balance; }
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getObjectId(){
        try (Cursor fetch = db.fetchData(
                getCc(),
                db.getTable("user"),
                db.getColumn("cc"))) {
            fetch.moveToFirst();
            if (fetch.getCount() > 0) {
                return fetch.getString(1);
            }else{
                return "";
            }
        }
    }

    public void loadData(User user){
        try (Cursor fetch = db.fetchData(
                sp.getActiveUser(),
                db.getTable("user"),
                db.getColumn("object id"))){
            fetch.moveToFirst();
            user.setId(fetch.getString(0));
            user.setCc(fetch.getString(2));
            user.setPin(fetch.getString(3));
            user.setBalance(Integer.parseInt(fetch.getString(4)));
            user.setName(fetch.getString(5));
        }
    }

    public User loadUser(String cc){
        try (Cursor fetch = db.fetchData(
                cc,
                db.getTable("user"),
                db.getColumn("cc"))){
            User user = new User(context);

            fetch.moveToFirst();
            user.setId(fetch.getString(0));
            user.setCc(fetch.getString(2));
            user.setPin(fetch.getString(3));
            user.setBalance(fetch.getInt(4));
            user.setName(fetch.getString(5));

            return user;
        }
    }

    public void update(Context context, User user){
        Database db = new Database(context);
        db.updateUser(user);
    }
}
