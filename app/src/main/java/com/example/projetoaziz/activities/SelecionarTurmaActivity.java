package com.example.projetoaziz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.adapters.RecyclerItemClickListener;
import com.example.projetoaziz.adapters.TurmaAdapter;
import com.example.projetoaziz.helpers.Base64Handler;
import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.example.projetoaziz.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class SelecionarTurmaActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private TurmaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_turma);
        recycler = findViewById(R.id.recyclerListagemTurmas);
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Agroplus Preços");
        setSupportActionBar(toolbar);

        FirebaseAuth mauth = ConfiguracaoDatabase.getFirebaseAutenticacao();
        FirebaseUser user = mauth.getCurrentUser();
        recuperarUsuario(user);
    }

    private void recuperarUsuario(FirebaseUser user) {
        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child(user.getPhotoUrl().toString()).child(user.getDisplayName())
                .child(Base64Handler.codificarBase64(user.getEmail()));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                fazerListagem(usuario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void fazerListagem(final Usuario usuario) {
        adapter = new TurmaAdapter(usuario.getListaTurmas(), this);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        recycler.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String caminho = usuario.getListaTurmas().get(position);
                Intent i = new Intent(SelecionarTurmaActivity.this, MainActivity.class);
                i.putExtra("idTurma", caminho);
                startActivity(i);
                finish();
            }
        }));


    }
}
