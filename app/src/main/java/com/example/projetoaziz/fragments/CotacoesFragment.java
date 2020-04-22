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
import com.example.projetoaziz.activities.GerenciarCommoditiesActivity;
import com.example.projetoaziz.adapters.ListagemCotacoesAdapter;
import com.example.projetoaziz.helpers.Base64Handler;
import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.example.projetoaziz.models.Commodity;
import com.example.projetoaziz.models.ListaCommodities;
import com.example.projetoaziz.models.Turma;
import com.example.projetoaziz.models.Usuario;
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

    private Usuario usuario = new Usuario();
    private String idProfessor = null, caminho = "";
    private DatabaseReference db;
    private FirebaseUser user;
    private View v;
    private List<Commodity> listCommodities;
    private Turma turma;

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
                if (!user.getPhotoUrl().toString().equals("aluno")) {
                        Intent i = new Intent(getActivity(), GerenciarCommoditiesActivity.class);
                        i.putExtra("acao", 3);
                        startActivity(i);
                        Objects.requireNonNull(getActivity()).finish();
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
        db = FirebaseDatabase.getInstance().getReference().child("turmas").child(caminho);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    turma = dataSnapshot.getValue(Turma.class);
                    if (turma != null) {
                        recuperarUsuario();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void recuperarUsuario() {
        db = ConfiguracaoDatabase.getFirebaseDatabase().child(user.getPhotoUrl().toString()).child(user.getDisplayName()).child(Base64Handler.codificarBase64(user.getEmail()));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    usuario = dataSnapshot.getValue(Usuario.class);
                    recuperarLista();
                } else {
                    Toast.makeText(getActivity(), "Registro não encontrado.", Toast.LENGTH_SHORT).show();
                    FirebaseAuth auth = ConfiguracaoDatabase.getFirebaseAutenticacao();
                    auth.signOut();
                    getActivity().finish();
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
        List<Commodity> list = turma.getListaCommodities().getListaCommodities();
        for (int i = 0; i < list.size(); i++) {
            lista.getListaCommodities().get(i).setValor(list.get(i).getValor());
        }
        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child("listaCommodities").child(Base64Handler.codificarBase64(user.getEmail())).child(caminho);
        db.setValue(lista);
        fazerListagem(lista);
    }

    private void recuperarLista() {
        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child("listaCommodities").child(Base64Handler.codificarBase64(user.getEmail())).child(caminho);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    ListaCommodities recuperada = dataSnapshot.getValue(ListaCommodities.class);
                    atualizarLista(recuperada);
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
