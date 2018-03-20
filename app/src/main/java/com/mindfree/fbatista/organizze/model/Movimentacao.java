package com.mindfree.fbatista.organizze.model;

import com.google.firebase.database.DatabaseReference;
import com.mindfree.fbatista.organizze.config.ConfiguracaoFirebase;
import com.mindfree.fbatista.organizze.helper.Base64Custom;
import com.mindfree.fbatista.organizze.helper.DateCustom;

/**
 * Created by fbatista on 20/03/18.
 */

public class Movimentacao {

    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private Double valor;

    public Movimentacao() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public void salvar() {

        String emailUsuario = ConfiguracaoFirebase.getAuth()
                .getCurrentUser()
                .getEmail();

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("movimentacao")
                .child(Base64Custom.codificarBase64(emailUsuario))
                .child(DateCustom.getPeriodo(this.data))
                .push()
                .setValue(this);

    }
}
