package com.example.projetoaziz.models;

import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Professor extends Usuario implements Serializable {


    private String codigoMonitor;
    private Boolean visibility;


    public Professor() {

        setVisibility(false);
    }

    public void salvar() {

        DatabaseReference firebaseRef = ConfiguracaoDatabase.getFirebaseDatabase();
        DatabaseReference usuario = firebaseRef.child("professor").child(getId());

        usuario.setValue(this);

    }


    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public String getCodigoMonitor() {
        return codigoMonitor;
    }

    public void setCodigoMonitor(String codigoMonitor) {
        this.codigoMonitor = codigoMonitor;
    }




}
