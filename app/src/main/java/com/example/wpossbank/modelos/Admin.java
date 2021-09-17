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

    // Obtiene el valor unico del ID del objeto registrado en la base de datos
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

    // actualiza la informaci�n de la base de datos con la informaci�n del objeto local admin
    public void update(Context context){
        Database db = new Database(context);
        Admin existingAdmin = new Admin();

        // carga la informaci�n en la base de datos a un nuevo objeto admin
        existingAdmin.loadData(context);

            // compara la informaci�n de la base de datos con la informacion local del admin y la actualiza
            if(!getEmail().equals(existingAdmin.getEmail())){
                Log.d("UPDATE","New email found for admin, current email="+existingAdmin.getEmail()+" new email="+getEmail());
                existingAdmin.setEmail(getEmail());
            }

            if(!getPassword().equals(existingAdmin.getPassword())){
                Log.d("UPDATE","New password found for admin, current password="+existingAdmin.getPassword()+" new password="+getPassword());
                existingAdmin.setPassword(getPassword());
            }

            // añade el nuevo saldo al saldo del corresponsal registrado en la base de datos
            int balance = existingAdmin.getBalance();
            existingAdmin.setBalance(balance + getBalance());

            db.updateAdmin(existingAdmin);
    }

    // carga la informaci�n del admin de la base de datos al objeto admin actual
    public void loadData(Context context){
        Database db = new Database(context);
        try (Cursor fetch = db.fetchData(
                "1",
                db.getTable("admin"),
                db.getColumn("id"))){
            if (fetch.getCount() > 0){

                fetch.moveToFirst();
                setId(fetch.getString(0));
                setEmail(fetch.getString(2));
                setPassword(fetch.getString(3));
                setBalance(fetch.getInt(4));
            }else{
                // escribe los valores por defecto si no encuentra datos en la base de datos
                Log.e("UPDATE","Couldn't find existing admin data, setting default values instead");
                Admin admin = new Admin();
                setId(admin.getId());
                setEmail(admin.getEmail());
                setPassword(admin.getPassword());
                setBalance(admin.getBalance());
            }
        }
    }
}
