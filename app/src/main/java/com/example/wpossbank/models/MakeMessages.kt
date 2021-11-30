package com.example.wpossbank.models

import android.content.Context
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.wpossbank.R
import com.example.wpossbank.database.Database

class MakeMessages : AppCompatActivity() {
    /* Devuelve el array de numeros separados por el caracter especificados cada N cantidad de numeros
       comenzando de derecha a izquierda */
    fun separateNumberRight(number: String, character: String, everyNCharacters: Int): String {
        return if (number.isNotEmpty()) {
            var newNumber = ""
            for (i in 1 until number.length + 1) {
                newNumber += number[i - 1].toString()
                if ((number.length - i) % everyNCharacters == 0 && number.length - i != 0) {
                    newNumber += character
                }
            }
            newNumber
        } else {
            ""
        }
    }

    /* Devuelve el array de numeros separados por el caracter especificados cada N cantidad de numeros
       comenzando de izquierda a derecha */
    fun separateNumberLeft(number: String, character: String, everyNCharacters: Int): String {
        return if (number.isNotEmpty()) {
            var newNumber = ""
            for (i in 1 until number.length + 1) {
                newNumber += number[i - 1].toString()
                if (i % everyNCharacters == 0) {
                    newNumber += character
                }
            }
            newNumber
        } else {
            ""
        }
    }

    // devuelve una sola string con el mensaje para el dialogo de pago con tarjeta
    fun cardPayment(context: Context, card: CreditCard): String {
        val message: String
        /* corta el resource string en pedazos  usnado el caracter / como referencia,
           para luego llenar los campos correspondientes */
        val template =
            context.getString(R.string.dialog_confirm_cardpayment).split("/").toTypedArray()

        //Reemplaza cada numero excepto los cuatro ultimos por *
        val cardLength = card.number.length
        var cardEnds = ""
        for (i in 0 until cardLength) {
            if (cardLength % 4 == 0) {
                //Agrega un espacio cada cuatro caracteres
                if (i % 4 == 0) {
                    cardEnds = "$cardEnds "
                }

                //reemplaza los numeros anteriores a los cuatro ultimos con *
                cardEnds = if (i < cardLength - 4) {
                    "$cardEnds*"
                } else {
                    cardEnds + card.number[i].toString()
                }
            } else {
                //Agrega un espacio cada tres caracteres
                if (i % 3 == 0) {
                    cardEnds = "$cardEnds "
                }

                //reemplaza los numeros anteriores a los tres ultimos con *
                cardEnds = if (i < cardLength - 3) {
                    "$cardEnds*"
                } else {
                    cardEnds + card.number[i].toString()
                }
            }
        }

        //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
        message = (template[0] + card.ownerName + System.getProperty("line.separator") +
                template[1] + separateNumberRight(card.paymentAmmount, ".", 3)
                + template[2] + card.duesNumber + template[3] + System.getProperty("line.separator") +
                System.getProperty("line.separator") + template[4] +
                System.getProperty("line.separator") + cardEnds + " - " + card.getType(context))
        return message
    }

    // devuelve una sola string con el mensaje para el dialogo de retirar dinero
    fun withdraw(context: Context, withdrawInput: EditText): String {
        val message: String
        /* corta el resource string en pedazos  usnado el caracter / como referencia,
           para luego llenar los campos correspondientes */
        val template =
            context.getString(R.string.dialog_confirm_withdrawal).split("/").toTypedArray()
        val user = User()
        user.loadData(context)

        //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
        message = (template[0] + user.name + System.getProperty("line.separator") +
                template[1] + separateNumberRight(withdrawInput.text.toString(), ".", 3)
                + template[2])
        return message
    }

    // devuelve una sola string con el mensaje para el dialogo de añadir fondos al corresponsal
    fun adminDeposit(context: Context, depositInput: EditText): String {
        val message: String
        /* corta el resource string en pedazos  usnado el caracter / como referencia,
           para luego llenar los campos correspondientes */
        val template =
            context.getString(R.string.dialog_confirm_admin_deposit).split("/").toTypedArray()

        //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
        message = (template[0] + System.getProperty("line.separator") +
                template[1] + separateNumberRight(depositInput.text.toString(), ".", 3)
                + template[2])
        return message
    }

    // devuelve una sola string con el mensaje para el dialogo de depositar dinero en la cuenta
    fun deposit(context: Context, depositInput: EditText): String {
        val message: String
        /* corta el resource string en pedazos  usnado el caracter / como referencia,
           para luego llenar los campos correspondientes */
        val template = context.getString(R.string.dialog_confirm_deposit).split("/").toTypedArray()
        val user = User()
        user.loadData(context)

        //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
        message = (template[0] + System.getProperty("line.separator") +
                template[1] + separateNumberRight(depositInput.text.toString(), ".", 3)
                + template[2] + user.name + template[3])
        return message
    }

    // devuelve una sola string con el mensaje para el dialogo de transferir dinero
    fun transfer(context: Context, transferInput: EditText?, ccTransferInput: EditText): String {
        val message: String
        /* corta el resource string en pedazos  usnado el caracter / como referencia,
           para luego llenar los campos correspondientes */
        val template = context.getString(R.string.dialog_confirm_transfer).split("/").toTypedArray()

        // carga la informacion del usuario que reliza la transferencia
        val user = User()
        user.loadData(context)

        // obtiene la informaci�n del usuario que recibe la transferencia
        val db = Database(context)
        val fetch = db.fetchData(
            ccTransferInput.text.toString(),
            db.getTable("user"),
            db.getColumn("cc")
        )
        return if (fetch.count > 0) {
            fetch.moveToFirst()

            //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
            message = (template[0] + user.name + System.getProperty("line.separator") +
                    template[1] + separateNumberRight(transferInput!!.text.toString(), ".", 3)
                    + template[2] + fetch.getString(5) + template[3])
            message
        } else {
            context.getString(R.string.error_fetch_data)
        }
    }

    // devuelve una sola string con el mensaje para el dialogo de ver el saldo de la cuenta
    fun getBalance(context: Context): String {
        val message: String
        /* corta el resource string en pedazos  usnado el caracter / como referencia,
           para luego llenar los campos correspondientes */
        val template =
            context.getString(R.string.dialog_confirm_get_balance).split("/").toTypedArray()
        val user = User()
        user.loadData(context)

        //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
        message = template[0] + user.name + System.getProperty("line.separator") +
                template[1] + System.getProperty("line.separator") + template[2]
        return message
    }
}