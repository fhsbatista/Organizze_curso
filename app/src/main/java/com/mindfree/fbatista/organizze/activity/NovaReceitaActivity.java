package com.mindfree.fbatista.organizze.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mindfree.fbatista.organizze.R;
import com.mindfree.fbatista.organizze.config.ConfiguracaoFirebase;
import com.mindfree.fbatista.organizze.helper.Base64Custom;
import com.mindfree.fbatista.organizze.helper.DateCustom;
import com.mindfree.fbatista.organizze.model.Movimentacao;
import com.mindfree.fbatista.organizze.model.Usuario;

public class NovaReceitaActivity extends AppCompatActivity {

    private android.widget.EditText mValor;
    private android.support.design.widget.TextInputEditText mData;
    private android.support.design.widget.TextInputEditText mCategoria;
    private android.support.design.widget.TextInputEditText mDescricao;
    private android.support.design.widget.FloatingActionButton fabnovareceita;
    private Double receitaAtual;
    private Movimentacao movimentacao = new Movimentacao();
    private FirebaseAuth auth = ConfiguracaoFirebase.getAuth();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_receita);
        this.fabnovareceita = (FloatingActionButton) findViewById(R.id.fab_nova_receita);
        this.mDescricao = (TextInputEditText) findViewById(R.id.et_descricao);
        this.mCategoria = (TextInputEditText) findViewById(R.id.et_categoria);
        this.mData = (TextInputEditText) findViewById(R.id.et_data);
        this.mValor = (EditText) findViewById(R.id.et_valor);

        mData.setText(DateCustom.dataAtual());
        recuperaReceitaAtual();

    }

    public void salvarReceita(View view){

        if(validarDados()){
            Double receitaPreenchida = Double.parseDouble(mValor.getText().toString());

            movimentacao.setTipo("r");
            movimentacao.setData(mData.getText().toString());
            movimentacao.setDescricao(mDescricao.getText().toString());
            movimentacao.setCategoria(mCategoria.getText().toString());
            movimentacao.setValor(receitaPreenchida);
            movimentacao.salvar();

            Double receitaAtualizada = receitaPreenchida + receitaAtual;
            atualizarReceitaAtual(receitaAtualizada);
            finish();
        }

    }

    public Boolean validarDados(){

        if(mValor.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite um valor", Toast.LENGTH_SHORT).show();
            return false;
        }else if(mData.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite uma data", Toast.LENGTH_SHORT).show();
            return false;
        }else if(mCategoria.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite uma categoria", Toast.LENGTH_SHORT).show();
            return false;
        }else if(mDescricao.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite uma descricao", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }

    }

    public void recuperaReceitaAtual(){
        //Este metodo e responsavel por recuperar o total de receitas do usuario
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios")
                                                  .child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaAtual = usuario.getTotalReceita();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void atualizarReceitaAtual(Double receitaAtualizada){
        //Este metodo atualiza a receita total do usuario
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios")
                .child(idUsuario);

        usuarioRef.child("totalReceita").setValue(receitaAtualizada);


    }
}
