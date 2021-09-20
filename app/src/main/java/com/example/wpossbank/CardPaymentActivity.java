package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wpossbank.fragments.Dialogs;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.CreditCard;
import com.example.wpossbank.modelos.MakeMessages;
import com.example.wpossbank.modelos.SharedPreference;
import com.example.wpossbank.modelos.Validate;
import com.example.wpossbank.database.Database;

import java.util.Calendar;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class CardPaymentActivity extends AppCompatActivity {
    Context context;
    Resources res;
    SharedPreference sp;

    Database db;
    Validate validate;
    MakeMessages messages;

    Admin admin;
    CreditCard card;

    BlurView blurView;

    TextView cardTextView;
    EditText cardNumberInput, expDateInput, ccvInput, nameInput, lastnameInput, paymentAmountInput, duesInput;

    Button confirmButton;
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);
        context = this;
        res = getResources();
        sp = new SharedPreference(context);

        //Declaracion de metodos
        validate = new Validate(context);

        //Declaracion de objetos
        db = new Database(context);
        messages = new MakeMessages();
        admin = new Admin();
        card = new CreditCard();

        blurView = findViewById(R.id.blurView);
        blurBackground();

        //Declaracion de elementos del layout
        cardTextView = findViewById(R.id.cardTextView);

        cardNumberInput = findViewById(R.id.cardNumberInput);
        expDateInput = findViewById(R.id.expDateInput);
        ccvInput = findViewById(R.id.ccvInput);
        nameInput = findViewById(R.id.nameInput);
        lastnameInput = findViewById(R.id.lastnameInput);
        paymentAmountInput = findViewById(R.id.paymentAmountInput);
        duesInput = findViewById(R.id.duesInput);

        confirmButton = findViewById(R.id.confirmButton);
        backArrow = findViewById(R.id.backArrow);

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
                        validate.expDate(expDateInput);
                    }, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });

        //Obtiene el tipo de tarjeta y actualzia el titulo del campo de texto acorde
        cardNumberInput.setOnKeyListener((view, i, keyEvent) -> {
            card.setNumber(cardNumberInput.getText().toString());

            if (!validate.isEmpty(cardNumberInput)) {
                cardTextView.setText(card.getType(context));
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

            if (cardNumberValidate && expDateValidate && ccvValidate && nameValidate
                    && lastnameValidate && paymentValidate && duesValidate){
                admin.setBalance(Integer.parseInt(card.getPaymentAmmount()));

                new Dialogs.ConfirmUpdateAdmin(context, admin,
                        messages.cardPayment(context, card), "card",
                        cardNumberInput.getText().toString(), sp.getActiveUser())
                        .show(getSupportFragmentManager(),"Confirm");
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