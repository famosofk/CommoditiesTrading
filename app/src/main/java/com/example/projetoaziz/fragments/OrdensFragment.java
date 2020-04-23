package com.example.projetoaziz.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.activities.CadastroLoginActivity;
import com.example.projetoaziz.adapters.PosseAdapter;
import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.example.projetoaziz.models.Commodity;
import com.example.projetoaziz.models.Ordens;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdensFragment extends Fragment {

    private View v;
    private DatabaseReference db;
    private FirebaseUser user;
    private RecyclerView recyclerOrdens;
    private List<Commodity> listProfessor = new ArrayList<>();
    private List<Ordens> listaOrdens = new ArrayList<>();
    private String caminho;

    public OrdensFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ordens, container, false);

        FirebaseAuth mauth = ConfiguracaoDatabase.getFirebaseAutenticacao();
        user = mauth.getCurrentUser();
        if (user == null) {
            mauth.signOut();
            Objects.requireNonNull(getActivity()).finish();
            startActivity(new Intent(getActivity(), CadastroLoginActivity.class));
            Toast.makeText(getActivity(), "Por favor, faça login novamente.", Toast.LENGTH_SHORT).show();
        } else {
            Bundle bundle = getArguments();
            caminho = bundle.getString("idTurma");

        }
        return v;
    }



    @SuppressLint("DefaultLocale")
    private void popularTela() {
        TextView nome = v.findViewById(R.id.nomeOrdens);
        TextView creditos = v.findViewById(R.id.creditoOrdens);
        String NOME = "";
        String CREDITOS = "$ ";
    /*    if (aluno != null) {
            NOME = aluno.getNome() + " " + aluno.getSobrenome();
            CREDITOS = CREDITOS.concat(String.format("%.2f", aluno.getCreditos()));
        }
        if (professor != null) {
            NOME = professor.getNome() + " " + professor.getSobrenome();
            CREDITOS = CREDITOS.concat(String.format("%.2f", professor.getCreditos()));
        }*/
        nome.setText(NOME);
        creditos.setText(CREDITOS);
        RecyclerView recyclerPropriedades = v.findViewById(R.id.recyclerPosses);
        PosseAdapter adapter = new PosseAdapter(listProfessor, getActivity());
        recyclerPropriedades.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerPropriedades.setAdapter(adapter);
        popularOrdens();
    }

    private void popularOrdens() {
        recyclerOrdens = v.findViewById(R.id.recylerOrdens);
/*
        if (Objects.requireNonNull(user.getPhotoUrl()).toString().equals("aluno")) {
            db = FirebaseDatabase.getInstance().getReference().child("ordens").child(idProfessor).child(aluno.getId());
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Ordens recuperada;
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        recuperada = dsp.getValue(Ordens.class);
                        listaOrdens.add(recuperada);
                    }
                    OrdensAdapter adapter = new OrdensAdapter(listaOrdens, getActivity());
                    recyclerOrdens.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerOrdens.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else if (user.getPhotoUrl().toString().equals("professor")) {
            db = FirebaseDatabase.getInstance().getReference().child("ordens").child(idProfessor).child(professor.getId());
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Ordens recuperada;
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        recuperada = dsp.getValue(Ordens.class);
                        listaOrdens.add(recuperada);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            db = FirebaseDatabase.getInstance().getReference().child("ordens").child(idProfessor);
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Ordens recuperada;
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        for (DataSnapshot dsp3 : dsp.getChildren()) {
                            recuperada = dsp3.getValue(Ordens.class);
                            assert recuperada != null;
                            if (!(recuperada.getIdDono().equals(professor.getId()))) {
                                listaOrdens.add(recuperada);
                            }
                        }
                    }
                    OrdensAdapter adapter = new OrdensAdapter(listaOrdens, getActivity());
                    recyclerOrdens.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerOrdens.setAdapter(adapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } */
    }



}
