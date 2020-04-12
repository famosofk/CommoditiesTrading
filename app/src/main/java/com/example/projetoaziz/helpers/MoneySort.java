package com.example.projetoaziz.helpers;

import com.example.projetoaziz.models.Commodity;
import com.example.projetoaziz.models.Usuario;

import java.util.Comparator;

public class MoneySort implements Comparator<Usuario> {
    @Override
    public int compare(Usuario o1, Usuario o2) {

        float patrimonio1 = o1.getCreditos();
        float patrimonio2 = o2.getCreditos();
        for (Commodity commodity : o1.getListaCommodities()) {
            patrimonio1 += commodity.getQuantidade() * commodity.getValor();
        }
        for (Commodity commodity : o2.getListaCommodities()) {
            patrimonio2 += commodity.getQuantidade() * commodity.getValor();
        }
        float diferenca = patrimonio1 - patrimonio2;
        if (diferenca > 0) {
            return -1;
        } else if (diferenca < 0) {
            return 1;
        }
        return 0;
    }
}
