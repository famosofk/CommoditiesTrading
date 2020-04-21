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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Agroplus Preços");
        setSupportActionBar(toolbar);

        configuraNavigationBottom();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewPager, new CotacoesFragment()).commit();
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
            case R.id.menuSair:
                deslogarUsuario();
                break;
            case R.id.menuSobre:
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
                OrdensFragment ordens = null;
                CotacoesFragment cotacoes = null;
                ChartsFragment chart = null;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_cavalos:
                        cotacoes = new CotacoesFragment();
                        fragmentTransaction.replace(R.id.viewPager, cotacoes).commit();
                        /*if (ordens != null) {
                            fragmentTransaction.remove(ordens).commit();
                        }
                        if (chart != null) {
                            fragmentTransaction.remove(chart).commit();
                        }*/

                        return true;

                    case R.id.navigation_receitas:
                        ordens = new OrdensFragment();
                        fragmentTransaction.replace(R.id.viewPager, ordens).commit();
                        /*if (cotacoes != null) {
                            fragmentTransaction.remove(cotacoes).commit();
                        }
                        if (chart != null) {
                            fragmentTransaction.remove(chart).commit();
                        }*/
                        return true;

                    case R.id.navigation_relatorios:
                        chart = new ChartsFragment();
                        fragmentTransaction.replace(R.id.viewPager, chart).commit();
                     /*   if (ordens != null) {
                            fragmentTransaction.remove(ordens).commit();
                        }
                        if (cotacoes != null) {
                            fragmentTransaction.remove(cotacoes).commit();
                        } */
                        return true;

                }
                return false;
            }
        });
    }

}

