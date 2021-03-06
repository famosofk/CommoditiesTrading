package com.example.projetoaziz.viewholders;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;

public class CompraVendaViewHolder extends RecyclerView.ViewHolder {
    public TextView nome, preco, unidade;
    public ImageView icone;
    public EditText quantidade;

    public CompraVendaViewHolder(@NonNull View itemView) {
        super(itemView);
        unidade = itemView.findViewById(R.id.unidadeCompraVenda);
        nome = itemView.findViewById(R.id.nomeCompraVenda);
        preco = itemView.findViewById(R.id.precoCompraVenda);
        icone = itemView.findViewById(R.id.iconeCompraVenda);
        quantidade = itemView.findViewById(R.id.quantidadeCompraVenda);


    }
}
