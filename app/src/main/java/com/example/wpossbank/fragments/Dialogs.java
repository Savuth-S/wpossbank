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

            user.loadData(user);
            this.transferUser = transferUser.loadUser(ccTransfer);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_confirm, null);
            TextView textView = view.findViewById(R.id.message);
            Button positiveButton = view.findViewById(R.id.positiveButton),
                    negativeButton = view.findViewById(R.id.negativeButton);

            textView.setText(message);
            builder.setView(view);

            positiveButton.setOnClickListener( showSuccessDialog ->{
                admin.update(context);

                user.setBalance(addAmount-(addAmount*2));
                user.update(context, user);

                transferUser.setBalance(addAmount);
                transferUser.update(context, transferUser);

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

            user.loadData(user);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_confirm, null);
            TextView textView = view.findViewById(R.id.message);
            Button positiveButton = view.findViewById(R.id.positiveButton),
                    negativeButton = view.findViewById(R.id.negativeButton);

            textView.setText(message);
            builder.setView(view);

            positiveButton.setOnClickListener( showSuccessDialog ->{
                admin.update(context);

                user.setBalance(admin.getBalance()-(admin.getBalance()*2));
                user.update(context, user);

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




    public static class ConfirmUserAddBalance extends DialogFragment {
        Context context;
        Database db;
        SharedPreference sp;

        Admin admin;
        User user;

        String message;
        String type;
        String source;
        int addAmount;

        public ConfirmUserAddBalance(Context context, Admin admin, String message, String type,
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

            user.loadData(user);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_confirm, null);
            TextView textView = view.findViewById(R.id.message);
            Button positiveButton = view.findViewById(R.id.positiveButton),
                    negativeButton = view.findViewById(R.id.negativeButton);

            textView.setText(message);
            builder.setView(view);

            positiveButton.setOnClickListener( showSuccessDialog ->{
                admin.update(context);

                user.setBalance(addAmount);
                user.update(context, user);

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


    public static class ConfirmUpdateAdmin extends DialogFragment {
        Context context;
        Database db;
        Admin admin;

        String message;
        String type;
        String source;

        public ConfirmUpdateAdmin(Context context, Admin admin, String message, String type, String source) {
            this.context = context;
            this.admin = admin;

            this.message = message;
            this.type = type;
            this.source = source;

            db = new Database(context);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_confirm, null);
            TextView textView = view.findViewById(R.id.message);
            Button positiveButton = view.findViewById(R.id.positiveButton),
                    negativeButton = view.findViewById(R.id.negativeButton);

            textView.setText(message);
            builder.setView(view);

            positiveButton.setOnClickListener( showSuccessDialog ->{
                admin.update(context);
                db.newLogEntry(type, Integer.toString(admin.getBalance()), source, admin.getEmail());

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

