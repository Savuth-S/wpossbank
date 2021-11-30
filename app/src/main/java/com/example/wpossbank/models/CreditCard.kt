package com.example.wpossbank.models

import androidx.appcompat.app.AppCompatActivity
import com.example.wpossbank.R
import android.content.Context

class CreditCard : AppCompatActivity() {
    var number = "123456789012345"
    var expDate = "2021/1/1"
    var ccv = "1234"
    var ownerName = "USER USER"
    var paymentAmmount = "0"
    var duesNumber = "1"

    // tabla que devuelve el tipo de tarjeta de credito en base al primer numero de la tarjeta
    fun getType(context: Context): String {
        return when (number[0]) {
            '3' -> "American Express"
            '4' -> "VISA"
            '5' -> "MasterCard"
            '6' -> "UnionPay"
            else -> context.getString(R.string.card_number)
        }
    }
}