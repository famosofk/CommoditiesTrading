package com.example.projetoaziz.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;

public class OrdensViewholder extends RecyclerView.ViewHolder {
    public TextView tipo, matricula, data, justificativa, dados;
    public ImageView icone;

    public OrdensViewholder(@NonNull View itemView) {
        super(itemView);
        tipo = itemView.findViewById(R.id.tipoOrdem);
        matricula = itemView.findViewById(R.id.matriculaOrdem);
        data = itemView.findViewById(R.id.dataOrdem);
        justificativa = itemView.findViewById(R.id.justificativaOrdem);
        dados = itemView.findViewById(R.id.dadosOrdem);
        icone = itemView.findViewById(R.id.iconeOrdens);
    }
}
