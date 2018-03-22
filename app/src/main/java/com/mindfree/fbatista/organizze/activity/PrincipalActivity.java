package com.mindfree.fbatista.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mindfree.fbatista.organizze.config.ConfiguracaoFirebase;
import com.mindfree.fbatista.organizze.R;
import com.mindfree.fbatista.organizze.helper.Base64Custom;
import com.mindfree.fbatista.organizze.model.Usuario;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class PrincipalActivity extends AppCompatActivity {

    private Button sair;
    private FirebaseAuth auth = ConfiguracaoFirebase.getAuth();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private MaterialCalendarView calendarView;
    private TextView mNome;
    private TextView mSaldo;
    private Double resumoDiario;
    private Double despesaTotal;
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);

        calendarView = findViewById(R.id.calendarView);
        mNome = findViewById(R.id.tv_nome);
        mSaldo = findViewById(R.id.tv_saldo);

        configurarCalendario();
        recuperarResumo();







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

    public void recuperarResumo(){
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        DatabaseReference usuarioRef = firebaseRef.child("usuarios")
                .child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                despesaTotal = usuario.getTotalDespesa();
                receitaTotal = usuario.getTotalReceita();
                resumoDiario = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("###0.00");
                String resumoDiarioFormatado = decimalFormat.format(resumoDiario);

                mNome.setText("Ola " + usuario.getNome());
                mSaldo.setText("R$" + resumoDiarioFormatado);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                auth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
