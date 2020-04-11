package com.example.projetoaziz.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;

public class PossesViewholder extends RecyclerView.ViewHolder {
    public TextView nome, preco, quantidade, unidade;
    public ImageView icone;

    public PossesViewholder(@NonNull View itemView) {
        super(itemView);
        unidade = itemView.findViewById(R.id.unidadePosse);
        nome = itemView.findViewById(R.id.nomePosse);
        preco = itemView.findViewById(R.id.valorPosse);
        quantidade = itemView.findViewById(R.id.quantidadePosse);
        icone = itemView.findViewById(R.id.iconePosse);

    }
}
