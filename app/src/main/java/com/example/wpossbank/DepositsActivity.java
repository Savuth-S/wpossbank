package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.wpossbank.fragments.Dialogs;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.MakeMessages;
import com.example.wpossbank.modelos.User;
import com.example.wpossbank.modelos.Validate;

public class DepositsActivity extends AppCompatActivity {
    Context context;
    Validate validate;
    MakeMessages messages;

    Admin admin;
    User user;

    EditText ccInput, ccDepositInput, depositInput;
    Button goBackButton, confirmButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        context = this;
        validate = new Validate(context);
        messages = new MakeMessages(context);

        admin = new Admin();
        user = new User(context);

        ccInput = findViewById(R.id.ccInput);
        ccDepositInput = findViewById(R.id.ccDepositInput);
        depositInput = findViewById(R.id.depositInput);

        goBackButton = findViewById(R.id.goBackButton);
        confirmButton = findViewById(R.id.confirmButton);

        user.loadData(user);//Carga la información del usuario desde la base de datos

        confirmButton.setOnClickListener(confirmDeposit -> {
            //Deckara y verifica si los campos tienen la información correcta
            boolean ccValidate = validate.isNumber(ccInput) && validate.matchUserData(ccInput, "cc"),
                    ccDepositValidate = validate.isNumber(ccDepositInput) && validate.isInRange(ccDepositInput,10,13),
                    depositValidate = validate.isInRange(depositInput,2,7);

            if (ccValidate && ccDepositValidate && depositValidate){
                int depositValue = Integer.parseInt(depositInput.getText().toString());

                admin.setBalance(admin.getCost()/2);
                new Dialogs.ConfirmUserAddBalance(context ,admin,
                            messages.deposit(context, depositInput), "deposit",
                        user.getCc(), depositValue).show(getSupportFragmentManager(),"CONFIRM");
            }
        });

        goBackButton.setOnClickListener( goBack -> finish());
    }
}