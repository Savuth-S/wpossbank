package com.example.wpossbank.models

import com.example.wpossbank.database.Database
import android.content.Context

class User {
    var id = "0"
    var pin = "1234"
    var cc = "1234"
    var balance = 0
    var name = "Usuario Usuario"

    // Obtiene el valor unico del ID del objeto registrado en la base de datos
    fun getObjectId(context: Context): String {
        val db = Database(context)
        db.fetchData(
            cc,
            db.getTable("user"),
            db.getColumn("cc")
        ).use { fetch ->
            fetch.moveToFirst()
            return if (fetch.count > 0) {
                fetch.getString(1)
            } else {
                ""
            }
        }
    }

    // carga la informaci�n en la base de datos del usuario activo a el objeto usuario local
    fun loadData(context: Context) {
        val db = Database(context)
        val sp = SharedPreference(context)
        db.fetchData(
            sp.activeUser,
            db.getTable("user"),
            db.getColumn("object id")
        ).use { fetch ->
            fetch.moveToFirst()
            id = fetch.getString(0)
            cc = fetch.getString(2)
            pin = fetch.getString(3)
            balance = fetch.getString(4).toInt()
            name = fetch.getString(5)
        }
    }

    // carga la informaci�n en la base de datos con respecto al numero de cuenta de un usuario al objeto usuario local
    fun loadUser(context: Context, cc: String) {
        val db = Database(context)
        db.fetchData(cc, db.getTable("user"), db.getColumn("cc"))
            .use { fetch ->
            fetch.moveToFirst()
            id = fetch.getString(0)
            this.cc = fetch.getString(2)
            pin = fetch.getString(3)
            balance = fetch.getInt(4)
            name = fetch.getString(5)
        }
    }

    // llama a la funcion para actualizar los datos del usuario en la base de datos
    fun update(context: Context) {
        Database(context).updateUser(this)
    }

    // a;ade el usuario a la base de datos
    fun add(context: Context) {
        Database(context).addUser(this)
    }
}