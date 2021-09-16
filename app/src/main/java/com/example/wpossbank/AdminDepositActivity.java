package com.example.wpossbank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wpossbank.fragments.Dialogs;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.MakeMessages;
import com.example.wpossbank.modelos.User;
import com.example.wpossbank.modelos.Validate;

public class AdminDepositActivity extends AppCompatActivity {
    Context context;
    Validate validate;
    MakeMessages messages;

    Admin admin;
    User user;

    EditText emailInput, passwordInput, depositInput;
    Button goBackButton, confirmButton;
    ImageView backArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_deposit);
        context = this;
        validate = new Validate(context);
        messages = new MakeMessages(context);

        admin = new Admin();
        user = new User(context);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.ccDepositInput);
        depositInput = findViewById(R.id.depositInput);
        
        confirmButton = findViewById(R.id.confirmButton);
        backArrow = findViewById(R.id.backArrow);

        user.loadData(user);//Carga la información del usuario desde la base de datos

        confirmButton.setOnClickListener(confirmDeposit -> {
            //Deckara y verifica si los campos tienen la información correcta
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