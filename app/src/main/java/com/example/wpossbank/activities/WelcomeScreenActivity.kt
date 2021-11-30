package com.example.wpossbank.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.wpossbank.R
import com.example.wpossbank.database.Database
import com.example.wpossbank.models.Admin

class WelcomeScreenActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var admin: Admin
    private lateinit var loginButton: Button
    private lateinit var adminButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)
        context = this

        admin = Admin()
        makeDefaultAdmin()

        loginButton = findViewById(R.id.loginButton)
        adminButton = findViewById(R.id.adminButton)

        loginButton.setOnClickListener {
            startActivity(
                Intent(context, LoginActivity::class.java)
            )
        }

        adminButton.setOnClickListener {
            startActivity(
                Intent(context, AdminLoginActivity::class.java)
            )
        }
    }

    private fun makeDefaultAdmin() {
        val db = Database(context)
        if (db.fetchData("1", db.getTable("admin"), db.getColumn("id")).count <= 0) {
            Log.e("LOGIN", "Couldn't find admin data, making default admin instead")
            db.makeDefaultAdmin(admin)
        }
    }
}