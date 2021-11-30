package com.example.wpossbank.models

import android.content.Context
import android.util.Log
import com.example.wpossbank.database.Database

class Admin{
    var id = "1"
    var email = "carlos@wposs.com"
    var password = "123456"
    var balance = 0
    var cost = 2000


    // Obtiene el valor unico del ID del objeto registrado en la base de datos
    fun getObjectId(context: Context): String {
        val db = Database(context)
        db.fetchData(
            "1",
            db.getTable("admin"),
            db.getColumn("id")
        ).use { fetch ->
            fetch.moveToFirst()
            return if (fetch.count > 0) {
                fetch.getString(1)
            } else {
                ""
            }
        }
    }

    // actualiza la informaci�n de la base de datos con la informaci�n del objeto local admin
    fun update(context: Context) {
        val db = Database(context)
        val existingAdmin = Admin()

        // carga la informaci�n en la base de datos a un nuevo objeto admin
        existingAdmin.loadData(context)

        // compara la informaci�n de la base de datos con la informacion local del admin y la actualiza
        if (email != existingAdmin.email) {
            Log.d(
                "UPDATE",
                "New email found for admin, current email=" + existingAdmin.email + " new email=" + email
            )
            existingAdmin.email = email
        }
        if (password != existingAdmin.password) {
            Log.d(
                "UPDATE",
                "New password found for admin, current password=" + existingAdmin.password + " new password=" + password
            )
            existingAdmin.password = password
        }

        // añade el nuevo saldo al saldo del corresponsal registrado en la base de datos
        existingAdmin.balance += balance
        db.updateAdmin(existingAdmin)
    }

    // carga la informaci�n del admin de la base de datos al objeto admin actual
    fun loadData(context: Context) {
        val db = Database(context)
        db.fetchData(
            "1",
            db.getTable("admin"),
            db.getColumn("id")
        ).use { fetch ->
            if (fetch.count > 0) {
                fetch.moveToFirst()
                id = fetch.getString(0)
                email = fetch.getString(2)
                password = fetch.getString(3)
                balance = fetch.getInt(4)
            } else {
                // escribe los valores por defecto si no encuentra datos en la base de datos
                Log.e("UPDATE", "Couldn't find existing admin data, setting default values instead")
                val admin = Admin()
                id = admin.id
                email = admin.email
                password = admin.password
                balance = admin.balance
            }
        }
    }
}