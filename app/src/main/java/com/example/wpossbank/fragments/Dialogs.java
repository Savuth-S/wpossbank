package com.example.wpossbank.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.wpossbank.MainActivity;
import com.example.wpossbank.R;
import com.example.wpossbank.database.Database;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.CreditCard;
import com.example.wpossbank.modelos.SharedPreference;
import com.example.wpossbank.modelos.User;

import java.util.Objects;

public class Dialogs {
    public static class ConfirmUserTransferBalance extends DialogFragment {
        Context context;
        Resources res;
        SharedPreference sp;

        Admin admin;
        User user;
        User transferUser;

        String message;
        int addAmount;

        public ConfirmUserTransferBalance(Context context, Admin admin, String ccTransfer, String message, int addAmount) {
            this.context = context;
            sp = new SharedPreference(context);

            this.admin = admin;
            user = new User(context);
            transferUser = new User(context);

            this.message = message;
            this.addAmount = addAmount;

            user.loadData(user);
            this.transferUser = transferUser.loadUser(ccTransfer);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            res = getResources();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(res.getString(R.string.confirm)).setMessage(message)
                    .setPositiveButton(res.getString(R.string.button_confirm),
                            (dialogInterface, i) -> {
                                admin.update(context, admin);

                                user.setBalance(addAmount);
                                user.update(context, user);

                                new Dialogs.TransactionSuccess().showNow(requireActivity().getSupportFragmentManager(), "SUCCESS");
                                dismiss();
                            })
                    .setNegativeButton(res.getString(R.string.button_cancel),
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
        Admin admin;
        User user;
        SharedPreference sp;
        String message;
        int addAmount;

        public ConfirmUserAddBalance(Context context, Admin admin, String message, int addAmount) {
            this.context = context;
            this.admin = admin;
            sp = new SharedPreference(context);
            user = new User(context);
            this.message = message;
            this.addAmount = addAmount;

            user.loadData(user);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            res = getResources();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(res.getString(R.string.confirm)).setMessage(message)
                    .setPositiveButton(res.getString(R.string.button_confirm),
                            (dialogInterface, i) -> {
                                admin.update(context, admin);

                                user.setBalance(addAmount);
                                user.update(context, user);

                                new Dialogs.TransactionSuccess().showNow(requireActivity().getSupportFragmentManager(), "SUCCESS");
                                dismiss();
                            })
                    .setNegativeButton(res.getString(R.string.button_cancel),
                            (dialogInterface, i) -> {
                                new Dialogs.TransactionFailed().show(requireActivity().getSupportFragmentManager(), "FAIL");
                                dismiss();
                            });

            return builder.create();
        }
    }


    public static class ConfirmAdminAddBalance extends DialogFragment {
        Context context;
        Resources res;
        Admin admin;
        String message;

        public ConfirmAdminAddBalance(Context context, Admin admin, String message) {
            this.context = context;
            this.admin = admin;
            this.message = message;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            res = getResources();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(res.getString(R.string.confirm)).setMessage(message)
                    .setPositiveButton(res.getString(R.string.button_confirm),
                            (dialogInterface, i) -> {
                                admin.update(context, admin);

                                new Dialogs.TransactionSuccess().showNow(requireActivity().getSupportFragmentManager(), "SUCCESS");
                                dismiss();
                            })
                    .setNegativeButton(res.getString(R.string.button_cancel),
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
                    .setNeutralButton(R.string.button_accept,
                            (dialogInterface, id) ->
                                    requireActivity().finish());

            return builder.create();
        }
    }
}

