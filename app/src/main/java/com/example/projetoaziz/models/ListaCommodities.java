package com.example.projetoaziz.models;

import java.util.ArrayList;
import java.util.List;

public class ListaCommodities {

    private float creditos;
    private float patrimonio;
    private float patrimonioAnterior;
    private List<Commodity> listaCommodities = new ArrayList<>();

    public ListaCommodities() {
    }

    public float getCreditos() {
        return creditos;
    }

    public void setCreditos(float creditos) {
        this.creditos = creditos;
    }

    public float getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(float patrimonio) {
        this.patrimonio = patrimonio;
    }

    public float getPatrimonioAnterior() {
        return patrimonioAnterior;
    }

    public void setPatrimonioAnterior(float patrimonioAnterior) {
        this.patrimonioAnterior = patrimonioAnterior;
    }

    public List<Commodity> getListaCommodities() {
        return listaCommodities;
    }

    public void setListaCommodities(List<Commodity> listaCommodities) {
        this.listaCommodities = listaCommodities;
    }
}
