package com.example.wpossbank.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.wpossbank.R
import com.example.wpossbank.models.*
import java.util.*

class Database(private val context: Context) : SQLiteOpenHelper(
    context, NAME, null, VERSION
) {
    private val sp: SharedPreference = SharedPreference(context)

    // metodo para acceder al nombre de las tablas de la base de datos fac�lmente
    fun getTable(tableName: String?): String {
        return when (tableName) {
            "admin" -> TABLE_ADMIN
            "user" -> TABLE_USER
            "log" -> TABLE_LOG
            else -> "No table by that name"
        }
    }

    // devuelve el nombre de las columnas para facil acceso
    fun getColumn(columnName: String?): String {
        return when (columnName) {
            "id" -> COLUMN_ID
            "object id" -> COLUMN_OBJECTID
            "active user" -> COLUMN_ACTIVEUSER
            "email" -> COLUMN_EMAIL
            "password" -> COLUMN_PASSWORD
            "balance" -> COLUMN_BALANCE
            "pin" -> COLUMN_PIN
            "cc" -> COLUMN_CC
            "name" -> COLUMN_NAME
            "date" -> COLUMN_DATE
            "type" -> COLUMN_TYPE
            "amount" -> COLUMN_AMMOUNT
            "source" -> COLUMN_SOURCE
            else -> "No column by that name"
        }
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val adminQuery = "CREATE TABLE " + TABLE_ADMIN +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_OBJECTID + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_BALANCE + " TEXT);"
        val userQuery = "CREATE TABLE " + TABLE_USER +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_OBJECTID + " TEXT, " +
                COLUMN_CC + " TEXT, " +
                COLUMN_PIN + " TEXT, " +
                COLUMN_BALANCE + " TEXT, " +
                COLUMN_NAME + " TEXT);"
        val logQuery = "CREATE TABLE " + TABLE_LOG +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ACTIVEUSER + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_AMMOUNT + " TEXT, " +
                COLUMN_SOURCE + " TEXT);"
        sqLiteDatabase.execSQL(adminQuery)
        sqLiteDatabase.execSQL(userQuery)
        sqLiteDatabase.execSQL(logQuery)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_ADMIN")
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_LOG")
    }

    // devuelve un cursor con la informaci�n de la fila si la hay
    fun fetchData(entry: String?, table: String?, column: String?): Cursor {
        val db = readableDatabase
        return db.query(
            table, arrayOf("*"),
            "$column=?", arrayOf(entry),
            null, null, null
        )
    }

    // escribe los valores por defecto del objeto Admin en la base de datos
    fun makeDefaultAdmin(admin: Admin) {
        writableDatabase.use { db ->
            val cv = ContentValues()
            cv.put(COLUMN_ID, admin.id)
            cv.put(COLUMN_OBJECTID, admin.toString().split("@").toTypedArray()[1])
            cv.put(COLUMN_EMAIL, admin.email)
            cv.put(COLUMN_PASSWORD, admin.password)
            cv.put(COLUMN_BALANCE, admin.balance)
            val result = db.insert(TABLE_ADMIN, null, cv)
            if (result == -1L) {
                Log.e(
                    "MAKE",
                    "Failed to add default admin to database, database=$db result=$result admin=$admin"
                )
                Toast.makeText(
                    context,
                    context.getString(R.string.error_default_admin),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Log.d("MAKE", "Default admin made successfully")
            }
        }
    }

    // actualiza la informaci�n de la cuenta del corresponsal en la base de datos
    fun updateAdmin(admin: Admin) {
        writableDatabase.use { db ->
            val cv = ContentValues()
            if (fetchData("1", TABLE_ADMIN, COLUMN_ID).count > 0) {
                cv.put(COLUMN_EMAIL, admin.email)
                cv.put(COLUMN_PASSWORD, admin.password)
                cv.put(COLUMN_BALANCE, admin.balance)
                val result = db.update(TABLE_ADMIN, cv, "_id=?", arrayOf("1")).toLong()
                if (result == -1L) {
                    Log.e(
                        "UPDATE",
                        "Failed to updated admin data, database=$db result=$result admin=$admin"
                    )
                } else {
                    Log.d("UPDATE", "Admin data updated successfully")
                }
            } else {
                Log.e("UPDATE", "Couldn't find admin data, making default admin instead")
                makeDefaultAdmin(Admin())

                // vuelve a intentar actualizar la informaci�n del admin despues de generar las entradas en la base de datos
                admin.update(context)
            }
        }
    }

    // añade un usuario a la base de datos
    fun addUser(user: User) {
        writableDatabase.use { db ->
            val cv = ContentValues()
            cv.put(COLUMN_OBJECTID, user.toString().split("@").toTypedArray()[1])
            cv.put(COLUMN_CC, user.cc)
            cv.put(COLUMN_PIN, user.pin)
            cv.put(COLUMN_BALANCE, user.balance)
            cv.put(COLUMN_NAME, user.name)
            val result = db.insert(TABLE_USER, null, cv)
            if (result == -1L) {
                Log.e("NEW USER", "Error while trying to write data to database, result=$result")
                Toast.makeText(
                    context,
                    context.getString(R.string.error_make_user),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_make_user_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // actualiza la informaci�n de el usuario activo en la ba se de datos
    fun updateUser(user: User) {
        writableDatabase.use { db ->
            val cv = ContentValues()
            val userData = fetchData(sp.activeUser, TABLE_USER, COLUMN_OBJECTID)
            if (userData.count > 0) {
                userData.moveToFirst()
                cv.put(COLUMN_CC, user.cc)
                cv.put(COLUMN_PIN, user.pin)
                cv.put(COLUMN_BALANCE, userData.getInt(4) + user.balance) // añade el dinero al saldo en la base de datos
                cv.put(COLUMN_NAME, user.name)
                val result = db.update(TABLE_USER, cv, "object_id=?", arrayOf(user.getObjectId(context))).toLong()
                if (result == -1L) {
                    Log.e(
                        "UPDATE",
                        "Failed to updated user data, database=$db result=$result admin=$user"
                    )
                } else {
                    Log.d("UPDATE", "Admin data updated successfully")
                }
            } else {
                Log.e("UPDATE", "Couldn't find user data, userData=$userData")
                Toast.makeText(
                    context,
                    context.getString(R.string.error_fetch_data),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // añade una entrada al registro de transacciones de la base de datos
    fun addLogEntry(logEntry: LogEntry) {
        writableDatabase.use { db ->
            val cv = ContentValues()
            val calendar = Calendar.getInstance()
            val messages = MakeMessages()

            // obtiene fecha y hora actual y crea una string con la fecha entera
            val dateArray = calendar.time.toString().split(" ").toTypedArray()
            val timeArray = dateArray[3].split(":").toTypedArray()
            val currentDate =
                dateArray[1] + " " + dateArray[2] + " " + dateArray[5] + "\n" + timeArray[0] + ":" + timeArray[1]
            Log.d("LOG ENTRY", "currentDate=$currentDate")

            // obtiene el tipo de acci�n de una tabla de tipos de acciones
            cv.put(COLUMN_ACTIVEUSER, logEntry.activeUser)
            cv.put(COLUMN_DATE, currentDate)
            cv.put(COLUMN_TYPE, logEntry.type)
            cv.put(
                COLUMN_AMMOUNT,
                "$" + messages.separateNumberRight(logEntry.amount.toString(), ".", 3)
            )
            cv.put(COLUMN_SOURCE, logEntry.source)
            val result = db.insert(TABLE_LOG, null, cv)
            if (result == -1L) {
                Log.e("LOG ENTRY", "Failed to write transaction entry to database")
                Toast.makeText(
                    context,
                    context.getString(R.string.error_write_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val NAME = "wpossbank.db"
        private const val VERSION = 1

        //Campos comunes
        private const val COLUMN_ID = "_id"
        private const val COLUMN_OBJECTID = "object_id"
        private const val COLUMN_BALANCE = "cuenta_balance"

        //Tabla de cuentas de administrador
        private const val TABLE_ADMIN = "administradores"
        private const val COLUMN_EMAIL = "cuenta_email"
        private const val COLUMN_PASSWORD = "cuenta_contraseña"

        //Tabla de cuentas de usuario
        private const val TABLE_USER = "usuarios"
        private const val COLUMN_CC = "usuario_cedula"
        private const val COLUMN_PIN = "usuario_pin"
        private const val COLUMN_NAME = "usuario_nombre"

        //Tabla de transacciones
        private const val TABLE_LOG = "transacciones"
        private const val COLUMN_ACTIVEUSER = "usuario_activo"
        private const val COLUMN_DATE = "tran_fecha"
        private const val COLUMN_TYPE = "tran_tipo"
        private const val COLUMN_AMMOUNT = "tran_monto"
        private const val COLUMN_SOURCE = "tran_origen"
    }

}