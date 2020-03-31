package com.example.projetoaziz.models;

import com.example.projetoaziz.helpers.Base64Handler;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String id;
    private String nome;
    private String sobrenome;
    private String email;
    private String universidade;
    private int algodao;
    private int amendoim;
    private int arroz;
    private int bezerro;
    private int boiGordo;
    private int cafe;
    private int feijao;
    private int frango;
    private int milho;
    private int soja;
    private int trigo;
    private int creditos;

    public Usuario() {
        algodao = amendoim = arroz = bezerro = boiGordo = cafe = feijao = frango = milho = soja = trigo = 0;
        creditos = 0;
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

    public int getAlgodao() {
        return algodao;
    }

    public void setAlgodao(int algodao) {
        this.algodao = algodao;
    }

    public int getAmendoim() {
        return amendoim;
    }

    public void setAmendoim(int amendoim) {
        this.amendoim = amendoim;
    }

    public int getArroz() {
        return arroz;
    }

    public void setArroz(int arroz) {
        this.arroz = arroz;
    }

    public int getBezerro() {
        return bezerro;
    }

    public void setBezerro(int bezerro) {
        this.bezerro = bezerro;
    }

    public int getBoiGordo() {
        return boiGordo;
    }

    public void setBoiGordo(int boiGordo) {
        this.boiGordo = boiGordo;
    }

    public int getCafe() {
        return cafe;
    }

    public void setCafe(int cafe) {
        this.cafe = cafe;
    }

    public int getFeijao() {
        return feijao;
    }

    public void setFeijao(int feijao) {
        this.feijao = feijao;
    }

    public int getFrango() {
        return frango;
    }

    public void setFrango(int frango) {
        this.frango = frango;
    }

    public int getMilho() {
        return milho;
    }

    public void setMilho(int milho) {
        this.milho = milho;
    }

    public int getSoja() {
        return soja;
    }

    public void setSoja(int soja) {
        this.soja = soja;
    }

    public int getTrigo() {
        return trigo;
    }

    public void setTrigo(int trigo) {
        this.trigo = trigo;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }
}
