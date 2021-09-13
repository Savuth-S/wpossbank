package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.User;
import com.example.wpossbank.modelos.Validate;
import com.example.wpossbank.database.Database;

public class NewUserActivity extends AppCompatActivity {
    Context context;
    Database db;
    Validate validate;

    Admin admin;
    User user;

    EditText nameInput, lastnameInput, ccInput, pinInput, pinConfirmInput, balanceInput;
    Button enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        context = this;
        validate = new Validate(context);

        admin = new Admin();
        user = new User(context);

        nameInput = findViewById(R.id.nameInput);
        lastnameInput = findViewById(R.id.lastnameInput);
        ccInput = findViewById(R.id.ccInput);
        pinInput = findViewById(R.id.pinInput);
        pinConfirmInput = findViewById(R.id.pinConfirmInput);
        balanceInput = findViewById(R.id.balanceInput);
        enterButton = findViewById(R.id.enterButton);

        enterButton.setOnClickListener( addNewUser -> {
            db = new Database(context);
            boolean ccValidate = validate.cc((ccInput)),
                    nameValidate = validate.name(nameInput),
                    lastnameValidate = validate.name(lastnameInput),
                    pinValidate = validate.pin(pinInput),
                    balanceValidate = validate.initialBalance(balanceInput);

            if (!validate.isEmpty(pinConfirmInput) &&
                    !pinInput.getText().toString().equals(pinConfirmInput.getText().toString())) {
                Log.d("NEW USER", "The pins do not match, " +
                        "pin="+pinInput.getText().toString() +" confirm="+pinConfirmInput.getText().toString());
                pinConfirmInput.setError(getResources().getString(R.string.error_wrong));
            }else if (!validate.isEmpty(pinConfirmInput) && nameValidate && lastnameValidate && ccValidate && pinValidate && balanceValidate){
                user.setName(nameInput.getText().toString() + " " + lastnameInput.getText().toString());
                user.setCc(ccInput.getText().toString());
                user.setPin(pinInput.getText().toString());
                user.setBalance(Integer.parseInt(balanceInput.getText().toString()));

                Log.d("NEW USER","user="+user.toString().split("@")[1]);
                admin.setBalance(admin.getCost()*5);
                admin.update(context, admin);

                db.addUser(user);
                db.newLogEntry("new user", "0", user.getCc());

                finish();
            }
        });

    }
}