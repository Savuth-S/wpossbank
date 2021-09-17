package com.example.wpossbank.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.wpossbank.R;
import com.example.wpossbank.UserProfileActivity;
import com.example.wpossbank.database.Database;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.SharedPreference;
import com.example.wpossbank.modelos.User;

public class Dialogs {

    // Dialogo para la transferencia de dinero entre usuarios
    public static class ConfirmUserTransferBalance extends DialogFragment {
        Context context;
        Database db;
        SharedPreference sp;

        Admin admin;
        User user;
        User transferUser;

        String message;
        String type;
        String source;
        int addAmount;

        public ConfirmUserTransferBalance(Context context, Admin admin, String ccTransfer, String message,
                                          String type, String source, int addAmount) {
            this.context = context;
            this.admin = admin;

            this.message = message;
            this.type = type;
            this.source = source;
            this.addAmount = addAmount;

            db = new Database(context);
            sp = new SharedPreference(context);
            user = new User(context);
            transferUser = new User(context);

            // carga la informaci�n dek usuario activo desde la base de datos al nuevo objeto local de usuario
            user.loadData();
            transferUser.loadUser(ccTransfer);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            /* carga el layout a la view del dialogo, luego obtiene los elementos del layout
                y declara los objetos correspondientes */
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_confirm, null);
            TextView textView = view.findViewById(R.id.message);
            Button positiveButton = view.findViewById(R.id.positiveButton),
                    negativeButton = view.findViewById(R.id.negativeButton);

            // carga el mensaje de confirmaci�n en el layout y luego carga la view al dialogo
            textView.setText(message);
            builder.setView(view);

            positiveButton.setOnClickListener( showSuccessDialog ->{
                // agrega el cobro de comision a la cuenta del corresponsal
                admin.update(context);

                // elimina el monto de la transferencia y el cobro de comision de el saldo del usuario
                user.setBalance(addAmount-(addAmount*2+admin.getBalance()));
                user.update(context, user);

                // agrega el monto de la transferencia al saldo del usuario que la esta recibiendo
                transferUser.setBalance(addAmount);
                transferUser.update(context, transferUser);

                // agrega la transacci�n al registro de transacciones
                db.newLogEntry(type, Integer.toString(addAmount-(addAmount*2)), source);
                db.newLogEntry(type, Integer.toString(addAmount), source, transferUser.getObjectId());

                new Dialogs.TransactionSuccess().showNow(requireActivity().getSupportFragmentManager(), "SUCCESS");
                dismiss();
            });

            negativeButton.setOnClickListener( showFailDialog ->{
                new Dialogs.TransactionFailed().show(requireActivity().getSupportFragmentManager(), "FAIL");
                dismiss();
            });

