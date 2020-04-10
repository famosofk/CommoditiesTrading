package com.example.projetoaziz.models;

import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.google.firebase.database.DatabaseReference;

public class Monitor extends Professor {

    private String matricula;
    private String idProfessor;

    public Monitor() {
    }

    public String getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(String idProfessor) {
        this.idProfessor = idProfessor;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void salvar() {

        DatabaseReference firebaseRef = ConfiguracaoDatabase.getFirebaseDatabase();
        DatabaseReference usuario = firebaseRef.child("monitor").child(getId());

        usuario.setValue(this);


    }
}
