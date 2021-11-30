package com.example.wpossbank.adapters

import com.example.wpossbank.R
import android.view.LayoutInflater
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import android.os.Build
import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext

class TransactionLogAdapter(
    private val context: Context,
    private val entryDate: List<String>,
    private val entryType: List<String>,
    private val entryValue: List<String>,
    private val entrySource: List<String>
) : RecyclerView.Adapter<TransactionLogAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, ViewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_log_entry, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Introduce el texto correspondiente de cada array en los elementos indicados
        holder.entryDateTxt.text = entryDate[position]
        holder.entryTypeTxt.text = entryType[position]
        holder.entryValueTxt.text = entryValue[position]
        holder.entrySourceTxt.text = entrySource[position]
        val valueString = entryValue[position].replace("[^0-9-]*".toRegex(), "")
        Log.d("TAG", valueString)
        if (valueString.toInt() >= 0) {
            holder.entryValueTxt.setTextColor(context.resources.getColor(R.color.value_add_money))
        } else {
            holder.entryValueTxt.setTextColor(context.resources.getColor(R.color.value_remove_money))
        }
    }

    override fun getItemCount(): Int {
        return entryDate.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Clase para cada entrada en el recyclerView
        var entryDateTxt: TextView
        var entryTypeTxt: TextView
        var entrySourceTxt: TextView
        var entryValueTxt: TextView

        init {
            entryDateTxt = itemView.findViewById(R.id.entryDateTxt)
            entryTypeTxt = itemView.findViewById(R.id.entryTypeTxt)
            entryValueTxt = itemView.findViewById(R.id.entryValueTxt)
            entrySourceTxt = itemView.findViewById(R.id.entrySourceTxt)
        }
    }
}