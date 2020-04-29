package com.example.projetoaziz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoaziz.R;
import com.example.projetoaziz.adapters.RecyclerItemClickListener;
import com.example.projetoaziz.adapters.SelecionarTurmaAdapter;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GerenciarTurmasActivity extends AppCompatActivity {
    FirebaseUser user;
    Usuario usuario;
    SelecionarTurmaAdapter adapter;
    List<Turma> list = new ArrayList<>();
    TextView toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_turmas);
        toast = findViewById(R.id.toastProvisorio);
        FirebaseAuth mauth = ConfiguracaoDatabase.getFirebaseAutenticacao();
        user = mauth.getCurrentUser();
        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child(user.getPhotoUrl().toString()).child(Base64Handler.codificarBase64(user.getEmail()));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    usuario = dataSnapshot.getValue(Usuario.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void exibirCriarTurma(View view) {
        LinearLayout selecao = findViewById(R.id.selectorAction);
        selecao.setVisibility(View.GONE);
        LinearLayout cadastrarTurma = findViewById(R.id.cadastrarTurma);
        cadastrarTurma.setVisibility(View.VISIBLE);
    }

    public void criarTurma(View view) {
        Turma turma = new Turma();
        EditText senhaMonitor = findViewById(R.id.senhaMonitorCadastroTurma);
        EditText nome = findViewById(R.id.nomeTurmaCadastro);
        EditText creditos = findViewById(R.id.creditosTurmaCadastro);
        EditText senha = findViewById(R.id.senhaTurmaCadastro);
        CheckBox requerSenha = findViewById(R.id.requerSenhaCheckbox);
        turma.setNome(nome.getText().toString());
        turma.setId(Base64Handler.codificarBase64(nome.getText().toString()));
        turma.setIdProfessor(Base64Handler.codificarBase64(user.getEmail()));
        ListaCommodities lista = criarListaCommoditiesProfessor();
        if (lista.getListaCommodities().size() > 10) {
            Toast.makeText(this, "Não é possível criar turmas com mais de 10 commodities.", Toast.LENGTH_SHORT).show();
        } else {
            if (!creditos.getText().toString().isEmpty()) {
                if (!nome.getText().toString().isEmpty()) {
                    lista.setCreditos(Float.parseFloat(creditos.getText().toString()));
                    lista.setPatrimonio(lista.getCreditos());
                    lista.setPatrimonioAnterior(lista.getCreditos());
                    turma.setSenhaMonitor(senhaMonitor.getText().toString());
                    turma.setListaCommodities(lista);
                    turma.getMonitores().add(usuario.getId());
                    turma.setSenha(senha.getText().toString());
                    if (requerSenha.isChecked()) {
                        turma.setRequerSenha(true);
                    }
                    DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child("turmas").child(turma.getId());
                    db.setValue(turma);
                    db = ConfiguracaoDatabase.getFirebaseDatabase().child("listaCommodities").child(turma.getId()).child(Base64Handler.codificarBase64(user.getEmail()));
                    lista.setIdDono(usuario.getId());
                    lista.setNome(usuario.getNome());
                    db.setValue(lista);
                    List<String> salas = usuario.getListaTurmas();
                    salas.add(turma.getId());
                    usuario.setListaTurmas(salas);
                    db = ConfiguracaoDatabase.getFirebaseDatabase().child(user.getPhotoUrl().toString()).child(Base64Handler.codificarBase64(user.getEmail()));
                    db.setValue(usuario);
                    finish();
                } else {
                    Toast.makeText(this, "Defina o nome da turma, por favor.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Defina o valor de créditos iniciais, por favor.", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private ListaCommodities criarListaCommoditiesProfessor() {
        ListaCommodities list = new ListaCommodities();
        list.setCreditos((float) 100000.00);

        CheckBox acucar = findViewById(R.id.sugarBox);
        if (acucar.isChecked()) {
            Commodity commodity = new Commodity("Açúcar", (float) 0.00);
            commodity.setUnidade("R$/sc");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Açúcar")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox abobora = findViewById(R.id.aboboraBox);
        if (abobora.isChecked()) {
            Commodity commodity = new Commodity("Abóbora", (float) 0.00);
            commodity.setUnidade("R$/kg");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Abóbora")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox alface = findViewById(R.id.checkBoxAlface);
        if (alface.isChecked()) {
            Commodity commodity = new Commodity("Alface", (float) 0.00);
            commodity.setUnidade("R$/kg");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Alface")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox algodao = findViewById(R.id.checkBoxAlgodao);
        if (algodao.isChecked()) {
            Commodity commodity = new Commodity("Algodão", (float) 0.00);
            commodity.setUnidade("R$/lp");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Algodão")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox arroz = findViewById(R.id.checkBoxArroz);
        if (arroz.isChecked()) {
            Commodity commodity = new Commodity("Arroz", (float) 0.00);
            commodity.setUnidade("R$/sc");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Arroz")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox batata = findViewById(R.id.checkBoxBatata);
        if (batata.isChecked()) {
            Commodity commodity = new Commodity("Batata", (float) 0.00);
            commodity.setUnidade("R$/kg");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Batata")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox banana = findViewById(R.id.checkBoxBanana);
        if (banana.isChecked()) {
            Commodity commodity = new Commodity("Banana", (float) 0.00);
            commodity.setUnidade("R$/kg");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Banana")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox bezerro = findViewById(R.id.checkBoxBezerro);
        if (bezerro.isChecked()) {
            Commodity commodity = new Commodity("Bezerro", (float) 0.00);
            commodity.setUnidade("R$/ub");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Bezerro")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox boi = findViewById(R.id.checkBoxBoi);
        if (boi.isChecked()) {
            Commodity commodity = new Commodity("Boi gordo", (float) 0.00);
            commodity.setUnidade("R$/@");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Boi gordo")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox cafe = findViewById(R.id.checkBoxCoffee);
        if (cafe.isChecked()) {
            Commodity commodity = new Commodity("Café", (float) 0.00);
            commodity.setUnidade("R$/sc");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Café")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox cebola = findViewById(R.id.checkBoxCebola);
        if (cebola.isChecked()) {
            Commodity commodity = new Commodity("Cebola", (float) 0.00);
            commodity.setUnidade("R$/kg");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Cebola")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }


        CheckBox cenoura = findViewById(R.id.checkBoxCenoura);
        if (cenoura.isChecked()) {
            Commodity commodity = new Commodity("Cenoura", (float) 0.00);
            commodity.setUnidade("R$/kg");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Cenoura")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox feijao = findViewById(R.id.checkBoxFeijao);
        if (feijao.isChecked()) {
            Commodity commodity = new Commodity("Feijão", (float) 0.00);
            commodity.setUnidade("R$/sc");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Feijão")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }


        CheckBox frango = findViewById(R.id.checkBoxChicken);
        if (frango.isChecked()) {
            Commodity commodity = new Commodity("Frango", (float) 0.00);
            commodity.setUnidade("R$/kg");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Frango")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox goiaba = findViewById(R.id.checkboxGoiaba);
        if (goiaba.isChecked()) {
            Commodity commodity = new Commodity("Goiaba", (float) 0.00);
            commodity.setUnidade("R$/kg");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Goiaba")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox laranja = findViewById(R.id.checkBoxLaranja);
        if (laranja.isChecked()) {
            Commodity commodity = new Commodity("Laranja", (float) 0.00);
            commodity.setUnidade("R$/kg");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Laranja")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }


        CheckBox leite = findViewById(R.id.checkBoxLeite);
        if (leite.isChecked()) {
            Commodity commodity = new Commodity("Leite", (float) 0.00);
            commodity.setUnidade("R$/l");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Leite")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox limao = findViewById(R.id.checkBoxLimao);
        if (limao.isChecked()) {
            Commodity commodity = new Commodity("Limão", (float) 0.00);
            commodity.setUnidade("R$/kg");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Limão")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox mandioca = findViewById(R.id.checkBoxMandioca);
        if (mandioca.isChecked()) {
            Commodity commodity = new Commodity("Mandioca", (float) 0.00);
            commodity.setUnidade("R$/t");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Mandioca")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox milho = findViewById(R.id.checkBoxMilho);
        if (milho.isChecked()) {
            Commodity commodity = new Commodity("Milho", (float) 0.00);
            commodity.setUnidade("R$/sc");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Milho")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox ovos = findViewById(R.id.checkBoxOvos);
        if (ovos.isChecked()) {
            Commodity commodity = new Commodity("Ovos", (float) 0.00);
            commodity.setUnidade("R$/30dz");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Ovos")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox soja = findViewById(R.id.checkBoxSoja);
        if (soja.isChecked()) {
            Commodity commodity = new Commodity("Soja", (float) 0.00);
            commodity.setUnidade("R$/sc");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Soja")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox suino = findViewById(R.id.checkBoxSuino);
        if (suino.isChecked()) {
            Commodity commodity = new Commodity("Suíno", (float) 0.00);
            commodity.setUnidade("R$/kg");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Suíno")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }

        CheckBox tomate = findViewById(R.id.checkBoxTomate);
        if (tomate.isChecked()) {
            Commodity commodity = new Commodity("Tomate", (float) 0.00);
            commodity.setUnidade("R$/sc");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Tomate")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }


        CheckBox trigo = findViewById(R.id.checkBoxTrigo);
        if (trigo.isChecked()) {
            Commodity commodity = new Commodity("Trigo", (float) 0.00);
            commodity.setUnidade("R$/t");
            list.getListaCommodities().add(commodity);
        } else {
            int i;
            Boolean tem = false;
            for (i = 0; i < list.getListaCommodities().size(); i++) {
                if (list.getListaCommodities().get(i).equals("Trigo")) {
                    tem = true;
                    break;
                }
            }
            if (tem) {
                list.getListaCommodities().remove(i);
            }
        }


        return list;
    }

    public void exibirSelecionarTurma(View view) {
        LinearLayout selecao = findViewById(R.id.selectorAction);
        selecao.setVisibility(View.GONE);
        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child("turmas");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        Turma turma = dsp.getValue(Turma.class);
                        list.add(turma);
                    }
                    fazerListagemTurmas();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    private void fazerListagemTurmas() {
        final LinearLayout selecionarTurma = findViewById(R.id.selecionarTurma);
        selecionarTurma.setVisibility(View.VISIBLE);
        RecyclerView recycler = findViewById(R.id.selecionarTurmaRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SelecionarTurmaAdapter(list, this);
        recycler.setAdapter(adapter);
        recycler.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final CardView confirmation = findViewById(R.id.confirmationPopUp);
                confirmation.setVisibility(View.VISIBLE);
                selecionarTurma.setVisibility(View.INVISIBLE);
                final Turma turma = list.get(position);
                EditText senhaSalaConfirmation = findViewById(R.id.senhaSalaConfirmation);
                if (turma.getRequerSenha()) {
                    senhaSalaConfirmation.setVisibility(View.VISIBLE);
                } else {
                    senhaSalaConfirmation.setVisibility(View.INVISIBLE);
                }
                Button confirmar = findViewById(R.id.confirmarButton);
                confirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toast.setText("");
                        if (turma.getRequerSenha()) {
                            EditText senhaSalaConfirmation = findViewById(R.id.senhaSalaConfirmation);
                            if (senhaSalaConfirmation.getText().toString().equals(turma.getSenha())) {
                                cadastrarUsuarioEmUmaTurma(turma);
                            } else {
                                toast.setText("Senha incorreta.");
                            }
                        } else {
                            cadastrarUsuarioEmUmaTurma(turma);
                        }
                    }
                });
                Button negar = findViewById(R.id.recusarButton);
                negar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmation.setVisibility(View.GONE);
                        selecionarTurma.setVisibility(View.VISIBLE);
                        toast.setText(" ");
                    }
                });
            }
        }));
    }

    private void cadastrarUsuarioEmUmaTurma(Turma turma) {
        CheckBox isMonitor = findViewById(R.id.checkBoxMonitor);
        EditText senhaMonitor = findViewById(R.id.senhaMonitorParticipar);
        if (isMonitor.isChecked()) {
            if (senhaMonitor.getText().toString().equals(turma.getSenhaMonitor())) {
                toast.setText("");
                turma.getMonitores().add(usuario.getId());
                Log.e("monitor: ", "correta.");
                DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child("turmas").child(turma.getId());
                db.setValue(turma);
                efetuarCadastroTurma(turma);
            } else if (senhaMonitor.getText().toString() != turma.getSenhaMonitor()) {
                toast.setText("Senha de MONITOR incorreta");
                Log.e("monitor: ", "incorreta");
            }
        } else {
            efetuarCadastroTurma(turma);
        }



    }

    private void efetuarCadastroTurma(Turma turma) {
        List<String> list = usuario.getListaTurmas();
        list.add(turma.getId());
        usuario.setListaTurmas(list);
        usuario.salvar(user.getPhotoUrl().toString(), Base64Handler.codificarBase64(user.getEmail()));

        ListaCommodities listaCommodities = turma.getListaCommodities();
        listaCommodities.setNome(usuario.getNome());
        listaCommodities.setIdDono(usuario.getId());
        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child("listaCommodities").child(turma.getId()).child(Base64Handler.codificarBase64(user.getEmail()));
        db.setValue(listaCommodities);
        startActivity(new Intent(GerenciarTurmasActivity.this, SelecionarTurmaActivity.class));
        finish();

    }




}
