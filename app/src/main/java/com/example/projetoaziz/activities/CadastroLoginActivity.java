
package com.example.projetoaziz.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.adapters.ListagemProfessorAdapter;
import com.example.projetoaziz.adapters.RecyclerItemClickListener;
import com.example.projetoaziz.helpers.Base64Handler;
import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.example.projetoaziz.models.Aluno;
import com.example.projetoaziz.models.Commodity;
import com.example.projetoaziz.models.ListaCommodities;
import com.example.projetoaziz.models.Monitor;
import com.example.projetoaziz.models.Professor;
import com.example.projetoaziz.models.Turma;
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

import java.util.ArrayList;
import java.util.List;

public class CadastroLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Professor professorSelecionado = null;
    private List<Professor> listaProfessores = new ArrayList<>();
    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        verificarSeHaProfessoresCadastrados();


    }


    public void exibirCadastroProfessor(View view) {
        setContentView(R.layout.activity_professor_cadastro);
    }

    public void autorizaCadastroMonitor(View view) {
        if (listaProfessores.size() > 0) {
            exibirCadastroMonitor();
        } else {
            Toast.makeText(this, "Ainda não há professores cadastrados.", Toast.LENGTH_SHORT).show();
        }
    }

    private void exibirCadastroMonitor() {
        setContentView(R.layout.activity_cadastro_monitor);
        RecyclerView recycler = findViewById(R.id.recyclerCadastroMonitor);
        ListagemProfessorAdapter adapter = new ListagemProfessorAdapter(listaProfessores, this);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        recycler.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                professorSelecionado = listaProfessores.get(position);
                TextView nome = findViewById(R.id.professorSelecionadoMonitor);
                nome.setText(professorSelecionado.getNome());
            }
        }));

    }

    public void criarMonitor(View view) {
        Monitor monitor = new Monitor();
        EditText codigoMonitor = findViewById(R.id.codigoCadastroMonitor);
        if (codigoMonitor.getText().toString().equals(professorSelecionado.getCodigoMonitor())) {
            EditText nome = findViewById(R.id.nomeCadastroMonitor);
            EditText sobrenome = findViewById(R.id.sobrenomeCadastroMonitor);
            EditText matricula = findViewById(R.id.matriculaCadastroMonitor);
            EditText email = findViewById(R.id.emailCadastroMonitor);
            monitor.setIdProfessor(professorSelecionado.getId());
            String EMAIL = email.getText().toString().toLowerCase().trim();
            monitor.setListaCommodities(professorSelecionado.getListaCommodities());

            monitor.setEmail(EMAIL);
            monitor.atualizarID();
            monitor.setNome(nome.getText().toString());
            monitor.setSobrenome(sobrenome.getText().toString());
            monitor.setMatricula(matricula.getText().toString());
            efetuarCadastroMonitor(monitor);

        } else {
            Toast.makeText(this, "Não foi possível criar o monitor. Código inválido.", Toast.LENGTH_SHORT).show();
        }

    }

    private void efetuarCadastroMonitor(final Monitor monitor) {

        EditText editSenha = findViewById(R.id.senhaCadastroMonitor);
        mAuth.createUserWithEmailAndPassword(monitor.getEmail(), editSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            monitor.salvar();
                            Uri uri = Uri.parse("monitor");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .setDisplayName(monitor.getIdProfessor())
                                        .build();
                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(CadastroLoginActivity.this, SelecionarTurmaActivity.class));
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

    public void criarProfessor(View view) {
        Professor professorCadastrando = new Professor();
        progressBar = findViewById(R.id.layoutProgressBar1);
        progressBar.setVisibility(View.VISIBLE);
        EditText editNome = findViewById(R.id.nomeCadastroProfessor);
        professorCadastrando.setNome(editNome.getText().toString());
        EditText editEmail = findViewById(R.id.emailCadastroProfessor);
        EditText editUniversidade = findViewById(R.id.universidadeCadastroProfessor);
        EditText editSobrenome = findViewById(R.id.sobrenomeCadastroProfessor);
        professorCadastrando.setSobrenome(editSobrenome.getText().toString());
        EditText editCogido = findViewById(R.id.editCodigoMonitor);
        professorCadastrando.setCodigoMonitor(editCogido.getText().toString().trim());
        String EMAIL = editEmail.getText().toString().trim().toLowerCase();
        professorCadastrando.setEmail(EMAIL);
        String UNIVERSIDADE = editUniversidade.getText().toString().trim().toUpperCase();
        professorCadastrando.setUniversidade(UNIVERSIDADE);

        professorCadastrando.atualizarID();

        Turma turma = new Turma();
        String nomeTurma1 = professorCadastrando.getNome() + professorCadastrando.getSobrenome() + "Turma1";
        turma.setNome(nomeTurma1.trim());
        turma.setListaCommodities(criarListaCommoditiesProfessor());
        turma.atualizarID();
        turma.setIdProfessor(professorCadastrando.getId());
        List<String> lista = new ArrayList<>();
        lista.add(turma.getId());
        professorCadastrando.setListaTurmas(lista);
        efetuarCadastroProfessor(professorCadastrando, turma, turma.getId(), professorCadastrando.getId());


    }

    private ListaCommodities criarListaCommoditiesProfessor() {
        ListaCommodities list = new ListaCommodities();
        list.setCreditos((float) 100000.00);
        CheckBox acucar = findViewById(R.id.sugarBox);
        if (acucar.isChecked()) {
            Commodity commodity = new Commodity("Açúcar", (float) 0.00);
            commodity.setUnidade("R$/kg");
            list.getListaCommodities().add(commodity);
        }
        CheckBox algodao = findViewById(R.id.checkBoxAlgodao);
        if (algodao.isChecked()) {
            Commodity commodity = new Commodity("Algodão", (float) 0.00);
            commodity.setUnidade("R$/lb");
            list.getListaCommodities().add(commodity);
        }
        CheckBox amendoim = findViewById(R.id.checkBoxAmendoim);
        if (amendoim.isChecked()) {
            Commodity commodity = new Commodity("Amendoim", (float) 0.00);
            commodity.setUnidade("R$/sc");
            list.getListaCommodities().add(commodity);
        }
        CheckBox arroz = findViewById(R.id.checkBoxArroz);
        if (arroz.isChecked()) {
            Commodity commodity = new Commodity("Arroz", (float) 0.00);
            commodity.setUnidade("R$/sc");
            list.getListaCommodities().add(commodity);
        }
        CheckBox bezerro = findViewById(R.id.checkBoxBezerro);
        if (bezerro.isChecked()) {
            Commodity commodity = new Commodity("Bezerro", (float) 0.00);
            commodity.setUnidade("R$/cab");
            list.getListaCommodities().add(commodity);
        }
        CheckBox boi = findViewById(R.id.checkBoxBoi);
        if (boi.isChecked()) {
            Commodity commodity = new Commodity("Boi gordo", (float) 0.00);
            commodity.setUnidade("R$/@");
            list.getListaCommodities().add(commodity);
        }
        CheckBox cafe = findViewById(R.id.checkBoxCafe);
        if (cafe.isChecked()) {
            Commodity commodity = new Commodity("Café", (float) 0.00);
            commodity.setUnidade("R$/sc");
            list.getListaCommodities().add(commodity);
        }
        CheckBox feijao = findViewById(R.id.checkBoxFeijao);
        if (feijao.isChecked()) {
            Commodity commodity = new Commodity("Feijão", (float) 0.00);
            commodity.setUnidade("R$/sc");
            list.getListaCommodities().add(commodity);
        }
        CheckBox frango = findViewById(R.id.checkBoxFrango);
        if (frango.isChecked()) {
            Commodity commodity = new Commodity("Frango", (float) 0.00);
            commodity.setUnidade("R$/kg");
            list.getListaCommodities().add(commodity);
        }
        CheckBox milho = findViewById(R.id.checkBoxMilho);
        if (milho.isChecked()) {
            Commodity commodity = new Commodity("Milho", (float) 0.00);
            commodity.setUnidade("R$/sc");
            list.getListaCommodities().add(commodity);
        }
        CheckBox soja = findViewById(R.id.checkBoxSoja);
        if (soja.isChecked()) {
            Commodity commodity = new Commodity("Soja", (float) 0.00);
            commodity.setUnidade("R$/sc");
            list.getListaCommodities().add(commodity);
        }
        CheckBox suino = findViewById(R.id.checkBoxSuino);
        if (suino.isChecked()) {
            Commodity commodity = new Commodity("Suíno", (float) 0.00);
            commodity.setUnidade("R$/kg");
            list.getListaCommodities().add(commodity);
        }
        CheckBox sorgo = findViewById(R.id.checkBoxSorgo);
        if (sorgo.isChecked()) {
            Commodity commodity = new Commodity("Sorgo", (float) 0.00);
            commodity.setUnidade("R$/sc");
            list.getListaCommodities().add(commodity);
        }
        CheckBox trigo = findViewById(R.id.checkBoxTrigo);
        if (trigo.isChecked()) {
            Commodity commodity = new Commodity("Trigo", (float) 0.00);
            commodity.setUnidade("R$/t");
            list.getListaCommodities().add(commodity);
        }

        return list;
    }

    private void efetuarCadastroProfessor(final Professor professorCadastrando, final Turma turmas, final String caminho, final String idProfessor) {

        EditText editSenha = findViewById(R.id.senhaCadastroProfessor);
        mAuth.createUserWithEmailAndPassword(professorCadastrando.getEmail(), editSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            professorCadastrando.salvar();
                            final Uri uri = Uri.parse("professor");
                            FirebaseUser user = mAuth.getCurrentUser();

                            DatabaseReference ref = ConfiguracaoDatabase.getFirebaseDatabase().child("listaCommodities").child(Base64Handler.codificarBase64(user.getEmail())).child(caminho);
                            ref.setValue(turmas.getListaCommodities());
                            if (user != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .setDisplayName(professorCadastrando.getId())
                                        .build();

                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DatabaseReference turma = ConfiguracaoDatabase.getFirebaseDatabase().child("turmas").child(caminho);
                                            turma.setValue(turmas);
                                            startActivity(new Intent(CadastroLoginActivity.this, SelecionarTurmaActivity.class));
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

    private void verificarSeHaProfessoresCadastrados() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("professor");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Professor recuperado;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    recuperado = dsp.getValue(Professor.class);
                    listaProfessores.add(recuperado);
                }
                setContentView(R.layout.activity_cadastro_login);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void permitirCadastroAluno(View view) {
        if (listaProfessores.size() > 0) {
            exibirCadastroAluno();
        } else {
            Toast.makeText(this, "Não há professores cadastrados no sistema.", Toast.LENGTH_SHORT).show();
        }


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

    public void criarAluno(View view) {
        progressBar = findViewById(R.id.layoutProgressBar2);
        progressBar.setVisibility(View.VISIBLE);
        Aluno aluno = new Aluno();
        EditText nomeAluno = findViewById(R.id.nomeCadastroAluno);
        EditText sobrenomeAluno = findViewById(R.id.sobrenomeCadastroAluno);
        EditText matriculaAluno = findViewById(R.id.matriculaCadastroAluno);
        EditText universidadeAluno = findViewById(R.id.universidadeCadastroAluno);
        EditText emailAluno = findViewById(R.id.emailCadastroAluno);
        aluno.setEmail(emailAluno.getText().toString());
        aluno.setUniversidade(universidadeAluno.getText().toString());
        aluno.setNome(nomeAluno.getText().toString());
        aluno.setSobrenome(sobrenomeAluno.getText().toString());
        aluno.setMatricula(matriculaAluno.getText().toString());
        aluno.atualizarID();
        if (professorSelecionado != null) {
            aluno.setProfessorID(professorSelecionado.getId());
        aluno.setListaCommodities(professorSelecionado.getListaCommodities());
        for (Commodity c : aluno.getListaCommodities()) {
            c.setQuantidade(0);
        }
            efetuarCadastroAluno(aluno);
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Selecione um professor.", Toast.LENGTH_SHORT).show();
        }



    }

    private void efetuarCadastroAluno(final Aluno aluno) {
        EditText senhaAluno = findViewById(R.id.senhaCadastroAluno);
        mAuth.createUserWithEmailAndPassword(aluno.getEmail(), senhaAluno.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            aluno.salvar();
                            DatabaseReference firebaseRef = ConfiguracaoDatabase.getFirebaseDatabase();
                            String nomeSobrenome = aluno.getMatricula() + "-" + aluno.getNome() + "-" + aluno.getSobrenome();

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
                                            startActivity(new Intent(CadastroLoginActivity.this, SelecionarTurmaActivity.class));
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

    public void exibirSelecaoTipoCadastro(View view) {
        setContentView(R.layout.activity_cadastro);
    }

    public void loginUsuario(View view) {
        final LinearLayout loading = findViewById(R.id.loadingLogin);
        loading.setVisibility(View.VISIBLE);

        EditText editEmail = findViewById(R.id.emailLogin);
        EditText editSenha = findViewById(R.id.senhaLogin);

        String email = editEmail.getText().toString().toLowerCase().trim();

        mAuth.signInWithEmailAndPassword(email, editSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(CadastroLoginActivity.this, SelecionarTurmaActivity.class);
                            startActivity(i);
                            finish();

                        } else {
                            Toast.makeText(CadastroLoginActivity.this, "Não foi possível realizar o login.", Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                        }

                        // ...
                    }
                });

    }

    public void exibirLogin(View view) {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent main = new Intent(CadastroLoginActivity.this, SelecionarTurmaActivity.class);
            startActivity(main);
            finish();
        }
    }

    public void abrirSobreLogin(View view) {
        Intent i = new Intent(CadastroLoginActivity.this, SobreActivity.class);
        startActivity(i);
    }
}

