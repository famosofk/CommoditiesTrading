package com.example.projetoaziz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.activities.CadastroLoginActivity;
import com.example.projetoaziz.activities.GerenciarCommoditiesActivity;
import com.example.projetoaziz.adapters.ListagemCotacoesAdapter;
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
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdensFragment extends Fragment {
    RecyclerView recycler;
    private Professor professor = null;
    private Aluno aluno = null;
    private String idProfessor = null;
    private DatabaseReference db;
    Boolean admin = false;
    private FirebaseUser user;
    private View v;
    ListagemCotacoesAdapter adapter;
    private List<Commodity> listProfessor = new ArrayList<>();

    public OrdensFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_ordens, container, false);
        ImageButton editar = v.findViewById(R.id.imageView3);


        FirebaseAuth mauth = ConfiguracaoDatabase.getFirebaseAutenticacao();
        user = mauth.getCurrentUser();
        if (user == null) {
            mauth.signOut();
            Objects.requireNonNull(getActivity()).finish();
            startActivity(new Intent(getActivity(), CadastroLoginActivity.class));
            Toast.makeText(getActivity(), "Por favor, faça login novamente.", Toast.LENGTH_SHORT).show();
        } else {

            if (user.getPhotoUrl().toString().equals("aluno")) {
                recuperarAluno();
            } else if (user.getPhotoUrl().toString().equals("professor")) {
                recuperarProfessor();
            }
        }
        //Nesse momento professor e email estão preenchidos e a listProfessorPopulada;


        //adapter.getItemCount(); ->usamos para saber o número de childs








        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idProfessor == Base64Handler.codificarBase64(user.getEmail())) {
                 /*   if (listaCommodities != null) {
                        listaCommodities.setAcao(3);
                        Intent i = new Intent(getActivity(), GerenciarCommoditiesActivity.class);
                        i.putExtra("lista", listaCommodities);
                        startActivity(i);
                    }*/
                }
            }
        });

        Button comprar = v.findViewById(R.id.buttonComprar);
        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent i = new Intent(getActivity(), GerenciarCommoditiesActivity.class);
                i.putExtra("acao", 1);
                        startActivity(i);
            }
        });

        Button vender = v.findViewById(R.id.buttonVender);
        vender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (listaCommodities != null) {

                    if (listaCommodities.isVisibility() || admin) {
                        listaCommodities.setAcao(2);
                        Intent i = new Intent(getActivity(), GerenciarCommoditiesActivity.class);
                        i.putExtra("lista", listaCommodities);
                        startActivity(i);
                    }
                }*/
            }
        });

        return v;
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
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    listProfessor = aluno.getListaCommodities();
                } else {
                    Toast.makeText(getActivity(), "Registro não encontrado.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void recuperarProfessor() {

        db = FirebaseDatabase.getInstance().getReference().child("professor").child(Base64Handler.codificarBase64(user.getEmail()));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    professor = dataSnapshot.getValue(Professor.class);
                    assert professor != null;
                    idProfessor = professor.getId();
                    listProfessor = professor.getListaCommodities();
                    fazerListagem();

                } else {
                    Toast.makeText(getActivity(), "Registro não encontrado.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fazerListagem() {
        recycler = v.findViewById(R.id.recyclerCommodities);
        adapter = new ListagemCotacoesAdapter(listProfessor, getActivity());

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter);


    }


}
