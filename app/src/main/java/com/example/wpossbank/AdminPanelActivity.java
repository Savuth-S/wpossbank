package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;

import com.example.wpossbank.adaptadores.TransactionLogAdapter;
import com.example.wpossbank.database.Database;
import com.example.wpossbank.fragments.Dialogs;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.MakeMessages;
import com.example.wpossbank.modelos.SharedPreference;
import com.example.wpossbank.modelos.User;
import com.example.wpossbank.modelos.Validate;

import java.util.ArrayList;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class AdminPanelActivity extends AppCompatActivity {
    Context context;
    Validate validate;
    Database db;
    MakeMessages messages;

    Admin admin;

    BlurView blurView;

    TextView balanceText;
    EditText emailInput, passwordInput, passwordConfirmInput;

    Button updateButton, addMoneyButton;
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        context = this;
        validate = new Validate(context);
        db = new Database(context);
        messages = new MakeMessages();

        admin = new Admin();
        admin.loadData(context, admin);

        blurView = findViewById(R.id.blurView);
        blurBackground();

        balanceText = findViewById(R.id.textView2);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        passwordConfirmInput = findViewById(R.id.passwordConfirmInput);

        updateButton = findViewById(R.id.updateButton);
        addMoneyButton = findViewById(R.id.addMoneyButton);
        backArrow = findViewById(R.id.backArrow);

        balanceText.setText(String.format("$%s", messages.separateNumberRight(
                Integer.toString(admin.getBalance()), ".", 3)));

        updateButton.setOnClickListener( validateLogin ->{
            boolean emailValidate = validate.email(emailInput),
                    passwordValidate = validate.password(passwordInput);

            if (!validate.isEmpty(passwordConfirmInput) &&
                    !passwordInput.getText().toString().equals(passwordConfirmInput.getText().toString())) {
                Log.d("CHANGE ADMIN", "The passwords do not match, " +
                        "password="+passwordInput.getText().toString() +" confirm="+passwordConfirmInput.getText().toString());
                passwordConfirmInput.setError(getResources().getString(R.string.error_wrong));
            }else if (!validate.isEmpty(passwordConfirmInput) && emailValidate && passwordValidate){
                admin.setEmail(emailInput.getText().toString());
                admin.setPassword(passwordInput.getText().toString());

                String message = context.getString(R.string.dialog_confirm_update_admin);

                new Dialogs.ConfirmUpdateAdmin(context, admin, message, "update", admin.getEmail(), admin.getEmail())
                        .show(getSupportFragmentManager(),"Confirm");
            }
        });

        addMoneyButton.setOnClickListener( showAddMoneyActivity ->
                startActivity(new Intent(context, AdminDepositActivity.class)));

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