package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wpossbank.Modelos.User;
import com.example.wpossbank.Modelos.Validate;
import com.example.wpossbank.database.Database;

public class NewUserActivity extends AppCompatActivity {
    Context context;
    Database db;
    Validate validate;
    User user;

    EditText nameInput, lastnameInput, ccInput, pinInput, pinCofirmInput, balanceInput;
    Button enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        context = this;
        user = new User();
        validate = new Validate(context);

        nameInput = findViewById(R.id.nameInput);
        lastnameInput = findViewById(R.id.lastnameInput);
        ccInput = findViewById(R.id.ccInput);
        pinInput = findViewById(R.id.pinInput);
        pinCofirmInput = findViewById(R.id.pinConfirmInput);
        balanceInput = findViewById(R.id.balanceInput);
        enterButton = findViewById(R.id.enterButton);

        enterButton.setOnClickListener( addNewUser -> {
            db = new Database(context);
            user.setName(nameInput.getText().toString() + " " + lastnameInput.getText().toString());
            user.setCc(ccInput.getText().toString());
            user.setPin(pinInput.getText().toString());
            user.setBalance(Integer.parseInt(balanceInput.getText().toString()));

            boolean ccValidate = validate.cc((ccInput)),
                    nameValidate = validate.name(nameInput),
                    lastnameValidate = validate.name(lastnameInput),
                    pinValidate = validate.pin(pinInput),
                    balanceValidate = validate.balance((balanceInput));

            if (!validate.isEmpty(pinCofirmInput) && !user.getPin().equals(pinCofirmInput.getText().toString())) {
                pinCofirmInput.setError("Los PINs no son iguales");
            }else if (!validate.isEmpty(pinCofirmInput) && nameValidate && lastnameValidate && ccValidate && pinValidate && balanceValidate){
                db.addUser(user);
                startActivity(new Intent(context, LoginActivity.class));
            }
        });

    }
}