package com.example.projetoaziz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.adapters.RecyclerItemClickListener;
import com.example.projetoaziz.adapters.TurmaAdapter;
import com.example.projetoaziz.helpers.Base64Handler;
import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.example.projetoaziz.models.Turma;
import com.example.projetoaziz.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class SelecionarTurmaActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private TurmaAdapter adapter;
    private Usuario usuario;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_turma);
        recycler = findViewById(R.id.recyclerListagemTurmas);
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Agroplus Preços");
        setSupportActionBar(toolbar);

        FirebaseAuth mauth = ConfiguracaoDatabase.getFirebaseAutenticacao();
        user = mauth.getCurrentUser();
        recuperarUsuario(user);
    }

    private void recuperarUsuario(FirebaseUser user) {
        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child(user.getPhotoUrl().toString()).child(Base64Handler.codificarBase64(user.getEmail()));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
                fazerListagem(usuario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void fazerListagem(final Usuario usuario) {



        TextView semTurma = findViewById(R.id.semTurmaText);
        if (usuario.getListaTurmas().size() != 0) {
            semTurma.setVisibility(View.GONE);
            Set<String> set = new LinkedHashSet<>(usuario.getListaTurmas());
            adapter = new TurmaAdapter(new ArrayList<String>(set), this);
            recycler.setLayoutManager(new LinearLayoutManager(this));
            new ItemTouchHelper(itemTouchSimpleCallBack).attachToRecyclerView(recycler);
            recycler.setAdapter(adapter);
            recycler.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                  final String caminho = usuario.getListaTurmas().get(position);

                    DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child("turmas").child(caminho);
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Intent i = new Intent(SelecionarTurmaActivity.this, MainActivity.class);
                                i.putExtra("idTurma", caminho);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(SelecionarTurmaActivity.this, "Turma foi excluída. Arraste para o lado para removê-la de sua lista.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }));

        } else {
            semTurma.setText(R.string.sem_turma_intro);
            semTurma.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_selecao_turma, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sairTurma:
                deslogarUsuario();
                break;
            case R.id.sobreTurma:
                startActivity(new Intent(SelecionarTurmaActivity.this, SobreActivity.class));
                finish();
                break;
            case R.id.adicionarTurma:
                startActivity(new Intent(SelecionarTurmaActivity.this, GerenciarTurmasActivity.class));
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        startActivity(new Intent(SelecionarTurmaActivity.this, CadastroLoginActivity.class));
        finish();
    }

    ItemTouchHelper.SimpleCallback itemTouchSimpleCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final String value = usuario.getListaTurmas().get(viewHolder.getAdapterPosition());
            usuario.getListaTurmas().remove(viewHolder.getAdapterPosition());
            adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child(user.getPhotoUrl().toString()).child(user.getDisplayName());
            db.setValue(usuario);
            db = ConfiguracaoDatabase.getFirebaseDatabase().child("listaCommodities").child(value);
            db.child(user.getDisplayName()).removeValue();
            db = ConfiguracaoDatabase.getFirebaseDatabase().child("turmas").child(value);
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Turma turma = dataSnapshot.getValue(Turma.class);
                    if (dataSnapshot.exists()) {
                        if (turma.getIdProfessor().equals(user.getDisplayName())) {
                            DatabaseReference excluir = ConfiguracaoDatabase.getFirebaseDatabase().child("turmas").child(value);
                            excluir.removeValue();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            Toast.makeText(SelecionarTurmaActivity.this, "Turma apagada.", Toast.LENGTH_SHORT).show();
        }
    };


}
