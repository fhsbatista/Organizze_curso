package com.mindfree.fbatista.organizze.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class NovaDespesactivity extends AppCompatActivity {

    private EditText mValor;
    private TextInputEditText mData;
    private TextInputEditText mCategoria;
    private TextInputEditText mDescricao;
    private FloatingActionButton fabnovadespesa;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth auth = ConfiguracaoFirebase.getAuth();
    private Double despesaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_despesa);
        this.fabnovadespesa = (FloatingActionButton) findViewById(R.id.fab_nova_despesa);
        this.mDescricao = (TextInputEditText) findViewById(R.id.et_descricao);
        this.mCategoria = (TextInputEditText) findViewById(R.id.et_categoria);
        this.mData = (TextInputEditText) findViewById(R.id.et_data);
        this.mValor = (EditText) findViewById(R.id.et_valor);

        mData.setText(DateCustom.dataAtual());
        recuperaDespesaAtual();


    }

    public void salvarDespesa(View view){

        if(validarDespesa()){

            Double despesaPreenchida = Double.parseDouble(mValor.getText().toString());
            movimentacao = new Movimentacao();
            movimentacao.setValor(despesaPreenchida);
            movimentacao.setCategoria(mCategoria.getText().toString());
            movimentacao.setDescricao(mDescricao.getText().toString());
            movimentacao.setData(mData.getText().toString());
            movimentacao.setTipo("d");
            movimentacao.salvar();

            Log.i("teste", "Pedindo dado");
            Double despesaAtualizada = despesaPreenchida + despesaAtual;
            atualizarDespesaAtual(despesaAtualizada);
        }


    }

    public Boolean validarDespesa(){

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


    public void recuperaDespesaAtual(){
    //Este metodo e responsavel por recuperar com o saldo do usuario
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
        Log.i("teste", "Acessando firebase");

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                Log.i("teste", "firebaseacessado");
                if (usuario.getTotalDespesa() == null) {
                    despesaAtual = 0.00;
                } else{
                    despesaAtual = usuario.getTotalDespesa();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void atualizarDespesaAtual(Double despesaAtualizada){
        //Este metodo atualizar o atributo "Despesa Total" do usuario no firebase

        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
        usuarioRef.child("totalDespesa").setValue(despesaAtualizada);



    }
}
