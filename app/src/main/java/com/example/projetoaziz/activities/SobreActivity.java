package com.example.projetoaziz.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetoaziz.R;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
    }

    public void abrirAgroplus(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.agroplusufv.com.br/")));
    }

    public void abrirUFV(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ufv.br/")));
    }

    public void abrirDER(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.der.ufv.br/")));
    }

    public void abrirAZIZ(View view) {
        //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("")));
    }

    public void abrirFABIANO(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/fabianogmjunior/")));
    }

    public void abrirJOHN(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/john-lucas-teixeira-9a07121a5")));
    }

    public void abrirPATRICIA(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/patr%C3%ADcia-b-souza-a57035175")));
    }


}
