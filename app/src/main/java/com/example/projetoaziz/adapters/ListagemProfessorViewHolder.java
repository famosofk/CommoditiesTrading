package com.example.projetoaziz.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;

public class ListagemProfessorViewHolder extends RecyclerView.ViewHolder {

    public TextView nomeProfessor, sobrenomeProfessor, universidadeProfessor;

    public ListagemProfessorViewHolder(@NonNull View itemView) {
        super(itemView);

        this.nomeProfessor = itemView.findViewById(R.id.nomeListagemViewholder);
        this.sobrenomeProfessor = itemView.findViewById(R.id.sobrenomeListagemViewHolder);
        this.universidadeProfessor = itemView.findViewById(R.id.universidadeListagemViewholder);

    }

}
