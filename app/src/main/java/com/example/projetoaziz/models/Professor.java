package com.example.projetoaziz.models;

import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.google.firebase.database.DatabaseReference;

public class Professor extends Usuario {

    public void salvar() {

        DatabaseReference firebaseRef = ConfiguracaoDatabase.getFirebaseDatabase();
        DatabaseReference usuario = firebaseRef.child("professor").child(getId());

        usuario.setValue(this);

    }
}
