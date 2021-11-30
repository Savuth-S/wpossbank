package com.example.wpossbank.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wpossbank.R
import com.example.wpossbank.models.SharedPreference
import com.example.wpossbank.models.User
import com.example.wpossbank.models.Validate

class LoginActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var sp: SharedPreference
    lateinit var validate: Validate
    lateinit var user: User
    lateinit var ccInput: EditText
    lateinit var pinInput: EditText
    private lateinit var enterButton: Button
    lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        context = this
        sp = SharedPreference(context)

        validate = Validate(context)
        user = User()

        ccInput = findViewById(R.id.ccInput)
        pinInput = findViewById(R.id.pinInput)
        enterButton = findViewById(R.id.enterButton)
        backArrow = findViewById(R.id.backArrow)

        enterButton.setOnClickListener {
            if (validate.login(ccInput, pinInput)) {
                user.cc = ccInput.text.toString()
                user.pin = pinInput.text.toString()
                Log.d("LOGIN", "user=" + user.getObjectId(context))
                sp.activeUser = user.getObjectId(context)
                startActivity(Intent(context, MainActivity::class.java))
            } else {
                // Avisa al usuario si hay un campo con valores invalidos
                Toast.makeText(context, R.string.error_invalid_input, Toast.LENGTH_LONG).show()
            }
        }
        backArrow.setOnClickListener { finish() }
    }
}