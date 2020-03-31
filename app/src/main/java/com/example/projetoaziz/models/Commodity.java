package com.example.projetoaziz.models;

public class Commodity {

    private String nome;
    private int valor;
    private int quantidade;

    public Commodity() {

        nome = "vazio";
        valor = 45;
        quantidade = 0;

    }

    public Commodity(String nome, int valor) {
        this.nome = nome;
        this.valor = valor;
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

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}
