package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wpossbank.fragments.Dialogs;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.LogEntry;
import com.example.wpossbank.modelos.MakeMessages;
import com.example.wpossbank.modelos.Validate;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class TransfersActivity extends AppCompatActivity {
    Context context;
    Validate validate;

    Admin admin;
    MakeMessages messages;

    BlurView blurView;

    EditText ccInput, pinInput, pinConfirmInput, ccTransferInput, transferInput;
    Button goBackButton, confirmButton;
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfers);
        context = this;
        validate = new Validate(context);

        admin = new Admin();
        messages = new MakeMessages();

        blurView = findViewById(R.id.blurView);
        blurBackground();

        ccInput = findViewById(R.id.ccInput);
        pinInput = findViewById(R.id.pinInput);
        pinConfirmInput = findViewById(R.id.pinConfirmInput);
        ccTransferInput = findViewById(R.id.ccTransferInput);
        transferInput = findViewById(R.id.transferInput);

        confirmButton = findViewById(R.id.confirmButton);
        backArrow = findViewById(R.id.backArrow);

        confirmButton.setOnClickListener(confirmDeposit -> {
            //Deckara y verifica si los campos tienen la información correcta
            boolean ccValidate = validate.isNumber(ccInput) && validate.matchUserData(ccInput, "cc"),
                    pinValidate = validate.pin(pinInput) && validate.matchUserData(pinInput, "pin"),
                    ccTransferValidate = validate.isInRange(ccTransferInput,10,13) &&
                            validate.isInDatabase(ccTransferInput, "user", "cc") &&
                            validate.notMatchUserData(ccTransferInput, "cc"),
                    transferValidate = validate.useBalance(transferInput);

            if (!validate.isEmpty(pinConfirmInput) &&
                    !pinInput.getText().toString().equals(pinConfirmInput.getText().toString())) {
                Log.d("TRANSFER", "The pins do not match, " +
                        "pin="+pinInput.getText().toString() +" confirm="+pinConfirmInput.getText().toString());

                pinConfirmInput.setError(getResources().getString(R.string.error_wrong));
            }else if (ccValidate && pinValidate && ccTransferValidate && transferValidate){
                admin.setBalance(admin.getCost()/2);

                LogEntry logEntry = new LogEntry();
                logEntry.setType("transfer");
                logEntry.setAmount(Integer.parseInt(transferInput.getText().toString()));
                logEntry.setSource(ccInput.getText().toString());

                new Dialogs.ConfirmUserTransferBalance(context , logEntry, admin, ccTransferInput.getText().toString(),
                        messages.transfer(context, transferInput, ccTransferInput))
                        .show(getSupportFragmentManager(),"CONFIRM");
            }else {
                // Avisa al usuario si hay un campo con valores invalidos
                Toast.makeText(context, R.string.error_invalid_input, Toast.LENGTH_LONG).show();
            }
        });

        backArrow.setOnClickListener( goBack -> finish());
    }

    private void blurBackground(){
        float radius = 20f;

        View decorView = getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();

        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setBlurAutoUpdate(true)
                .setHasFixedTransformationMatrix(true);
    }
}