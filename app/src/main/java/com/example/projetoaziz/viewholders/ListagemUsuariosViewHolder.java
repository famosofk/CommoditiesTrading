package com.example.projetoaziz.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;

public class ListagemUsuariosViewHolder extends RecyclerView.ViewHolder {
    public TextView name, money, position;

    public ListagemUsuariosViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.nomeUsuarioListagemChat);
        money = itemView.findViewById(R.id.patrimonioListagemChart);
        position = itemView.findViewById(R.id.positionUsuarioListagemChat);
    }
}
