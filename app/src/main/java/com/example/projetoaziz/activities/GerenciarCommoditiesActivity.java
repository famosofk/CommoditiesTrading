package com.example.projetoaziz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.adapters.CompraAdapter;
import com.example.projetoaziz.adapters.GerenciarAcoesAdapter;
import com.example.projetoaziz.adapters.VendaAdapter;
import com.example.projetoaziz.helpers.Base64Handler;
import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.example.projetoaziz.models.Aluno;
import com.example.projetoaziz.models.Commodity;
import com.example.projetoaziz.models.Monitor;
import com.example.projetoaziz.models.Ordens;
import com.example.projetoaziz.models.Professor;
import com.example.projetoaziz.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GerenciarCommoditiesActivity extends AppCompatActivity {

    Professor professor;
    Monitor monitor;
    DatabaseReference db;
    FirebaseUser user;
    FirebaseAuth mauth;
    List<Commodity> listProfessor, listVendas;
    String idProfessor;
    RecyclerView recycler;
    CompraAdapter adapterCompras;
    VendaAdapter adapterVendas;
    GerenciarAcoesAdapter adapterGerenciar;
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mauth = ConfiguracaoDatabase.getFirebaseAutenticacao();
        user = mauth.getCurrentUser();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            value = (int) bundle.getSerializable("acao");

            if (Objects.requireNonNull(user.getPhotoUrl()).toString().equals("professor"))
                professorRecoveryData();
            else if (Objects.requireNonNull(user.getPhotoUrl().toString().equals("aluno"))) {
                studentRecoveryData();
            } else if (Objects.requireNonNull(user.getPhotoUrl().toString().equals("monitor"))) {
                monitorRecoveryData();
            }


        }
    }


    private void professorBuyingCommodities() {
        setContentView(R.layout.comprar_commodities);
        recycler = findViewById(R.id.recyclerComprarCommodities);
        adapterCompras = new CompraAdapter(listProfessor, getApplicationContext(), professor);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapterCompras);
        Button compra = findViewById(R.id.confirmarCompraButton);
        compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ordens ordem = createOrder(professor, 1);
                db = FirebaseDatabase.getInstance().getReference().child("ordens").child(idProfessor).child(ordem.getIdDono()).child(ordem.getIdOrdem());
                float gasto = adapterCompras.calcularGastoTotal();
                professor.setCreditos(professor.getCreditos() - gasto);
                db.setValue(ordem);
                professor.salvar();
                startActivity(new Intent(GerenciarCommoditiesActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void studentBuyingCommodities(final Aluno aluno) {
        setContentView(R.layout.comprar_commodities);
        recycler = findViewById(R.id.recyclerComprarCommodities);
        adapterCompras = new CompraAdapter(listProfessor, getApplicationContext(), aluno);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapterCompras);
        Button compra = findViewById(R.id.confirmarCompraButton);
        compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ordens ordem = createOrder(aluno, 1);
                db = FirebaseDatabase.getInstance().getReference().child("ordens").child(idProfessor).child(ordem.getIdDono()).push();
                if (!ordem.getJustificativa().isEmpty()) {
                    float gasto = adapterCompras.calcularGastoTotal();
                    aluno.setCreditos(aluno.getCreditos() - gasto);
                    ordem.setNome(aluno.getNome());
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

    private void professorSellingCommodities() {
        showSellingScreen();
        adapterVendas = new VendaAdapter(listVendas, getApplicationContext(), professor);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapterVendas);
        Button venda = findViewById(R.id.confirmarVendaButton);

        venda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ordens ordem = createOrder(professor, 2);
                ordem.setTipo("venda");
                db = FirebaseDatabase.getInstance().getReference().child("ordens").child(idProfessor).child(ordem.getIdDono()).child(ordem.getIdOrdem());
                db.setValue(ordem);
                professor.setCreditos(adapterVendas.calcularLucroTotal() + professor.getCreditos());
                professor.salvar();
                startActivity(new Intent(GerenciarCommoditiesActivity.this, MainActivity.class));
                finish();


            }
        });
    }

    private void studentSellingCommodities(final Aluno aluno) {
        showSellingScreen();
        adapterVendas = new VendaAdapter(listVendas, getApplicationContext(), aluno);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapterVendas);
        Button venda = findViewById(R.id.confirmarVendaButton);

        venda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ordens ordem = createOrder(aluno, 2);
                    ordem.setTipo("venda");

                    db = FirebaseDatabase.getInstance().getReference().child("ordens").child(idProfessor).child(ordem.getIdDono()).child(ordem.getIdOrdem());
                if (!ordem.getJustificativa().isEmpty()) {
                    aluno.setCreditos(adapterVendas.calcularLucroTotal() + aluno.getCreditos());
                    ordem.setNome(aluno.getNome());
                    db.setValue(ordem);
                    aluno.salvar();
                    startActivity(new Intent(GerenciarCommoditiesActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(GerenciarCommoditiesActivity.this, "Preencha a justificativa da ordem.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private Ordens createOrder(Usuario usuario, int value) {

        Ordens ordem = new Ordens();
        ordem.setIdDono(usuario.getId());
        if (value == 1) {
            ordem.setTipo("compra");
            EditText justificativa = findViewById(R.id.justificativaCompra);
            ordem.setDados(getOrderDetails(1));
            ordem.setJustificativa(justificativa.getText().toString());
        } else if (value == 2) {
            ordem.setTipo("venda");
            EditText justificativa = findViewById(R.id.justificativaVenda);
            ordem.setDados(getOrderDetails(2));
            ordem.setJustificativa(justificativa.getText().toString());
        }


        ordem.setMatricula(usuario.getMatricula());
        return ordem;
    }

    private String getOrderDetails(int value) {
        if (value == 1) {
            String detalhes = "As compras foram de: ";

            int[] originais = adapterCompras.getOriginais();
            for (int i = 0; i < listProfessor.size(); i++) {
                Commodity commodity = listProfessor.get(i);
                int diferenca = commodity.getQuantidade() - originais[i];
                if (diferenca != 0) {
                    String transacao = commodity.getNome() + ": " + diferenca + "  ";
                    detalhes = detalhes.concat(transacao);
                }
            }
            return detalhes;
        } else if (value == 2) {
            int[] originais = adapterVendas.getOriginais();
            String detalhes = "As vendas foram de: ";
            Commodity cAluno, cVenda;
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
            return detalhes;
        }

        return null;
    }

    private void showSellingScreen() {
        setContentView(R.layout.vender_commodities);
        recycler = findViewById(R.id.recyclerVenderCommodities);
        listVendas = new ArrayList<>();
        for (Commodity commodity : listProfessor) {
            if (commodity.getQuantidade() != 0) {
                listVendas.add(commodity);
            }
        }
    }

    private void allowManageCommodities() {

        if (professor == null) {
            db = FirebaseDatabase.getInstance().getReference().child("professor").child(monitor.getIdProfessor());
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        professor = dataSnapshot.getValue(Professor.class);
                        assert professor != null;
                        showManageCommoditiesScreen();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            showManageCommoditiesScreen();
        }


    }

    private void showManageCommoditiesScreen() {
        setContentView(R.layout.gerenciar_commodities);
        recycler = findViewById(R.id.recyclerGerenciarCommodities);
        adapterGerenciar = new GerenciarAcoesAdapter(listProfessor, getApplicationContext());
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapterGerenciar);
        Button gerenciar = findViewById(R.id.confirmarGerenciarButton);
        gerenciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attCommodities();
            }
        });
    }

    private void attCommodities() {
        professor.setListaCommodities(listProfessor);
        CheckBox visibility = findViewById(R.id.visibilitycheckBox);
        if (visibility.isChecked()) {
            professor.setVisibility(true);
        } else {
            professor.setVisibility(false);
        }
        professor.salvar();
        Intent i = new Intent(GerenciarCommoditiesActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void professorRecoveryData() {

        db = FirebaseDatabase.getInstance().getReference().child("professor").child(Base64Handler.codificarBase64(Objects.requireNonNull(user.getEmail())));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    professor = dataSnapshot.getValue(Professor.class);
                    assert professor != null;
                    listProfessor = professor.getListaCommodities();
                    idProfessor = professor.getId();
                    if (value == 1) {
                        professorBuyingCommodities();
                    } else if (value == 2) {
                        professorSellingCommodities();
                    } else if (value == 3) {
                        allowManageCommodities();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void monitorRecoveryData() {

        db = FirebaseDatabase.getInstance().getReference().child("monitor").child(Base64Handler.codificarBase64(Objects.requireNonNull(user.getEmail())));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    monitor = dataSnapshot.getValue(Monitor.class);
                    assert monitor != null;
                    listProfessor = monitor.getListaCommodities();
                    idProfessor = monitor.getIdProfessor();
                    allowManageCommodities();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void studentRecoveryData() {

        db = FirebaseDatabase.getInstance().getReference().child("aluno").child(Base64Handler.codificarBase64(Objects.requireNonNull(user.getEmail())));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Aluno aluno = dataSnapshot.getValue(Aluno.class);
                    assert aluno != null;
                    listProfessor = aluno.getListaCommodities();
                    idProfessor = aluno.getProfessorID();
                    if (value == 1) {
                        studentBuyingCommodities(aluno);
                    } else if (value == 2) {
                        studentSellingCommodities(aluno);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }




}
