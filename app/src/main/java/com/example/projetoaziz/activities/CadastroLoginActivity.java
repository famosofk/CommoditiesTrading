
package com.example.projetoaziz.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projetoaziz.R;
import com.example.projetoaziz.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class CadastroLoginActivity extends AppCompatActivity {

    private String tipoCadastrado = "";
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
        tipoCadastrado = "professor";

    }

    public void exibirCadastroAluno(View view) {
        setContentView(R.layout.activity_professor_cadastro);
        tipoCadastrado = "aluno";

    }



    public void criarProfessor(View view) {
        Usuario professorCadastrando = new Usuario();
        progressBar = findViewById(R.id.layoutProgressBar1);
        progressBar.setVisibility(View.VISIBLE);
        EditText matricula = findViewById(R.id.matriculaCadastroAluno);
        professorCadastrando.setMatricula(matricula.getText().toString());
        EditText editNome = findViewById(R.id.nomeCadastroProfessor);
        professorCadastrando.setNome(editNome.getText().toString());
        EditText editEmail = findViewById(R.id.emailCadastroProfessor);
        EditText editUniversidade = findViewById(R.id.universidadeCadastroProfessor);
        EditText editSobrenome = findViewById(R.id.sobrenomeCadastroProfessor);
        professorCadastrando.setSobrenome(editSobrenome.getText().toString());
        String EMAIL = editEmail.getText().toString().trim().toLowerCase();
        professorCadastrando.setEmail(EMAIL);
        String UNIVERSIDADE = editUniversidade.getText().toString().trim().toUpperCase();
        professorCadastrando.setUniversidade(UNIVERSIDADE);
        professorCadastrando.atualizarID();

        efetuarCadastroUsuario(professorCadastrando);



    }


    private void efetuarCadastroUsuario(final Usuario professorCadastrando) {

        EditText editSenha = findViewById(R.id.senhaCadastroProfessor);
        mAuth.createUserWithEmailAndPassword(professorCadastrando.getEmail(), editSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            professorCadastrando.salvar(tipoCadastrado, professorCadastrando.getId());
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(Uri.parse(tipoCadastrado))
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

