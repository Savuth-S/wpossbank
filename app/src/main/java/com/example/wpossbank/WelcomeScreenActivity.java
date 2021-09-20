package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.wpossbank.database.Database;
import com.example.wpossbank.modelos.Admin;

public class WelcomeScreenActivity extends AppCompatActivity {
    Context context;
    Admin admin;

    Button loginButton, adminButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        context = this;
        admin = new Admin();

        makeDefaultAdmin();

        loginButton = findViewById(R.id.loginButton);
        adminButton = findViewById(R.id.adminButton);

        loginButton.setOnClickListener( showLoginScreen -> startActivity(new Intent(context, LoginActivity.class)));
        adminButton.setOnClickListener( showAdminPanel -> startActivity(new Intent(context, AdminLoginActivity.class)));
    }

    protected void makeDefaultAdmin(){
        Database db = new Database(context);
        if (db.fetchData("1", db.getTable("admin"), db.getColumn("id")).getCount() < 1) {
            Log.e("LOGIN","Couldn't find admin data, making default admin instead");
            db.makeDefaultAdmin(admin);
        }
    }
}