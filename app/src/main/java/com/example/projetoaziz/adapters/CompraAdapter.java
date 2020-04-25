package com.example.projetoaziz.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.models.Commodity;
import com.example.projetoaziz.models.ListaCommodities;
import com.example.projetoaziz.viewholders.CompraVendaViewHolder;

public class CompraAdapter extends RecyclerView.Adapter<CompraVendaViewHolder> {

    float diferenca = 0;
    private ListaCommodities listCompras;
    private int[] quantidades;
    private Context c;


    public CompraAdapter(ListaCommodities listCompras, Context c) {
        this.listCompras = listCompras;
        this.c = c;
        quantidades = new int[listCompras.getListaCommodities().size()];
        for (int i = 0; i < listCompras.getListaCommodities().size(); i++) {
            quantidades[i] = 0;
            listCompras.getListaCommodities().get(i).setQuantidade(0);
        }
    }

    @NonNull
    @Override
    public CompraVendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cotacao_operacao, parent, false);
        return new CompraVendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompraVendaViewHolder holder, final int position) {

        Commodity commodity = listCompras.getListaCommodities().get(position);
        holder.nome.setText(commodity.getNome());
        holder.preco.setText(String.format("%.2f", commodity.getValor()));
        holder.unidade.setText(commodity.getUnidade());
        String NOME = commodity.getNome();
        holder.quantidade.setText("0");

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
                if (s.toString().isEmpty()) {
                    quantidades[position] = 0;
                    listCompras.getListaCommodities().get(position).setQuantidade(0);
                } else {
                    Commodity nova = listCompras.getListaCommodities().get(position);
                    int quantidadeAntiga = nova.getQuantidade();
                    quantidades[position] = Integer.parseInt(s.toString());
                    nova.setQuantidade(Integer.parseInt(s.toString()));
                    float gasto = calcularGastoTotal();
                    float creditos = listCompras.getCreditos();
                    if (creditos > gasto) {
                        listCompras.getListaCommodities().remove(position);
                        listCompras.getListaCommodities().add(position, nova);
                        float diferenca = creditos - gasto;
                        Toast.makeText(c, "Saldo restante: " + diferenca, Toast.LENGTH_SHORT).show();

                    } else {
                        holder.quantidade.setText(Integer.toString(quantidadeAntiga));
                        quantidades[position] = quantidadeAntiga;
                    }


                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return listCompras.getListaCommodities().size();
    }


    public float calcularGastoTotal() {
        float value = 0;
        for (int i = 0; i < listCompras.getListaCommodities().size(); i++)
            value += quantidades[i] * listCompras.getListaCommodities().get(i).getValor();
        return value;
    }
}
