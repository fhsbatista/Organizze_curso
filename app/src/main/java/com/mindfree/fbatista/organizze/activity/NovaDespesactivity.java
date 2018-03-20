package com.mindfree.fbatista.organizze.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.mindfree.fbatista.organizze.R;
import com.mindfree.fbatista.organizze.helper.DateCustom;
import com.mindfree.fbatista.organizze.model.Movimentacao;

public class NovaDespesactivity extends AppCompatActivity {

    private EditText mValor;
    private TextInputEditText mData;
    private TextInputEditText mCategoria;
    private TextInputEditText mDescricao;
    private FloatingActionButton fabnovadespesa;
    private Movimentacao movimentacao;

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


    }

    public void salvarDespesa(View view){

        movimentacao = new Movimentacao();
        movimentacao.setValor(Double.parseDouble(mValor.getText().toString()));
        movimentacao.setCategoria(mCategoria.getText().toString());
        movimentacao.setDescricao(mDescricao.getText().toString());
        movimentacao.setData(mData.getText().toString());
        movimentacao.setTipo("d");

        movimentacao.salvar();
    }
}
