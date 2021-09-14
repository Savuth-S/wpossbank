package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wpossbank.adaptadores.TransactionLogAdapter;
import com.example.wpossbank.database.Database;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.MakeMessages;
import com.example.wpossbank.modelos.SharedPreference;
import com.example.wpossbank.modelos.User;
import com.example.wpossbank.modelos.Validate;

import java.util.ArrayList;

public class AdminPanelActivity extends AppCompatActivity {
    Context context;
    Validate validate;
    Database db;
    MakeMessages messages;

    Admin admin;

    TextView balanceText;
    EditText emailInput, passwordInput, passwordConfirmInput;
    Button enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        context = this;
        validate = new Validate(context);
        db = new Database(context);
        messages = new MakeMessages(context);

        admin = new Admin();
        admin.loadData(context, admin);

        balanceText = findViewById(R.id.textView2);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        passwordConfirmInput = findViewById(R.id.passwordConfirmInput);
        enterButton = findViewById(R.id.enterButton);

        balanceText.setText(String.format("$%s", messages.separateNumberRight(
                Integer.toString(admin.getBalance()), ".", 3)));

        enterButton.setOnClickListener( validateLogin ->{
            boolean emailValidate = validate.email(emailInput),
                    passwordValidate = validate.password(passwordInput);

            if (!validate.isEmpty(passwordConfirmInput) &&
                    !passwordInput.getText().toString().equals(passwordConfirmInput.getText().toString())) {
                Log.d("CHANGE ADMIN", "The passwords do not match, " +
                        "password="+passwordInput.getText().toString() +" confirm="+passwordConfirmInput.getText().toString());
                passwordConfirmInput.setError(getResources().getString(R.string.error_wrong));
            }else if (!validate.isEmpty(passwordConfirmInput) && emailValidate && passwordValidate){
                admin.setEmail(emailInput.getText().toString());
                admin.setPassword(passwordInput.getText().toString());

                admin.update(context, admin);
            }
        });
    }
}