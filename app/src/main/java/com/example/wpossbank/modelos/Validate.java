package com.example.wpossbank.modelos;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.wpossbank.R;
import com.example.wpossbank.database.Database;

import java.util.Calendar;

public class Validate {
    Context context;
    Database db;
    SharedPreference sp;

    public Validate(Context context) {
        this.context = context;
        db = new Database(context);
        sp = new SharedPreference(context);
    }

    // valida si un input esta vacio y devuelve verdadero si lo es
    public boolean isEmpty(@NonNull EditText editText){
        if (editText.getText().toString().isEmpty()) {
            editText.setError(context.getString(R.string.error_empty));
            return true;
        }else{
            return false;
        }
    }

    // valida si un input solo contiene numeros
    public boolean isNumber(@NonNull EditText editText){
        if (!isEmpty(editText)) {

            String text = editText.getText().toString();

            if (text.matches("^[0-9]+$")) {
                return true;
            } else {
                editText.setError(context.getString(R.string.error_only_numbers));
                return false;
            }
        }else{ return false;}
    }

    // valida si la string ingresada en un input es de un tamaño dentro del rango especificado
    public boolean isInRange(@NonNull EditText editText, int min, int max){
        if (!isEmpty(editText)) {
            String text = editText.getText().toString();
            String regex = "(.{" + min + "," + max + "})";
            if (text.matches(regex)) {
                return true;
            } else {
                editText.setError(context.getString(R.string.error_invalid));
                return false;
            }
        }else{ return false;}
    }

    // valida si la informacion ingresada en el input ya existe en la base de datos
    public boolean isInDatabase(@NonNull EditText editText, String table, String column){
        if (!isEmpty(editText)) {
            String text = editText.getText().toString();
            Cursor fetch = db.fetchData(text, db.getTable(table), db.getColumn(column));
            fetch.moveToFirst();

            if (fetch.getCount() > 0){
                return true;
            }else {
                editText.setError(context.getString(R.string.error_not_registered));
                return false;
            }
        }else{ return false;}
    }

    // valida si la informaci�n ingresada en el input no existe en la base de datos
    public boolean isNotInDatabase(@NonNull EditText editText, String table, String column){
        if (!isEmpty(editText)) {
            String text = editText.getText().toString();
            Cursor fetch = db.fetchData(text, db.getTable(table), db.getColumn(column));
            fetch.moveToFirst();

            if (fetch.getCount() > 0){
                editText.setError(context.getString(R.string.error_already_registered));
                return false;
            }else {
                return true;
            }
        }else{ return false;}
    }

    // valida si la informacion ingresada en el input es una direcci�n de correo valida
    public boolean email(@NonNull EditText emailInput){
        String email = emailInput.getText().toString();

        if (isEmpty(emailInput)){
            return false;
        }else if (email.matches("(.*\\s.*)")){
            emailInput.setError(context.getString(R.string.error_spaces));
            return false;
        }else if(email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            return true;
        }else {
            emailInput.setError(context.getString(R.string.error_invalid));
            return false;
        }
    }

