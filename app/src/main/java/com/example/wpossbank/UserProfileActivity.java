package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wpossbank.modelos.User;

public class UserProfileActivity extends AppCompatActivity {
    Context context;
    User user;

    TextView balanceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        context = this;
        user = new User(context);

        user.loadData(user);
        balanceText = findViewById(R.id.textView2);

        balanceText.setText(Integer.toString(user.getBalance()));
    }
}