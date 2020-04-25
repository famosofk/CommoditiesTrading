package com.example.projetoaziz.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.activities.CadastroLoginActivity;
import com.example.projetoaziz.adapters.OrdensAdapter;
import com.example.projetoaziz.adapters.PosseAdapter;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdensFragment extends Fragment {
    List<Ordens> fake = new ArrayList<>();
    private View v;
    private Usuario usuario;
    private DatabaseReference db;
    private FirebaseUser user;
    private RecyclerView recyclerOrdens;
    private ListaCommodities lista;
    private Turma turma;
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
            getTurma();
            userRecoveryData();
            userRecoveryList();


        }
        return v;
    }



    @SuppressLint("DefaultLocale")
    private void popularTela() {

    }

    private void popularOrdens() {
    /*    recyclerOrdens = v.findViewById(R.id.recylerOrdens);

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

    private void userRecoveryData() {
        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child(user.getPhotoUrl().toString()).child(Base64Handler.codificarBase64(user.getEmail()));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    usuario = dataSnapshot.getValue(Usuario.class);
                    popularDadosUsuario();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void userRecoveryList() {
        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child("listaCommodities").child(caminho).child(Base64Handler.codificarBase64(user.getEmail()));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    lista = dataSnapshot.getValue(ListaCommodities.class);
                    popularDadosLista();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void userRecoveryOrders() {

        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child("ordens").child(caminho).child(Base64Handler.codificarBase64(user.getEmail()));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Ordens recuperada = null;
                if (dataSnapshot != null) {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        recuperada = dsp.getValue(Ordens.class);
                        listaOrdens.add(recuperada);
                    }
                    popularDadosOrdens();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void popularDadosUsuario() {

        TextView nome = v.findViewById(R.id.nomeOrdens);

        String NOME = "";

        if (usuario != null) {
            NOME = usuario.getNome() + " " + usuario.getSobrenome();

        }
        nome.setText(NOME);


        userRecoveryOrders();

    }

    private void popularDadosOrdens() {
        Boolean monitor = false;

        for (String id : turma.getMonitores()) {
            if (id.equals(usuario.getId())) {
                monitor = true;
            }
        }

        if (monitor) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("ordens").child(caminho);
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Ordens recuperada;
                    listaOrdens.clear();
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        for (DataSnapshot dsp3 : dsp.getChildren()) {
                            recuperada = dsp3.getValue(Ordens.class);
                            assert recuperada != null;
                            listaOrdens.add(recuperada);
                        }
                    }

                    Collections.reverse(listaOrdens);
                    Set<Ordens> set = new LinkedHashSet<>(listaOrdens);
                    recyclerOrdens = v.findViewById(R.id.recylerOrdens);
                    OrdensAdapter adapter = new OrdensAdapter(new ArrayList<Ordens>(set), getActivity());
                    recyclerOrdens.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerOrdens.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            recyclerOrdens = v.findViewById(R.id.recylerOrdens);
            OrdensAdapter adapter = new OrdensAdapter(listaOrdens, getActivity());
            recyclerOrdens.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerOrdens.setAdapter(adapter);
        }





    }

    private void popularDadosLista() {
        TextView creditos = v.findViewById(R.id.creditoOrdens);
        String CREDITOS = "$ ";
        String valor = CREDITOS.concat(String.format("%.2f", lista.getCreditos()));
        creditos.setText(valor);
        RecyclerView recyclerPropriedades = v.findViewById(R.id.recyclerPosses);
        List<Commodity> posses = new ArrayList<>();
        for (int i = 0; i < lista.getListaCommodities().size(); i++) {
            if (lista.getListaCommodities().get(i).getQuantidade() != 0) {
                posses.add(lista.getListaCommodities().get(i));
            }
        }
        PosseAdapter adapter = new PosseAdapter(posses, getActivity());
        recyclerPropriedades.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerPropriedades.setAdapter(adapter);

    }

    private void getTurma() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("turmas").child(caminho);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    turma = dataSnapshot.getValue(Turma.class);
                    if (turma != null) {
                        userRecoveryData();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}
