package com.example.wpossbank.adaptadores;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wpossbank.R;

import java.util.ArrayList;
import java.util.Scanner;

public class TransactionLogAdapter extends RecyclerView.Adapter<TransactionLogAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<String> entryDate, entryType,entryValue, entrySource;

    public TransactionLogAdapter(Context context, ArrayList<String> entryDate, ArrayList<String> entryType,
                                 ArrayList<String> entryValue, ArrayList<String> entrySource) {
        this.context = context;
        this.entryDate = entryDate;
        this.entryType = entryType;
        this.entryValue = entryValue;
        this.entrySource = entrySource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_log_entry, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Introduce el texto correspondiente de cada array en los elementos indicados
        holder.entryDateTxt.setText(String.valueOf(entryDate.get(position)));
        holder.entryTypeTxt.setText(String.valueOf(entryType.get(position)));
        holder.entryValueTxt.setText(String.valueOf(entryValue.get(position)));
        holder.entrySourceTxt.setText(String.valueOf(entrySource.get(position)));

        String valueString = String.valueOf(entryValue.get(position)).replaceAll("[^0-9-]*", "");
        Log.d("TAG", valueString);

        if (Integer.parseInt(valueString) >= 0){
            holder.entryValueTxt.setTextColor(context.getColor(R.color.value_add_money));
        }else{
            holder.entryValueTxt.setTextColor(context.getColor(R.color.value_remove_money));
        }
    }

    @Override
    public int getItemCount() {
        return entryDate.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //Clase para cada entrada en el recyclerView
        TextView entryDateTxt, entryTypeTxt, entrySourceTxt, entryValueTxt;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            entryDateTxt = itemView.findViewById(R.id.entryDateTxt);
            entryTypeTxt = itemView.findViewById(R.id.entryTypeTxt);
            entryValueTxt = itemView.findViewById(R.id.entryValueTxt);
            entrySourceTxt = itemView.findViewById(R.id.entrySourceTxt);
        }

    }

}
