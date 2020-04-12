package com.example.projetoaziz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projetoaziz.R;
import com.example.projetoaziz.helpers.Base64Handler;
import com.example.projetoaziz.helpers.MoneySort;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartsFragment extends Fragment {

    View v;

    public ChartsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_charts, container, false);

        recuperarProfessor(getCurrentUser());


        return v;
    }

    private void plotarGraficos(List<Usuario> list) {
        String value = list.get(0).getNome() + " " + list.get(1).getNome();

        Toast.makeText(getActivity(), "" + value, Toast.LENGTH_SHORT).show();
    }

    private void recuperarListaUsuarios(Professor professor) {

        final List<Usuario> list = new ArrayList<>();
        list.add(professor);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("aluno");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario;
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        usuario = dsp.getValue(Usuario.class);
                        list.add(usuario);
                    }
                    Collections.sort(list, new MoneySort());
                    plotarGraficos(list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void recuperarProfessor(final FirebaseUser user) {

        DatabaseReference db = null;
        if ("professor".equals(user.getPhotoUrl().toString())) {
            db = FirebaseDatabase.getInstance().getReference().child("professor").child(Base64Handler.codificarBase64(Objects.requireNonNull(user.getEmail())));
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Professor professor = dataSnapshot.getValue(Professor.class);
                        recuperarListaUsuarios(professor);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {

            db = FirebaseDatabase.getInstance().getReference().child("professor").child(Objects.requireNonNull(user.getDisplayName()));
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Professor professor = dataSnapshot.getValue(Professor.class);
                        recuperarListaUsuarios(professor);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    private FirebaseUser getCurrentUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        return user;
    }

}
