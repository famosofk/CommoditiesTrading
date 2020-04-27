package com.example.projetoaziz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.helpers.Base64Handler;
import com.example.projetoaziz.viewholders.TurmasViewHolder;

import java.util.List;

public class TurmaAdapter extends RecyclerView.Adapter<TurmasViewHolder> {

    private List<String> list;
    private Context context;

    public TurmaAdapter(List<String> list, Context context) {
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
        String TURMA = Base64Handler.decodificarBase64(list.get(position));
        holder.nome.setText(TURMA);
        holder.lock.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
