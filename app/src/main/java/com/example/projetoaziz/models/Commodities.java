package com.example.projetoaziz.models;

import java.io.Serializable;

public class Commodities implements Serializable {

    private boolean visibility;
    private int acao;
    private String algodao;
    private String amendoim;
    private String arroz;
    private String bezerro;
    private String boiGordo;
    private String cafe;
    private String feijao;
    private String frango;
    private String milho;
    private String soja;
    private String trigo;

    public Commodities() {
        algodao = amendoim = arroz = bezerro = boiGordo = cafe = feijao = frango = milho = soja = trigo = "0";
        visibility = false;
        acao = 0;
    }

    public int getAcao() {
        return acao;
    }

    public void setAcao(int acao) {
        this.acao = acao;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public String getAlgodao() {
        return algodao;
    }

    public void setAlgodao(String algodao) {
        this.algodao = algodao;
    }

    public String getAmendoim() {
        return amendoim;
    }

    public void setAmendoim(String amendoim) {
        this.amendoim = amendoim;
    }

    public String getArroz() {
        return arroz;
    }

    public void setArroz(String arroz) {
        this.arroz = arroz;
    }

    public String getBezerro() {
        return bezerro;
    }

    public void setBezerro(String bezerro) {
        this.bezerro = bezerro;
    }

    public String getBoiGordo() {
        return boiGordo;
    }

    public void setBoiGordo(String boiGordo) {
        this.boiGordo = boiGordo;
    }

    public String getCafe() {
        return cafe;
    }

    public void setCafe(String cafe) {
        this.cafe = cafe;
    }

    public String getFeijao() {
        return feijao;
    }

    public void setFeijao(String feijao) {
        this.feijao = feijao;
    }

    public String getFrango() {
        return frango;
    }

    public void setFrango(String frango) {
        this.frango = frango;
    }

    public String getMilho() {
        return milho;
    }

    public void setMilho(String milho) {
        this.milho = milho;
    }

    public String getSoja() {
        return soja;
    }

    public void setSoja(String soja) {
        this.soja = soja;
    }

    public String getTrigo() {
        return trigo;
    }

    public void setTrigo(String trigo) {
        this.trigo = trigo;
    }
}
