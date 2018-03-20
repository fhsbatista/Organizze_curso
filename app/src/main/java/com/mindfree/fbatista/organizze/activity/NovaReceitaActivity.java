package com.mindfree.fbatista.organizze.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.mindfree.fbatista.organizze.R;
import com.mindfree.fbatista.organizze.helper.DateCustom;

public class NovaReceitaActivity extends AppCompatActivity {

    private android.widget.EditText mValor;
    private android.support.design.widget.TextInputEditText mData;
    private android.support.design.widget.TextInputEditText mCategoria;
    private android.support.design.widget.TextInputEditText mDescricao;
    private android.support.design.widget.FloatingActionButton fabnovareceita;

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

    }
}
