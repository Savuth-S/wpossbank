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

import com.example.wpossbank.fragments.Dialogs;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.MakeMessages;
import com.example.wpossbank.modelos.User;
import com.example.wpossbank.modelos.Validate;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class WithdrawalsActivity extends AppCompatActivity {
    Context context;
    Validate validate;
    MakeMessages messages;

    Admin admin;
    User user;

    BlurView blurView;

    EditText ccInput, pinInput, pinConfirmInput, withdrawalInput;
    Button goBackButton, confirmButton;
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals);
        context = this;
        validate = new Validate(context);
        messages = new MakeMessages();

        admin = new Admin();
        user = new User(context);

        blurView = findViewById(R.id.blurView);
        blurBackground();

        ccInput = findViewById(R.id.ccInput);
        pinInput = findViewById(R.id.pinInput);
        pinConfirmInput = findViewById(R.id.pinConfirmInput);
        withdrawalInput = findViewById(R.id.withdrawalInput);

        confirmButton = findViewById(R.id.confirmButton);
        backArrow = findViewById(R.id.backArrow);

        user.loadData(user);//Carga la información del usuario desde la base de datos

        confirmButton.setOnClickListener(confirmPayment -> {
            //Deckara y verifica si los campos tienen la información correcta
            boolean ccValidate = validate.isNumber(ccInput) && validate.matchUserData(ccInput, "cc"),
                    pinValidate = validate.pin(pinInput) && validate.matchUserData(pinInput, "pin"),
                    withdrawalValidate = validate.useBalance(withdrawalInput);

            if (!validate.isEmpty(pinConfirmInput) &&
                    !pinInput.getText().toString().equals(pinConfirmInput.getText().toString())) {
                Log.d("WITHDRAW", "The pins do not match, " +
                        "pin=" + pinInput.getText().toString() + " confirm=" + pinConfirmInput.getText().toString());
                pinConfirmInput.setError(getResources().getString(R.string.error_wrong));
            }else if (!validate.isEmpty(pinConfirmInput) && ccValidate && pinValidate && withdrawalValidate){
                int withdrawValue = Integer.parseInt(withdrawalInput.getText().toString());

                admin.setBalance(admin.getCost());
                new Dialogs.ConfirmUserAddBalance(context , admin,
                        messages.withdraw(context,withdrawalInput), "withdraw", user.getCc(),
                        withdrawValue-(withdrawValue*2+admin.getCost()))
                        .show(getSupportFragmentManager(),"CONFIRM2");
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