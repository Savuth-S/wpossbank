package com.example.wpossbank.activities

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wpossbank.R
import com.example.wpossbank.fragments.Dialogs.ConfirmUpdateAdmin
import com.example.wpossbank.models.Admin
import com.example.wpossbank.models.LogEntry
import com.example.wpossbank.models.MakeMessages
import com.example.wpossbank.models.Validate
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

class AdminDepositActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var validate: Validate
    lateinit var messages: MakeMessages
    lateinit var admin: Admin
    lateinit var blurView: BlurView
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var depositInput: EditText
    lateinit var confirmButton: Button
    lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_deposit)
        context = this

        validate = Validate(context)
        messages = MakeMessages()
        admin = Admin()

        blurView = findViewById(R.id.blurView)
        blurBackground()

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        depositInput = findViewById(R.id.depositInput)
        confirmButton = findViewById(R.id.confirmButton)
        backArrow = findViewById(R.id.backArrow)

        confirmButton.setOnClickListener {
            //Deckara y verifica si los campos tienen la información correcta
            if (validate.adminLogin(emailInput, passwordInput)) {
                admin.balance = depositInput.text.toString().toInt()

                val logEntry = LogEntry()
                logEntry.type = "deposit"
                logEntry.amount = admin.balance
                logEntry.source = admin.email
                logEntry.activeUser = admin.getObjectId(context)

                ConfirmUpdateAdmin(logEntry, admin, messages.adminDeposit(context, depositInput))
                    .show(supportFragmentManager, "CONFIRM")
            } else {
                // Avisa al usuario si hay un campo con valores invalidos
                Toast.makeText(context, R.string.error_invalid_input, Toast.LENGTH_LONG).show()
            }
        }

        backArrow.setOnClickListener { finish() }
    }

    private fun blurBackground() {
        val radius = 20f
        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowBackground = decorView.background
        blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(true)
    }
}