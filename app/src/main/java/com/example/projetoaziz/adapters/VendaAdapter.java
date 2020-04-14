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
import com.example.projetoaziz.viewholders.CompraVendaViewHolder;

import java.util.List;

public class VendaAdapter extends RecyclerView.Adapter<CompraVendaViewHolder> {

    private List<Commodity> listCompras;
    private Context c;
    private Usuario u;
    private int maximo = 0;
    private int[] quantidades, originais;

    public VendaAdapter(List<Commodity> listCompras, Context c, Usuario u) {
        this.listCompras = listCompras;
        this.c = c;
        this.u = u;
        originais = new int[listCompras.size()];
        quantidades = new int[listCompras.size()];
        for (int i = 0; i < listCompras.size(); i++) {
            originais[i] = listCompras.get(i).getQuantidade();
            quantidades[i] = 0;
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
        Commodity commodity = listCompras.get(position);

        holder.nome.setText(commodity.getNome());
        holder.preco.setText(Float.toString(commodity.getValor()));
        holder.unidade.setText(commodity.getUnidade());
        String NOME = commodity.getNome();
        holder.quantidade.setHint(Integer.toString(commodity.getQuantidade()));
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
                    Commodity nova = listCompras.get(position);
                    maximo = Integer.parseInt(holder.quantidade.getHint().toString());
                    int valorNovo = Integer.parseInt(s.toString());

                    nova.setQuantidade(originais[position] - valorNovo);
                    if (valorNovo <= maximo) {
                        listCompras.remove(position);
                        listCompras.add(position, nova);
                        quantidades[position] = valorNovo;
                        Toast.makeText(c, "Valor da venda: " + calcularLucroTotal(), Toast.LENGTH_SHORT).show();
                    } else {
                        holder.quantidade.setText(Integer.toString(maximo));
                        quantidades[position] = maximo;
                    }
                } else {
                    quantidades[position] = 0;
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return listCompras.size();
    }

    public int[] getOriginais() {
        return originais;
    }


    public float calcularLucroTotal() {
        float value = 0;
        for (int i = 0; i < listCompras.size(); i++)
            value += quantidades[i] * listCompras.get(i).getValor();
        return value;
    }


}
