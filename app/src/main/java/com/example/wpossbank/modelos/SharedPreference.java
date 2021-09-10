package com.example.wpossbank.modelos;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public SharedPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("bd_shared", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public String getActiveUser(){
        return sharedPreferences.getString("usuario", "no encontrado");
    }

    public void setActiveUser(String userObjectId){
        editor.putString("usuario", userObjectId);
        editor.apply();
    }
}
