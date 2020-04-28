package com.example.projetoaziz.helpers;

import com.example.projetoaziz.models.ListaCommodities;

import java.util.Comparator;

public class VariationSort implements Comparator<ListaCommodities> {
    @Override
    public int compare(ListaCommodities o1, ListaCommodities o2) {

        float diferenca = (o1.getPatrimonio() / o1.getPatrimonioAnterior()) - (o2.getPatrimonio() / o2.getPatrimonioAnterior());
        if (diferenca > 0) {
            return -1;
        } else if (diferenca < 0) {
            return 1;
        }
        return 0;


    }
}
