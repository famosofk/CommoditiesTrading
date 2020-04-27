package com.example.projetoaziz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.helpers.Base64Handler;
import com.example.projetoaziz.models.Turma;
import com.example.projetoaziz.viewholders.TurmasViewHolder;

import java.util.List;

public class SelecionarTurmaAdapter extends RecyclerView.Adapter<TurmasViewHolder> {

    List<Turma> list;
    Context context;

    public SelecionarTurmaAdapter(List<Turma> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TurmasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listagem_turma, parent, false);
        return new TurmasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TurmasViewHolder holder, int position) {
        Turma turma = list.get(position);
        holder.nome.setText(Base64Handler.decodificarBase64(turma.getId()));
        if (turma.getRequerSenha()) {
            holder.lock.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
