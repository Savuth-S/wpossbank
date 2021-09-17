package com.example.wpossbank;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wpossbank.adaptadores.TransactionLogAdapter;
import com.example.wpossbank.database.Database;
import com.example.wpossbank.modelos.MakeMessages;
import com.example.wpossbank.modelos.SharedPreference;
import com.example.wpossbank.modelos.User;

import java.util.ArrayList;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class TransfersHistoryActivity extends AppCompatActivity {
    Context context;
    SharedPreference sp;
    Database db;
    MakeMessages messages;

    User user;

    ImageView backArrow;

    BlurView blurView;

    TransactionLogAdapter adapter;
    RecyclerView recyclerView;

    ArrayList<String> entryDate, entryType, entrySource,entryValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfers_history);

        context = this;
        sp = new SharedPreference(context);
        db = new Database(context);
        messages = new MakeMessages();

        user = new User(context);

        user.loadData();
        backArrow = findViewById(R.id.backArrow);

        recyclerView = findViewById((R.id.recyclerView));

        blurView = findViewById(R.id.blurView);
        blurBackground();

        entryDate = new ArrayList<>();
        entryType = new ArrayList<>();
        entryValue = new ArrayList<>();
        entrySource = new ArrayList<>();
        StoreDataInArrays();

        adapter = new TransactionLogAdapter(context, entryDate, entryType, entryValue, entrySource);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        backArrow.setOnClickListener( goBack -> finish());
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

    private void blurBackground(){
        float radius = 20f;

        View decorView = getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();

        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setBlurAutoUpdate(true)
                .setHasFixedTransformationMatrix(true);
    }
}