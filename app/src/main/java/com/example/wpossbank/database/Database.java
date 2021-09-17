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
        sp = new SharedPreference(context);
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

    public Cursor fetchData(String entry, String table, String column){
        SQLiteDatabase db = getReadableDatabase();
        return db.query(table, new String[]{"*"},
                         column+"=?", new String[]{entry},
                        null, null, null);
    }

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

    public void updateAdmin(@NonNull Admin admin){
        try (SQLiteDatabase db = getWritableDatabase()){
            ContentValues cv = new ContentValues();
            Cursor adminData = fetchData("1", TABLE_ADMIN, COLUMN_ID);

            Log.e("TAG DATABASE",admin.getEmail());

            if (adminData.getCount() > 0){
                adminData.moveToFirst();

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
                Log.e("UPDATE","Couldn't find admin data, making default admin instead, adminData="+adminData.toString());
                makeDefaultAdmin(new Admin());
                admin.update(context);
            }
        }
    }

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
                Toast.makeText(context, context.getString(R.string.error_make_user), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, context.getString(R.string.toast_make_user_success), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateUser(@NonNull User user){
        try (SQLiteDatabase db = getWritableDatabase()){
            ContentValues cv = new ContentValues();
            Cursor userData = fetchData(sp.getActiveUser(), TABLE_USER, COLUMN_OBJECTID);

            if (userData.getCount() > 0){
                userData.moveToFirst();

                cv.put(COLUMN_CC, user.getCc());
                cv.put(COLUMN_PIN, user.getPin());
                cv.put(COLUMN_BALANCE, userData.getInt(4)+user.getBalance());
                cv.put(COLUMN_NAME, user.getName());

                long result = db.update(TABLE_USER, cv, "object_id=?", new String[]{user.getObjectId()});
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

    public void newLogEntry(String type, String amount, String source){
        newLogEntry(type, amount, source, sp.getActiveUser());
    }

    public void newLogEntry(String type, String amount, String source, String activeUser){
        try(SQLiteDatabase db = getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            Calendar calendar = Calendar.getInstance();
            MakeMessages messages = new MakeMessages();

            String[] dateArray = calendar.getTime().toString().split(" ");
            String[] timeArray = dateArray[3].split(":");
            String currentDate = dateArray[1]+" "+dateArray[2]+" "+dateArray[5]+" "+timeArray[0]+":"+timeArray[1];

            Log.d("TAG",currentDate);

            String actionType;

            actionType = getLogEntryType(type);

            cv.put(COLUMN_ACTIVEUSER, activeUser);
            cv.put(COLUMN_DATE, currentDate);
            cv.put(COLUMN_TYPE, actionType);
            cv.put(COLUMN_AMMOUNT, "$"+messages.separateNumberRight(amount, ".", 3));
            cv.put(COLUMN_SOURCE, source);

            long result = db.insert(TABLE_LOG,null,cv);
            if (result == -1) {
                Toast.makeText(context, context.getString(R.string.error_write_data), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getLogEntryType(String type){
        String actionType;

        switch(type){
            case "card":
                actionType = context.getString(R.string.credit_card_sell);
                break;
            case "withdraw":
                actionType = context.getString(R.string.money_withdrawal);
                break;
            case "deposit":
                actionType = context.getString(R.string.money_deposit);
                break;
            case "transfer":
                actionType = context.getString(R.string.money_transfer);
                break;
            case "show balance":
                actionType = context.getString(R.string.show_account_balance);
                break;
            case "new user":
                actionType = context.getString(R.string.make_new_account);
                break;
            case "update":
                actionType = context.getString(R.string.update);
                break;
            default:
                actionType = context.getString(R.string.error_invalid);
                break;
        }
        return actionType;
    }
}
