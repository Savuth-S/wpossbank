package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

public class DepositsActivity extends AppCompatActivity {
    Context context;
    Validate validate;
    MakeMessages messages;

    Admin admin;
    User user;

    BlurView blurView;

    EditText ccInput, ccDepositInput, depositInput;
    Button confirmButton;
    ImageView backArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        context = this;
        validate = new Validate(context);
        messages = new MakeMessages();

        admin = new Admin();
        user = new User(context);

        blurView = findViewById(R.id.blurView);
        blurBackground();

        ccInput = findViewById(R.id.ccInput);
        ccDepositInput = findViewById(R.id.ccDepositInput);
        depositInput = findViewById(R.id.depositInput);

        confirmButton = findViewById(R.id.confirmButton);
        backArrow = findViewById(R.id.backArrow);

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
                            messages.adminDeposit(context, depositInput), "deposit",
                        user.getCc(), depositValue).show(getSupportFragmentManager(),"CONFIRM");
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