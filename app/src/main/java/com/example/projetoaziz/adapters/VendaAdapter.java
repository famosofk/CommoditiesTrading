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

public class VendaAdapter extends RecyclerView.Adapter<CompraVendaViewHolder> {

    private ListaCommodities listCompras;
    private Context c;

    private int maximo = 0;
    private int[] quantidades, originais;

    public VendaAdapter(ListaCommodities listCompras, Context c) {
        this.listCompras = listCompras;
        this.c = c;

        originais = new int[listCompras.getListaCommodities().size()];
        quantidades = new int[listCompras.getListaCommodities().size()];
        for (int i = 0; i < listCompras.getListaCommodities().size(); i++) {
            originais[i] = listCompras.getListaCommodities().get(i).getQuantidade();
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
        Commodity commodity = listCompras.getListaCommodities().get(position);

        holder.nome.setText(commodity.getNome());
        holder.preco.setText(Float.toString(commodity.getValor()));
        holder.unidade.setText(commodity.getUnidade());
        String NOME = commodity.getNome();
        holder.quantidade.setHint(Integer.toString(commodity.getQuantidade()));
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

            case "Soja":
                holder.icone.setImageResource(R.drawable.soy);
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

                if (!s.toString().isEmpty()) {
                    Commodity nova = listCompras.getListaCommodities().get(i);
                    maximo = Integer.parseInt(holder.quantidade.getHint().toString());
                    int valorNovo = Integer.parseInt(s.toString());

                    if (valorNovo <= maximo) {
                        nova.setQuantidade(originais[i] - valorNovo);
                        listCompras.getListaCommodities().set(i, nova);
                        quantidades[i] = valorNovo;
                        Toast.makeText(c, "Valor da venda: " + calcularLucroTotal(), Toast.LENGTH_SHORT).show();
                    } else {
                        holder.quantidade.setText(Integer.toString(maximo));
                        quantidades[i] = maximo;
                    }
                } else {
                    quantidades[i] = 0;
                    listCompras.getListaCommodities().get(i).setQuantidade(originais[i]);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return listCompras.getListaCommodities().size();
    }

    public int[] getOriginais() {
        return originais;
    }


    public float calcularLucroTotal() {
        float value = 0;
        for (int i = 0; i < listCompras.getListaCommodities().size(); i++)
            value += quantidades[i] * listCompras.getListaCommodities().get(i).getValor();
        return value;
    }


}
