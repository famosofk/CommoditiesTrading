package com.example.projetoaziz.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;

public class ListagemCotacoesViewHolder extends RecyclerView.ViewHolder {

    public TextView nome, preco;
    public ImageView icone;

    public ListagemCotacoesViewHolder(@NonNull View itemView) {
        super(itemView);

        nome = itemView.findViewById(R.id.nomeCotacao);
        preco = itemView.findViewById(R.id.precoCotacao);
        icone = itemView.findViewById(R.id.iconeCotacao);

    }
}
