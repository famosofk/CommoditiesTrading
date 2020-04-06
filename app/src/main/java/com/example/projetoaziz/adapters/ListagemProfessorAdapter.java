package com.example.projetoaziz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.models.Professor;
import com.example.projetoaziz.viewholders.ListagemProfessorViewHolder;

import java.util.List;

public class ListagemProfessorAdapter extends RecyclerView.Adapter<ListagemProfessorViewHolder> {

    private List<Professor> list;
    private Context c;

    public ListagemProfessorAdapter(List<Professor> list, Context c) {
        this.list = list;
        this.c = c;
    }

    @NonNull
    @Override
    public ListagemProfessorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listagem_professor, parent, false);
        return new ListagemProfessorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListagemProfessorViewHolder holder, int position) {
        Professor p = list.get(position);
        holder.nomeProfessor.setText(p.getNome());
        holder.sobrenomeProfessor.setText(p.getSobrenome());
        holder.universidadeProfessor.setText(p.getUniversidade());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

