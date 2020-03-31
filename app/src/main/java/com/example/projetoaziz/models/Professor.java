package com.example.projetoaziz.models;

import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.google.firebase.database.DatabaseReference;

public class Professor extends Usuario {


    private Boolean visibility;
    private String codigoMonitor;


    public Professor() {

    }

    public void salvar() {

        DatabaseReference firebaseRef = ConfiguracaoDatabase.getFirebaseDatabase();
        DatabaseReference usuario = firebaseRef.child("professor").child(getId());

        usuario.setValue(this);

    }


    public String getCodigoMonitor() {
        return codigoMonitor;
    }

    public void setCodigoMonitor(String codigoMonitor) {
        this.codigoMonitor = codigoMonitor;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }


}
