package com.example.wpossbank.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wpossbank.R
import com.example.wpossbank.models.User
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

class MainActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var user: User
    lateinit var blurView: BlurView
    private lateinit var userView: TextView
    private lateinit var cardPaymentsButton: Button
    private lateinit var withdrawalsButton: Button
    private lateinit var depositsButton: Button
    private lateinit var transfersButton: Button
    private lateinit var balanceButton: Button
    private lateinit var historyButton: Button
    private lateinit var logOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        user = User()
        blurView = findViewById(R.id.blurView)
        blurBackground()
        userView = findViewById(R.id.userView)
        cardPaymentsButton = findViewById(R.id.cardPaymentsButton)
        withdrawalsButton = findViewById(R.id.withdrawalsButton)
        depositsButton = findViewById(R.id.depositsButton)
        transfersButton = findViewById(R.id.transfersButton)
        balanceButton = findViewById(R.id.balanceButton)
        historyButton = findViewById(R.id.historyButton)
        logOutButton = findViewById(R.id.logOutButton)
        user.loadData(context)
        userView.text = getString(
            R.string.main_welcome_message,
                user.name.split(" ").toTypedArray()[0])

        cardPaymentsButton.setOnClickListener {
            startActivity(
                Intent(context, CardPaymentActivity::class.java)
            )
        }
        withdrawalsButton.setOnClickListener {
            startActivity(
                Intent(context, WithdrawalsActivity::class.java)
            )
        }
        depositsButton.setOnClickListener {
            startActivity(
                Intent(context, DepositsActivity::class.java)
            )
        }
        transfersButton.setOnClickListener {
            startActivity(
                Intent(context, TransfersActivity::class.java)
            )
        }
        balanceButton.setOnClickListener {
            startActivity(
                Intent(context, GetBalanceActivity::class.java)
            )
        }
        historyButton.setOnClickListener {
            startActivity(
                Intent(context, TransfersHistoryActivity::class.java)
            )
        }
        logOutButton.setOnClickListener { finish() }
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