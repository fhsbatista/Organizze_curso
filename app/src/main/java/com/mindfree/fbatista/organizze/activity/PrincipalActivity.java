package com.mindfree.fbatista.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.mindfree.fbatista.organizze.config.ConfiguracaoFirebase;
import com.mindfree.fbatista.organizze.R;

public class PrincipalActivity extends AppCompatActivity {

    private Button sair;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        sair = findViewById(R.id.botao);

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = ConfiguracaoFirebase.getAuth();
                if (auth.getCurrentUser() != null) {
                    auth.signOut();
                    finish();
                }
            }
        });
    }

    public void adicionarDespesa(View view){
        startActivity(new Intent(PrincipalActivity.this, NovaDespesactivity.class));
    }

    public void adicionarReceita(View view){
        startActivity(new Intent(PrincipalActivity.this, NovaReceitaActivity.class));
    }

}
