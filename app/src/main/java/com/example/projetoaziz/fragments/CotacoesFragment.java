package com.example.projetoaziz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.projetoaziz.activities.GerenciarCommoditiesActivity;
import com.example.projetoaziz.adapters.ListagemCotacoesAdapter;
import com.example.projetoaziz.helpers.Base64Handler;
import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.example.projetoaziz.models.Commodity;
import com.example.projetoaziz.models.ListaCommodities;
import com.example.projetoaziz.models.Turma;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class CotacoesFragment extends Fragment {

    private String caminho = "";
    private FirebaseUser user;
    private View v;
    private ListaCommodities recuperada;
    private Turma turma;
    private DatabaseReference dataChange;

    public CotacoesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cotacoes, container, false);
        Bundle bundle = getArguments();
        caminho = bundle.getString("idTurma");

        ImageButton editar = v.findViewById(R.id.imageView3);
        FirebaseAuth mauth = ConfiguracaoDatabase.getFirebaseAutenticacao();
        user = mauth.getCurrentUser();
        getTurma();


        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean monitor = false;

                if (turma.getMonitores() != null) {
                    for (String id : turma.getMonitores()) {
                        if (id.equals(Base64Handler.codificarBase64(user.getEmail()))) {
                            monitor = true;
                        }
                    }

                    if (monitor) {
                        Intent i = new Intent(getActivity(), GerenciarCommoditiesActivity.class);
                        i.putExtra("acao", 3);
                        i.putExtra("lista", recuperada);
                        i.putExtra("caminho", caminho);
                        i.putExtra("turma", turma);
                        startActivity(i);
                        getActivity().finish();
                    }

                }
            }
        });
        Button comprar = v.findViewById(R.id.buttonComprar);
        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getPhotoUrl().toString().equals("professor")) {
                    transicaoTelaCompra();
                } else if (user.getPhotoUrl().toString().equals("aluno")) {
                    if (turma.getVisibility()) {
                        transicaoTelaCompra();
                    } else {
                        exibirToastMercadoFechado();
                    }
                }
            }
        });

        Button vender = v.findViewById(R.id.buttonVender);
        vender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getPhotoUrl().toString().equals("professor")) {
                    transicaoTelaVenda();
                } else if (user.getPhotoUrl().toString().equals("aluno")) {
                    if (turma.getVisibility()) {
                        transicaoTelaVenda();
                    } else {
                        exibirToastMercadoFechado();
                    }
                }
            }
        });

        return v;
    }

    private void getTurma() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("turmas").child(caminho);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    turma = dataSnapshot.getValue(Turma.class);
                    if (turma != null) {
                        recuperarLista();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void fazerListagem(ListaCommodities lista) {
        RecyclerView recycler = v.findViewById(R.id.recyclerCommodities);
        ListagemCotacoesAdapter adapter = new ListagemCotacoesAdapter(lista.getListaCommodities(), getActivity());
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter);
    }

    private void atualizarLista(ListaCommodities lista) {
        dataChange = null;
        Boolean troca = false;
        List<Commodity> list = turma.getListaCommodities().getListaCommodities();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getValor() != lista.getListaCommodities().get(i).getValor()) {
                lista.getListaCommodities().get(i).setValor(list.get(i).getValor());
                troca = true;
            }
        }
        if (troca) {
            if (lista.getPatrimonio() != lista.getPatrimonioAnterior()) {
                float value = lista.getCreditos();
                for (int i = 0; i < list.size(); i++) {
                    value += lista.getListaCommodities().get(i).getValor() * lista.getListaCommodities().get(i).getQuantidade();
                }
                if (value != lista.getPatrimonio()) {
                    lista.setPatrimonioAnterior(lista.getPatrimonio());
                    lista.setPatrimonio(value);
                }
            }

            DatabaseReference dr = ConfiguracaoDatabase.getFirebaseDatabase().child("listaCommodities").child(caminho).child(Base64Handler.codificarBase64(user.getEmail()));
            dr.setValue(list);
        }
        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child("listaCommodities").child(caminho).child(Base64Handler.codificarBase64(user.getEmail()));
        db.setValue(lista);
        fazerListagem(lista);
    }

    private void recuperarLista() {
        dataChange = ConfiguracaoDatabase.getFirebaseDatabase().child("listaCommodities").child(caminho).child(Base64Handler.codificarBase64(user.getEmail()));
        dataChange.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    try {
                        recuperada = dataSnapshot.getValue(ListaCommodities.class);
                        atualizarLista(recuperada);
                    } catch (Exception e) {
                        Log.e("deu ruim", "");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void exibirToastMercadoFechado() {
        Toast.makeText(getActivity(), "O professor não liberou a negociação.", Toast.LENGTH_SHORT).show();
    }

    private void transicaoTelaCompra() {
        Intent i = new Intent(getActivity(), GerenciarCommoditiesActivity.class);
        i.putExtra("acao", 1);
        i.putExtra("lista", recuperada);
        i.putExtra("caminho", caminho);
        startActivity(i);
        Objects.requireNonNull(getActivity()).finish();
    }

    private void transicaoTelaVenda() {
        Intent i = new Intent(getActivity(), GerenciarCommoditiesActivity.class);
        i.putExtra("acao", 2);
        i.putExtra("lista", recuperada);
        i.putExtra("caminho", caminho);
        startActivity(i);
        Objects.requireNonNull(getActivity()).finish();
    }


}
