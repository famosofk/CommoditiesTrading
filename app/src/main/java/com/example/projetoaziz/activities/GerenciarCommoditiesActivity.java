package com.example.projetoaziz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.projetoaziz.models.Ordens;
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
    List<Commodity> listProfessor, listVendas;
    String idProfessor, detalhes;
    RecyclerView recycler;
    CompraAdapter adapterCompras;
    VendaAdapter adapterVendas;
    float gasto;
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mauth = ConfiguracaoDatabase.getFirebaseAutenticacao();
        user = mauth.getCurrentUser();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            value = (int) bundle.getSerializable("acao");

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
                gasto = adapterCompras.calcularGastoTotal();
                professor.setCreditos(professor.getCreditos() - gasto);
                detalhes = "As compras foram de: ";
                int[] originais = adapterCompras.getOriginais();
                for (int i = 0; i < listProfessor.size(); i++) {
                    Commodity commodity = listProfessor.get(i);
                    int diferenca = commodity.getQuantidade() - originais[i];
                    if (diferenca != 0) {
                        String transacao = commodity.getNome() + ": " + Integer.toString(diferenca) + "  ";
                        detalhes = detalhes.concat(transacao);
                    }
                }//no final disso tenho os dados da compra em uma string
                Ordens ordem = new Ordens();
                ordem.setTipo("compra");
                ordem.setIdDono(professor.getId());
                ordem.setDados(detalhes); //nesse ponto tenho a ordem atualizada, basta salvar as coisas no banco agora.
                EditText justificativa = findViewById(R.id.justificativaCompra);
                ordem.setJustificativa(justificativa.getText().toString());
                db = FirebaseDatabase.getInstance().getReference().child("ordens").child(idProfessor).child(ordem.getIdDono()).child(ordem.getIdOrdem());
                db.setValue(ordem);
                professor.salvar();
                startActivity(new Intent(GerenciarCommoditiesActivity.this, MainActivity.class));
                finish();

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
                EditText justificativa = findViewById(R.id.justificativaCompra);
                if (!justificativa.getText().toString().isEmpty()) {

                    gasto = adapterCompras.calcularGastoTotal();
                    aluno.setCreditos(aluno.getCreditos() - gasto);
                    detalhes = "As compras foram de: ";
                    int[] originais = adapterCompras.getOriginais();
                    for (int i = 0; i < listProfessor.size(); i++) {
                        Commodity commodity = listProfessor.get(i);
                        int diferenca = commodity.getQuantidade() - originais[i];
                        if (diferenca != 0) {
                            String transacao = commodity.getNome() + ": " + diferenca + "  ";
                            detalhes = detalhes.concat(transacao);
                        }
                    }//no final disso tenho os dados da compra em uma string
                    Ordens ordem = new Ordens();
                    ordem.setTipo("compra");
                    ordem.setMatricula(aluno.getMatricula());
                    ordem.setIdDono(aluno.getId());
                    ordem.setDados(detalhes); //nesse ponto tenho a ordem atualizada, basta salvar as coisas no banco agora.
                    ordem.setJustificativa(justificativa.getText().toString());
                    db = FirebaseDatabase.getInstance().getReference().child("ordens").child(idProfessor).child(ordem.getIdDono()).child(ordem.getIdOrdem());
                    db.setValue(ordem);
                    aluno.salvar();
                    startActivity(new Intent(GerenciarCommoditiesActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(GerenciarCommoditiesActivity.this, "Preencha a justificativa.", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void vendaAcoesProfessor() {
        setContentView(R.layout.vender_commodities);
        recycler = findViewById(R.id.recyclerVenderCommodities);
        listVendas = new ArrayList<>();
        for (Commodity commodity : listProfessor) {
            if (commodity.getQuantidade() != 0) {
                listVendas.add(commodity);
            }
        }
        adapterVendas = new VendaAdapter(listVendas, getApplicationContext(), professor);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapterVendas);
        Button venda = findViewById(R.id.confirmarVendaButton);

        venda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] originais = adapterVendas.getOriginais();
                Ordens ordem = new Ordens();
                ordem.setIdDono(professor.getId());
                ordem.setTipo("venda");
                detalhes = "As vendas foram de: ";
                Commodity cProfessor, cVenda;
                professor.setCreditos(adapterVendas.calcularLucroTotal() + professor.getCreditos());
                for (int lpIndex = 0; lpIndex < listProfessor.size(); lpIndex++) {
                    cProfessor = listProfessor.get(lpIndex);
                    for (int lvIndex = 0; lvIndex < listVendas.size(); lvIndex++) {
                        cVenda = listVendas.get(lvIndex);
                        if (cProfessor.getNome().equals(cVenda.getNome())) {
                            int diferenca = originais[lvIndex] - cProfessor.getQuantidade();
                            if (diferenca != 0) {
                                String transacao = cProfessor.getNome() + ": " + diferenca + "  ";
                                detalhes = detalhes.concat(transacao);
                            }
                        }
                    }
                }
                EditText justificativa = findViewById(R.id.justificativaVenda);
                ordem.setJustificativa(justificativa.getText().toString());
                ordem.setDados(detalhes);
                db = FirebaseDatabase.getInstance().getReference().child("ordens").child(idProfessor).child(ordem.getIdDono()).child(ordem.getIdOrdem());
                db.setValue(ordem);
                professor.salvar();
                startActivity(new Intent(GerenciarCommoditiesActivity.this, MainActivity.class));
                finish();


            }
        });
    }

    private void vendaAcoesAluno() {
        setContentView(R.layout.vender_commodities);
        recycler = findViewById(R.id.recyclerVenderCommodities);
        listVendas = new ArrayList<>();
        for (Commodity commodity : listProfessor) {
            if (commodity.getQuantidade() != 0) {
                listVendas.add(commodity);
            }
        }
        adapterVendas = new VendaAdapter(listVendas, getApplicationContext(), aluno);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapterVendas);
        Button venda = findViewById(R.id.confirmarVendaButton);

        venda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText justificativa = findViewById(R.id.justificativaVenda);
                if (!justificativa.getText().toString().isEmpty()) {

                    int[] originais = adapterVendas.getOriginais();
                    Ordens ordem = new Ordens();
                    ordem.setIdDono(aluno.getId());
                    ordem.setTipo("venda");
                    detalhes = "As vendas foram de: ";
                    Commodity cAluno, cVenda;
                    professor.setCreditos(adapterVendas.calcularLucroTotal() + professor.getCreditos());
                    for (int lpIndex = 0; lpIndex < listProfessor.size(); lpIndex++) {
                        cAluno = listProfessor.get(lpIndex);
                        for (int lvIndex = 0; lvIndex < listVendas.size(); lvIndex++) {
                            cVenda = listVendas.get(lvIndex);
                            if (cAluno.getNome().equals(cVenda.getNome())) {
                                int diferenca = originais[lvIndex] - cAluno.getQuantidade();
                                if (diferenca != 0) {
                                    String transacao = cAluno.getNome() + ": " + diferenca + "  ";
                                    detalhes = detalhes.concat(transacao);
                                }
                            }
                        }
                    }
                    ordem.setJustificativa(justificativa.getText().toString());
                    ordem.setDados(detalhes);
                    ordem.setMatricula(aluno.getMatricula());
                    db = FirebaseDatabase.getInstance().getReference().child("ordens").child(idProfessor).child(ordem.getIdDono()).child(ordem.getIdOrdem());
                    db.setValue(ordem);
                    professor.salvar();
                    startActivity(new Intent(GerenciarCommoditiesActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(GerenciarCommoditiesActivity.this, "Preencha a justificativa.", Toast.LENGTH_SHORT).show();
                }

            }
        });
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
                        compraAcoesProfessor();
                    } else if (value == 2) {

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
                    idProfessor = aluno.getProfessorID();
                    if (value == 1) {

                        compraAcoesAluno();
                    } else if (value == 2) {

                        vendaAcoesAluno();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




}
