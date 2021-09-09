package com.example.wpossbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.SharedPreference;
import com.example.wpossbank.modelos.User;
import com.example.wpossbank.modelos.Validate;
import com.example.wpossbank.database.Database;

public class LoginActivity extends AppCompatActivity {
    Context context = this;
    SharedPreference sp;
    Validate validate;
    Admin admin;
    User user;

    EditText ccInput, pinInput;
    Button loginButton, newUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = new SharedPreference(context);
        validate = new Validate(context);
        admin = new Admin();
        user = new User();
        makeDefaultAdmin();

        ccInput= findViewById(R.id.ccInput);
        pinInput = findViewById(R.id.pinInput);

        loginButton = findViewById(R.id.loginButton);
        newUserButton = findViewById(R.id.newUserButton);

        loginButton.setOnClickListener( validateLogin ->{
            if (validate.login(ccInput,pinInput)){
                user.setCc(ccInput.getText().toString());
                user.setPin(pinInput.getText().toString());

                sp.setActiveUser(user.getCc());
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
        Admin admin = new Admin();
        newUserButton.setOnClickListener( verNewUser -> startActivity(new Intent(context, NewUserActivity.class)));
    }

    protected void makeDefaultAdmin(){
        Database db = new Database(context);
        if (db.fetchData("1", db.getTable("admin"), db.getColumn("id")).getCount() < 1) {
            Log.e("LOGIN","Couldn't find admin data, making default admin instead");
            db.makeDefaultAdmin(admin);
        }
    }
}