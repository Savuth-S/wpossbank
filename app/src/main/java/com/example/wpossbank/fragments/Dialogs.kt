package com.example.wpossbank.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.wpossbank.R
import com.example.wpossbank.activities.UserProfileActivity
import com.example.wpossbank.database.Database
import com.example.wpossbank.models.Admin
import com.example.wpossbank.models.LogEntry
import com.example.wpossbank.models.SharedPreference
import com.example.wpossbank.models.User

class Dialogs
{
    // Dialogo para la transferencia de dinero entre usuarios
    class ConfirmUserTransferBalance(var logEntry: LogEntry, var admin: Admin,
                                     ccTransfer: String, private var message: String) : DialogFragment()
    {
        var db: Database = Database(requireContext())
        var sp: SharedPreference = SharedPreference(requireContext())
        var user: User = User()
        private var transferUser: User = User()

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(activity)
            /* carga el layout a la view del dialogo, luego obtiene los elementos del layout
                y declara los objetos correspondientes */
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_confirm, null)
            val textView = view.findViewById<TextView>(R.id.message)
            val positiveButton = view.findViewById<Button>(R.id.positiveButton)
            val negativeButton = view.findViewById<Button>(R.id.negativeButton)

            // carga el mensaje de confirmaci�n en el layout y luego carga la view al dialogo
            textView.text = message
            builder.setView(view)
            positiveButton.setOnClickListener {
                // agrega el cobro de comision a la cuenta del corresponsal
                admin.update(requireContext())

                // elimina el monto de la transferencia y el cobro de comision de el saldo del usuario
                user.balance = logEntry.amount - (logEntry.amount * 2 + admin.balance)
                user.update(requireContext())

                // agrega el monto de la transferencia al saldo del usuario que la esta recibiendo
                transferUser.balance = logEntry.amount
                transferUser.update(requireContext())

                // agrega la transacci�n al registro de transacciones
                logEntry.amount = logEntry.amount - (logEntry.amount * 2 + admin.balance)
                logEntry.addLogEntry(requireContext())
                logEntry.amount = logEntry.amount * -1 - admin.balance
                logEntry.activeUser = transferUser.getObjectId(requireContext())
                logEntry.addLogEntry(requireContext())
                TransactionSuccess().showNow(requireActivity().supportFragmentManager, "SUCCESS")
                dismiss()
            }
            negativeButton.setOnClickListener {
                TransactionFailed().show(requireActivity().supportFragmentManager, "FAIL")
                dismiss()
            }
            return builder.create()
        }

        init {
            // carga la informaci�n dek usuario activo desde la base de datos al nuevo objeto local de usuario
            user.loadData(requireContext())
            transferUser.loadUser(requireContext(), ccTransfer)
        }
    }

    // Dialogo para confirmar el cobro de comision por ver el saldo actual del usuario
    class ConfirmUserGetBalance(context: Context, var logEntry: LogEntry, var admin: Admin,
                                private var message: String?) : DialogFragment()
    {
        var user: User = User()

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
        {
            val builder = AlertDialog.Builder(activity)

            /* carga el layout a la view del dialogo, luego obtiene los elementos del layout
                y declara los objetos correspondientes */
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_confirm, null)
            val textView = view.findViewById<TextView>(R.id.message)
            val positiveButton = view.findViewById<Button>(R.id.positiveButton)
            val negativeButton = view.findViewById<Button>(R.id.negativeButton)

            // carga el mensaje de confirmaci�n en el layout y luego carga la view al dialogo
            textView.text = message
            builder.setView(view)

            positiveButton.setOnClickListener {
                // agrega el cobro de comision a la cuenta del corresponsal
                context?.let { it1 -> admin.update(it1) }

                // elimina el cobro de comision del saldo del usuario
                user.balance = logEntry.amount
                context?.let { it1 -> user.update(it1) }

                // añade la transacci�n en el registro de transacciones
                context?.let { it1 -> logEntry.addLogEntry(it1) }
                requireActivity().finish()
                startActivity(Intent(context, UserProfileActivity::class.java))
                dismiss()
            }

            negativeButton.setOnClickListener {
                TransactionFailed().show(requireActivity().supportFragmentManager, "FAIL")
                dismiss()
            }

            return builder.create()
        }

        init
        {
            // carga la informaci�n dek usuario activo desde la base de datos al nuevo objeto local de usuario
            user.loadData(context)
        }
    }

    // Dialogo para la confirmaci�n de las transacciones del usuario
    class ConfirmUserUpdateBalance(context : Context,var logEntry: LogEntry, var admin: Admin,
                                   private var message: String?) : DialogFragment()
    {
        var sp: SharedPreference = SharedPreference(context)
        var user: User = User()

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
        {
            val builder = AlertDialog.Builder(activity)

            /* carga el layout a la view del dialogo, luego obtiene los elementos del layout
                y declara los objetos correspondientes */
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_confirm, null)
            val textView = view.findViewById<TextView>(R.id.message)
            val positiveButton = view.findViewById<Button>(R.id.positiveButton)
            val negativeButton = view.findViewById<Button>(R.id.negativeButton)

            // carga el mensaje de confirmaci�n en el layout y luego carga la view al dialogo
            textView.text = message
            builder.setView(view)

            positiveButton.setOnClickListener {
                // agrega el cobro de comision a la cuenta del corresponsal
                context?.let { it1 -> admin.update(it1) }

                // añade (o elimina si es negativo) el monto de la transacci�n al saldo del usuario
                user.balance = logEntry.amount
                context?.let { it1 -> user.update(it1) }

                // añade la transaccion al registro de transacciones
                logEntry.addLogEntry(requireContext())
                TransactionSuccess().showNow(requireActivity().supportFragmentManager, "SUCCESS")
                dismiss()
            }

            negativeButton.setOnClickListener {
                TransactionFailed().show(requireActivity().supportFragmentManager, "FAIL")
                dismiss()
            }
            return builder.create()
        }

        init
        {
            // carga la informaci�n dek usuario activo desde la base de datos al nuevo objeto local de usuario
            user.loadData(context)
        }
    }

    // Dialogo para confirmar la actualizacion de informacio�n de la cuenta del corresponsal
    class ConfirmUpdateAdmin(var logEntry: LogEntry, var admin: Admin,
                             private var message: String?) : DialogFragment()
    {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
        {
            val builder = AlertDialog.Builder(activity)

            /* carga el layout a la view del dialogo, luego obtiene los elementos del layout
                y declara los objetos correspondientes */
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_confirm, null)
            val textView = view.findViewById<TextView>(R.id.message)
            val positiveButton = view.findViewById<Button>(R.id.positiveButton)
            val negativeButton = view.findViewById<Button>(R.id.negativeButton)

            // carga el mensaje de confirmaci�n en el layout y luego carga la view al dialogo
            textView.text = message
            builder.setView(view)

            positiveButton.setOnClickListener {
                /* actualiza la informaci�n del objeto local del admin a la base de datos
                   y añade una entrada a la tabla de transacciones */
                admin.update(requireContext())
                logEntry.addLogEntry(requireContext())
                TransactionSuccess().showNow(requireActivity().supportFragmentManager, "SUCCESS")
                dismiss()
            }

            negativeButton.setOnClickListener {
                TransactionFailed().show(requireActivity().supportFragmentManager, "FAIL")
                dismiss()
            }

            return builder.create()
        }
    }

    class TransactionSuccess : DialogFragment()
    {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
        {
            val builder = AlertDialog.Builder(activity)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_success, null)
            val neutralButton = view.findViewById<Button>(R.id.neutralButton)
            builder.setView(view)
            neutralButton.setOnClickListener { requireActivity().finish() }
            return builder.create()
        }
    }

    class TransactionFailed : DialogFragment()
    {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
        {
            val builder = AlertDialog.Builder(activity)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_fail, null)
            val neutralButton = view.findViewById<Button>(R.id.neutralButton)
            builder.setView(view)
            neutralButton.setOnClickListener { requireActivity().finish() }
            return builder.create()
        }
    }
}