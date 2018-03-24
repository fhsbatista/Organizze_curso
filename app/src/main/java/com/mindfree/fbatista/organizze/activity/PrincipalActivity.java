package com.mindfree.fbatista.organizze.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mindfree.fbatista.organizze.adapter.MovimentoAdapter;
import com.mindfree.fbatista.organizze.config.ConfiguracaoFirebase;
import com.mindfree.fbatista.organizze.R;
import com.mindfree.fbatista.organizze.helper.Base64Custom;
import com.mindfree.fbatista.organizze.model.Movimentacao;
import com.mindfree.fbatista.organizze.model.Usuario;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    //Layout
    private MaterialCalendarView calendarView;
    private TextView mNome;
    private TextView mSaldo;
    private Double resumoDiario;
    private Double despesaTotal;
    private Double receitaTotal;
    private RecyclerView recyclerView;
    private MovimentoAdapter movimentoAdapter;

    //Firebase
    private FirebaseAuth auth = ConfiguracaoFirebase.getAuth();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerUsuario;
    private DatabaseReference movimentoRef;
    private ValueEventListener valueEventListenerMovimento;
    private String mesAnoSelecionado;
    private List<Movimentacao> movimentos = new ArrayList<>();


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
        recyclerView = findViewById(R.id.recyclerView);
        //Configura layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Configura adapter
        movimentoAdapter = new MovimentoAdapter(this, movimentos);
        recyclerView.setAdapter(movimentoAdapter);

        configurarCalendario();
        swipe();







    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarMovimentos();
        configurarCalendario();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        movimentoRef.removeEventListener(valueEventListenerMovimento);
    }



    public void configurarCalendario(){
        //Este metodo faz com que o calendario exiba os meses e os dias em portugues
        CharSequence[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio",
                "Junho", "Julho", "Agosto","Setembro", "Outubro", "Novembro", "Dezembro"};


        calendarView.setTitleMonths(meses);

        //Recuperando o mes e ano selecionado no calendario
        CalendarDay data = calendarView.getCurrentDate();
        //Formata o mes para que sempre tenha dois digitos
        String mesSelecionado = String.format("%02d", (data.getMonth() + 1));
        mesAnoSelecionado = String.valueOf( mesSelecionado + "" + data.getYear());

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                //Formata o mes para que sempre tenha dois digitos
                String mesSelecionado = String.format("%02d", (date.getMonth() + 1));
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + date.getYear());
                //Removendo um listener caso ja existente antes de criar um novo, para que nao fique duplicado
                movimentoRef.removeEventListener(valueEventListenerMovimento);
                recuperarMovimentos();

            }
        });

    }

    public void recuperarResumo(){
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        usuarioRef = firebaseRef.child("usuarios")
                                .child(idUsuario);

        valueEventListenerUsuario =  usuarioRef.addValueEventListener(new ValueEventListener() {
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

    public void recuperarMovimentos(){

        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);



        //Buscando as movimentacoes
        movimentoRef = firebaseRef.child("movimentacao")
                                  .child(idUsuario)
                                  .child(mesAnoSelecionado);



        //Recuperando as movimentacoes
        valueEventListenerMovimento = movimentoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                movimentos.clear();

                for (DataSnapshot item : dataSnapshot.getChildren()){
                    Movimentacao movimentacao = item.getValue(Movimentacao.class);
                    movimentacao.setKey(item.getKey());
                    movimentos.add(movimentacao);
                }
                movimentoAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void excluirMovimento(final RecyclerView.ViewHolder viewHolder){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exclusao de movimento");
        builder.setMessage("Voce tem certeza de que deseja excluir esta movimentacao?");
        builder.setCancelable(false);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                Movimentacao movimentacao = movimentos.get(position);
                movimentoRef.child(movimentacao.getKey()).removeValue();
                movimentoAdapter.notifyItemRemoved(position);

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PrincipalActivity.this, "Exclusao cancelada", Toast.LENGTH_SHORT).show();
                movimentoAdapter.notifyDataSetChanged();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
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

    public void adicionarDespesa(View view){
        startActivity(new Intent(PrincipalActivity.this, NovaDespesactivity.class));
    }

    public void adicionarReceita(View view){
        startActivity(new Intent(PrincipalActivity.this, NovaReceitaActivity.class));
    }

    public void swipe(){
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlasgs = ItemTouchHelper.START | ItemTouchHelper.END;


                return makeMovementFlags(dragFlags, swipeFlasgs);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimento(viewHolder);



            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }
}
