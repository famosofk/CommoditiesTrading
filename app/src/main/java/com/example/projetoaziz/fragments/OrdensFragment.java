package com.example.projetoaziz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projetoaziz.R;
import com.example.projetoaziz.activities.CadastroLoginActivity;
import com.example.projetoaziz.activities.GerenciarCommoditiesActivity;
import com.example.projetoaziz.helpers.Base64Handler;
import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.example.projetoaziz.models.Aluno;
import com.example.projetoaziz.models.Commodities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdensFragment extends Fragment {

    FirebaseAuth mauth;
    String idProfessor = null;
    DatabaseReference db, cotacao;
    FirebaseUser user;
    Boolean admin = false;
    Commodities listaCommodities;
    View v;

    public OrdensFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_ordens, container, false);
        ImageButton editar = v.findViewById(R.id.imageView3);


        mauth = ConfiguracaoDatabase.getFirebaseAutenticacao();
        user = mauth.getCurrentUser();
        if (user == null) {
            mauth.signOut();
            Objects.requireNonNull(getActivity()).finish();
            startActivity(new Intent(getActivity(), CadastroLoginActivity.class));
            Toast.makeText(getActivity(), "Por favor, faça login novamente.", Toast.LENGTH_SHORT).show();
        }


        db = FirebaseDatabase.getInstance().getReference().child("aluno").child(Base64Handler.codificarBase64(user.getEmail()));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    idProfessor = Base64Handler.codificarBase64(user.getEmail());
                    admin = true;
                    atualizarCotacao();
                } else {
                    Aluno aluno = dataSnapshot.getValue(Aluno.class);
                    idProfessor = aluno.getProfessorID();
                    atualizarCotacao();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idProfessor == Base64Handler.codificarBase64(user.getEmail())) {
                    if (listaCommodities != null) {
                        listaCommodities.setAcao(3);
                        Intent i = new Intent(getActivity(), GerenciarCommoditiesActivity.class);
                        i.putExtra("lista", listaCommodities);
                        startActivity(i);
                    }
                }
            }
        });

        Button comprar = v.findViewById(R.id.buttonComprar);
        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listaCommodities != null) {
                    if (listaCommodities.isVisibility() || admin) {

                        listaCommodities.setAcao(1);
                        Intent i = new Intent(getActivity(), GerenciarCommoditiesActivity.class);
                        i.putExtra("lista", listaCommodities);
                        startActivity(i);
                    }
                }
            }
        });

        Button vender = v.findViewById(R.id.buttonVender);
        vender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listaCommodities != null) {

                    if (listaCommodities.isVisibility() || admin) {
                        listaCommodities.setAcao(2);
                        Intent i = new Intent(getActivity(), GerenciarCommoditiesActivity.class);
                        i.putExtra("lista", listaCommodities);
                        startActivity(i);
                    }
                }
            }
        });

        return v;
    }


    private void atualizarCotacao() {
        cotacao = FirebaseDatabase.getInstance().getReference().child("cotacoes").child(idProfessor);
        cotacao.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCommodities = dataSnapshot.getValue(Commodities.class);
                Toast.makeText(getActivity(), "dados chegaram", Toast.LENGTH_SHORT).show();

                assert listaCommodities != null;
                if (listaCommodities.isVisibility() || admin) {

                    TextView algodao = v.findViewById(R.id.precoAlgodao);
                    algodao.setText(listaCommodities.getAlgodao());
                    TextView amendoim = v.findViewById(R.id.precoAmendoim);
                    amendoim.setText(listaCommodities.getAmendoim());
                    TextView arroz = v.findViewById(R.id.precoArroz);
                    arroz.setText(listaCommodities.getArroz());
                    TextView bezerro = v.findViewById(R.id.precoBezerro);
                    bezerro.setText(listaCommodities.getBezerro());
                    TextView boigordo = v.findViewById(R.id.precoBoigordo);
                    boigordo.setText(listaCommodities.getBoiGordo());
                    TextView cafe = v.findViewById(R.id.precoCafe);
                    cafe.setText(listaCommodities.getCafe());
                    TextView feijao = v.findViewById(R.id.precoFeijao);
                    feijao.setText(listaCommodities.getFeijao());
                    TextView frango = v.findViewById(R.id.precoFrango);
                    frango.setText(listaCommodities.getFrango());
                    TextView milho = v.findViewById(R.id.precoMilho);
                    milho.setText(listaCommodities.getMilho());
                    TextView soja = v.findViewById(R.id.precoSoja);
                    soja.setText(listaCommodities.getSoja());
                    TextView trigo = v.findViewById(R.id.precoTrigo);
                    trigo.setText(listaCommodities.getTrigo());

                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    TextView atualizacao = v.findViewById(R.id.dadosAtualizados);
                    String textoDeAtualizacao = "Dados atualizados em: " + currentTime;
                    atualizacao.setText(textoDeAtualizacao);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
