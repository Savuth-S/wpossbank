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

    public MakeMessages(@NonNull Context context){
        res = context.getResources();
    }

    public String separateNumberRight(String number, String character, int everyNCharacters){

        if (number.length() > 0) {
            String newNumber = "";

            for (int i = 1; i < number.length()+1; i++){
                newNumber = newNumber.concat(Character.toString(number.charAt(i-1)));

                if ((number.length()-i) % everyNCharacters == 0 && number.length()-i != 0){
                    newNumber = newNumber.concat(character);
                }
            }

            return newNumber;
        }else{
            return "";
        }
    }

    public String separateNumberLeft(String number, String character, int everyNCharacters){

        if (number.length() > 0) {
            String newNumber = "";

            for (int i = 1; i < number.length()+1; i++){
                newNumber = newNumber.concat(Character.toString(number.charAt(i-1)));

                if (i % everyNCharacters == 0){
                    newNumber = newNumber.concat(character);
                }
            }

            return newNumber;
        }else{
            return "";
        }
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
                template[1] + separateNumberRight(card.getPaymentAmmount(),".", 3)
                + template[2] + card.getDuesNumber() + template[3] + System.getProperty("line.separator") +
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
                template[1] + separateNumberRight(withdrawInput.getText().toString(),".",3)
                + template[2];

        return message;
    }

    public String deposit(Context context, @NonNull EditText depositInput) {
        String message;
        String[] template = res.getString(R.string.dialog_confirm_deposit).split("/");

        User user = new User(context);
        user.loadData(user);

        //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
        message = template[0] + System.getProperty("line.separator") +
                template[1] + separateNumberRight(depositInput.getText().toString(),".",3)
                + template[2] + user.getName() + template[3];
        return message;
    }

    public String transfer(Context context, EditText transferInput, @NonNull EditText ccTransferInput) {
        String message;
        String[] template = res.getString(R.string.dialog_confirm_transfer).split("/");

        User user = new User(context);
        user.loadData(user);

        Database db = new Database(context);
        Cursor fetch = db.fetchData(ccTransferInput.getText().toString(),
                db.getTable("user"),
                db.getColumn("cc"));

        if (fetch.getCount() > 0) {
            fetch.moveToFirst();

            //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
            message = template[0] + user.getName() + System.getProperty("line.separator") +
                    template[1] + separateNumberRight(transferInput.getText().toString(), ".", 3)
                    + template[2] + fetch.getString(5) + template[3];

            return message;
        }else{
            return res.getString(R.string.error_fetch_data);
        }
    }

    public String getBalance(Context context) {
        String message;
        String[] template = res.getString(R.string.dialog_confirm_get_balance).split("/");

        User user = new User(context);
        user.loadData(user);

        //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
        message = template[0] + user.getName() + System.getProperty("line.separator") +
                    template[1];

        return message;

    }

}
