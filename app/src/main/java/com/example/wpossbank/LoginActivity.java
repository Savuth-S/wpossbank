package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.SharedPreference;
import com.example.wpossbank.modelos.User;
import com.example.wpossbank.modelos.Validate;
import com.example.wpossbank.database.Database;

public class LoginActivity extends AppCompatActivity {
    Context context = this;
    SharedPreference sp;
    Validate validate;
    User user;

    EditText ccInput, pinInput;
    Button enterButton;
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = new SharedPreference(context);
        validate = new Validate(context);
        user = new User(context);

        ccInput= findViewById(R.id.ccInput);
        pinInput = findViewById(R.id.pinInput);

        enterButton = findViewById(R.id.enterButton);
        backArrow = findViewById(R.id.backArrow);

        enterButton.setOnClickListener( validateLogin ->{
            if (validate.login(ccInput,pinInput)){
                user.setCc(ccInput.getText().toString());
                user.setPin(pinInput.getText().toString());

                Log.d("LOGIN","user="+user.getObjectId());
                sp.setActiveUser(user.getObjectId());
                startActivity(new Intent(context, MainActivity.class));
            }else {
                // Avisa al usuario si hay un campo con valores invalidos
                Toast.makeText(context, R.string.error_invalid_input, Toast.LENGTH_LONG).show();
            }
        });

        backArrow.setOnClickListener( goBack -> finish());
    }
}