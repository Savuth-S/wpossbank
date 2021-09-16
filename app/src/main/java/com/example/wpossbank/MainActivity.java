package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wpossbank.modelos.User;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MainActivity extends AppCompatActivity{
    Context context;
    User user;

    BlurView blurView;

    TextView userView;
    Button cardPaymentsButton, withdrawalsButton, depositsButton,
            transfersButton, balanceButton, historyButton, logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        user = new User(context);

        blurView = findViewById(R.id.blurView);
        blurBackground();

        userView = findViewById(R.id.userView);

        cardPaymentsButton = findViewById(R.id.cardPaymentsButton);
        withdrawalsButton = findViewById(R.id.withdrawalsButton);
        depositsButton = findViewById(R.id.depositsButton);
        transfersButton = findViewById(R.id.transfersButton);
        balanceButton = findViewById(R.id.balanceButton);
        historyButton = findViewById(R.id.historyButton);
        logOutButton = findViewById(R.id.logOutButton);

        user.loadData(user);
        userView.setText(getString(R.string.main_welcome_message, user.getName()));

        cardPaymentsButton.setOnClickListener(showCardPayment ->
            startActivity(new Intent(context, CardPaymentActivity.class)));

        withdrawalsButton.setOnClickListener(showWithdraws ->
            startActivity(new Intent(context, WithdrawalsActivity.class)));

        depositsButton.setOnClickListener(showDeposits ->
            startActivity(new Intent(context, DepositsActivity.class)));

        transfersButton.setOnClickListener(showTransfers ->
            startActivity(new Intent(context, TransfersActivity.class)));

        balanceButton.setOnClickListener(showBalance ->
            startActivity(new Intent(context, GetBalanceActivity.class)));

        historyButton.setOnClickListener(showHistory ->
                startActivity(new Intent(context, TransfersHistoryActivity.class)));

        logOutButton.setOnClickListener(finishActivity -> finish());
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