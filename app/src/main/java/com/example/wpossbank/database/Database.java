package com.example.wpossbank.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.wpossbank.fragments.Dialogs;
import com.example.wpossbank.modelos.Admin;
import com.example.wpossbank.modelos.LogEntry;
import com.example.wpossbank.modelos.MakeMessages;
import com.example.wpossbank.modelos.SharedPreference;
import com.example.wpossbank.modelos.User;
import com.example.wpossbank.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Database extends SQLiteOpenHelper {
    private final Context context;
    private final SharedPreference sp;

    private static final String NAME = "wpossbank.db";
    private static final int VERSION = 1;

    //Campos comunes
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_OBJECTID = "object_id";
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
    private static final String COLUMN_ACTIVEUSER = "usuario_activo";
    private static final String COLUMN_DATE = "tran_fecha";
    private static final String COLUMN_TYPE = "tran_tipo";
    private static final String COLUMN_AMMOUNT = "tran_monto";
    private static final String COLUMN_SOURCE = "tran_origen";

    public Database(@NonNull Context context){
        super(context, NAME, null, VERSION);

        this.context = context;
        sp = new SharedPreference(context);
    }

    // metodo para acceder al nombre de las tablas de la base de datos fac�lmente
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

    // devuelve el nombre de las columnas para facil acceso
    public String getColumn(String columnName){
        switch (columnName){
            case "id":
                return COLUMN_ID;
            case "object id":
                return COLUMN_OBJECTID;
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
            case "type":
                return COLUMN_TYPE;
            case "amount":
                return COLUMN_AMMOUNT;
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
                    COLUMN_OBJECTID +" TEXT, "+
                    COLUMN_EMAIL +" TEXT, "+
                    COLUMN_PASSWORD +" TEXT, "+
                    COLUMN_BALANCE +" TEXT);";
        String userQuery =
            "CREATE TABLE "+ TABLE_USER +
                    "("+COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_OBJECTID +" TEXT, "+
                    COLUMN_CC +" TEXT, "+
                    COLUMN_PIN +" TEXT, "+
                    COLUMN_BALANCE +" TEXT, "+
                    COLUMN_NAME +" TEXT);";
        String logQuery =
            "CREATE TABLE "+ TABLE_LOG +
                    "("+COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_ACTIVEUSER +" TEXT, "+
                    COLUMN_DATE+" TEXT, "+
                    COLUMN_TYPE +" TEXT, "+
                    COLUMN_AMMOUNT +" TEXT, "+
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


    // devuelve un cursor con la informaci�n de la fila si la hay
    public Cursor fetchData(String entry, String table, String column){
        SQLiteDatabase db = getReadableDatabase();
        return db.query(table, new String[]{"*"},
                         column+"=?", new String[]{entry},
                        null, null, null);
    }

    // escribe los valores por defecto del objeto Admin en la base de datos
    public void makeDefaultAdmin(@NonNull Admin admin){
        try(SQLiteDatabase db = getWritableDatabase()){
            ContentValues cv = new ContentValues();

            cv.put(COLUMN_ID, admin.getId());
            cv.put(COLUMN_OBJECTID, admin.toString().split("@")[1]);
            cv.put(COLUMN_EMAIL, admin.getEmail());
            cv.put(COLUMN_PASSWORD, admin.getPassword());
            cv.put(COLUMN_BALANCE, admin.getBalance());

            long result = db.insert(TABLE_ADMIN, null, cv);
            if (result == -1){
                Log.e("MAKE","Failed to add default admin to database, database="+db.toString() +" result="+result +" admin="+admin.toString());
                Toast.makeText(context, context.getString(R.string.error_default_admin), Toast.LENGTH_LONG).show();
            }else{
                Log.d("MAKE","Default admin made successfully");
            }
        }
    }

    // actualiza la informaci�n de la cuenta del corresponsal en la base de datos
    public void updateAdmin(@NonNull Admin admin){
        try (SQLiteDatabase db = getWritableDatabase()){
            ContentValues cv = new ContentValues();

            if (fetchData("1", TABLE_ADMIN, COLUMN_ID).getCount() > 0){

                cv.put(COLUMN_EMAIL, admin.getEmail());
                cv.put(COLUMN_PASSWORD, admin.getPassword());
                cv.put(COLUMN_BALANCE, admin.getBalance());

                long result = db.update(TABLE_ADMIN, cv, "_id=?", new String[]{"1"});
                if (result == -1){
                    Log.e("UPDATE","Failed to updated admin data, database="+db.toString() +" result="+result +" admin="+admin.toString());
                }else{
                    Log.d("UPDATE","Admin data updated successfully");
                }
            }else{
                Log.e("UPDATE","Couldn't find admin data, making default admin instead");
                makeDefaultAdmin(new Admin());

                // vuelve a intentar actualizar la informaci�n del admin despues de generar las entradas en la base de datos
                admin.update(context);
            }
        }
    }

    // añade un usuario a la base de datos
    public void addUser(@NonNull User user){
        try(SQLiteDatabase db = getWritableDatabase()){
            ContentValues cv = new ContentValues();

            cv.put(COLUMN_OBJECTID, user.toString().split("@")[1]);
            cv.put(COLUMN_CC, user.getCc());
            cv.put(COLUMN_PIN, user.getPin());
            cv.put(COLUMN_BALANCE, user.getBalance());
            cv.put(COLUMN_NAME, user.getName());

            long result = db.insert(TABLE_USER,null,cv);
            if (result == -1) {
                Log.e("NEW USER","Error while trying to write data to database, result="+result);
                Toast.makeText(context, context.getString(R.string.error_make_user), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, context.getString(R.string.toast_make_user_success), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // actualiza la informaci�n de el usuario activo en la ba se de datos
    public void updateUser(@NonNull User user){
        try (SQLiteDatabase db = getWritableDatabase()){
            ContentValues cv = new ContentValues();
            Cursor userData = fetchData(sp.getActiveUser(), TABLE_USER, COLUMN_OBJECTID);

            if (userData.getCount() > 0){
                userData.moveToFirst();

                cv.put(COLUMN_CC, user.getCc());
                cv.put(COLUMN_PIN, user.getPin());
                cv.put(COLUMN_BALANCE, userData.getInt(4)+user.getBalance()); // añade el dinero al saldo en la base de datos
                cv.put(COLUMN_NAME, user.getName());

                long result = db.update(TABLE_USER, cv, "object_id=?", new String[]{user.getObjectId(context)});
                if (result == -1){
                    Log.e("UPDATE","Failed to updated user data, database="+db.toString() +" result="+result +" admin="+user.toString());
                }else{
                    Log.d("UPDATE","Admin data updated successfully");
                }
            }else{
                Log.e("UPDATE","Couldn't find user data, userData="+userData.toString());
                Toast.makeText(context,context.getString(R.string.error_fetch_data),Toast.LENGTH_LONG).show();
            }
        }
    }

    // añade una entrada al registro de transacciones de la base de datos
    public void addLogEntry(LogEntry logEntry){
        try(SQLiteDatabase db = getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            Calendar calendar = Calendar.getInstance();
            MakeMessages messages = new MakeMessages();

            // obtiene fecha y hora actual y crea una string con la fecha entera
            String[] dateArray = calendar.getTime().toString().split(" ");
            String[] timeArray = dateArray[3].split(":");
            String currentDate = dateArray[1]+" "+dateArray[2]+" "+dateArray[5]+" "+timeArray[0]+":"+timeArray[1];

            Log.d("LOG ENTRY","currentDate="+currentDate);

            // obtiene el tipo de acci�n de una tabla de tipos de acciones

            cv.put(COLUMN_ACTIVEUSER, logEntry.getActiveUser());
            cv.put(COLUMN_DATE, currentDate);
            cv.put(COLUMN_TYPE, logEntry.getType());
            cv.put(COLUMN_AMMOUNT, "$"+messages.separateNumberRight(String.valueOf(logEntry.getAmount()), ".", 3));
            cv.put(COLUMN_SOURCE, logEntry.getSource());

            long result = db.insert(TABLE_LOG,null,cv);
            if (result == -1) {
                Log.e("LOG ENTRY","Failed to write transaction entry to database");
                Toast.makeText(context, context.getString(R.string.error_write_data), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
