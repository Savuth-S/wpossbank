package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.wpossbank.database.Database;

public class MainActivity extends AppCompatActivity{
    Context context = this;
    Button cardPaymentsButton, withdrawalsButton, depositsButton,
            transfersButton, balanceButton, historyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardPaymentsButton = findViewById(R.id.cardPaymentsButton);
        withdrawalsButton = findViewById(R.id.withdrawalsButton);
        depositsButton = findViewById(R.id.depositsButton);
        transfersButton = findViewById(R.id.transfersButton);
        balanceButton = findViewById(R.id.balanceButton);
        historyButton = findViewById(R.id.historyButton);

        cardPaymentsButton.setOnClickListener(verCardPayment -> {
            startActivity(new Intent(context, CardPaymentActivity.class));
        });

        withdrawalsButton.setOnClickListener(verCardPayment -> {
            startActivity(new Intent(context, WithdrawalsActivity.class));
        });

        depositsButton.setOnClickListener(verCardPayment -> {
            startActivity(new Intent(context, DepositsActivity.class));
        });
    }
}