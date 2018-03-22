package com.mindfree.fbatista.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.mindfree.fbatista.organizze.config.ConfiguracaoFirebase;
import com.mindfree.fbatista.organizze.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.w3c.dom.Text;

public class PrincipalActivity extends AppCompatActivity {

    private Button sair;
    private FirebaseAuth auth;
    private MaterialCalendarView calendarView;
    private TextView mNome;
    private TextView mValor;
    private TextView mSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);

        calendarView = findViewById(R.id.calendarView);
        mNome = findViewById(R.id.tv_nome);
        mValor = findViewById(R.id.tv_valor);
        mSaldo = findViewById(R.id.tv_saldo);

        configurarCalendario();







    }

    public void adicionarDespesa(View view){
        startActivity(new Intent(PrincipalActivity.this, NovaDespesactivity.class));
    }

    public void adicionarReceita(View view){
        startActivity(new Intent(PrincipalActivity.this, NovaReceitaActivity.class));
    }

    public void configurarCalendario(){
        //Este metodo faz com que o calendario exiba os meses e os dias em portugues
        CharSequence[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio",
                "Junho", "Julho", "Agosto","Setembro", "Outubro", "Novembro", "Dezembro"};


        calendarView.setTitleMonths(meses);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.principal_sair:
                auth = ConfiguracaoFirebase.getAuth();
                auth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
