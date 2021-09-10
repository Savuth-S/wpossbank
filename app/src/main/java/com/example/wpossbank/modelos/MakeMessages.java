package com.example.wpossbank.modelos;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wpossbank.R;
import com.example.wpossbank.database.Database;

public class MakeMessages extends AppCompatActivity {
    Resources res;

    public MakeMessages(Context context){
        res = context.getResources();
    }

    public String cardPayment(@NonNull CreditCard card) {
        String message;
        String[] template = res.getString(R.string.dialog_confirm_cardpayment).split("/");

        //Reemplaza cada numero excepto los cuatro ultimos por *
        int cardLength = card.getNumber().length();
        String cardEnds = "";

        for (int i = 0; i < cardLength; i++) {
            if (cardLength % 4 == 0) {
                //Agrega un espacio cada cuatro caracteres
                if (i % 4 == 0) {
                    cardEnds = cardEnds.concat(" ");
                }

                if (i < cardLength - 4) {
                    cardEnds = cardEnds.concat("*");
                } else {
                    cardEnds = cardEnds.concat(Character.toString(card.getNumber().charAt(i)));
                }
            } else {
                //Agrega un espacio cada tres caracteres
                if (i % 3 == 0) {
                    cardEnds = cardEnds.concat(" ");
                }

                if (i < cardLength - 3) {
                    cardEnds = cardEnds.concat("*");
                } else {
                    cardEnds = cardEnds.concat(Character.toString(card.getNumber().charAt(i)));
                }
            }


        }

        //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
        message = template[0] + card.getOwnerName() + System.getProperty("line.separator") +
                template[1] + card.getPaymentAmmount() + template[2] +
                card.getDuesNumber() + template[3] + System.getProperty("line.separator") +
                System.getProperty("line.separator") + template[4] +
                System.getProperty("line.separator") + cardEnds + " - " + card.getType();

        return message;
    }

    public String withdraw(Context context, EditText withdrawInput) {
        String message;
        String[] template = res.getString(R.string.dialog_confirm_deposit).split("/");

        User user = new User(context);
        user.loadData(user);

        //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
        message = template[0] + user.getName() + System.getProperty("line.separator") +
                template[1] + withdrawInput.getText() + template[2];

        return message;
    }

    public String deposit(Context context, EditText depositInput, EditText ccInput) {
        String message;
        String[] template = res.getString(R.string.dialog_confirm_withdrawal).split("/");

        User user = new User(context);
        user.loadData(user);

        Database db = new Database(context);
        Cursor fetch = db.fetchData(ccInput.getText().toString(),
                db.getTable("user"),
                db.getColumn("cc"));

        fetch.moveToFirst();

        //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
        message = template[0] + user.getName() + System.getProperty("line.separator") +
                template[1] + depositInput.getText() + template[2] + fetch.getString(5) +
                template[3];

        return message;
    }

    public String transfer(Context context, EditText transferInput, EditText ccTransferInput) {
        String message;
        String[] template = res.getString(R.string.dialog_confirm_withdrawal).split("/");

        User user = new User(context);
        user.loadData(user);

        Database db = new Database(context);
        Cursor fetch = db.fetchData(ccTransferInput.getText().toString(),
                db.getTable("user"),
                db.getColumn("cc"));

        fetch.moveToFirst();

        //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
        message = template[0] + user.getName() + System.getProperty("line.separator") +
                template[1] + transferInput.getText() + template[2] + fetch.getString(5) +
                template[3];

        return message;
    }
}
