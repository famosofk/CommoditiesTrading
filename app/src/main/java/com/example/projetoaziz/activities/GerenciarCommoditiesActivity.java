package com.example.projetoaziz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

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
import com.example.projetoaziz.models.Commodity;
import com.example.projetoaziz.models.ListaCommodities;
import com.example.projetoaziz.models.Ordens;
import com.example.projetoaziz.models.Turma;
import com.example.projetoaziz.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class GerenciarCommoditiesActivity extends AppCompatActivity {
    Usuario usuario;
    DatabaseReference db;
    FirebaseUser user;
    FirebaseAuth mauth;
    RecyclerView recycler;
    CompraAdapter adapterCompras;
    VendaAdapter adapterVendas;
    GerenciarAcoesAdapter adapterGerenciar;
    int value;
    ListaCommodities list;
    String caminho;
    int[] originais;
    Turma turma;
    float creditosIniciais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mauth = ConfiguracaoDatabase.getFirebaseAutenticacao();
        user = mauth.getCurrentUser();
        userRecoveryData();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            value = (int) bundle.getSerializable("acao");
            list = (ListaCommodities) bundle.getSerializable("lista");
            caminho = bundle.getString("caminho");
            turma = (Turma) bundle.getSerializable("turma");
            originais = new int[list.getListaCommodities().size()];
            for (int i = 0; i < list.getListaCommodities().size(); i++) {
                originais[i] = list.getListaCommodities().get(i).getQuantidade();
            }
            if (value == 1) {
                comprarCommodities();
            } else if (value == 2) {
                venderCommodities();
            } else if (value == 3) {
                editarCommodities();
            }


        }
    }

    private void comprarCommodities() {
        setContentView(R.layout.comprar_commodities);
        recycler = findViewById(R.id.recyclerComprarCommodities);
        adapterCompras = new CompraAdapter(list, getApplicationContext());
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapterCompras);

        Button compra = findViewById(R.id.confirmarCompraButton);
        compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ordens ordem = createOrder(1);

                for (int i = 0; i < list.getListaCommodities().size(); i++) {
                    list.getListaCommodities().get(i).setQuantidade(list.getListaCommodities().get(i).getQuantidade() + originais[i]);
                }
                db = FirebaseDatabase.getInstance().getReference().child("ordens").child(caminho).child(ordem.getIdDono()).push();
                float gasto = adapterCompras.calcularGastoTotal();
                list.setCreditos(list.getCreditos() - gasto);

                db.setValue(ordem);
                    list.salvar(caminho, ordem.getIdDono());
                    Intent i = new Intent(GerenciarCommoditiesActivity.this, MainActivity.class);
                    i.putExtra("idTurma", caminho);
                    startActivity(i);
                finish();

            }
        });


    }

    private void venderCommodities() {
        setContentView(R.layout.vender_commodities);
        recycler = findViewById(R.id.recyclerVenderCommodities);
        adapterVendas = new VendaAdapter(list, getApplicationContext());
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapterVendas);
        Button venda = findViewById(R.id.confirmarVendaButton);
        venda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ordens ordem = createOrder(2);
                ordem.setTipo("venda");
                db = FirebaseDatabase.getInstance().getReference().child("ordens").child(caminho).child(ordem.getIdDono()).push();
                db.setValue(ordem);
                list.setCreditos(adapterVendas.calcularLucroTotal() + list.getCreditos());
                list.setPatrimonioAnterior(list.getPatrimonio());
                list.setPatrimonio(list.getPatrimonio() + adapterVendas.calcularLucroTotal());
                list.salvar(caminho, Base64Handler.codificarBase64(user.getEmail()));
                Intent i = new Intent(GerenciarCommoditiesActivity.this, MainActivity.class);
                i.putExtra("idTurma", caminho);
                startActivity(i);
                finish();
            }
        });


    }

    private void editarCommodities() {
        setContentView(R.layout.gerenciar_commodities);
        recycler = findViewById(R.id.recyclerGerenciarCommodities);
        adapterGerenciar = new GerenciarAcoesAdapter(turma.getListaCommodities(), this);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapterGerenciar);
        Button gerenciar = findViewById(R.id.confirmarGerenciarButton);
        gerenciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attCommodities();
            }
        });
    }

    private void userRecoveryData() {
        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child(user.getPhotoUrl().toString()).child(Base64Handler.codificarBase64(user.getEmail()));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    usuario = dataSnapshot.getValue(Usuario.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Ordens createOrder(int value) {

        Ordens ordem = new Ordens();
        ordem.setIdDono(Base64Handler.codificarBase64(Objects.requireNonNull(user.getEmail())));
        ordem.setNome(usuario.getNome());
        ordem.setMatricula(usuario.getMatricula());
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
        return ordem;
    }

    private String getOrderDetails(int value) {
        if (value == 1) {
            String detalhes = "As compras foram de: ";

            for (int i = 0; i < list.getListaCommodities().size(); i++) {
                Commodity commodity = list.getListaCommodities().get(i);
                int diferenca = commodity.getQuantidade();
                if (diferenca != 0) {
                    String transacao = commodity.getNome() + ": " + diferenca + "  ";
                    detalhes = detalhes.concat(transacao);
                }
            }
            return detalhes;
        } else if (value == 2) {

            String detalhes = "As vendas foram de: ";

            for (int i = 0; i < list.getListaCommodities().size(); i++) {
                if ((list.getListaCommodities().get(i).getQuantidade() - originais[i]) != list.getListaCommodities().get(i).getQuantidade()) {
                    String transacao = list.getListaCommodities().get(i).getNome() + ": " + (originais[i] - list.getListaCommodities().get(i).getQuantidade()) + ".  ";
                    detalhes = detalhes.concat(transacao);
                }
            }

            return detalhes;
        }

        return null;
    }

    private void attCommodities() {
        CheckBox visibility = findViewById(R.id.visibilitycheckBox);
        if (visibility.isChecked()) {
            turma.setVisibility(true);
        } else {
            turma.setVisibility(false);
        }


        DatabaseReference listaAtualizada = ConfiguracaoDatabase.getFirebaseDatabase().child("turmas").child(caminho);
        turma.setListaCommodities(turma.getListaCommodities());
        listaAtualizada.setValue(turma);
        Intent i = new Intent(GerenciarCommoditiesActivity.this, MainActivity.class);
        i.putExtra("idTurma", caminho);
        startActivity(i);
        finish();
    }

}
