package com.example.projetoaziz.models;

import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.google.firebase.database.DatabaseReference;

public class Aluno extends Usuario {


    private String professorID;

    public Aluno() {
    }

    public void salvar() {

        DatabaseReference firebaseRef = ConfiguracaoDatabase.getFirebaseDatabase();
        DatabaseReference usuario = firebaseRef.child("aluno").child(getId());

        usuario.setValue(this);

    }

    public String getProfessorID() {
        return professorID;
    }

    public void setProfessorID(String professorID) {
        this.professorID = professorID;
    }


}
