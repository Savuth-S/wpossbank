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

    // Obtiene el valor unico del ID del objeto registrado en la base de datos
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

    // carga la informaci�n en la base de datos del usuario activo a el objeto usuario local
    public void loadData(){
        try (Cursor fetch = db.fetchData(
                sp.getActiveUser(),
                db.getTable("user"),
                db.getColumn("object id"))){
            fetch.moveToFirst();
            setId(fetch.getString(0));
            setCc(fetch.getString(2));
            setPin(fetch.getString(3));
            setBalance(Integer.parseInt(fetch.getString(4)));
            setName(fetch.getString(5));
        }
    }

    // carga la informaci�n en la base de datos con respecto al numero de cuenta de un usuario al objeto usuario local
    public void loadUser(String cc){
        try (Cursor fetch = db.fetchData(
                cc,
                db.getTable("user"),
                db.getColumn("cc"))){

            fetch.moveToFirst();
            setId(fetch.getString(0));
            setCc(fetch.getString(2));
            setPin(fetch.getString(3));
            setBalance(fetch.getInt(4));
            setName(fetch.getString(5));
        }
    }

    // llama a la funcion para actualizar los datos del usuario en la base de datos
    public void update(Context context, User user){
        Database db = new Database(context);
        db.updateUser(user);
    }
}
