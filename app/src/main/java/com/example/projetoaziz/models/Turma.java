package com.example.projetoaziz.models;

import com.example.projetoaziz.helpers.Base64Handler;

import java.io.Serializable;

public class Turma implements Serializable {

    private String id;
    private String nome;
    private Boolean requerSenha;
    private String senha;
    private String idProfessor;
    private ListaCommodities listaCommodities = new ListaCommodities();
    private Boolean visibility;

    public Turma() {
        visibility = false;
        requerSenha = false;
    }

    public ListaCommodities getListaCommodities() {
        return listaCommodities;
    }

    public void setListaCommodities(ListaCommodities listaCommodities) {
        this.listaCommodities = listaCommodities;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public String getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(String idProfessor) {
        this.idProfessor = idProfessor;
    }

    public void atualizarID() {
        id = Base64Handler.codificarBase64(nome);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome.trim();
    }

    public Boolean getRequerSenha() {
        return requerSenha;
    }

    public void setRequerSenha(Boolean requerSenha) {
        this.requerSenha = requerSenha;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
