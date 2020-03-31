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
    private int acao;
    private int creditos;

    public Usuario() {
        acao = 0;
        creditos = 0;
        listaCommodities = new ArrayList<>();
    }

    public void atualizarID() {
        setId(Base64Handler.codificarBase64(getEmail()));
    }

    public String getId() {
        return id;
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

    public int getAcao() {
        return acao;
    }

    public void setAcao(int acao) {
        this.acao = acao;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }


    public List<Commodity> getListaCommodities() {
        return listaCommodities;
    }

    public void setListaCommodities(List<Commodity> listaCommodities) {
        this.listaCommodities = listaCommodities;
    }
}
