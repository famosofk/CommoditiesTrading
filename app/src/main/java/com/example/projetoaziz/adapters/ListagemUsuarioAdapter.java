package com.example.projetoaziz.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.models.ListaCommodities;
import com.example.projetoaziz.viewholders.ListagemUsuariosViewHolder;

import java.util.List;

public class ListagemUsuarioAdapter extends RecyclerView.Adapter<ListagemUsuariosViewHolder> {

    List<ListaCommodities> list;
    Context context;

    public ListagemUsuarioAdapter(List<ListaCommodities> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ListagemUsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listagem_usuarios_charts, parent, false);
        return new ListagemUsuariosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListagemUsuariosViewHolder holder, int position) {
        ListaCommodities lista = list.get(position);
        String[] value = lista.getNome().split(" ");
        holder.name.setText(value[0] + " " + lista.getSobrenome().substring(0, 1));
        holder.position.setText(Integer.toString(position));
        holder.money.setText("R$ " + lista.getPatrimonio());
        if (lista.getPatrimonio() == 100000) {
            holder.money.setTextColor(Color.RED);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
