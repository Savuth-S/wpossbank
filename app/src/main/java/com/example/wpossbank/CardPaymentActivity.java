package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.wpossbank.Modelos.Validate;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class CardPaymentActivity extends AppCompatActivity {
    Context context;
    Validate validate;

    TextInputLayout textInputLayout;
    EditText cardNumberInput, expDateInput, ccvInput, nameInput, paymentAmountInput, duesInput;
    Button goBackButton, confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);
        context = this;
        validate = new Validate(context);

        textInputLayout = findViewById(R.id.textInputLayout);

        cardNumberInput = findViewById(R.id.cardNumberInput);
        expDateInput = findViewById(R.id.expDateInput);
        ccvInput = findViewById(R.id.ccvInput);
        nameInput = findViewById(R.id.nameInput);
        paymentAmountInput = findViewById(R.id.paymentAmountInput);
        duesInput = findViewById(R.id.duesInput);

        goBackButton = findViewById(R.id.goBackButton);
        confirmButton = findViewById(R.id.confirmButton);
        expDateInput.setOnClickListener( openDatePicker-> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dpd = new DatePickerDialog(context,
                    (datePicker, year, month, day) -> {
                        String string = datePicker.getYear() + "/" +
                                datePicker.getMonth() + "/" +
                                datePicker.getDayOfMonth();
                        expDateInput.setText(string);
                    }, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });

        cardNumberInput.setOnKeyListener((view, i, keyEvent) -> {
            if (!validate.isEmpty(cardNumberInput) && cardNumberInput.getText().toString().charAt(0) == '3') {
                textInputLayout.setHint("American Express");
            } else if (!validate.isEmpty(cardNumberInput) && cardNumberInput.getText().toString().charAt(0) == '4') {
                textInputLayout.setHint("VISA");
            } else if (!validate.isEmpty(cardNumberInput) && cardNumberInput.getText().toString().charAt(0) == '5') {
                textInputLayout.setHint("MasterCard");
            } else if (!validate.isEmpty(cardNumberInput) && cardNumberInput.getText().toString().charAt(0) == '6') {
                textInputLayout.setHint("UnionPay");
            } else {
                cardNumberInput.setError("Número de tarjeta invalido.");
                textInputLayout.setHint("Número de Tarjeta de Credito");
            }

            return false;
        });

        confirmButton.setOnClickListener(confirmPayment -> {
            boolean cardNumberValidate = validate.cardNumber(cardNumberInput),
                    expDateValidate = validate.expDate(expDateInput),
                    ccvValidate = validate.ccv(ccvInput),
                    nameValidate = validate.name(nameInput),
                    paymentValidate = validate.payment(paymentAmountInput),
                    duesValidate = validate.dues(duesInput);

            if (cardNumberValidate && expDateValidate && ccvValidate && nameValidate && paymentValidate && duesValidate){

            }
        });
    }
}