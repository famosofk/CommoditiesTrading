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
import com.example.projetoaziz.models.Monitor;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class CotacoesFragment extends Fragment {

    private Professor professor = null;
    private Monitor monitor = null;
    private Aluno aluno = null;
    private String idProfessor = null;
    private DatabaseReference db;
    private FirebaseUser user;
    private View v;
    private List<Commodity> listProfessor = new ArrayList<>();

    public CotacoesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cotacoes, container, false);
        ImageButton editar = v.findViewById(R.id.imageView3);
        FirebaseAuth mauth = ConfiguracaoDatabase.getFirebaseAutenticacao();
        user = mauth.getCurrentUser();
        if (user == null) {
            mauth.signOut();
            Objects.requireNonNull(getActivity()).finish();
            startActivity(new Intent(getActivity(), CadastroLoginActivity.class));
            Toast.makeText(getActivity(), "Por favor, faça login novamente.", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        } else {
            if (Objects.requireNonNull(user.getPhotoUrl()).toString().equals("aluno")) {
                recuperarAluno();
            } else if (user.getPhotoUrl().toString().equals("professor")) {
                recuperarProfessor();
            } else if (user.getPhotoUrl().toString().equals("monitor")) {
                recuperarMonitor();
            }
        }
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aluno == null) {
                    if (listProfessor != null) {
                        Intent i = new Intent(getActivity(), GerenciarCommoditiesActivity.class);
                        i.putExtra("acao", 3);
                        startActivity(i);
                        Objects.requireNonNull(getActivity()).finish();
                    }
                }
            }
        });
        Button comprar = v.findViewById(R.id.buttonComprar);
        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aluno != null) {
                    if (professor.getVisibility()) {
                        transicaoTelaCompra();
                    } else {
                        exibirToastMercadoFechado();
                    }
                } else {
                    transicaoTelaCompra();
                }
            }
        });

        Button vender = v.findViewById(R.id.buttonVender);
        vender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aluno != null) {
                    if (professor.getVisibility()) {
                        transicaoTelaVenda();
                    } else {
                        exibirToastMercadoFechado();
                    }
                } else {
                    transicaoTelaVenda();
                }
            }
        });
        return v;
    }

    private void recuperarMonitor() {
        db = FirebaseDatabase.getInstance().getReference().child("monitor").child(Base64Handler.codificarBase64(Objects.requireNonNull(user.getEmail())));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    monitor = dataSnapshot.getValue(Monitor.class);
                    assert monitor != null;
                    idProfessor = monitor.getIdProfessor();
                    DatabaseReference busca = FirebaseDatabase.getInstance().getReference().child("professor").child(idProfessor);
                    busca.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            professor = dataSnapshot.getValue(Professor.class);
                            assert professor != null;
                            listProfessor = professor.getListaCommodities();
                            monitor.setListaCommodities(listProfessor);
                            monitor.salvar();
                            fazerListagem();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Registro não encontrado.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void recuperarAluno() {

        db = FirebaseDatabase.getInstance().getReference().child("aluno").child(Base64Handler.codificarBase64(Objects.requireNonNull(user.getEmail())));
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
                            aluno.setListaCommodities(atualizarLista(listProfessor, aluno));
                            listProfessor = aluno.getListaCommodities();
                            aluno.salvar();
                            if (professor.getVisibility()) {
                                fazerListagem();
                            } else {
                                exibirToastMercadoFechado();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
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

        db = FirebaseDatabase.getInstance().getReference().child("professor").child(Base64Handler.codificarBase64(Objects.requireNonNull(user.getEmail())));
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

        RecyclerView recycler = v.findViewById(R.id.recyclerCommodities);
        ListagemCotacoesAdapter adapter = new ListagemCotacoesAdapter(listProfessor, getActivity());
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter);


    }

    private List<Commodity> atualizarLista(List<Commodity> lista, Usuario usuario) {
        List<Commodity> listaUsuario = usuario.getListaCommodities();

        for (int i = 0; i < lista.size(); i++) {
            listaUsuario.get(i).setValor(lista.get(i).getValor());
        }

        return listaUsuario;
    }

    private void exibirToastMercadoFechado() {
        Toast.makeText(getActivity(), "O professor não liberou a negociação.", Toast.LENGTH_SHORT).show();
    }

    private void transicaoTelaCompra() {
        Intent i = new Intent(getActivity(), GerenciarCommoditiesActivity.class);
        i.putExtra("acao", 1);
        startActivity(i);
        Objects.requireNonNull(getActivity()).finish();
    }

    private void transicaoTelaVenda() {
        Intent i = new Intent(getActivity(), GerenciarCommoditiesActivity.class);
        i.putExtra("acao", 2);
        startActivity(i);
        Objects.requireNonNull(getActivity()).finish();
    }

}
