package com.example.wpossbank.models

import android.content.Context
import com.example.wpossbank.R
import com.example.wpossbank.database.Database

class LogEntry {
    var type = ""
    var source: String? = ""
    var activeUser: String? = ""
    var amount = 0
    fun addLogEntry(context: Context) {
        getLogEntryType(context, this)
        if (activeUser!!.isEmpty()) {
            activeUser = SharedPreference(context).activeUser
        }
        Database(context).addLogEntry(this)
    }

    // tabla de tipo de acciones al registrar una entrada en el historial de transacciones
    private fun getLogEntryType(context: Context, logEntry: LogEntry) {
        when (type) {
            "card" -> logEntry.type = context.getString(R.string.credit_card_sell)
            "withdraw" -> logEntry.type = context.getString(R.string.money_withdrawal)
            "deposit" -> logEntry.type = context.getString(R.string.money_deposit)
            "transfer" -> logEntry.type = context.getString(R.string.money_transfer)
            "show balance" -> logEntry.type = context.getString(R.string.show_account_balance)
            "new user" -> logEntry.type = context.getString(R.string.make_new_account)
            "update" -> logEntry.type = context.getString(R.string.update)
            else -> logEntry.type = context.getString(R.string.error_invalid)
        }
    }
}