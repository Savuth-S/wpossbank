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

    /* Devuelve el array de numeros separados por el caracter especificados cada N cantidad de numeros
       comenzando de derecha a izquierda */
    public String separateNumberRight(@NonNull String number, String character, int everyNCharacters){
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

    /* Devuelve el array de numeros separados por el caracter especificados cada N cantidad de numeros
       comenzando de izquierda a derecha */
    public String separateNumberLeft(@NonNull String number, String character, int everyNCharacters){

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

    // devuelve una sola string con el mensaje para el dialogo de pago con tarjeta
    public String cardPayment(@NonNull Context context, @NonNull CreditCard card) {
        String message;
        /* corta el resource string en pedazos  usnado el caracter / como referencia,
           para luego llenar los campos correspondientes */
        String[] template = context.getString(R.string.dialog_confirm_cardpayment).split("/");

        //Reemplaza cada numero excepto los cuatro ultimos por *
        int cardLength = card.getNumber().length();
        String cardEnds = "";

        for (int i = 0; i < cardLength; i++) {
            if (cardLength % 4 == 0) {
                //Agrega un espacio cada cuatro caracteres
                if (i % 4 == 0) {
                    cardEnds = cardEnds.concat(" ");
                }

                //reemplaza los numeros anteriores a los cuatro ultimos con *
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

                //reemplaza los numeros anteriores a los tres ultimos con *
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
                System.getProperty("line.separator") + cardEnds + " - " + card.getType(context);

        return message;
    }

    // devuelve una sola string con el mensaje para el dialogo de retirar dinero
    public String withdraw(@NonNull Context context, @NonNull EditText withdrawInput) {
        String message;
        /* corta el resource string en pedazos  usnado el caracter / como referencia,
           para luego llenar los campos correspondientes */
        String[] template = context.getString(R.string.dialog_confirm_withdrawal).split("/");

        User user = new User();
        user.loadData(context);

        //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
        message = template[0] + user.getName() + System.getProperty("line.separator") +
                template[1] + separateNumberRight(withdrawInput.getText().toString(),".",3)
                + template[2];

        return message;
    }

    // devuelve una sola string con el mensaje para el dialogo de añadir fondos al corresponsal
    public String adminDeposit(@NonNull Context context, @NonNull EditText depositInput) {
        String message;
        /* corta el resource string en pedazos  usnado el caracter / como referencia,
           para luego llenar los campos correspondientes */
        String[] template = context.getString(R.string.dialog_confirm_admin_deposit).split("/");

        //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
        message = template[0] + System.getProperty("line.separator") +
                template[1] + separateNumberRight(depositInput.getText().toString(),".",3)
                + template[2];
        return message;
    }

    // devuelve una sola string con el mensaje para el dialogo de depositar dinero en la cuenta
    public String deposit(@NonNull Context context, @NonNull EditText depositInput) {
        String message;
        /* corta el resource string en pedazos  usnado el caracter / como referencia,
           para luego llenar los campos correspondientes */
        String[] template = context.getString(R.string.dialog_confirm_deposit).split("/");

        User user = new User();
        user.loadData(context);

        //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
        message = template[0] + System.getProperty("line.separator") +
                template[1] + separateNumberRight(depositInput.getText().toString(),".",3)
                + template[2] + user.getName() + template[3];
        return message;
    }

    // devuelve una sola string con el mensaje para el dialogo de transferir dinero
    public String transfer(@NonNull Context context, EditText transferInput, @NonNull EditText ccTransferInput) {
        String message;
        /* corta el resource string en pedazos  usnado el caracter / como referencia,
           para luego llenar los campos correspondientes */
        String[] template = context.getString(R.string.dialog_confirm_transfer).split("/");

        // carga la informacion del usuario que reliza la transferencia
        User user = new User();
        user.loadData(context);

        // obtiene la informaci�n del usuario que recibe la transferencia
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
            return context.getString(R.string.error_fetch_data);
        }
    }

    // devuelve una sola string con el mensaje para el dialogo de ver el saldo de la cuenta
    public String getBalance(@NonNull Context context) {
        String message;
        /* corta el resource string en pedazos  usnado el caracter / como referencia,
           para luego llenar los campos correspondientes */
        String[] template = context.getString(R.string.dialog_confirm_get_balance).split("/");

        User user = new User();
        user.loadData(context);

        //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
        message = template[0] + user.getName() + System.getProperty("line.separator") +
                    template[1]  + System.getProperty("line.separator") + template[2];

        return message;
    }

}
