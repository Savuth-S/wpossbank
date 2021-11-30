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
import com.example.wpossbank.models.*
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

class NewUserActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var sp: SharedPreference
    lateinit var validate: Validate
    lateinit var admin: Admin
    lateinit var user: User
    lateinit var blurView: BlurView
    private lateinit var nameInput: EditText
    private lateinit var lastnameInput: EditText
    lateinit var ccInput: EditText
    lateinit var pinInput: EditText
    private lateinit var pinConfirmInput: EditText
    private lateinit var enterButton: Button
    lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
        context = this

        sp = SharedPreference(context)
        validate = Validate(context)
        admin = Admin()
        user = User()

        blurView = findViewById(R.id.blurView)
        blurBackground()

        nameInput = findViewById(R.id.nameInput)
        lastnameInput = findViewById(R.id.lastnameInput)
        ccInput = findViewById(R.id.ccInput)
        pinInput = findViewById(R.id.pinInput)
        pinConfirmInput = findViewById(R.id.pinConfirmInput)
        //balanceInput = findViewById(R.id.balanceInput);
        enterButton = findViewById(R.id.enterButton)
        backArrow = findViewById(R.id.backArrow)

        enterButton.setOnClickListener {
            val ccValidate = (validate.isNotInDatabase(ccInput, "user", "cc")
                    && validate.isNumber(ccInput) && validate.isInRange(ccInput, 10, 13))
            val nameValidate = validate.name(nameInput)
            val lastnameValidate = validate.name(lastnameInput)
            val pinValidate = validate.pin(pinInput)
            //balanceValidate = validate.initialBalance(balanceInput);
            if (!validate.isEmpty(pinConfirmInput) &&
                pinInput.text.toString() != pinConfirmInput.text.toString()
            ) {
                Log.d(
                    "NEW USER", "The pins do not match, " +
                            "pin=" + pinInput.text
                        .toString() + " confirm=" + pinConfirmInput.text.toString()
                )
                pinConfirmInput.error = resources.getString(R.string.error_wrong)
            } else if (!validate.isEmpty(pinConfirmInput) && nameValidate && lastnameValidate && ccValidate && pinValidate) {
                user.name = nameInput.text.toString() + " " + lastnameInput.text.toString()

                user.cc = ccInput.text.toString()
                user.pin = pinInput.text.toString()
                //user.setBalance(Integer.parseInt(balanceInput.getText().toString()));
                Log.d("NEW USER", "user=" + user.toString().split("@").toTypedArray()[1])
                admin.balance = admin.cost * 5
                admin.update(context)
                user.add(context)
                sp.activeUser = user.getObjectId(context)
                val logEntry = LogEntry()
                logEntry.type = "new user"
                logEntry.source = user.cc
                logEntry.addLogEntry(context)
                finish()
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