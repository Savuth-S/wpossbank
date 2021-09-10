package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.wpossbank.fragments.Dialogs;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.MakeMessages;
import com.example.wpossbank.modelos.Validate;

public class TransfersActivity extends AppCompatActivity {
    Context context;
    Validate validate;

    Admin admin;
    MakeMessages messages;

    EditText ccInput, pinInput, pinConfirmInput, ccTransferInput, transferInput;
    Button goBackButton, confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfers);
        context = this;
        validate = new Validate(context);

        admin = new Admin();
        messages = new MakeMessages(context);

        ccInput = findViewById(R.id.ccInput);
        pinInput = findViewById(R.id.pinInput);
        pinConfirmInput = findViewById(R.id.pinConfirmInput);
        ccTransferInput = findViewById(R.id.ccTransferInput);
        transferInput = findViewById(R.id.transferInput);

        goBackButton = findViewById(R.id.goBackButton);
        confirmButton = findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(confirmDeposit -> {
            //Deckara y verifica si los campos tienen la información correcta
            boolean ccValidate = validate.isNumber(ccInput) && validate.matchUserData(ccInput, "cc"),
                    pinValidate = validate.pin(pinInput) && validate.matchUserData(pinInput, "pin"),
                    ccTransferValidate = validate.isInRange(ccTransferInput,9,13) &&
                            validate.isInDatabase(ccTransferInput, "user", "cc") &&
                            validate.notMatchUserData(ccInput, "cc"),
                    transferValidate = validate.useBalance(transferInput);

            if (!validate.isEmpty(pinConfirmInput) &&
                    !pinInput.getText().toString().equals(pinConfirmInput.getText().toString())) {
                Log.d("TRANSFER", "The pins do not match, " +
                        "pin="+pinInput.getText().toString() +" confirm="+pinConfirmInput.getText().toString());

                pinConfirmInput.setError(getResources().getString(R.string.error_wrong));
            }else if (ccValidate && pinValidate && ccTransferValidate && transferValidate){
                int transferValue = Integer.parseInt(transferInput.getText().toString());

                admin.setBalance(admin.getCost()/2);
                new Dialogs.ConfirmUserTransferBalance(context , admin,
                        ccTransferInput.getText().toString(),
                        messages.transfer(context, transferInput, ccTransferInput), transferValue)
                        .show(getSupportFragmentManager(),"CONFIRM3");
            }
        });

        goBackButton.setOnClickListener( goBack -> finish());
    }
}