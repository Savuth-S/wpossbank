package com.example.wpossbank.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.wpossbank.R
import com.example.wpossbank.models.Admin
import com.example.wpossbank.models.SharedPreference
import com.example.wpossbank.models.Validate

class AdminLoginActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var sp: SharedPreference
    lateinit var validate: Validate
    lateinit var admin: Admin
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var enterButton: Button
    lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)
        context = this
        sp = SharedPreference(context)

        validate = Validate(context)
        admin = Admin()

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        enterButton = findViewById(R.id.enterButton)
        backArrow = findViewById(R.id.backArrow)

        enterButton.setOnClickListener {
            if (validate.adminLogin(emailInput, passwordInput)) {
                admin.email = emailInput.text.toString()
                admin.password = passwordInput.text.toString()
                Log.d("ADMIN", "logged into admin panel")
                startActivity(Intent(context, AdminPanelActivity::class.java))
            } else {
                // Avisa al usuario si hay un campo con valores invalidos
                Toast.makeText(context, R.string.error_invalid_input, Toast.LENGTH_LONG).show()
            }
        }

        backArrow.setOnClickListener { finish() }
    }
}