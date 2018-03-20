package com.mindfree.fbatista.organizze.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.mindfree.fbatista.organizze.config.ConfiguracaoFirebase;

/**
 * Created by fbatista on 18/03/18.
 */

public class Usuario {

    private String idUsuario;
    private String nome;
    private String email;
    private String senha;
    private Double totalDespesa;
    private Double totalReceita;

    public Usuario() {
    }

    public void salvar(){

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios")
                .child(this.idUsuario)
                .setValue(this);
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public String getNome() {
        return nome;
    }



    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
