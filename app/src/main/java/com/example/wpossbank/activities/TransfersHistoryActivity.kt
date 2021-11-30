package com.example.wpossbank.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
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

class TransfersHistoryActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var sp: SharedPreference
    lateinit var db: Database
    lateinit var messages: MakeMessages
    lateinit var user: User
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
        setContentView(R.layout.activity_transfers_history)
        context = this
        sp = SharedPreference(context)
        db = Database(context)
        messages = MakeMessages()
        user = User()
        user.loadData(context)
        backArrow = findViewById(R.id.backArrow)
        recyclerView = findViewById(R.id.recyclerView)
        blurView = findViewById(R.id.blurView)
        blurBackground()
        entryDate = ArrayList()
        entryType = ArrayList()
        entryValue = ArrayList()
        entrySource = ArrayList()
        storeDataInArrays()

        context.resources

        adapter = TransactionLogAdapter(context, entryDate, entryType, entryValue, entrySource)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
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