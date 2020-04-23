package com.example.projetoaziz.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.models.Commodity;
import com.example.projetoaziz.models.ListaCommodities;
import com.example.projetoaziz.viewholders.CompraVendaViewHolder;

public class GerenciarAcoesAdapter extends RecyclerView.Adapter<CompraVendaViewHolder> {

    ListaCommodities list;
    Context c;

    public GerenciarAcoesAdapter(ListaCommodities list, Context c) {
        this.list = list;
        this.c = c;
    }

    @NonNull
    @Override
    public CompraVendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cotacao_operacao, parent, false);
        return new CompraVendaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompraVendaViewHolder holder, final int position) {
        Commodity commodity = list.getListaCommodities().get(position);
        String NOME = commodity.getNome();
        String VALORRECUPERADO = String.format("%.2f", commodity.getValor());
        String VALOR = VALORRECUPERADO.replace(',', '.');
        holder.nome.setText(NOME);
        holder.unidade.setText(commodity.getUnidade());
        holder.preco.setText(VALOR);
        holder.quantidade.setText(VALOR);

        switch (NOME) {
            case "Algodão":
                holder.icone.setImageResource(R.drawable.cotton);
                break;
            case "Amendoim":
                holder.icone.setImageResource(R.drawable.peanut);
                break;
            case "Arroz":
                holder.icone.setImageResource(R.drawable.rice);
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
            case "Feijão":
                holder.icone.setImageResource(R.drawable.beans);
                break;
            case "Frango":
                holder.icone.setImageResource(R.drawable.chicken);
                break;
            case "Milho":
                holder.icone.setImageResource(R.drawable.corn);
                break;
            case "Soja":
                holder.icone.setImageResource(R.drawable.soy);
                break;
            case "Sorgo":
                holder.icone.setImageResource(R.drawable.sorghum);
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

        holder.quantidade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    Commodity nova = list.getListaCommodities().get(position);
                    nova.setValor(Float.parseFloat(s.toString()));
                    list.getListaCommodities().remove(position);
                    list.getListaCommodities().add(position, nova);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.getListaCommodities().size();
    }
}
