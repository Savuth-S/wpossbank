package com.example.wpossbank;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wpossbank.fragments.Dialogs;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.MakeMessages;
import com.example.wpossbank.modelos.Validate;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class AdminDepositActivity extends AppCompatActivity {
    Context context;
    Validate validate;
    MakeMessages messages;

    Admin admin;

    BlurView blurView;

    EditText emailInput, passwordInput, depositInput;
    Button confirmButton;
    ImageView backArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_deposit);
        context = this;
        validate = new Validate(context);
        messages = new MakeMessages();

        admin = new Admin();

        blurView = findViewById(R.id.blurView);
        blurBackground();

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        depositInput = findViewById(R.id.depositInput);
        
        confirmButton = findViewById(R.id.confirmButton);
        backArrow = findViewById(R.id.backArrow);

        confirmButton.setOnClickListener(confirmDeposit -> {
            //Deckara y verifica si los campos tienen la información correcta
            if (validate.adminLogin(emailInput, passwordInput)) {
                admin.setBalance(Integer.parseInt(depositInput.getText().toString()));

                new Dialogs.ConfirmUpdateAdmin(context, admin,
                        messages.adminDeposit(context, depositInput), "deposit",
                        admin.getEmail(), admin.getObjectId(context))
                        .show(getSupportFragmentManager(),"CONFIRM");
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