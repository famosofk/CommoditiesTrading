package com.example.projetoaziz.models;

public class Commodity {

    private String nome;
    private float valor;
    private int quantidade;
    private String unidade;

    public Commodity() {

        nome = "vazio";
        valor = 45;
        quantidade = 0;

    }

    public Commodity(String nome, float valor) {
        this.nome = nome;
        this.valor = valor;
        quantidade = 0;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
}
