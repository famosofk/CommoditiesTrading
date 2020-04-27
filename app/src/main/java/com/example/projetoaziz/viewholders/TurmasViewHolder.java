package com.example.projetoaziz.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;

public class TurmasViewHolder extends RecyclerView.ViewHolder {

    public TextView nome;
    public ImageView lock;

    public TurmasViewHolder(@NonNull View itemView) {
        super(itemView);
        nome = itemView.findViewById(R.id.nomeTurmaListagem);
        lock = itemView.findViewById(R.id.imageView17);
    }
}
