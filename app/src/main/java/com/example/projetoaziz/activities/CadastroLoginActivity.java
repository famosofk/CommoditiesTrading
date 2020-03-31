package com.example.projetoaziz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.adapters.ListagemProfessorAdapter;
import com.example.projetoaziz.adapters.RecyclerItemClickListener;
import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.example.projetoaziz.models.Aluno;
import com.example.projetoaziz.models.Professor;
import com.example.projetoaziz.models.RegisterData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CadastroLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Professor professorCadastrando;
    private Aluno aluno;
    private Professor professorSelecionado;
    private List<Professor> listaProfessores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_login);
        mAuth = FirebaseAuth.getInstance();
        professorSelecionado = new Professor();


    }

    public void exibirLogin(View view) {
        setContentView(R.layout.activity_login);
    }

    public void exibirCadastro(View view) {
        setContentView(R.layout.activity_cadastro);
    }

    public void iniciarCadastroProfessor(View view) {
        setContentView(R.layout.activity_professor_cadastro);
    }

    public void iniciarCadastroAluno(View view) {


        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("professor");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Professor recuperado;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    recuperado = dsp.getValue(Professor.class);
                    listaProfessores.add(recuperado);
                }

                if (listaProfessores.size() > 0) {
                    exibirCadastroAluno();
                } else {
                    Toast.makeText(CadastroLoginActivity.this, "Desculpe, ainda não há professor cadastrado.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    private void exibirCadastroAluno() {
        setContentView(R.layout.activity_cadastro_aluno);
        RecyclerView recycler = findViewById(R.id.recyclerCadastroAluno);

        ListagemProfessorAdapter adapter = new ListagemProfessorAdapter(listaProfessores, this);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        recycler.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                professorSelecionado = listaProfessores.get(position);
                TextView nome = findViewById(R.id.professorSelecionadoNome);
                nome.setText(professorSelecionado.getNome());
                EditText universidadeAluno = findViewById(R.id.universidadeCadastroAluno);
                universidadeAluno.setText(professorSelecionado.getUniversidade());
            }
        }));
    }

    public void cadastrarProfessor(View view) {

        EditText editNome = findViewById(R.id.nomeCadastroProfessor);
        EditText editEmail = findViewById(R.id.emailCadastroProfessor);
        EditText editUniversidade = findViewById(R.id.universidadeCadastroProfessor);
        EditText editSobrenome = findViewById(R.id.sobrenomeCadastroProfessor);
        EditText editSenha = findViewById(R.id.senhaCadastroProfessor);
        String EMAIL = editEmail.getText().toString().trim().toLowerCase();
        String UNIVERSIDADE = editUniversidade.getText().toString().trim().toUpperCase();

        professorCadastrando = new Professor();

        professorCadastrando.setEmail(EMAIL);
        professorCadastrando.setNome(editNome.getText().toString());
        professorCadastrando.setUniversidade(UNIVERSIDADE);
        professorCadastrando.setSobrenome(editSobrenome.getText().toString());
        professorCadastrando.atualizarID();
        mAuth.createUserWithEmailAndPassword(professorCadastrando.getEmail(), editSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            professorCadastrando.salvar();

                            DatabaseReference firebaseRef = ConfiguracaoDatabase.getFirebaseDatabase();
                            String nomeSobrenome = professorCadastrando.getNome() + " " + professorCadastrando.getSobrenome();
                            DatabaseReference universidade = firebaseRef.child("universidades").child(professorCadastrando.getUniversidade()).child(nomeSobrenome);
                            String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                            RegisterData data = new RegisterData(currentDate);
                            universidade.setValue(data);

                            startActivity(new Intent(CadastroLoginActivity.this, MainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(CadastroLoginActivity.this, "Não foi possível cadastra-lo.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    public void cadastrarAluno(View view) {


        aluno = new Aluno();
        EditText nomeAluno = findViewById(R.id.nomeCadastroAluno);
        EditText sobrenomeAluno = findViewById(R.id.sobrenomeCadastroAluno);
        EditText matriculaAluno = findViewById(R.id.matriculaCadastroAluno);
        EditText universidadeAluno = findViewById(R.id.universidadeCadastroAluno);
        EditText emailAluno = findViewById(R.id.emailCadastroAluno);
        EditText senhaAluno = findViewById(R.id.senhaCadastroAluno);


        aluno.setEmail(emailAluno.getText().toString());
        aluno.setUniversidade(universidadeAluno.getText().toString());
        aluno.setNome(nomeAluno.getText().toString());
        aluno.setSobrenome(sobrenomeAluno.getText().toString());
        aluno.setMatricula(matriculaAluno.getText().toString());
        aluno.atualizarID();
        aluno.setProfessorID(professorSelecionado.getId());


        mAuth.createUserWithEmailAndPassword(aluno.getEmail(), senhaAluno.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            aluno.salvar();

                            DatabaseReference firebaseRef = ConfiguracaoDatabase.getFirebaseDatabase();
                            String nomeSobrenome = aluno.getMatricula() + "-" + aluno.getNome() + "-" + aluno.getSobrenome();
                            DatabaseReference turma = firebaseRef.child("turmas").child(professorSelecionado.getUniversidade()).child(professorSelecionado.getNome() + professorSelecionado.getSobrenome()).child(nomeSobrenome);
                            String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                            RegisterData registrarTurma = new RegisterData(currentDate);
                            turma.setValue(registrarTurma);
                            startActivity(new Intent(CadastroLoginActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(CadastroLoginActivity.this, "Não foi possível cadastra-lo.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();
       /* FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent main = new Intent(CadastroLoginActivity.this, MainActivity.class);
            startActivity(main);
            finish();
        }*/
    }
}
