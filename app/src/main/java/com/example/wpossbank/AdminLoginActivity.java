package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.wpossbank.database.Database;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.SharedPreference;
import com.example.wpossbank.modelos.User;
import com.example.wpossbank.modelos.Validate;

public class AdminLoginActivity extends AppCompatActivity {
    Context context = this;
    SharedPreference sp;
    Validate validate;

    Admin admin;

    EditText emailInput, passwordInput;
    Button enterButton;
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        sp = new SharedPreference(context);
        validate = new Validate(context);

        admin = new Admin();

        emailInput= findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        enterButton = findViewById(R.id.enterButton);
        backArrow = findViewById(R.id.backArrow);

        enterButton.setOnClickListener( validateLogin ->{
            if (validate.adminLogin(emailInput,passwordInput)){
                admin.setEmail(emailInput.getText().toString());
                admin.setPassword(passwordInput.getText().toString());

                Log.d("ADMIN","logged into admin panel");
                startActivity(new Intent(context, AdminPanelActivity.class));
            }
        });

        backArrow.setOnClickListener( goBack -> finish());
    }


}