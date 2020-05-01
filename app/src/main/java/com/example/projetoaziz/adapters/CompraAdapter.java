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
        int value = (int) (listCompras.getCreditos() / commodity.getValor()  );
        holder.quantidade.setHint(Integer.toString(value));
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
            case "Soja":
                holder.icone.setImageResource(R.drawable.soy);
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
                int i;
                for (i = 0; i < listCompras.getListaCommodities().size(); i++) {
                    if (listCompras.getListaCommodities().get(i).getNome().equals(holder.nome.getText())) {
                        break;
                    }
                }
                if (s.toString().isEmpty()) {
                    quantidades[i] = 0;
                    listCompras.getListaCommodities().get(i).setQuantidade(0);
                } else {
                    Commodity nova = listCompras.getListaCommodities().get(i);
                    int quantidadeAntiga = nova.getQuantidade();
                    quantidades[i] = Integer.parseInt(s.toString());
                    nova.setQuantidade(Integer.parseInt(s.toString()));
                    float gasto = calcularGastoTotal();
                    float creditos = listCompras.getCreditos();
                    if (creditos >= gasto) {
                        listCompras.getListaCommodities().set(i, nova);
                        float diferenca = creditos - gasto;
                        Toast.makeText(c, "Saldo restante: " + diferenca, Toast.LENGTH_SHORT).show();

                    } else {
                        holder.quantidade.setText(Integer.toString(quantidadeAntiga));
                        quantidades[i] = quantidadeAntiga;
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
