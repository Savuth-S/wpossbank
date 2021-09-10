package com.example.wpossbank.modelos;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.wpossbank.R;
import com.example.wpossbank.database.Database;

import java.util.Calendar;

public class Validate {
    Context context;
    Database db;
    Resources res;
    SharedPreference sp;

    public Validate(Context context) {
        this.context = context;
        db = new Database(context);
        res = context.getResources();
        sp = new SharedPreference(context);
    }

    public boolean isEmpty(@NonNull EditText editText){
        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError(res.getString(R.string.error_empty));
            return true;
        }else{
            return false;
        }
    }

    public boolean name(@NonNull EditText nameInput){
        String name = nameInput.getText().toString();

        if (isEmpty(nameInput)){
            return false;
        }else if (name.matches("^[A-ZÁ-Ú\\s]+$")){
            return true;
        }else{
            nameInput.setError(res.getString(R.string.error_only_caps));
            return false;
        }
    }

    public boolean cc(@NonNull EditText ccInput) {
        String cc = ccInput.getText().toString();
        if (isEmpty(ccInput)) {
            return false;
        } else if (db.fetchData(cc, db.getTable("user"), db.getColumn("cc")).getCount() > 0){
            ccInput.setError(res.getString(R.string.error_already_registered));
            return false;
        } else if (cc.matches("(.*\\s.*)")) {
            ccInput.setError(res.getString(R.string.error_spaces));
            return false;
        } else if (cc.matches("^[0-9]+$")) {
            return true;
        }else{
            ccInput.setError(res.getString(R.string.error_already_registered));
            return false;
        }
    }

    public boolean pin(@NonNull EditText pinInput) {
        String pin = pinInput.getText().toString();
        if (isEmpty(pinInput)) {
            return false;
        } else if (pin.matches("(.{4})")){
            pinInput.setError(res.getString(R.string.error_atleast_8));
            return false;
        } else if (pin.matches("(.*\\s.*)")) {
            pinInput.setError(res.getString(R.string.error_spaces));
            return false;
        } else if (pin.matches("^[0-9]+$")) {
            return true;
        }else{
            pinInput.setError(res.getString(R.string.error_only_numbers));
            return false;
        }
    }

    public boolean initialBalance(@NonNull EditText balanceInput) {
        String balance = balanceInput.getText().toString();
        if (isEmpty(balanceInput)) {
            return false;
        } else if (balance.matches("(.*\\s.*)")) {
            balanceInput.setError(res.getString(R.string.error_spaces));
            return false;
        } else if (balance.matches("^[0-9]+$") && Integer.parseInt(balance) >= 10_000) {
            return true;
        }else{
            balanceInput.setError(res.getString(R.string.error_invalid));
            return false;
        }
    }

    /*
    public boolean email(EditText emailInput){
        String email = emailInput.getText().toString();

        if (isEmpty(emailInput)){
            return false;
        }else if (db.fetchData(email, db.getTableUser(), db.getColumnEmail()).getCount() > 0) {
            emailInput.setError("La direccion de correo ya esta registrada.");
            return false;
        }else if (email.matches("(.*\\s.*)")){
            emailInput.setError("La direccion de correo no puede contener espacios.");
            return false;
        }else if(email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            return true;
        }else {
            emailInput.setError("Direccion de correo electronica invalida.");
            return false;
        }
    }

    public boolean password(EditText passwordInput){
        String password = passwordInput.getText().toString();

        if (isEmpty(passwordInput)){
            return false;
        }else if (password.length() < 8){
            passwordInput.setError("La contraseña debe contener almenos ocho caracteres.");
            return false;
        }else if (password.matches("(.*\\s.*)")){
            passwordInput.setError("La contraseña no puede contener espacios.");
            return false;
        }else if (!password.matches("(.*[A-Z].*)")){
            passwordInput.setError("La contraseña debe contener al menos una letra mayuscula.");
            return false;
        }else if (!password.matches("(.*[a-z].*)")){
            passwordInput.setError("La contraseña debe contener al menos una letra minuscula.");
            return false;
        }else if (!password.matches("(.*[0-9].*)")){
            passwordInput.setError("La contraseña debe contener al menos un numero.");
            return false;
        }else if (!password.matches("(.*[@#$%^&+*=!()].*)")){
            passwordInput.setError("La contraseña debe contener al menos un caracter especial.");
            return false;
        }else{
            return true;
        }
    }*/

    public boolean cardNumber(@NonNull EditText cardNumberInput){
        String cardNumber = cardNumberInput.getText().toString();

        if (isEmpty(cardNumberInput)){
            return false;
        }else if (!cardNumber.matches("(.{15,16})")){
            cardNumberInput.setError(res.getString(R.string.error_between_15and16));
            return false;
        }else if(cardNumber.charAt(0) == '3' || cardNumber.charAt(0) == '4'||
                cardNumber.charAt(0) == '5' || cardNumber.charAt(0) == '6'){
            return true;
        }else{
            cardNumberInput.setError(res.getString(R.string.error_invalid));
            return false;
        }
    }

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
                expDateInput.setError(res.getString(R.string.error_invalid));
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean ccv(@NonNull EditText ccvInput){
        String ccv = ccvInput.getText().toString();
        if (isEmpty(ccvInput)){
            return false;
        }else if (ccv.matches("(.{4})")){
            return true;
        }else{
            ccvInput.setError(res.getString(R.string.error_invalid));
            return false;
        }
    }

    public boolean payment(@NonNull EditText paymentInput){
        if (!isEmpty(paymentInput)) {
            int payment = Integer.parseInt(paymentInput.getText().toString());
            if (payment > 10000 && payment < 1000000) {
                return true;
            } else {
                paymentInput.setError(res.getString(R.string.error_invalid));
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean dues(@NonNull EditText duesInput){
        if(!isEmpty(duesInput)) {
            int dues = Integer.parseInt(duesInput.getText().toString());

            if (dues > 0 && dues < 13) {
                return true;
            } else {
                duesInput.setError(res.getString(R.string.error_invalid));
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean withdrawal(@NonNull EditText withdrawalInput){
        if(!isEmpty(withdrawalInput)) {
            try (Cursor fetch = db.fetchData(sp.getActiveUser(),
                    db.getTable("user"),
                    db.getColumn("user id"))) {
                fetch.moveToNext();

                if (fetch.getCount() > 0) {
                    if (withdrawalInput.getText().toString().matches("(.{2,7})")){
                        int withdrawal = Integer.parseInt(withdrawalInput.getText().toString());

                        if (withdrawal + 2_000 <= Integer.parseInt(fetch.getString(4))) {
                            return true;
                        } else {
                            withdrawalInput.setError(res.getString(R.string.error_enough_funds));
                            return false;
                        }
                    }else {
                        withdrawalInput.setError(res.getString(R.string.error_between_2and7));
                        return false;
                    }
                } else {
                    Log.e("VALIDATE", "Failed to load cursor data from database, " +
                            "database=" + db.toString() + "fetch=" + fetch.toString());
                    withdrawalInput.setError(res.getString(R.string.error_fetch_data));
                    return false;
                }
            }
        }else{ return false;}
    }

    public boolean login(@NonNull EditText ccInput, @NonNull EditText pinInput) {
        String cc = ccInput.getText().toString();
        String pin = pinInput.getText().toString();

        if (isEmpty(ccInput) || isEmpty(pinInput)) {
            return false;
        }else{
            Cursor fetch = db.fetchData(cc, db.getTable("user"), db.getColumn("cc"));
            String registeredPin = "empty";

            while (fetch.moveToNext()) {
                registeredPin = fetch.getString(3);// TABLE_USERS - COLUMN_PIN
            }

            if (fetch.getCount() > 0) {
                if (pin.equals(registeredPin)) {
                    return true;
                } else {
                    pinInput.setError(res.getString(R.string.error_wrong));
                    return false;
                }
            } else {
                ccInput.setError(res.getString(R.string.error_not_registered));
                return false;
            }
        }
    }

    public boolean matchUserData(@NonNull EditText textInput, String parameter){
        if(!isEmpty(textInput)){
            String text = textInput.getText().toString();
            User user = new User(context);
            user.loadData(user);

            switch (parameter){
                case "pin":
                    if (text.equals(user.getPin())){
                        return true;
                    }else{
                        textInput.setError(res.getString(R.string.error_wrong));
                        return false;
                    }
                case "cc":
                    if (text.equals(user.getCc())){
                        return true;
                    }else{
                        textInput.setError(res.getString(R.string.error_wrong));
                        return false;
                    }
                case "balance":
                    if (text.equals(String.valueOf(user.getBalance()))){
                        return true;
                    }else{
                        textInput.setError(res.getString(R.string.error_wrong));
                        return false;
                    }
                case "name":
                    if (text.equals(user.getName())){
                        return true;
                    }else{
                        textInput.setError(res.getString(R.string.error_wrong));
                        return false;
                    }
                default:
                    textInput.setError(res.getString(R.string.error_wrong));
                    return false;
                }
        }else{
            return false;
        }
    }
}