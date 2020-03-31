package com.example.projetoaziz.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetoaziz.R;
import com.example.projetoaziz.models.Commodities;

public class GerenciarCommoditiesActivity extends AppCompatActivity {

    Commodities listaCommodities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            listaCommodities = (Commodities) bundle.getSerializable("lista");

            switch (listaCommodities.getAcao()) {
                case 1:
                    compraAcoes();
                    break;
                case 2:
                    vendaAcoes();
                    break;
                case 3:
                    gerenciarAcoes();
                    break;
            }
        }


    }

    private void compraAcoes() {
        setContentView(R.layout.comprar_commodities);
    }

    private void vendaAcoes() {
        setContentView(R.layout.vender_commodities);
    }

    private void gerenciarAcoes() {
        setContentView(R.layout.gerenciar_commodities);
    }


}