            return builder.create();
        }
    }



    // Dialogo para confirmar el cobro de comision por ver el saldo actual del usuario
    public static class ConfirmUserGetBalance extends DialogFragment {
        Context context;
        Database db;
        SharedPreference sp;

        Admin admin;
        User user;

        String message;
        String type;
        String source;

        public ConfirmUserGetBalance(Context context, Admin admin, String message,
                                     String type, String source) {
            this.context = context;
            this.admin = admin;

            this.message = message;
            this.type = type;
            this.source = source;

            db = new Database(context);
            sp = new SharedPreference(context);
            user = new User(context);

            // carga la informaci�n dek usuario activo desde la base de datos al nuevo objeto local de usuario
            user.loadData();
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            /* carga el layout a la view del dialogo, luego obtiene los elementos del layout
                y declara los objetos correspondientes */
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_confirm, null);
            TextView textView = view.findViewById(R.id.message);
            Button positiveButton = view.findViewById(R.id.positiveButton),
                    negativeButton = view.findViewById(R.id.negativeButton);

            // carga el mensaje de confirmaci�n en el layout y luego carga la view al dialogo
            textView.setText(message);
            builder.setView(view);

            positiveButton.setOnClickListener( showSuccessDialog ->{
                // agrega el cobro de comision a la cuenta del corresponsal
                admin.update(context);

                // elimina el cobro de comision del saldo del usuario
                user.setBalance(admin.getBalance()-(admin.getBalance()*2));
                user.update(context, user);

                // añade la transacci�n en el registro de transacciones
                db.newLogEntry(type, "0", source);

                requireActivity().finish();
                startActivity(new Intent(context, UserProfileActivity.class));
                dismiss();
            });

            negativeButton.setOnClickListener( showFailDialog ->{
                new Dialogs.TransactionFailed().show(requireActivity().getSupportFragmentManager(), "FAIL");
                dismiss();
            });

            return builder.create();
        }
    }



    // Dialogo para la confirmaci�n de las transacciones del usuario
    public static class ConfirmUserUpdateBalance extends DialogFragment {
        Context context;
        Database db;
        SharedPreference sp;

        Admin admin;
        User user;

        String message;
        String type;
        String source;
        int addAmount;

        public ConfirmUserUpdateBalance(Context context, Admin admin, String message, String type,
                                     String source, int addAmount) {
            this.context = context;
            this.admin = admin;

            this.message = message;
            this.type = type;
            this.source = source;
            this.addAmount = addAmount;

            db = new Database(context);
            sp = new SharedPreference(context);
            user = new User(context);

            // carga la informaci�n dek usuario activo desde la base de datos al nuevo objeto local de usuario
            user.loadData();
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            /* carga el layout a la view del dialogo, luego obtiene los elementos del layout
                y declara los objetos correspondientes */
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_confirm, null);
            TextView textView = view.findViewById(R.id.message);
            Button positiveButton = view.findViewById(R.id.positiveButton),
                    negativeButton = view.findViewById(R.id.negativeButton);

            // carga el mensaje de confirmaci�n en el layout y luego carga la view al dialogo
            textView.setText(message);
            builder.setView(view);

            positiveButton.setOnClickListener( showSuccessDialog ->{
                // agrega el cobro de comision a la cuenta del corresponsal
                admin.update(context);

                // añade (o elimina si es negativo) el monto de la transacci�n al saldo del usuario
                user.setBalance(addAmount);
                user.update(context, user);

                // añade la transaccion al registro de transacciones
                db.newLogEntry(type, Integer.toString(addAmount), source);

                new Dialogs.TransactionSuccess().showNow(requireActivity().getSupportFragmentManager(), "SUCCESS");
                dismiss();
            });

            negativeButton.setOnClickListener( showFailDialog-> {
                new Dialogs.TransactionFailed().show(requireActivity().getSupportFragmentManager(), "FAIL");
                dismiss();
            });

            return builder.create();
        }
    }



    // Dialogo para confirmar la actualizacion de informacio�n de la cuenta del corresponsal
    public static class ConfirmUpdateAdmin extends DialogFragment {
        Context context;
        Database db;
        Admin admin;

        String message, type, source, active;

        public ConfirmUpdateAdmin(Context context, Admin admin, String message, String type, String source, String active) {
            this.context = context;
            this.admin = admin;

            this.message = message;
            this.type = type;
            this.source = source;
            this.active = active; // usuario activo con el que asociar esta transacci�n

            db = new Database(context);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            /* carga el layout a la view del dialogo, luego obtiene los elementos del layout
                y declara los objetos correspondientes */
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_confirm, null);
            TextView textView = view.findViewById(R.id.message);
            Button positiveButton = view.findViewById(R.id.positiveButton),
                    negativeButton = view.findViewById(R.id.negativeButton);

            // carga el mensaje de confirmaci�n en el layout y luego carga la view al dialogo
            textView.setText(message);
            builder.setView(view);

            positiveButton.setOnClickListener( showSuccessDialog ->{
                /* actualiza la informaci�n del objeto local del admin a la base de datos
                   y añade una entrada a la tabla de transacciones */
                admin.update(context);
                db.newLogEntry(type, Integer.toString(admin.getBalance()), source, active);

                new Dialogs.TransactionSuccess().showNow(requireActivity().getSupportFragmentManager(), "SUCCESS");
                dismiss();
            });

            negativeButton.setOnClickListener( showFailDialog ->{
                new Dialogs.TransactionFailed().show(requireActivity().getSupportFragmentManager(), "FAIL");
                dismiss();
            });

            return builder.create();
        }


    }



    public static class TransactionSuccess extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_success, null);
            Button neutralButton = view.findViewById(R.id.neutralButton);

            builder.setView(view);

            neutralButton.setOnClickListener( finishActivity -> requireActivity().finish());

            return builder.create();
        }
    }


    public static class TransactionFailed extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_fail, null);
            Button neutralButton = view.findViewById(R.id.neutralButton);

            builder.setView(view);

            neutralButton.setOnClickListener( finishActivity -> requireActivity().finish());

            return builder.create();
        }
    }
}

