package com.example.wpossbank.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.wpossbank.MainActivity;
import com.example.wpossbank.R;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.CreditCard;

import java.util.Objects;

public class Dialogs {

    public static class ConfirmTransaction extends DialogFragment {
        Context context;
        Resources res;
        Admin admin;
        CreditCard card;

        public ConfirmTransaction(Context context, Admin admin, CreditCard card) {
            this.context = context;
            this.admin = admin;
            this.card = card;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            res = getResources();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(res.getString(R.string.confirm)).setMessage(makeMessage(card))
                    .setPositiveButton(res.getString(R.string.button_confirm),
                            (dialogInterface, i) -> {
                                admin.update(context, admin);
                                new Dialogs.TransactionSucces().showNow(requireActivity().getSupportFragmentManager(), "SUCCESS");
                                dismiss();
                            })
                    .setNegativeButton(res.getString(R.string.button_cancel),
                            (dialogInterface, i) -> {
                                new Dialogs.TransactionFailed().show(requireActivity().getSupportFragmentManager(), "FAIL");
                                dismiss();
                            });

            return builder.create();
        }

        public String makeMessage(@NonNull CreditCard card) {
            String message;
            String[] template = res.getString(R.string.dialog_confirm_payment).split("/");

            //Reemplaza cada numero excepto los cuatro ultimos por *
            int cardLength = card.getNumber().length();
            String cardEnds = "";

            for (int i = 0; i < cardLength; i++) {
                if (cardLength % 4 == 0) {
                    //Agrega un espacio cada cuatro caracteres
                    if (i % 4 == 0) {
                        cardEnds = cardEnds.concat(" ");
                    }

                    if (i < cardLength - 4) {
                        cardEnds = cardEnds.concat("*");
                    } else {
                        cardEnds = cardEnds.concat(Character.toString(card.getNumber().charAt(i)));
                    }
                }else{
                    //Agrega un espacio cada tres caracteres
                    if (i % 3 == 0){
                        cardEnds = cardEnds.concat(" ");
                    }

                    if (i < cardLength - 3) {
                        cardEnds = cardEnds.concat("*");
                    } else {
                        cardEnds = cardEnds.concat(Character.toString(card.getNumber().charAt(i)));
                    }
                }


            }

            //Concatena el array de los mensajes de la plantilla con los valores de la tarjeta
            message = template[0] + card.getOwnerName() + System.getProperty("line.separator") +    //Estimado: USER
                    template[1] + card.getPaymentAmmount() + template[2] +                    //¿Desea realizar el pago a WPOSS por
                    card.getDuesNumber() + template[3] + System.getProperty("line.separator") +     //un valor de: 12345 a 12 cuotas?
                    System.getProperty("line.separator") + template[4] +                            //
                    System.getProperty("line.separator") + cardEnds + " - " + card.getType();             //Número de tarjeta
            //**** **** **** 1234 - VISA
            return message;
        }
    }

    public static class TransactionSucces extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();

            builder.setView(inflater.inflate(R.layout.dialog_success, null))
                    .setNeutralButton(R.string.button_accept,
                            (dialogInterface, id) ->
                                    startActivity(new Intent(getContext(), MainActivity.class)));

            return builder.create();
        }
    }

    public static class TransactionFailed extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();

            builder.setView(inflater.inflate(R.layout.dialog_fail, null))
                    .setNeutralButton(R.string.button_accept,
                            (dialogInterface, id) ->
                                    startActivity(new Intent(getContext(), MainActivity.class)));

            return builder.create();
        }
    }
}

