package com.example.wpossbank.models

import android.content.SharedPreferences
import android.content.Context

class SharedPreference(var context: Context) {

    var sharedPreferences: SharedPreferences = context.getSharedPreferences("bd_shared", Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = sharedPreferences.edit()

    var activeUser: String?
        get() = sharedPreferences.getString("usuario", "no encontrado")
        set(userObjectId) {
            editor.putString("usuario", userObjectId)
            editor.apply()
        }

}