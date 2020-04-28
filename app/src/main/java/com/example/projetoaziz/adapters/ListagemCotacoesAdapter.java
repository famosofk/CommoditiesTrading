package com.example.projetoaziz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.models.Commodity;
import com.example.projetoaziz.viewholders.ListagemCotacoesViewHolder;

import java.util.List;

public class ListagemCotacoesAdapter extends RecyclerView.Adapter<ListagemCotacoesViewHolder> {

    private List<Commodity> list;
    private Context c;

    public ListagemCotacoesAdapter(List<Commodity> list, Context c) {
        this.list = list;
        this.c = c;
    }

    @NonNull
    @Override
    public ListagemCotacoesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listagem_cotacao, parent, false);

        return new ListagemCotacoesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListagemCotacoesViewHolder holder, int position) {
        Commodity commodity = list.get(position);
        String NOME = commodity.getNome();
        holder.nome.setText(NOME);
        holder.unidade.setText(commodity.getUnidade());
        holder.preco.setText(String.format("%.2f", commodity.getValor()));

        switch (NOME) {
            case "Algodão":
                holder.icone.setImageResource(R.drawable.cotton);
                break;
            case "Abóbora":
                holder.icone.setImageResource(R.drawable.pumpkin);
                break;
            case "Arroz":
                holder.icone.setImageResource(R.drawable.rice);
                break;
            case "Alface":
                holder.icone.setImageResource(R.drawable.lettuce);
                break;
            case "Banana":
                holder.icone.setImageResource(R.drawable.banana);
                break;
            case "Batata":
                holder.icone.setImageResource(R.drawable.potato);
                break;
            case "Bezerro":
                holder.icone.setImageResource(R.drawable.ox);
                break;
            case "Boi gordo":
                holder.icone.setImageResource(R.drawable.cow);
                break;
            case "Café":
                holder.icone.setImageResource(R.drawable.coffee);
                break;
            case "Cebola":
                holder.icone.setImageResource(R.drawable.onion);
                break;
            case "Cenoura":
                holder.icone.setImageResource(R.drawable.carrot);
                break;
            case "Feijão":
                holder.icone.setImageResource(R.drawable.beans);
                break;
            case "Frango":
                holder.icone.setImageResource(R.drawable.chicken);
                break;
            case "Goiaba":
                holder.icone.setImageResource(R.drawable.guava);
                break;
            case "Laranja":
                holder.icone.setImageResource(R.drawable.orange);
                break;
            case "Leite":
                holder.icone.setImageResource(R.drawable.milk);
                break;
            case "Limão":
                holder.icone.setImageResource(R.drawable.lemon);
                break;
            case "Milho":
                holder.icone.setImageResource(R.drawable.corn);
                break;
            case "Mandioca":
                holder.icone.setImageResource(R.drawable.mandioca);
                break;
            case "Ovos":
                holder.icone.setImageResource(R.drawable.egg);
                break;
            case "Tomate":
                holder.icone.setImageResource(R.drawable.tomato);
                break;
            case "Trigo":
                holder.icone.setImageResource(R.drawable.wheatdraw);
                break;
            case "Açúcar":
                holder.icone.setImageResource(R.drawable.sugar);
                break;
            case "Suíno":
                holder.icone.setImageResource(R.drawable.pig);
                break;

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
