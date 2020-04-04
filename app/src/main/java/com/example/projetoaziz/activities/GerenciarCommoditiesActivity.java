package com.example.projetoaziz.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.adapters.CompraAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mauth = ConfiguracaoDatabase.getFirebaseAutenticacao();
        user = mauth.getCurrentUser();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            int value = (int) bundle.getSerializable("acao");
            if (value == 1) {
                recuperarProfessorCompra();

            }

            }
    }




    private void compraAcoes() {
        setContentView(R.layout.comprar_commodities);
        recycler = findViewById(R.id.recyclerComprarCommodities);
        CompraAdapter adapter = new CompraAdapter(listProfessor, getApplicationContext(), professor);

        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapter);


    }

    private void vendaAcoes() {

    }

    private void gerenciarAcoes() {
        setContentView(R.layout.gerenciar_commodities);
    }

    private void recuperarProfessorCompra() {

        db = FirebaseDatabase.getInstance().getReference().child("professor").child(Base64Handler.codificarBase64(user.getEmail()));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    professor = dataSnapshot.getValue(Professor.class);
                    assert professor != null;
                    listProfessor = professor.getListaCommodities();
                    idProfessor = professor.getId();
                    compraAcoes();

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
                    idProfessor = aluno.getProfessorID();
                    DatabaseReference busca = FirebaseDatabase.getInstance().getReference().child("professor").child(idProfessor);
                    busca.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            professor = dataSnapshot.getValue(Professor.class);
                            assert professor != null;
                            listProfessor = professor.getListaCommodities();
                            idProfessor = aluno.getProfessorID();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    listProfessor = aluno.getListaCommodities();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
