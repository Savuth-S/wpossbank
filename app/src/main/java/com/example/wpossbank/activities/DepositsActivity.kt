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
import com.example.wpossbank.fragments.Dialogs.ConfirmUserUpdateBalance
import com.example.wpossbank.models.*
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

class DepositsActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var validate: Validate
    lateinit var messages: MakeMessages
    lateinit var admin: Admin
    lateinit var user: User
    lateinit var blurView: BlurView
    lateinit var ccInput: EditText
    private lateinit var ccDepositInput: EditText
    private lateinit var depositInput: EditText
    lateinit var confirmButton: Button
    lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit)
        context = this

        validate = Validate(context)
        messages = MakeMessages()
        admin = Admin()
        user = User()

        blurView = findViewById(R.id.blurView)
        blurBackground()

        ccInput = findViewById(R.id.ccInput)
        ccDepositInput = findViewById(R.id.ccDepositInput)
        depositInput = findViewById(R.id.depositInput)
        confirmButton = findViewById(R.id.confirmButton)
        backArrow = findViewById(R.id.backArrow)

        user.loadData(context) //Carga la información del usuario desde la base de datos

        confirmButton.setOnClickListener {
            //Deckara y verifica si los campos tienen la información correcta
            val ccValidate = validate.isNumber(ccInput) && validate.matchUserData(ccInput, "cc")
            val ccDepositValidate =
                validate.isNumber(ccDepositInput) && validate.isInRange(ccDepositInput, 10, 13)
            val depositValidate = validate.isInRange(depositInput, 2, 7)
            if (ccValidate && ccDepositValidate && depositValidate) {
                val depositValue = depositInput.text.toString().toInt()
                admin.balance = admin.cost / 2

                val logEntry = LogEntry()
                logEntry.type = "deposit"
                logEntry.source = user.cc
                logEntry.amount = depositValue

                ConfirmUserUpdateBalance(context, logEntry, admin, messages.deposit(context, depositInput))
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