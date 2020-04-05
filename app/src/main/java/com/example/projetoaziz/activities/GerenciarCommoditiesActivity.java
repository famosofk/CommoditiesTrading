package com.example.projetoaziz.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.adapters.CompraAdapter;
import com.example.projetoaziz.adapters.VendaAdapter;
import com.example.projetoaziz.helpers.Base64Handler;
import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.example.projetoaziz.models.Aluno;
import com.example.projetoaziz.models.Commodity;
import com.example.projetoaziz.models.Professor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GerenciarCommoditiesActivity extends AppCompatActivity {

    Professor professor;
    Aluno aluno;
    DatabaseReference db;
    FirebaseUser user;
    FirebaseAuth mauth;
    List<Commodity> listProfessor;
    String idProfessor;
    RecyclerView recycler;
    CompraAdapter adapterCompras;
    VendaAdapter adapterVendas;
    float gasto;
    List<Commodity> listaNova;
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mauth = ConfiguracaoDatabase.getFirebaseAutenticacao();
        user = mauth.getCurrentUser();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            value = (int) bundle.getSerializable("acao");
            Toast.makeText(this, "" + value, Toast.LENGTH_SHORT).show();

            if (user.getPhotoUrl().toString().equals("professor"))
                recuperarProfessor();
            else if (user.getPhotoUrl().toString().equals("aluno")) {
                recuperarAluno();
            }


        }
    }


    private void compraAcoesProfessor() {
        setContentView(R.layout.comprar_commodities);
        recycler = findViewById(R.id.recyclerComprarCommodities);
        adapterCompras = new CompraAdapter(listProfessor, getApplicationContext(), professor);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapterCompras);
        Button compra = findViewById(R.id.confirmarCompraButton);
        compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaNova = adapterCompras.getListCompras();
                gasto = adapterCompras.calcularGastoTotal();
                professor.setCreditos(professor.getCreditos() - gasto);
                professor.setListaCommodities(listaNova);
                //salvar no banco e fechar.

            }
        });


    }

    private void compraAcoesAluno() {
        setContentView(R.layout.comprar_commodities);
        recycler = findViewById(R.id.recyclerComprarCommodities);
        adapterCompras = new CompraAdapter(listProfessor, getApplicationContext(), aluno);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapterCompras);
        Button compra = findViewById(R.id.confirmarCompraButton);
        compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaNova = adapterCompras.getListCompras();
                gasto = adapterCompras.calcularGastoTotal();
                aluno.setCreditos(aluno.getCreditos() - gasto);
                aluno.setListaCommodities(listaNova);
                //salvar no banco e fechar.

            }
        });


    }

    private void vendaAcoesProfessor() {
        setContentView(R.layout.vender_commodities);
        recycler = findViewById(R.id.recyclerVenderCommodities);
        List<Commodity> listVendas = new ArrayList<>();
        for (Commodity commodity : listProfessor) {
            if (commodity.getQuantidade() != 0) {
                listVendas.add(commodity);
            }
        }
        Toast.makeText(this, "" + listVendas.size(), Toast.LENGTH_SHORT).show();
        adapterVendas = new VendaAdapter(listVendas, getApplicationContext(), aluno);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapterVendas);
        Button venda = findViewById(R.id.confirmarVendaButton);
    }

    private void vendaAcoesAluno() {
        setContentView(R.layout.vender_commodities);
        recycler = findViewById(R.id.recyclerVenderCommodities);
        List<Commodity> listVendas = new ArrayList<>();
        for (Commodity commodity : listProfessor) {
            if (commodity.getQuantidade() != 0) {
                listVendas.add(commodity);
            }
        }
        Toast.makeText(this, "" + listVendas.size(), Toast.LENGTH_SHORT).show();
        adapterVendas = new VendaAdapter(listVendas, getApplicationContext(), aluno);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapterVendas);
        Button venda = findViewById(R.id.confirmarVendaButton);

    }

    private void gerenciarAcoes() {
        setContentView(R.layout.gerenciar_commodities);
    }

    private void recuperarProfessor() {

        db = FirebaseDatabase.getInstance().getReference().child("professor").child(Base64Handler.codificarBase64(user.getEmail()));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    professor = dataSnapshot.getValue(Professor.class);
                    assert professor != null;
                    listProfessor = professor.getListaCommodities();
                    idProfessor = professor.getId();
                    if (value == 1) {
                        Toast.makeText(GerenciarCommoditiesActivity.this, "Abrirá compra professor", Toast.LENGTH_SHORT).show();
                        compraAcoesProfessor();
                    } else if (value == 2) {
                        Toast.makeText(GerenciarCommoditiesActivity.this, "Abrirá venda professor", Toast.LENGTH_SHORT).show();

                        vendaAcoesProfessor();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void recuperarAluno() {

        db = FirebaseDatabase.getInstance().getReference().child("aluno").child(Base64Handler.codificarBase64(user.getEmail()));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    aluno = dataSnapshot.getValue(Aluno.class);
                    assert aluno != null;
                    listProfessor = aluno.getListaCommodities();
                    idProfessor = professor.getId();
                    if (value == 1) {
                        Toast.makeText(GerenciarCommoditiesActivity.this, "Abrirá compra aluno", Toast.LENGTH_SHORT).show();

                        compraAcoesAluno();
                    } else if (value == 2) {
                        Toast.makeText(GerenciarCommoditiesActivity.this, "Abrirá venda aluno", Toast.LENGTH_SHORT).show();

                        vendaAcoesAluno();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void recuperarAunoVenda() {

    }


}
