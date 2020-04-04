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
import com.example.projetoaziz.models.Usuario;

import java.util.List;

public class CompraAdapter extends RecyclerView.Adapter<CompraVendaViewHolder> {

    Commodity commodity;
    private List<Commodity> list, listCompras;
    private Context c;
    private Usuario u;
    private float maxValue;
    private float gastoTotal = 0;
    private int transacaoAnterior;

    public CompraAdapter(List<Commodity> list, Context c, Usuario u) {
        this.list = list;
        this.listCompras = list;
        this.c = c;
        this.u = u;
        maxValue = u.getCreditos();
    }

    @NonNull
    @Override
    public CompraVendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cotacao_operacao, parent, false);
        return new CompraVendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompraVendaViewHolder holder, final int position) {
        commodity = list.get(position);
        holder.nome.setText(commodity.getNome());
        holder.preco.setText(Float.toString(commodity.getValor()));
        String NOME = commodity.getNome();
        if (commodity.getValor() != 0) {
            int maximo = (int) (u.getCreditos() / commodity.getValor());

            holder.quantidade.setHint(Integer.toString(maximo));
        }
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
                Commodity nova = listCompras.get(position);
                float transacao = nova.getValor() * Integer.parseInt(s.toString());


                nova.setQuantidade(Integer.parseInt(s.toString()));
                listCompras.remove(position);
                listCompras.add(position, nova);


            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<Commodity> getListCompras() {
        return listCompras;
    }

    public void apagarTransacoes() {
        listCompras = list;

    }
}
