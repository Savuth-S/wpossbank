package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.wpossbank.fragments.Dialogs;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.SharedPreference;
import com.example.wpossbank.modelos.User;
import com.example.wpossbank.modelos.Validate;

public class WithdrawalsActivity extends AppCompatActivity {
    Context context;
    SharedPreference sp;
    EditText ccInput, pinInput, pinConfirmInput, withdrawalInput;
    Button goBackButton, confirmButton;
    User user;
    Admin admin;
    Validate validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals);
        context = this;
        sp = new SharedPreference(context);
        validate = new Validate(context);
        user = new User(context);
        admin = new Admin();

        ccInput = findViewById(R.id.ccInput);
        pinInput = findViewById(R.id.pinInput);
        pinConfirmInput = findViewById(R.id.pinConfirmInput);
        withdrawalInput = findViewById(R.id.withdrawalInput);

        goBackButton = findViewById(R.id.goBackButton);
        confirmButton = findViewById(R.id.confirmButton);

        user.loadData(user);//Carga la información del usuario desde la base de datos

        confirmButton.setOnClickListener(confirmPayment -> {
            //Deckara y verifica si los campos tienen la información correcta
            boolean ccValidate = !validate.isEmpty(ccInput) && validate.matchUserData(ccInput, "cc"),
                    pinValidate = validate.pin(pinInput) && validate.matchUserData(pinInput, "pin"),
                    withdrawalValidate = validate.withdrawal(withdrawalInput);

            if (!validate.isEmpty(pinConfirmInput) &&
                    !pinInput.getText().toString().equals(pinConfirmInput.getText().toString())) {
                Log.d("WITHDRAW", "The pins do not match, " +
                        "pin=" + pinInput.getText().toString() + " confirm=" + pinConfirmInput.getText().toString());
                pinConfirmInput.setError(getResources().getString(R.string.error_wrong));
            }else if (!validate.isEmpty(pinConfirmInput) && ccValidate && pinValidate && withdrawalValidate){
                admin.setBalance(admin.getCost());
                new Dialogs.ConfirmWithdrawal(this ,admin, Integer.parseInt(withdrawalInput.getText().toString()))
                        .show(getSupportFragmentManager(),"CONFIRM");
            }
        });
    }
}