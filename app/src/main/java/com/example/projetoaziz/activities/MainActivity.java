package com.example.projetoaziz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projetoaziz.R;
import com.example.projetoaziz.fragments.ChartsFragment;
import com.example.projetoaziz.fragments.CotacoesFragment;
import com.example.projetoaziz.fragments.OrdensFragment;
import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {
    String caminho = "";
    CotacoesFragment cotacoes;
    OrdensFragment ordens;
    ChartsFragment charts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Agroplus Preços");
        setSupportActionBar(toolbar);
        cotacoes = new CotacoesFragment();
        ordens = new OrdensFragment();
        charts = new ChartsFragment();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            caminho = bundle.getString("idTurma");
            Bundle bundle2 = new Bundle();
            bundle2.putString("idTurma", caminho);
            cotacoes.setArguments(bundle2);
            ordens.setArguments(bundle2);
            charts.setArguments(bundle2);

        }

        configuraNavigationBottom();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewPager, cotacoes).commit();
    }


    public void deslogarUsuario() {
        FirebaseAuth mauth = ConfiguracaoDatabase.getFirebaseAutenticacao();
        try {
            if (mauth.getCurrentUser() != null) {
                mauth.signOut();
            }
            Intent i = new Intent(MainActivity.this, CadastroLoginActivity.class);
            startActivity(i);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sairTurma:
                deslogarUsuario();
                break;
            case R.id.sobreTurma:
                startActivity(new Intent(MainActivity.this, SobreActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }



    private void configuraNavigationBottom() {

        BottomNavigationViewEx navigationView;
        navigationView = findViewById(R.id.bottomNavigation);
        navigationView.enableAnimation(true);
        navigationView.enableItemShiftingMode(true);
        navigationView.enableShiftingMode(true);
        habilitarNavegacao(navigationView);

    }


    private void habilitarNavegacao(BottomNavigationViewEx viewEx) {

        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                switch (menuItem.getItemId()) {
                    case R.id.navigation_cavalos:
                        fragmentTransaction.replace(R.id.viewPager, cotacoes).commit();
                        return true;

                    case R.id.navigation_receitas:
                        fragmentTransaction.replace(R.id.viewPager, ordens).commit();
                        return true;

                    case R.id.navigation_relatorios:
                        fragmentTransaction.replace(R.id.viewPager, charts).commit();
                        return true;

                }
                return false;
            }
        });
    }

}

