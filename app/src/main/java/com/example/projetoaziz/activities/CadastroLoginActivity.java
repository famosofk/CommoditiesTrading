package com.example.projetoaziz.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.example.projetoaziz.models.Commodity;
import com.example.projetoaziz.models.Professor;
import com.example.projetoaziz.models.RegisterData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
    private ProgressBar progressBar;

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
        progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.VISIBLE);
        List<Commodity> list = new ArrayList<>();
        EditText editNome = findViewById(R.id.nomeCadastroProfessor);
        EditText editEmail = findViewById(R.id.emailCadastroProfessor);
        EditText editUniversidade = findViewById(R.id.universidadeCadastroProfessor);
        EditText editSobrenome = findViewById(R.id.sobrenomeCadastroProfessor);
        EditText editSenha = findViewById(R.id.senhaCadastroProfessor);
        EditText editCogido = findViewById(R.id.editCodigoMonitor);
        String EMAIL = editEmail.getText().toString().trim().toLowerCase();
        String UNIVERSIDADE = editUniversidade.getText().toString().trim().toUpperCase();

        CheckBox algodao = findViewById(R.id.checkBoxAlgodao);
        if (algodao.isChecked()) {
            Commodity commodity = new Commodity("Algodão", (float) 0.00);
            list.add(commodity);
        }
        CheckBox amendoim = findViewById(R.id.checkBoxAmendoim);
        if (amendoim.isChecked()) {
            Commodity commodity = new Commodity("Amendoim", (float) 0.00);
            list.add(commodity);
        }
        CheckBox arroz = findViewById(R.id.checkBoxArroz);
        if (arroz.isChecked()) {
            Commodity commodity = new Commodity("Arroz", (float) 0.00);
            list.add(commodity);
        }
        CheckBox bezerro = findViewById(R.id.checkBoxBezerro);
        if (bezerro.isChecked()) {
            Commodity commodity = new Commodity("Bezerro", (float) 0.00);
            list.add(commodity);
        }
        CheckBox boi = findViewById(R.id.checkBoxBoi);
        if (boi.isChecked()) {
            Commodity commodity = new Commodity("Boi gordo", (float) 0.00);
            list.add(commodity);
        }
        CheckBox cafe = findViewById(R.id.checkBoxCafe);
        if (cafe.isChecked()) {
            Commodity commodity = new Commodity("Café", (float) 0.00);
            list.add(commodity);
        }
        CheckBox feijao = findViewById(R.id.checkBoxFeijao);
        if (feijao.isChecked()) {
            Commodity commodity = new Commodity("Feijão", (float) 0.00);
            list.add(commodity);
        }
        CheckBox frango = findViewById(R.id.checkBoxFrango);
        if (frango.isChecked()) {
            Commodity commodity = new Commodity("Frango", (float) 0.00);
            list.add(commodity);
        }
        CheckBox milho = findViewById(R.id.checkBoxMilho);
        if (milho.isChecked()) {
            Commodity commodity = new Commodity("Milho", (float) 0.00);
            list.add(commodity);
        }
        CheckBox soja = findViewById(R.id.checkBoxSoja);
        if (soja.isChecked()) {
            Commodity commodity = new Commodity("Soja", (float) 0.00);
            list.add(commodity);
        }
        CheckBox sorgo = findViewById(R.id.checkBoxSorgo);
        if (sorgo.isChecked()) {
            Commodity commodity = new Commodity("Sorgo", (float) 0.00);
            list.add(commodity);
        }
        CheckBox trigo = findViewById(R.id.checkBoxTrigo);
        if (trigo.isChecked()) {
            Commodity commodity = new Commodity("Trigo", (float) 0.00);
            list.add(commodity);
        }

        professorCadastrando = new Professor();
        professorCadastrando.setEmail(EMAIL);
        professorCadastrando.setNome(editNome.getText().toString());
        professorCadastrando.setUniversidade(UNIVERSIDADE);
        professorCadastrando.setSobrenome(editSobrenome.getText().toString());
        professorCadastrando.setCodigoMonitor(editCogido.getText().toString().trim());
        professorCadastrando.atualizarID();
        professorCadastrando.setListaCommodities(list);
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
                            Uri uri = Uri.parse("professor");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .build();
                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(CadastroLoginActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(CadastroLoginActivity.this, "Não foi possível cadastra-lo.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }


    public void cadastrarAluno(View view) {
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);


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
        aluno.setListaCommodities(professorSelecionado.getListaCommodities());


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
                            Uri uri = Uri.parse("aluno");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .setDisplayName(aluno.getProfessorID())
                                        .build();
                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(CadastroLoginActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    }
                                });
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CadastroLoginActivity.this, "Não foi possível cadastra-lo.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent main = new Intent(CadastroLoginActivity.this, MainActivity.class);
            startActivity(main);
            finish();
        }
    }
}
