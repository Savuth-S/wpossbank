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

import com.example.wpossbank.R;
import com.example.wpossbank.UserProfileActivity;
import com.example.wpossbank.database.Database;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.SharedPreference;
import com.example.wpossbank.modelos.User;

public class Dialogs {
    public static class ConfirmUserTransferBalance extends DialogFragment {
        Context context;
        Resources res;
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
            builder.setTitle(R.string.confirm).setMessage(message)
                    .setPositiveButton(R.string.button_confirm,
                            (dialogInterface, i) -> {
                                admin.update(context, admin);

                                user.setBalance(addAmount-(addAmount*2));
                                user.update(context, user);

                                transferUser.setBalance(addAmount);
                                transferUser.update(context, transferUser);

                                db.newLogEntry(type, Integer.toString(addAmount-(addAmount*2)), source);
                                db.newLogEntry(type, Integer.toString(addAmount), source, transferUser.getUserId());

                                new Dialogs.TransactionSuccess().showNow(requireActivity().getSupportFragmentManager(), "SUCCESS");
                                dismiss();
                            })
                    .setNegativeButton(R.string.button_cancel,
                            (dialogInterface, i) -> {
                                new Dialogs.TransactionFailed().show(requireActivity().getSupportFragmentManager(), "FAIL");
                                dismiss();
                            });

            return builder.create();
        }
    }



    public static class ConfirmUserGetBalance extends DialogFragment {
        Context context;
        Resources res;
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
            builder.setTitle(R.string.confirm).setMessage(message)
                    .setPositiveButton(R.string.button_confirm,
                            (dialogInterface, i) -> {
                                admin.update(context, admin);

                                user.setBalance(admin.getBalance()-(admin.getBalance()*2));
                                user.update(context, user);

                                db.newLogEntry(type, "0", source);

                                requireActivity().finish();
                                startActivity(new Intent(context, UserProfileActivity.class));
                                dismiss();
                            })
                    .setNegativeButton(R.string.button_cancel,
                            (dialogInterface, i) -> {
                                new Dialogs.TransactionFailed().show(requireActivity().getSupportFragmentManager(), "FAIL");
                                dismiss();
                            });

            return builder.create();
        }
    }




    public static class ConfirmUserAddBalance extends DialogFragment {
        Context context;
        Resources res;
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
            builder.setTitle(R.string.confirm).setMessage(message)
                    .setPositiveButton(R.string.button_confirm,
                            (dialogInterface, i) -> {
                                admin.update(context, admin);

                                user.setBalance(addAmount);
                                user.update(context, user);

                                db.newLogEntry(type, Integer.toString(addAmount), source);

                                new Dialogs.TransactionSuccess().showNow(requireActivity().getSupportFragmentManager(), "SUCCESS");
                                dismiss();
                            })
                    .setNegativeButton(R.string.button_cancel,
                            (dialogInterface, i) -> {
                                new Dialogs.TransactionFailed().show(requireActivity().getSupportFragmentManager(), "FAIL");
                                dismiss();
                            });

            return builder.create();
        }
    }


    public static class ConfirmAdminAddBalance extends DialogFragment {
        Context context;
        Database db;
        Admin admin;

        String message;
        String type;
        String source;

        public ConfirmAdminAddBalance(Context context, Admin admin, String message, String type, String source) {
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
            builder.setTitle(R.string.confirm).setMessage(message)
                    .setPositiveButton(R.string.button_confirm,
                            (dialogInterface, i) -> {
                                admin.update(context, admin);
                                db.newLogEntry(type, Integer.toString(admin.getBalance()), source);

                                new Dialogs.TransactionSuccess().showNow(requireActivity().getSupportFragmentManager(), "SUCCESS");
                                dismiss();
                            })
                    .setNegativeButton(R.string.button_cancel,
                            (dialogInterface, i) -> {
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

            builder.setView(inflater.inflate(R.layout.dialog_success, null))
                    .setNeutralButton(R.string.button_accept,
                            (dialogInterface, id) ->
                                    requireActivity().finish());

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
                    .setNeutralButton(R.string.button_accept, (dialogInterface, id) -> requireActivity().finish());

            return builder.create();
        }
    }
}

