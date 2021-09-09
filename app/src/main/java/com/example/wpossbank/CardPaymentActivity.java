package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.wpossbank.fragments.Dialogs;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.CreditCard;
import com.example.wpossbank.modelos.Validate;
import com.example.wpossbank.database.Database;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class CardPaymentActivity extends AppCompatActivity {
    Context context;
    Resources res;

    Database db;
    Validate validate;

    Admin admin;
    CreditCard card;

    TextInputLayout textInputLayout;
    EditText cardNumberInput, expDateInput, ccvInput, nameInput, lastnameInput, paymentAmountInput, duesInput;
    Button goBackButton, confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);
        context = this;
        res = getResources();

        //Declaracion de metodos
        validate = new Validate(context);

        //Declaracion de objetos
        db = new Database(context);
        admin = new Admin();
        card = new CreditCard();

        //Declaracion de elementos del layout
        textInputLayout = findViewById(R.id.textInputLayout);

        cardNumberInput = findViewById(R.id.cardNumberInput);
        expDateInput = findViewById(R.id.expDateInput);
        ccvInput = findViewById(R.id.ccvInput);
        nameInput = findViewById(R.id.nameInput);
        lastnameInput = findViewById(R.id.lastnameInput);
        paymentAmountInput = findViewById(R.id.paymentAmountInput);
        duesInput = findViewById(R.id.duesInput);

        goBackButton = findViewById(R.id.goBackButton);
        confirmButton = findViewById(R.id.confirmButton);

        //Abre el calendario para seleccionar la fecha al tocar el campo de texto
        expDateInput.setOnClickListener( openDatePicker-> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dpd = new DatePickerDialog(context,
                    (datePicker, year, month, day) -> {
                //Traduce el objeto calendar a string para mostrar la fecha en el campo de texto
                        String string = datePicker.getYear() + "/" +
                                datePicker.getMonth() + "/" +
                                datePicker.getDayOfMonth();
                        expDateInput.setText(string);
                    }, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });

        //Obtiene el tipo de tarjeta y actualzia el titulo del campo de texto acorde
        cardNumberInput.setOnKeyListener((view, i, keyEvent) -> {
            card.setNumber(cardNumberInput.getText().toString());

            if (!validate.isEmpty(cardNumberInput)) {
                textInputLayout.setHint(card.getType());
            }else{
                cardNumberInput.setError(res.getString(R.string.error_invalid));
            }

            return false;
        });

        //Confirma todos los campos antes de continuar
        confirmButton.setOnClickListener(confirmPayment -> {
            //Declara los valores del formulario en las propiedades correspondientes de la tarjeta
            card.setNumber(cardNumberInput.getText().toString());
            card.setExpDate(expDateInput.getText().toString());
            card.setCcv(ccvInput.getText().toString());
            card.setOwnerName(nameInput.getText().toString() + " " + lastnameInput.getText().toString());
            card.setPaymentAmmount(paymentAmountInput.getText().toString());
            card.setDuesNumber(duesInput.getText().toString());

            boolean cardNumberValidate = validate.cardNumber(cardNumberInput),
                    expDateValidate = validate.expDate(expDateInput),
                    ccvValidate = validate.ccv(ccvInput),
                    nameValidate = validate.name(nameInput),
                    lastnameValidate = validate.name(lastnameInput),
                    paymentValidate = validate.payment(paymentAmountInput),
                    duesValidate = validate.dues(duesInput);

            if (cardNumberValidate && expDateValidate && ccvValidate && nameValidate && lastnameValidate && paymentValidate && duesValidate){
                admin.setBalance(Integer.parseInt(card.getPaymentAmmount()));
                new Dialogs.ConfirmTransaction(this ,admin, card)
                        .show(getSupportFragmentManager(),"Confirm");
            }
        });
    }
}