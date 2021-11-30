package com.example.wpossbank.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.wpossbank.R
import com.example.wpossbank.database.Database
import com.example.wpossbank.fragments.Dialogs.ConfirmUpdateAdmin
import com.example.wpossbank.models.Admin
import com.example.wpossbank.models.LogEntry
import com.example.wpossbank.models.MakeMessages
import com.example.wpossbank.models.Validate
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

class AdminPanelActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var validate: Validate
    lateinit var db: Database
    lateinit var messages: MakeMessages
    lateinit var admin: Admin
    lateinit var blurView: BlurView
    private lateinit var balanceText: TextView
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var passwordConfirmInput: EditText
    private lateinit var updateButton: Button
    private lateinit var addMoneyButton: Button
    private lateinit var newUserButton: Button
    lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)
        context = this

        validate = Validate(context)
        db = Database(context)
        messages = MakeMessages()

        admin = Admin()
        admin.loadData(context)

        blurView = findViewById(R.id.blurView)
        blurBackground()

        balanceText = findViewById(R.id.textView2)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        passwordConfirmInput = findViewById(R.id.passwordConfirmInput)
        updateButton = findViewById(R.id.updateButton)
        addMoneyButton = findViewById(R.id.addMoneyButton)
        newUserButton = findViewById(R.id.newUserButton)
        backArrow = findViewById(R.id.backArrow)

        balanceText.text = String.format("$%s", messages.separateNumberRight(
            admin.balance.toString(), ".", 3))

        updateButton.setOnClickListener {
            val emailValidate = validate.email(emailInput)
            val passwordValidate = validate.password(passwordInput)
            if (!validate.isEmpty(passwordConfirmInput) &&
                passwordInput.text.toString() != passwordConfirmInput.text.toString()) {
                Log.d("CHANGE ADMIN", "The passwords do not match, " +
                            "password=" + passwordInput.text.toString() + " confirm=" +
                            passwordConfirmInput.text.toString())
                passwordConfirmInput.error = resources.getString(R.string.error_wrong)
            } else if (!validate.isEmpty(passwordConfirmInput) && emailValidate && passwordValidate) {
                admin.email = emailInput.text.toString()
                admin.password = passwordInput.text.toString()

                val logEntry = LogEntry()
                logEntry.type = "update"
                logEntry.source = admin.email
                logEntry.activeUser = admin.getObjectId(context)

                val message = context.getString(R.string.dialog_confirm_update_admin)
                ConfirmUpdateAdmin(logEntry, admin, message)
                    .show(supportFragmentManager, "Confirm")
            } else {
                // Avisa al usuario si hay un campo con valores invalidos
                Toast.makeText(context, R.string.error_invalid_input, Toast.LENGTH_LONG).show()
            }
        }

        addMoneyButton.setOnClickListener {
            startActivity(
                Intent(context, AdminDepositActivity::class.java)
            )
        }

        newUserButton.setOnClickListener {
            startActivity(
                Intent(context, NewUserActivity::class.java)
            )
        }

        backArrow.setOnClickListener { finish() }
    }

    public override fun onPause() {
        super.onPause()
        recreate()
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