package com.example.wpossbank.Modelos;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.wpossbank.database.Database;

import java.util.Calendar;

public class Validate {
    Database db;

    public Validate(Context context) {
        db = new Database(context);
    }

    public boolean isEmpty(@NonNull EditText editText){
        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError("Este campo no puede quedar vacio.");
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
            nameInput.setError("El nombre solo puede contener letras mayusculas.");
            return false;
        }
    }

    public boolean cc(@NonNull EditText ccInput) {
        String cc = ccInput.getText().toString();
        if (isEmpty(ccInput)) {
            return false;
        } else if (db.fetchData(cc, db.getTableUser(), db.getColumnCc()).getCount() > 0){
            ccInput.setError("El documento de identidad ya esta registrada.");
            return false;
        } else if (cc.matches("(.*\\s.*)")) {
            ccInput.setError("El documento de identidad no puede contener espacios.");
            return false;
        } else if (cc.matches("^[0-9]+$")) {
            return true;
        }else{
            ccInput.setError("El documento de identidad solo puede contener numeros.");
            return false;
        }
    }

    public boolean pin(@NonNull EditText pinInput) {
        String pin = pinInput.getText().toString();
        if (isEmpty(pinInput)) {
            return false;
        } else if (pin.length() < 8){
            pinInput.setError("El PIN debe contener almenos ocho caracteres.");
            return false;
        } else if (pin.matches("(.*\\s.*)")) {
            pinInput.setError("El PIN no puede contener espacios.");
            return false;
        } else if (pin.matches("^[0-9]+$")) {
            return true;
        }else{
            pinInput.setError("El PIN solo puede contener numeros.");
            return false;
        }
    }

    public boolean balance(@NonNull EditText balanceInput) {
        String balance = balanceInput.getText().toString();
        if (isEmpty(balanceInput)) {
            return false;
        } else if (balance.matches("(.*\\s.*)")) {
            balanceInput.setError("El saldo no puede contener espacios.");
            return false;
        } else if (balance.matches("^[0-9]+$")) {
            return true;
        }else{
            balanceInput.setError("El saldo solo puede contener numeros.");
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
            cardNumberInput.setError("El número de la tarjeta debe ser de quince a dieciséis digitos.");
            return false;
        }else if(cardNumber.charAt(0) == '3' || cardNumber.charAt(0) == '4'||
                cardNumber.charAt(0) == '5' || cardNumber.charAt(0) == '6'){
            return true;
        }else{
            cardNumberInput.setError("Número de tarjeta invalido.");
            return false;
        }
    }

    public boolean expDate(@NonNull EditText expDateInput){
        Calendar currentDate = Calendar.getInstance();
        Calendar expDate = Calendar.getInstance();

        int year = Integer.parseInt(expDateInput.getText().toString().split("/")[0]),
                month = Integer.parseInt(expDateInput.getText().toString().split("/")[1]),
                day = Integer.parseInt(expDateInput.getText().toString().split("/")[2]);
        expDate.set(year, month, day);

        if (isEmpty(expDateInput)){
            return false;
        }else if (expDate.after(currentDate)){
            return true;
        }else{
            expDateInput.setError("Fecha de expiració invalida.");
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
            ccvInput.setError("CCV invalido.");
            return false;
        }
    }

    public boolean login(@NonNull EditText ccInput, @NonNull EditText pinInput) {
        String cc = ccInput.getText().toString();
        String pin = pinInput.getText().toString();

        if (isEmpty(ccInput) || isEmpty(pinInput)) {
            return false;
        }else{
            Cursor fetch = db.fetchData(cc, db.getTableUser(), db.getColumnCc());
            String registeredPin = "empty";

            while (fetch.moveToNext()) {
                registeredPin = fetch.getString(2);// TABLE_USERS - COLUMN_PIN
            }

            if (fetch.getCount() > 0) {
                if (pin.equals(registeredPin)) {
                    return true;
                } else {
                    pinInput.setError("PIN incorrecto.");
                    return false;
                }
            } else {
                ccInput.setError("El numero de cedula no esta registrado.");
                return false;
            }
        }
    }
}