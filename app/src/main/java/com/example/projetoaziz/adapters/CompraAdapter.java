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
import com.example.projetoaziz.models.Usuario;

import java.util.List;

public class CompraAdapter extends RecyclerView.Adapter<CompraVendaViewHolder> {

    float diferenca = 0;
    private List<Commodity> listCompras;
    private Context c;
    private Usuario u;
    private int[] quantidades = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public CompraAdapter(List<Commodity> list, Context c, Usuario u) {
        this.listCompras = list;
        this.c = c;
        this.u = u;

    }

    @NonNull
    @Override
    public CompraVendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cotacao_operacao, parent, false);
        return new CompraVendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompraVendaViewHolder holder, final int position) {

        Commodity commodity = listCompras.get(position);
        holder.nome.setText(commodity.getNome());
        holder.preco.setText(Float.toString(commodity.getValor()));
        String NOME = commodity.getNome();
        if (commodity.getValor() != 0) {
            int maximo = (int) (u.getCreditos() / commodity.getValor());
            if (maximo > 0) {
                holder.quantidade.setHint(Integer.toString(maximo));
            }
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

                if (!s.toString().isEmpty()) {
                    Commodity nova = listCompras.get(position);
                    int quantidadeAntiga = nova.getQuantidade();
                    quantidades[position] = Integer.parseInt(s.toString());
                    nova.setQuantidade(Integer.parseInt(s.toString()));
                    float gasto = calcularGastoTotal();
                    float creditos = u.getCreditos();
                    if (creditos > gasto) {
                        listCompras.remove(position);
                        listCompras.add(position, nova);
                        float diferenca = creditos - gasto;
                        Toast.makeText(c, "Saldo restante: " + diferenca, Toast.LENGTH_SHORT).show();

                    } else {
                        holder.quantidade.setText(Integer.toString(quantidadeAntiga));
                        quantidades[position] = quantidadeAntiga;
                    }
                } else {
                    quantidades[position] = 0;
                    float diferenca = u.getCreditos() - calcularGastoTotal();
                    Toast.makeText(c, "Saldo restante: " + diferenca, Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return listCompras.size();
    }

    public List<Commodity> getListCompras() {
        return listCompras;
    }


    public float calcularGastoTotal() {
        float value = 0;
        for (int i = 0; i < 12; i++)
            value += quantidades[i] * listCompras.get(i).getValor();
        return value;
    }
}
