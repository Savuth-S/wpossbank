package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.wpossbank.adaptadores.TransactionLogAdapter;
import com.example.wpossbank.database.Database;
import com.example.wpossbank.modelos.MakeMessages;
import com.example.wpossbank.modelos.SharedPreference;
import com.example.wpossbank.modelos.User;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {
    Context context;
    SharedPreference sp;
    Database db;
    MakeMessages messages;

    User user;

    TextView balanceText;

    TransactionLogAdapter adapter;
    RecyclerView recyclerView;

    ArrayList<String> entryDate, entryType, entrySource,entryValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        context = this;
        sp = new SharedPreference(context);
        db = new Database(context);
        messages = new MakeMessages(context);

        user = new User(context);

        user.loadData(user);
        balanceText = findViewById(R.id.textView2);
        recyclerView = findViewById((R.id.recyclerView));

        balanceText.setText(String.format("$%s", messages.separateNumberRight(
                Integer.toString(user.getBalance()), ".", 3)));

        entryDate = new ArrayList<>();
        entryType = new ArrayList<>();
        entryValue = new ArrayList<>();
        entrySource = new ArrayList<>();
        StoreDataInArrays();

        adapter = new TransactionLogAdapter(context, entryDate, entryType, entryValue, entrySource);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void StoreDataInArrays(){
        Cursor fetch = db.fetchData(sp.getActiveUser(),db.getTable("log"),db.getColumn("active user"));

        if (fetch.getCount() > 0){
            while (fetch.moveToNext()){
                entryDate.add(fetch.getString(2));
                Log.d("TAG",fetch.getString(2));
                entryType.add(fetch.getString(3));
                entryValue.add(fetch.getString(4));
                entrySource.add(fetch.getString(5));
            }
        }

        fetch.close();
    }
}