package com.example.wpossbank.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wpossbank.R
import com.example.wpossbank.adapters.TransactionLogAdapter
import com.example.wpossbank.database.Database
import com.example.wpossbank.models.MakeMessages
import com.example.wpossbank.models.SharedPreference
import com.example.wpossbank.models.User
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import java.util.*

class UserProfileActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var sp: SharedPreference
    lateinit var db: Database
    lateinit var messages: MakeMessages
    lateinit var user: User
    private lateinit var balanceText: TextView
    private lateinit var addMoneyButton: Button
    lateinit var backArrow: ImageView
    lateinit var blurView: BlurView
    private lateinit var adapter: TransactionLogAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var entryDate: MutableList<String>
    private lateinit var entryType: MutableList<String>
    private lateinit var entrySource: MutableList<String>
    private lateinit var entryValue: MutableList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        context = this
        sp = SharedPreference(context)
        db = Database(context)
        messages = MakeMessages()
        user = User()
        user.loadData(context)
        balanceText = findViewById(R.id.textView2)
        addMoneyButton = findViewById(R.id.addMoneyButton)
        backArrow = findViewById(R.id.backArrow)
        recyclerView = findViewById(R.id.recyclerView)
        blurView = findViewById(R.id.blurView)
        blurBackground()
        balanceText.text = String.format("$%s", messages.separateNumberRight(
                    user.balance.toString(), ".", 3) )
        entryDate = ArrayList()
        entryType = ArrayList()
        entryValue = ArrayList()
        entrySource = ArrayList()
        storeDataInArrays()
        adapter = TransactionLogAdapter(context, entryDate, entryType, entryValue, entrySource)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        addMoneyButton.setOnClickListener{
            startActivity(
                Intent(context, DepositsActivity::class.java)
            )
        }
        backArrow.setOnClickListener { finish() }
    }

    private fun storeDataInArrays() {
        val fetch =
            db.fetchData(sp.activeUser, db.getTable("log"), db.getColumn("active user"))
        if (fetch.count > 0) {
            while (fetch.moveToNext()) {
                entryDate.add(fetch.getString(2))
                Log.d("TAG", fetch.getString(2))
                entryType.add(fetch.getString(3))
                entryValue.add(fetch.getString(4))
                entrySource.add(fetch.getString(5))
            }
        }
        fetch.close()
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