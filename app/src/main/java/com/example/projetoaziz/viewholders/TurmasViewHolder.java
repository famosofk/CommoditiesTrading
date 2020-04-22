package com.example.projetoaziz.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;

public class TurmasViewHolder extends RecyclerView.ViewHolder {

    public TextView nome;

    public TurmasViewHolder(@NonNull View itemView) {
        super(itemView);
        nome = itemView.findViewById(R.id.nomeTurmaListagem);
    }
}
