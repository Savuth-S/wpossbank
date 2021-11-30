package com.example.wpossbank.models

import android.content.Context
import android.util.Log
import android.widget.EditText
import com.example.wpossbank.R
import com.example.wpossbank.database.Database
import java.util.*

class Validate(var context: Context)
{
    var db: Database = Database(context)
    var sp: SharedPreference = SharedPreference(context)

    //Regex matches
    private val onlyNumbers = Regex("^[0-9]+$")
    private val hasSpaces = Regex("(.*\\s.*)")

    // valida si un input esta vacio y devuelve verdadero si lo es
    fun isEmpty(editText: EditText): Boolean {
        return if (editText.text.toString().isEmpty())
        {
            editText.error = context.getString(R.string.error_empty)
            true
        } else {
            false
        }
    }

    // valida si un input solo contiene numeros
    fun isNumber(editText: EditText): Boolean
    {
        return if (!isEmpty(editText)) {
            val text = editText.text.toString()

            if (text.matches(onlyNumbers)) {
                true
            } else {
                editText.error = context.getString(R.string.error_only_numbers)
                false
            }
        } else {
            false
        }
    }

    // valida si la string ingresada en un input es de un tamaño dentro del rango especificado
    fun isInRange(editText: EditText, min: Int, max: Int): Boolean
    {
        return if (!isEmpty(editText)) {
            val text = editText.text.toString()
            val sizeRange = Regex("(.{$min,$max})")

            if (text.matches(sizeRange)) {
                true
            } else {
                editText.error = context.getString(R.string.error_invalid)
                false
            }
        } else {
            false
        }
    }

    // valida si la informacion ingresada en el input ya existe en la base de datos
    fun isInDatabase(editText: EditText, table: String?, column: String?): Boolean
    {
        return if (!isEmpty(editText)) {
            val text = editText.text.toString()
            val fetch = db.fetchData(text, db.getTable(table), db.getColumn(column))

            fetch.moveToFirst()
            if (fetch.count > 0) {
                true
            } else {
                editText.error = context.getString(R.string.error_not_registered)
                false
            }
        } else {
            false
        }
    }

    // valida si la informaci�n ingresada en el input no existe en la base de datos
    fun isNotInDatabase(editText: EditText, table: String?, column: String?): Boolean
    {
        return if (!isEmpty(editText)) {
            val text = editText.text.toString()
            val fetch = db.fetchData(text, db.getTable(table), db.getColumn(column))

            fetch.moveToFirst()
            if (fetch.count > 0) {
                editText.error = context.getString(R.string.error_already_registered)
                false
            } else {
                true
            }
        } else {
            false
        }
    }

