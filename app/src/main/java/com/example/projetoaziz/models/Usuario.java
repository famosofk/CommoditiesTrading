package com.example.projetoaziz.models;

import com.example.projetoaziz.helpers.Base64Handler;
import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements Serializable {

    private String id;
    private String nome;
    private String sobrenome;
    private String email;
    private String universidade;
    private String matricula;
    private List<String> listaTurmas;

    public Usuario() {
        listaTurmas = new ArrayList<>();
    }

    public void salvar(String tipo, String id) {
        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child(tipo).child(id);
        db.setValue(this);
    }

    public List<String> getListaTurmas() {
        return listaTurmas;
    }

    public void setListaTurmas(List<String> listaTurmas) {
        this.listaTurmas = listaTurmas;
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



}
