package com.example.projetoaziz.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetoaziz.R;

public class GerenciarTurmasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_turmas);
    }


    public void exibirCriarTurma(View view) {
        LinearLayout selecao = findViewById(R.id.selectorAction);
        selecao.setVisibility(View.GONE);
        LinearLayout cadastrarTurma = findViewById(R.id.cadastrarTurma);
        cadastrarTurma.setVisibility(View.VISIBLE);
    }

    public void exibirSelecionarTurma(View view) {
        LinearLayout selecao = findViewById(R.id.selectorAction);
        selecao.setVisibility(View.GONE);

    }


}
