package com.example.wpossbank.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wpossbank.Modelos.Admin;
import com.example.wpossbank.Modelos.User;

public class Database extends SQLiteOpenHelper {
    private final Context context;

    private static final String NAME = "wpossbank.db";
    private static final int VERSION = 1;

    //Campos comunes
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ACTIVEUSER = "usuario_activo";
    private static final String COLUMN_BALANCE = "cuenta_balance";

    //Tabla de cuentas de administrador
    private static final String TABLE_ADMIN = "administradores";
    private static final String COLUMN_EMAIL = "cuenta_email";
    private static final String COLUMN_PASSWORD = "cuenta_contraseña";

    //Tabla de cuentas de usuario
    private static final String TABLE_USER = "usuarios";
    private static final String COLUMN_CC = "usuario_cedula";
    private static final String COLUMN_PIN = "usuario_pin";
    private static final String COLUMN_NAME = "usuario_nombre";

    //Tabla de transacciones
    private static final String TABLE_LOG = "transacciones";
    private static final String COLUMN_DATE = "tran_fecha";
    private static final String COLUMN_TIME = "tran_hora";
    private static final String COLUMN_TYPE = "tran_tipo";
    private static final String COLUMN_COST = "tran_monto";
    private static final String COLUMN_SOURCE = "tran_origen";

    public Database(@Nullable Context context){
        super(context, NAME, null, VERSION);
        this.context = context;
    }

    public String getTable(String tableName){
        switch (tableName){
            case "admin":
                return TABLE_ADMIN;
            case "user":
                return TABLE_USER;
            case "log":
                return TABLE_LOG;
            default:
                return "No table by that name";
        }
    }

    public String getColumn(String columnName){
        switch (columnName){
            case "id":
                return COLUMN_ID;
            case "active user":
                return COLUMN_ACTIVEUSER;
            case "email":
                return COLUMN_EMAIL;
            case "password":
                return COLUMN_PASSWORD;
            case "balance":
                return COLUMN_BALANCE;
            case "pin":
                return COLUMN_PIN;
            case "cc":
                return COLUMN_CC;
            case "name":
                return COLUMN_NAME;
            case "date":
                return COLUMN_DATE;
            case "time":
                return COLUMN_TIME;
            case "type":
                return COLUMN_TYPE;
            case "cost":
                return COLUMN_COST;
            case "source":
                return COLUMN_SOURCE;
            default:
                return "No column by that name";
        }
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase sqLiteDatabase) {
        String adminQuery =
            "CREATE TABLE "+ TABLE_ADMIN +
                    "(" + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_EMAIL +" TEXT, "+
                    COLUMN_PASSWORD +" TEXT, "+
                    COLUMN_BALANCE +" INTEGER);";
        String userQuery =
            "CREATE TABLE "+ TABLE_USER +
                    "("+COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_CC +" TEXT, "+
                    COLUMN_PIN +" TEXT, "+
                    COLUMN_BALANCE +" INTEGER, "+
                    COLUMN_NAME +" TEXT);";
        String logQuery =
            "CREATE TABLE "+ TABLE_LOG +
                    "("+COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_ACTIVEUSER +" TEXT, "+
                    COLUMN_DATE+" TEXT, "+
                    COLUMN_TIME +" TEXT, "+
                    COLUMN_TYPE +" TEXT, "+
                    COLUMN_COST +" TEXT, "+
                    COLUMN_SOURCE +" TEXT);";


        sqLiteDatabase.execSQL(adminQuery);
        sqLiteDatabase.execSQL(userQuery);
        sqLiteDatabase.execSQL(logQuery);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_ADMIN);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_LOG);
    }

    public Cursor fetchData(String entry, String table, String column){
        SQLiteDatabase db = getReadableDatabase();
        return db.query(table, new String[]{"*"},
                         column+"=?", new String[]{entry},
                        null, null, null, "1");
    }

    public void makeDefaultAdmin(@NonNull Admin admin){
        try(SQLiteDatabase db = getWritableDatabase()){
            ContentValues cv = new ContentValues();

            cv.put(COLUMN_ID, admin.getId());
            cv.put(COLUMN_EMAIL, admin.getEmail());
            cv.put(COLUMN_PASSWORD, admin.getPassword());
            cv.put(COLUMN_PASSWORD, admin.getBalance());

            long result = db.insert(TABLE_ADMIN, null, cv);
            if (result == -1){
                Toast.makeText(context, "Se produjo un error al crear la cuenta por defecto de admin.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateAdmin(@NonNull Admin admin){
        try (SQLiteDatabase db = getWritableDatabase()){
            ContentValues cv = new ContentValues();
            Cursor adminData = fetchData("1", TABLE_ADMIN, COLUMN_ID);

            if (adminData.getCount() > 0){
                cv.put(COLUMN_EMAIL, admin.getEmail());
                cv.put(COLUMN_PASSWORD, admin.getPassword());
                Log.i("TAG",adminData.getString(2)+ admin.getBalance());
                cv.put(COLUMN_BALANCE, adminData.getInt(2)+admin.getBalance());

                long result = db.update(TABLE_ADMIN, cv, "_id=?", new String[]{admin.getId()});
                if (result == -1){
                    Toast.makeText(context, "Se produjo un error al actualizar la información del corresponsal.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Se proceso la petición con exito.", Toast.LENGTH_SHORT).show();
                }
            }else{
                makeDefaultAdmin(new Admin());
            }
        }
    }

    public void addUser(@NonNull User user){
        try(SQLiteDatabase db = getWritableDatabase()){
            ContentValues cv = new ContentValues();

            cv.put(COLUMN_CC, user.getCc());
            cv.put(COLUMN_PIN, user.getPin());
            cv.put(COLUMN_BALANCE, user.getBalance());
            cv.put(COLUMN_NAME, user.getName());

            long result = db.insert(TABLE_USER,null,cv);
            if (result == -1) {
                Toast.makeText(context, "Se produjo un error al crear la cuenta.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "¡Cuenta creada con exito!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