    // valida si el input contiene una contraseña valida
    public boolean password(EditText passwordInput){
        int minPassLength = 8;

        if (!isEmpty(passwordInput)){
            String password = passwordInput.getText().toString();

            if (password.length() < minPassLength){
                passwordInput.setError(context.getString(R.string.error_atleast_8));
                return false;
            }else if (password.matches("(.*\\s.*)")){
                passwordInput.setError(context.getString(R.string.error_spaces));
                return false;
            }else if (!password.matches("(.*[A-Z].*)")){
                passwordInput.setError(context.getString(R.string.error_atleast_cap));
                return false;
            }else if (!password.matches("(.*[a-z].*)")){
                passwordInput.setError(context.getString(R.string.error_atleast_letter));
                return false;
            }else if (!password.matches("(.*[0-9].*)")){
                passwordInput.setError(context.getString(R.string.error_atleast_number));
                return false;
            }else if (!password.matches("(.*[@#$%^&+*=!()].*)")){
                passwordInput.setError(context.getString(R.string.error_atleast_special));
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    // valida si el string del input solo contiene letras mayusculas
    public boolean name(@NonNull EditText nameInput){
        if (!isEmpty(nameInput)) {

            String name = nameInput.getText().toString();
            if (name.matches("^[A-ZÁ-Ú\\s]+$")) {
                return true;
            } else {
                nameInput.setError(context.getString(R.string.error_only_caps));
                return false;
            }
        }else{ return false; }
    }

    // valida si el nuymero de cedula del input es valido
    public boolean cc(@NonNull EditText ccInput) {
        String cc = ccInput.getText().toString();
        if (isEmpty(ccInput)) {
            return false;
        }else if (!isInRange(ccInput, 10,13)){
            return  false;
        }else if (isInDatabase(ccInput,"user","cc")){
            return false;
        } else if (cc.matches("(.*\\s.*)")) {
            ccInput.setError(context.getString(R.string.error_spaces));
            return false;
        } else if (cc.matches("^[0-9]+$")) {
            return true;
        }else{
            ccInput.setError(context.getString(R.string.error_already_registered));
            return false;
        }
    }

    // valida si el pin ingresado en el input es valido
    public boolean pin(@NonNull EditText pinInput) {
        String pin = pinInput.getText().toString();
        if (isEmpty(pinInput)) {
            return false;
        } else if (!pin.matches("(.{4})")){
            pinInput.setError(context.getString(R.string.error_mustbe_4));
            return false;
        } else if (pin.matches("(.*\\s.*)")) {
            pinInput.setError(context.getString(R.string.error_spaces));
            return false;
        } else if (pin.matches("^[0-9]+$")) {
            return true;
        }else{
            pinInput.setError(context.getString(R.string.error_only_numbers));
            return false;
        }
    }

    // valida si el saldo inicial del input es valido
    public boolean initialBalance(@NonNull EditText balanceInput) {
        String balance = balanceInput.getText().toString();
        if (isEmpty(balanceInput)) {
            return false;
        } else if (balance.matches("(.*\\s.*)")) {
            balanceInput.setError(context.getString(R.string.error_spaces));
            return false;
        } else if (balance.matches("^[0-9]+$") && Integer.parseInt(balance) >= 10_000) {
            return true;
        }else{
            balanceInput.setError(context.getString(R.string.error_invalid));
            return false;
        }
    }

    // valida si el n�mero de tarjeta ingresado en el input es valido
    public boolean cardNumber(@NonNull EditText cardNumberInput){
        String cardNumber = cardNumberInput.getText().toString();

        if (isEmpty(cardNumberInput)){
            return false;
        }else if (!cardNumber.matches("(.{15,16})")){
            cardNumberInput.setError(context.getString(R.string.error_between_15and16));
            return false;
        }else if(cardNumber.charAt(0) == '3' || cardNumber.charAt(0) == '4'||
                cardNumber.charAt(0) == '5' || cardNumber.charAt(0) == '6'){
            return true;
        }else{
            cardNumberInput.setError(context.getString(R.string.error_invalid));
            return false;
        }
    }

    // valida si la fecha de expiracion de la tarjeta es mayor al actual
    public boolean expDate(@NonNull EditText expDateInput){
        if (!isEmpty(expDateInput)) {
            Calendar currentDate = Calendar.getInstance();
            Calendar expDate = Calendar.getInstance();

            int year = Integer.parseInt(expDateInput.getText().toString().split("/")[0]),
                    month = Integer.parseInt(expDateInput.getText().toString().split("/")[1]),
                    day = Integer.parseInt(expDateInput.getText().toString().split("/")[2]);
            expDate.set(year, month, day);

            if (expDate.after(currentDate)){
                expDateInput.setError(null);
                return true;
            }else{
                expDateInput.setError(context.getString(R.string.error_invalid));
                return false;
            }
        }else{
            return false;
        }
    }

    // valida si el ccv de la tarjeta es de cuatro caracteres
    public boolean ccv(@NonNull EditText ccvInput){
        String ccv = ccvInput.getText().toString();
        if (isEmpty(ccvInput)){
            return false;
        }else if (ccv.matches("(.{4})")){
            return true;
        }else{
            ccvInput.setError(context.getString(R.string.error_invalid));
            return false;
        }
    }

    // valuda si el pago a realizar es valido
    public boolean payment(@NonNull EditText paymentInput){
        if (!isEmpty(paymentInput) && isInRange(paymentInput,3,7) && isNumber(paymentInput)) {
            int payment = Integer.parseInt(paymentInput.getText().toString());
            if (payment >= 10000 && payment <= 1000000) {
                return true;
            } else {
                paymentInput.setError(context.getString(R.string.error_invalid));
                return false;
            }
        }else{
            return false;
        }
    }

    // valida si la cantidad de cuotas esta dentro del rango valido
    public boolean dues(@NonNull EditText duesInput){
        if(!isEmpty(duesInput)) {
            if (isInRange(duesInput, 1, 12)) {
                return true;
            } else {
                duesInput.setError(context.getString(R.string.error_invalid));
                return false;
            }
        }else{
            return false;
        }
    }

    // verifica los credenciales de logeo para el panel de administrador del corresponsal
    public boolean adminLogin(@NonNull EditText emailInput, @NonNull EditText passwordInput) {
        boolean passwordEmpty = isEmpty(passwordInput),
                emailEmpty = isEmpty(emailInput);

        if (passwordEmpty ||  emailEmpty){
            return false;
        }

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        Cursor fetch = db.fetchData(email, db.getTable("admin"), db.getColumn("email"));
        String registeredPassword = "empty";
        while (fetch.moveToNext()) {
            registeredPassword = fetch.getString(3);// TABLE_ADMINS - COLUMN_PASSWORD
        }
        if (fetch.getCount() > 0) {
            if (password.equals(registeredPassword)) {
                return true;
            } else {
                passwordInput.setError(context.getString(R.string.error_wrong));
                return false;
            }
        } else {
            emailInput.setError(context.getString(R.string.error_not_registered));
            return false;
        }
    }

    // verifica credenciales de logeo para el usuario
    public boolean login(@NonNull EditText ccInput, @NonNull EditText pinInput) {
        boolean ccEmpty = isEmpty(ccInput),
                pinEmpty = isEmpty(pinInput);

        if (ccEmpty || pinEmpty) {
            return false;
        }

        String cc = ccInput.getText().toString();
        String pin = pinInput.getText().toString();

        Cursor fetch = db.fetchData(cc, db.getTable("user"), db.getColumn("cc"));
        String registeredPin = "empty";
        while (fetch.moveToNext()) {
            registeredPin = fetch.getString(3);// TABLE_USERS - COLUMN_PIN
        }
        if (fetch.getCount() > 0) {
            if (pin.equals(registeredPin)) {
                return true;
            } else {
                pinInput.setError(context.getString(R.string.error_wrong));
                return false;
            }
        } else {
            ccInput.setError(context.getString(R.string.error_not_registered));
            return false;
        }
    }

    // verifica si hay suficientes fondos en la cuenta para relizar la transacci�n
    public boolean useBalance(@NonNull EditText moneyInput){
        return useBalance(moneyInput, Integer.parseInt(moneyInput.getText().toString()));
    }

    public boolean useBalance(@NonNull EditText moneyInput, int withdrawal){
        if(!isEmpty(moneyInput)) {
            try (Cursor fetch = db.fetchData(sp.getActiveUser(),
                    db.getTable("user"),
                    db.getColumn("object id"))) {
                fetch.moveToNext();

                if (fetch.getCount() > 0) {
                    if (withdrawal >= 0 && withdrawal <= 9_999_999){

                        if (withdrawal + 2_000 <= Integer.parseInt(fetch.getString(4))) {
                            return true;
                        } else {
                            moneyInput.setError(context.getString(R.string.error_enough_funds));
                            return false;
                        }
                    }else {
                        moneyInput.setError(context.getString(R.string.error_between_2and7));
                        return false;
                    }
                } else {
                    Log.e("VALIDATE", "Failed to load cursor data from database, " +
                            "database=" + db.toString() + "fetch=" + fetch.toString());
                    moneyInput.setError(context.getString(R.string.error_fetch_data));
                    return false;
                }
            }
        }else{ return false; }
    }

    // verifica si la informaci�n ingresada concuerda con la informaci�n del usuario actual
    public boolean matchUserData(@NonNull EditText textInput, String parameter){
        if(!isEmpty(textInput)){
            String text = textInput.getText().toString();
            User user = new User();
            user.loadData(context);

            // tabla para verificar cada tipo individual de informaci�n de usuario
            switch (parameter){
                case "pin":
                    if (text.equals(user.getPin())){
                        return true;
                    }else{
                        textInput.setError(context.getString(R.string.error_wrong));
                        return false;
                    }
                case "cc":
                    if (text.equals(user.getCc())){
                        return true;
                    }else{
                        textInput.setError(context.getString(R.string.error_wrong));
                        return false;
                    }
                case "balance":
                    if (text.equals(String.valueOf(user.getBalance()))){
                        return true;
                    }else{
                        textInput.setError(context.getString(R.string.error_wrong));
                        return false;
                    }
                case "name":
                    if (text.equals(user.getName())){
                        return true;
                    }else{
                        textInput.setError(context.getString(R.string.error_wrong));
                        return false;
                    }
                default:
                    textInput.setError(context.getString(R.string.error_wrong));
                    return false;
                }
        }else{ return false; }
    }

    // verifica si la informaci�n ingresada no es igual a la informaci�n del usuario actual
    public boolean notMatchUserData(@NonNull EditText textInput, String parameter){
        if(!isEmpty(textInput)){
            if (matchUserData(textInput, parameter)){
                textInput.setError(context.getString(R.string.error_wrong));
                return false;
            }else{
                textInput.setError(null);
                return  true;
            }

        }else{ return false; }
    }
}