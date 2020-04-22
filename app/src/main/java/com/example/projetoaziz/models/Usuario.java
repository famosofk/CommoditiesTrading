package com.example.projetoaziz.models;

import com.example.projetoaziz.helpers.Base64Handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements Serializable {

    private String id;
    private String nome;
    private String sobrenome;
    private String email;
    private String universidade;
    private List<Commodity> listaCommodities;
    private float creditos;
    private String matricula;
    private float patrimonio;
    private float patrimonioAnterior;
    private List<String> listaTurmas;

    public Usuario() {
        creditos = (float) 100000.00;
        listaCommodities = new ArrayList<>();
        patrimonio = (float) 100000.00;
        patrimonioAnterior = (float) 100000.00;
        listaTurmas = new ArrayList<>();
    }

    public void atualizarPatrimonio() {
        float value = getCreditos();
        for (int i = 0; i < listaCommodities.size(); i++) {
            value += listaCommodities.get(i).getQuantidade() * listaCommodities.get(i).getValor();
        }
        if (patrimonio != value) {
            patrimonioAnterior = patrimonio;
            patrimonio = value;
        }
    }

    public List<String> getListaTurmas() {
        return listaTurmas;
    }

    public void setListaTurmas(List<String> listaTurmas) {
        this.listaTurmas = listaTurmas;
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

    public void atualizarID() {
        setId(Base64Handler.codificarBase64(getEmail()));
    }

    public String getId() {
        return id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUniversidade() {
        return universidade;
    }

    public void setUniversidade(String universidade) {
        this.universidade = universidade;
    }

    public float getCreditos() {
        return creditos;
    }

    public void setCreditos(Float creditos) {
        this.creditos = creditos;
    }


    public List<Commodity> getListaCommodities() {
        return listaCommodities;
    }

    public void setListaCommodities(List<Commodity> listaCommodities) {
        this.listaCommodities = listaCommodities;
    }
}
