package com.example.projetoaziz.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Ordens implements Serializable {

    String idDono;
    String idOrdem;
    String dados;
    String justificativa;
    String matricula = "Professor";
    String data;
    String nome;
    int tipo;

    public Ordens() {
        idOrdem = UUID.randomUUID().toString();
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        setData(currentDate);

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public void setIdOrdem(String idOrdem) {
        this.idOrdem = idOrdem;
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
