package com.example.wpossbank.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wpossbank.R
import com.example.wpossbank.fragments.Dialogs.ConfirmUserGetBalance
import com.example.wpossbank.models.*
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

class GetBalanceActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var validate: Validate
    lateinit var messages: MakeMessages
    lateinit var admin: Admin
    lateinit var user: User
    lateinit var blurView: BlurView
    lateinit var ccInput: EditText
    lateinit var pinInput: EditText
    private lateinit var pinConfirmInput: EditText
    lateinit var confirmButton: Button
    lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_balance)
        context = this

        validate = Validate(context)
        messages = MakeMessages()
        admin = Admin()
        user = User()

        blurView = findViewById(R.id.blurView)
        blurBackground()

        ccInput = findViewById(R.id.ccInput)
        pinInput = findViewById(R.id.pinInput)
        pinConfirmInput = findViewById(R.id.pinConfirmInput)
        confirmButton = findViewById(R.id.confirmButton)
        backArrow = findViewById(R.id.backArrow)

        user.loadData(context) //Carga la información del usuario desde la base de datos

        confirmButton.setOnClickListener {
            //Deckara y verifica si los campos tienen la información correcta
            val ccValidate = validate.isNumber(ccInput) && validate.matchUserData(ccInput, "cc")
            val pinValidate = validate.pin(pinInput) && validate.matchUserData(pinInput, "pin")
            val balanceValidate = validate.useBalance(ccInput, 0)
            if (!validate.isEmpty(pinConfirmInput) &&
                pinInput.text.toString() != pinConfirmInput.text.toString()
            ) {
                Log.d(
                    "WITHDRAW", "The pins do not match, " +
                            "pin=" + pinInput.text
                        .toString() + " confirm=" + pinConfirmInput.text.toString()
                )
                pinConfirmInput.error = resources.getString(R.string.error_wrong)
            } else if (!validate.isEmpty(pinConfirmInput) && ccValidate && pinValidate && balanceValidate) {
                admin.balance = admin.cost / 2

                val logEntry = LogEntry()
                logEntry.type = "show balance"
                logEntry.amount = admin.balance - admin.balance * 2
                logEntry.source = user.cc

                ConfirmUserGetBalance(context, logEntry, admin, messages.getBalance(context))
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