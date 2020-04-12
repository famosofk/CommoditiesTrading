package com.example.projetoaziz.activities;

import android.os.Bundle;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Nome do aplicativo");
        setSupportActionBar(toolbar);

        configuraNavigationBottom();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewPager, new CotacoesFragment()).commit();
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
                        fragmentTransaction.replace(R.id.viewPager, new CotacoesFragment()).commit();
                        return true;

                    case R.id.navigation_receitas:
                        fragmentTransaction.replace(R.id.viewPager, new OrdensFragment()).commit();
                        return true;

                    case R.id.navigation_relatorios:
                        fragmentTransaction.replace(R.id.viewPager, new ChartsFragment()).commit();
                        return true;

                }
                return false;
            }
        });
    }

}

