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
import com.example.wpossbank.fragments.Dialogs.ConfirmUserTransferBalance
import com.example.wpossbank.models.Admin
import com.example.wpossbank.models.LogEntry
import com.example.wpossbank.models.MakeMessages
import com.example.wpossbank.models.Validate
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

class TransfersActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var validate: Validate
    lateinit var admin: Admin
    lateinit var messages: MakeMessages
    lateinit var blurView: BlurView
    lateinit var ccInput: EditText
    lateinit var pinInput: EditText
    private lateinit var pinConfirmInput: EditText
    private lateinit var ccTransferInput: EditText
    private lateinit var transferInput: EditText
    lateinit var confirmButton: Button
    lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfers)
        context = this

        validate = Validate(context)
        admin = Admin()
        messages = MakeMessages()

        blurView = findViewById(R.id.blurView)
        blurBackground()

        ccInput = findViewById(R.id.ccInput)
        pinInput = findViewById(R.id.pinInput)
        pinConfirmInput = findViewById(R.id.pinConfirmInput)
        ccTransferInput = findViewById(R.id.ccTransferInput)
        transferInput = findViewById(R.id.transferInput)
        confirmButton = findViewById(R.id.confirmButton)
        backArrow = findViewById(R.id.backArrow)

        confirmButton.setOnClickListener {
            //Deckara y verifica si los campos tienen la información correcta
            val ccValidate = validate.isNumber(ccInput) && validate.matchUserData(ccInput, "cc")
            val pinValidate = validate.pin(pinInput) && validate.matchUserData(pinInput, "pin")
            val ccTransferValidate = validate.isInRange(ccTransferInput, 10, 13) &&
                    validate.isInDatabase(ccTransferInput, "user", "cc") &&
                    validate.notMatchUserData(ccTransferInput, "cc")
            val transferValidate = validate.useBalance(transferInput)
            if (!validate.isEmpty(pinConfirmInput) &&
                pinInput.text.toString() != pinConfirmInput.text.toString()
            ) {
                Log.d(
                    "TRANSFER", "The pins do not match, " +
                            "pin=" + pinInput.text
                        .toString() + " confirm=" + pinConfirmInput.text.toString()
                )
                pinConfirmInput.error = resources.getString(R.string.error_wrong)
            } else if (ccValidate && pinValidate && ccTransferValidate && transferValidate) {
                admin.balance = admin.cost / 2
                val logEntry = LogEntry()
                logEntry.type = "transfer"
                logEntry.amount = transferInput.text.toString().toInt()
                logEntry.source = ccInput.text.toString()
                ConfirmUserTransferBalance(logEntry, admin, ccTransferInput.text.toString(),
                    messages.transfer(context, transferInput, ccTransferInput))
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