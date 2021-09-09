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
    private final String COLUMN_ID = "_id";
    private final String COLUMN_ACTIVEUSER = "usuario_activo";
    private final String COLUMN_BALANCE = "cuenta_balance";

    //Tabla de cuentas de administrador
    private final String TABLE_ADMIN = "administradores";
    private final String COLUMN_EMAIL = "cuenta_email";
    private final String COLUMN_PASSWORD = "cuenta_contraseña";

    //Tabla de cuentas de usuario
    private final String TABLE_USER = "usuarios";
    private final String COLUMN_CC = "usuario_cedula";
    private final String COLUMN_PIN = "usuario_pin";
    private final String COLUMN_NAME = "usuario_nombre";

    //Tabla de transacciones
    private final String TABLE_LOG = "transacciones";
    private final String COLUMN_DATE = "tran_fecha";
    private final String COLUMN_TIME = "tran_hora";
    private final String COLUMN_TYPE = "tran_tipo";
    private final String COLUMN_COST = "tran_monto";
    private final String COLUMN_ORIGIN = "tran_origen";

    public Database(@Nullable Context context){
        super(context, NAME, null, VERSION);
        this.context = context;
    }

    public String getTableAdmin() { return TABLE_ADMIN; }
    public String getTableUser() { return TABLE_USER; }
    public String getTableLog() { return TABLE_LOG; }

    public String getColumnId() { return COLUMN_ID; }
    public String getColumnActiveUser() { return COLUMN_ACTIVEUSER; }
    public String getColumnEmail() { return COLUMN_EMAIL; }
    public String getColumnPassword() { return COLUMN_PASSWORD; }
    public String getColumnBalance() { return COLUMN_BALANCE; }
    public String getColumnPin() { return COLUMN_PIN; }
    public String getColumnCc() { return COLUMN_CC; }
    public String getColumnName() { return COLUMN_NAME; }
    public String getColumnDate() { return COLUMN_DATE; }
    public String getColumnTime() { return COLUMN_TIME; }
    public String getColumnType() { return COLUMN_TYPE; }
    public String getColumnCost() { return COLUMN_COST; }
    public String getColumnOrigin() { return COLUMN_ORIGIN; }

    @Override
    public void onCreate(@NonNull SQLiteDatabase sqLiteDatabase) {
        String adminQuery =
            "CREATE TABLE "+ TABLE_ADMIN +
                    "(" + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_EMAIL +" TEXT, "+
                    COLUMN_PASSWORD +" TEXT, "+
                    COLUMN_BALANCE +" TEXT);";
        String userQuery =
            "CREATE TABLE "+ TABLE_USER +
                    "("+COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_CC +" TEXT, "+
                    COLUMN_PIN +" TEXT, "+
                    COLUMN_BALANCE +" TEXT, "+
                    COLUMN_NAME +" TEXT);";
        String logQuery =
            "CREATE TABLE "+ TABLE_LOG +
                    "("+COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_ACTIVEUSER +" TEXT, "+
                    COLUMN_DATE+" TEXT, "+
                    COLUMN_TIME +" TEXT, "+
                    COLUMN_TYPE +" TEXT, "+
                    COLUMN_COST +" TEXT, "+
                    COLUMN_ORIGIN +" TEXT);";


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

            cv.put(COLUMN_EMAIL, admin.getEmail());
            cv.put(COLUMN_PASSWORD, admin.getPassword());
            cv.put(COLUMN_PASSWORD, admin.getBalance());

            long result = db.insert(TABLE_ADMIN, null, cv);
            if (result == -1){
                Toast.makeText(context, "Se produjo un error al crear la cuenta por defecto de admin.", Toast.LENGTH_SHORT).show();
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
