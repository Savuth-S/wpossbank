package com.example.wpossbank.modelos;

import android.content.Context;


import com.example.wpossbank.R;
import com.example.wpossbank.database.Database;

public class LogEntry {
    String type = "";
    String source = "";
    String activeUser = "";
    int amount = 0;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getActiveUser() { return activeUser; }
    public void setActiveUser(String activeUser) { this.activeUser = activeUser; }

    public void addLogEntry(Context context){
        getLogEntryType(context, this);

        if (getActiveUser().isEmpty()){
            setActiveUser(new SharedPreference(context).getActiveUser());
        }

        new Database(context).addLogEntry(this);
    }

    // tabla de tipo de acciones al registrar una entrada en el historial de transacciones
    private void getLogEntryType(Context context, LogEntry logEntry){
        switch(type){
            case "card":
                logEntry.setType(context.getString(R.string.credit_card_sell));
                break;
            case "withdraw":
                logEntry.setType(context.getString(R.string.money_withdrawal));
                break;
            case "deposit":
                logEntry.setType(context.getString(R.string.money_deposit));
                break;
            case "transfer":
                logEntry.setType(context.getString(R.string.money_transfer));
                break;
            case "show balance":
                logEntry.setType(context.getString(R.string.show_account_balance));
                break;
            case "new user":
                logEntry.setType(context.getString(R.string.make_new_account));
                break;
            case "update":
                logEntry.setType(context.getString(R.string.update));
                break;
            default:
                logEntry.setType(context.getString(R.string.error_invalid));
                break;
        }
    }
}
