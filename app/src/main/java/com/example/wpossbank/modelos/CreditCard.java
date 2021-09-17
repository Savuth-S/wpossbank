package com.example.wpossbank.modelos;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wpossbank.R;

public class CreditCard extends AppCompatActivity {
    private String number = "123456789012345";
    private String expDate = "2021/1/1";
    private String ccv = "1234";
    private String ownerName = "USER USER";
    private String paymentAmmount = "0";
    private String duesNumber = "1";

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getCcv() { return ccv; }
    public void setCcv(String ccv) { this.ccv = ccv; }

    public String getExpDate() { return expDate; }
    public void setExpDate(String expDate) { this.expDate = expDate; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getPaymentAmmount() { return paymentAmmount; }
    public void setPaymentAmmount(String paymentAmmount) { this.paymentAmmount = paymentAmmount; }

    public String getDuesNumber() { return duesNumber; }
    public void setDuesNumber(String duesNumber) { this.duesNumber = duesNumber; }

    // tabla que devuelve el tipo de tarjeta de credito en base al primer numero de la tarjeta
    public String getType(Context context){
        switch (getNumber().charAt(0)) {
            case '3':
                return "American Express";
            case '4':
                return "VISA";
            case '5':
                return "MasterCard";
            case '6':
                return "UnionPay";
            default:
                return context.getString(R.string.card_number);
        }
    }
}
