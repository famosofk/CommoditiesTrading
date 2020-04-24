
package com.example.projetoaziz.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projetoaziz.R;
import com.example.projetoaziz.models.Monitor;
import com.example.projetoaziz.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class CadastroLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_cadastro_login);


    }


    public void exibirCadastroProfessor(View view) {
        setContentView(R.layout.activity_professor_cadastro);
        TextView codigoMonitor = findViewById(R.id.textmatricula);
        EditText codigo = findViewById(R.id.matriculaCadastroAluno);
        codigo.setVisibility(View.GONE);
        codigoMonitor.setVisibility(View.GONE);
    }

    public void exibirCadastroAluno(View view) {
        setContentView(R.layout.activity_professor_cadastro);
        TextView codigoMonitor = findViewById(R.id.textView11);
        EditText codigo = findViewById(R.id.editCodigoMonitor);
        codigo.setVisibility(View.GONE);
        codigoMonitor.setVisibility(View.GONE);
    }

    public void autorizaCadastroMonitor(View view) {
            exibirCadastroMonitor();
    }

    private void exibirCadastroMonitor() {
        setContentView(R.layout.activity_cadastro_monitor);
    /*    RecyclerView recycler = findViewById(R.id.recyclerCadastroMonitor);
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
        })); */

    }

    public void criarMonitor(View view) {
        Monitor monitor = new Monitor();
       /* EditText codigoMonitor = findViewById(R.id.codigoCadastroMonitor);
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
        } */

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
        Usuario professorCadastrando = new Usuario();
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

        if (editCogido.getText().toString().isEmpty()) {
            efetuarCadastroUsuario(professorCadastrando, 2);
        } else {
            efetuarCadastroUsuario(professorCadastrando, 1);
        }


    }


    private void efetuarCadastroUsuario(final Usuario professorCadastrando, final int tipo) {

        EditText editSenha = findViewById(R.id.senhaCadastroProfessor);
        mAuth.createUserWithEmailAndPassword(professorCadastrando.getEmail(), editSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Uri uri = Uri.parse("erro");
                            if (tipo == 1) {
                                uri = Uri.parse("professor");
                            } else if (tipo == 2) {
                                uri = Uri.parse("aluno");
                            }
                            professorCadastrando.salvar(uri.toString(), professorCadastrando.getId());
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .setDisplayName(professorCadastrando.getId())
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

