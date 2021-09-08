package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.wpossbank.Modelos.SharedPreference;
import com.example.wpossbank.Modelos.User;
import com.example.wpossbank.Modelos.Validate;
import com.example.wpossbank.database.Database;
import com.example.wpossbank.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    Context context = this;
    Validate validate;
    User user;
    SharedPreference sp;

    EditText ccInput, pinInput;
    Button loginButton, newUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = new SharedPreference(context);
        validate = new Validate(context);
        user = new User();

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

        newUserButton.setOnClickListener( verNewUser ->{
            startActivity(new Intent(context, NewUserActivity.class));
        });
    }

}