    // valida si la informacion ingresada en el input es una direcci�n de correo valida
    fun email(emailInput: EditText): Boolean
    {
        val email = emailInput.text.toString()

        return when {isEmpty(emailInput) -> { false
            } email.matches(hasSpaces) -> {
                emailInput.error = context.getString(R.string.error_spaces)
                false
            } email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) -> {
                true
            } else -> {
                emailInput.error = context.getString(R.string.error_invalid)
                false
            }
        }
    }

    // valida si el input contiene una contraseña valida
    fun password(passwordInput: EditText): Boolean
    {
        val minPassLength = 8
        val password = passwordInput.text.toString()

        return when {!isEmpty(passwordInput) -> {
                false
            } password.length < minPassLength -> {
                passwordInput.error = context.getString(R.string.error_atleast_8)
                false
            } password.matches(hasSpaces) -> {
                passwordInput.error = context.getString(R.string.error_spaces)
                false
            } !password.matches(Regex("(.*[A-Z].*)")) -> {
                passwordInput.error = context.getString(R.string.error_atleast_cap)
                false
            } !password.matches(Regex("(.*[a-z].*)")) -> {
                passwordInput.error = context.getString(R.string.error_atleast_letter)
                false
            } !password.matches(Regex("(.*[0-9].*)")) -> {
                passwordInput.error = context.getString(R.string.error_atleast_number)
                false
            } !password.matches(Regex("(.*[@#$%^&+*=!()].*)")) -> {
                passwordInput.error = context.getString(R.string.error_atleast_special)
                false
            } else  -> {
                true
            }
        }
    }

    // valida si el string del input solo contiene letras mayusculas
    fun name(nameInput: EditText): Boolean
    {
        return if (!isEmpty(nameInput)) {
            val name = nameInput.text.toString()
            val onlyCaps = Regex("^[A-ZÁ-Ú\\s]+$")

            if (name.matches(onlyCaps)) {
                true
            } else {
                nameInput.error = context.getString(R.string.error_only_caps)
                false
            }
        } else {
            false
        }
    }

    // valida si el nuymero de cedula del input es valido
    fun cc(ccInput: EditText): Boolean
    {
        val cc = ccInput.text.toString()

        return when { isEmpty(ccInput) -> {
                false
            } !isInRange(ccInput, 10, 13) -> {
                false
            } isInDatabase(ccInput, "user", "cc") -> {
                false
            } cc.matches(hasSpaces) -> {
                ccInput.error = context.getString(R.string.error_spaces)
                false
            } cc.matches(onlyNumbers) -> {
                true
            } else -> {
                ccInput.error = context.getString(R.string.error_already_registered)
                false
            }
        }
    }

    // valida si el pin ingresado en el input es valido
    fun pin(pinInput: EditText): Boolean
    {
        val pin = pinInput.text.toString()

        return when {isEmpty(pinInput) -> {
                false
            } !pin.matches(Regex("(.{4})")) -> {
                pinInput.error = context.getString(R.string.error_mustbe_4)
                false
            } pin.matches(hasSpaces) -> {
                pinInput.error = context.getString(R.string.error_spaces)
                false
            } pin.matches(onlyNumbers) -> {
                true
            } else -> {
                pinInput.error = context.getString(R.string.error_only_numbers)
                false
            }
        }
    }

    /*
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
     */

    // valida si el n�mero de tarjeta ingresado en el input es valido
    fun cardNumber(cardNumberInput: EditText): Boolean {
        val cardNumber = cardNumberInput.text.toString()
        val sizeRange = Regex("(.{15,16})")

        return when {isEmpty(cardNumberInput) -> {
                false
            }!cardNumber.matches(sizeRange) -> {
                cardNumberInput.error = context.getString(R.string.error_between_15and16)
                false
            }cardNumber[0] == '3' || cardNumber[0] == '4' || cardNumber[0] == '5' || cardNumber[0] == '6' -> {
                true
            }else -> {
                cardNumberInput.error = context.getString(R.string.error_invalid)
                false
            }
        }
    }

    // valida si la fecha de expiracion de la tarjeta es mayor al actual
    fun expDate(expDateInput: EditText): Boolean
    {
        return if (!isEmpty(expDateInput)) {
            val currentDate = Calendar.getInstance()
            val expDate = Calendar.getInstance()

            val year = expDateInput.text.toString().split("/").toTypedArray()[0].toInt()
            val month = expDateInput.text.toString().split("/").toTypedArray()[1].toInt()
            val day = expDateInput.text.toString().split("/").toTypedArray()[2].toInt()
            expDate[year, month] = day

            if (expDate.after(currentDate)) {
                expDateInput.error = null
                true
            } else {
                expDateInput.error = context.getString(R.string.error_invalid)
                false
            }
        } else {
            false
        }
    }

    // valida si el ccv de la tarjeta es de cuatro caracteres
    fun ccv(ccvInput: EditText): Boolean
    {
        val ccv = ccvInput.text.toString()
        val size = Regex("(.{4})")

        return when {isEmpty(ccvInput) -> {
                false
            }ccv.matches(size) -> {
                true
            }else -> {
                ccvInput.error = context.getString(R.string.error_invalid)
                false
            }
        }
    }

    // valuda si el pago a realizar es valido
    fun payment(paymentInput: EditText): Boolean
    {
        return if (!isEmpty(paymentInput) && isInRange(paymentInput, 3, 7)
                    && isNumber(paymentInput)) {
            val minPayment = 10000
            val maxPayment = 9999999
            val payment = paymentInput.text.toString().toInt()

            if (payment in minPayment..maxPayment) {
                true
            } else {
                paymentInput.error = context.getString(R.string.error_invalid)
                false
            }
        } else {
            false
        }
    }

    // valida si la cantidad de cuotas esta dentro del rango valido
    fun dues(duesInput: EditText): Boolean
    {
        return when {!isEmpty(duesInput) -> {
                false
            }isInRange(duesInput, 1, 12) -> {
                true
            }else -> {
                duesInput.error = context.getString(R.string.error_invalid)
                false
            }
        }
    }

    // verifica los credenciales de logeo para el panel de administrador del corresponsal
    fun adminLogin(emailInput: EditText, passwordInput: EditText): Boolean
    {
        val passwordEmpty = isEmpty(passwordInput)
        val emailEmpty = isEmpty(emailInput)

        if (passwordEmpty || emailEmpty) { return false }

        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()
        val fetch = db.fetchData(email, db.getTable("admin"), db.getColumn("email"))
        var registeredPassword = "empty"

        while (fetch.moveToNext()) { registeredPassword = fetch.getString(3) } // TABLE_ADMINS - COLUMN_PASSWORD }

        return when { fetch.count < 1 -> {
                emailInput.error = context.getString(R.string.error_not_registered)
                false
            } password == registeredPassword -> {
                true
            } else -> {
                passwordInput.error = context.getString(R.string.error_wrong)
                false
            }
        }
    }

    // verifica credenciales de logeo para el usuario
    fun login(ccInput: EditText, pinInput: EditText): Boolean
    {
        val ccEmpty = isEmpty(ccInput)
        val pinEmpty = isEmpty(pinInput)

        if (ccEmpty || pinEmpty) { return false }

        val cc = ccInput.text.toString()
        val pin = pinInput.text.toString()
        val fetch = db.fetchData(cc, db.getTable("user"), db.getColumn("cc"))
        var registeredPin = "empty"

        while (fetch.moveToNext()) { registeredPin = fetch.getString(3) } // TABLE_USERS - COLUMN_PIN }

        return when { fetch.count < 1 -> {
                ccInput.error = context.getString(R.string.error_not_registered)
                false
            } pin == registeredPin -> {
                true
            } else -> {
                pinInput.error = context.getString(R.string.error_wrong)
                false
            }
        }
    }

    // verifica si hay suficientes fondos en la cuenta para relizar la transacci�n
    fun useBalance(moneyInput: EditText, withdrawal: Int = moneyInput.text.toString().toInt()): Boolean
    {
        when {!isEmpty(moneyInput) ->
            db.fetchData(sp.activeUser, db.getTable("user"), db.getColumn("object id"))
            .use { fetch -> fetch.moveToNext()

                return when {
                    fetch.count < 1 -> {
                        Log.e("VALIDATE", "Failed to load cursor data from database, " +
                                    "database=" + db.toString() + "fetch=" + fetch.toString())
                        moneyInput.error = context.getString(R.string.error_fetch_data)
                        false
                    }
                    withdrawal !in 0..9999999 -> {
                        moneyInput.error = context.getString(R.string.error_between_2and7)
                        false
                    }
                    withdrawal + 2000 <= fetch.getString(4).toInt() -> {
                        true
                    }
                    else -> {
                        moneyInput.error = context.getString(R.string.error_enough_funds)
                        false
                    }
                }
            }
            else -> {
                return false
            }
        }
    }

    // verifica si la informaci�n ingresada concuerda con la informaci�n del usuario actual
    fun matchUserData(textInput: EditText, parameter: String): Boolean
    {
        return if (!isEmpty(textInput)) {
            val text = textInput.text.toString()
            val user = User()
            user.loadData(context)

            when (parameter) {
                "pin" -> if (text == user.pin) {
                    true
                } else {
                    textInput.error = context.getString(R.string.error_wrong)
                    false
                }

                "cc" -> if (text == user.cc) {
                    true
                } else {
                    textInput.error = context.getString(R.string.error_wrong)
                    false
                }

                "balance" -> if (text == user.balance.toString()) {
                    true
                } else {
                    textInput.error = context.getString(R.string.error_wrong)
                    false
                }

                "name" -> if (text == user.name) {
                    true
                } else {
                    textInput.error = context.getString(R.string.error_wrong)
                    false
                }

                else -> {
                    textInput.error = context.getString(R.string.error_wrong)
                    false
                }
            }
        } else {
            false
        }
    }

    // verifica si la informaci�n ingresada no es igual a la informaci�n del usuario actual
    fun notMatchUserData(textInput: EditText, parameter: String): Boolean {
        return if (!isEmpty(textInput)) {
            if (matchUserData(textInput, parameter)) {
                textInput.error = context.getString(R.string.error_wrong)
                false
            } else {
                textInput.error = null
                true
            }
        } else {
            false
        }
    }
}
