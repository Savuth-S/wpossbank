package com.example.wpossbank.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wpossbank.R;

import java.util.ArrayList;

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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.entryDateTxt.setText(String.valueOf(entryDate.get(position)));
        holder.entryTypeTxt.setText(String.valueOf(entryType.get(position)));
        holder.entryValueTxt.setText(String.valueOf(entryValue.get(position)));
        holder.entrySourceTxt.setText(String.valueOf(entrySource.get(position)));
    }

    @Override
    public int getItemCount() {
        return entryDate.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

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