package com.example.projetoaziz.models;

import java.util.UUID;

public class Ordens {

    String idDono;
    String idOrdem;
    int tipo;
    String dados;
    String justificativa;

    public Ordens() {

        idOrdem = UUID.randomUUID().toString();

    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public String getIdDono() {
        return idDono;
    }

    public void setIdDono(String idDono) {
        this.idDono = idDono;
    }

    public String getIdOrdem() {
        return idOrdem;
    }

    public String getTipo() {
        String tipoOrdem = "Vazia";
        if (tipo == 1) {
            tipoOrdem = "Compra";
        }
        if (tipo == 2) {
            tipoOrdem = "Venda";
        }

        return tipoOrdem;
    }

    public void setTipo(String tipoOrdem) {

        if (tipoOrdem.equals("Compra") || tipoOrdem.equals("compra")) {
            this.tipo = 1;
        }

        if (tipoOrdem.equals("venda") || tipoOrdem.equals("Venda")) {
            this.tipo = 2;
        }

    }

    public String getDados() {
        return dados;
    }

    public void setDados(String dados) {
        this.dados = dados;
    }
}
