package com.example.projetoaziz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.models.Ordens;
import com.example.projetoaziz.viewholders.OrdensViewholder;

import java.util.ArrayList;
import java.util.List;

public class OrdensAdapter extends RecyclerView.Adapter<OrdensViewholder> {

    private List<Ordens> list;
    private Context context;

    public OrdensAdapter(List<Ordens> list, Context context) {
        List<Ordens> listaReversa = new ArrayList<>();
        for(Ordens ordens : list){
            listaReversa.add(0, ordens);
        }
        this.list = listaReversa;
        this.context = context;
    }

    @NonNull
    @Override
    public OrdensViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ordens_listagem, parent, false);
        return new OrdensViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdensViewholder holder, int position) {
        Ordens ordem = list.get(position);
        holder.tipo.setText(ordem.getTipo());
        holder.justificativa.setText(ordem.getJustificativa());
        String matriculaNome = ordem.getMatricula() + " - " + ordem.getNome();
        holder.matricula.setText(matriculaNome);
        holder.dados.setText(ordem.getDados());
        holder.data.setText(ordem.getData());
        if (ordem.getTipo().equals("Venda")) {
            holder.icone.setImageResource(R.drawable.money);
        }
        if (ordem.getMatricula().equals("Professor")) {
            holder.matricula.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }
    }

    @Override
    public int getItemCount() {
        return list.size() / 2;
    }
}
