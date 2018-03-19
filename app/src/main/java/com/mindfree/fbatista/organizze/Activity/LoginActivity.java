package com.mindfree.fbatista.organizze.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.mindfree.fbatista.organizze.Config.ConfiguracaoFirebase;
import com.mindfree.fbatista.organizze.Model.Usuario;
import com.mindfree.fbatista.organizze.R;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail, mSenha;
    private Button mLogar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.et_email_login);
        mSenha = findViewById(R.id.et_senha_login);
        mLogar = findViewById(R.id.bt_login);

        mLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String senha = mSenha.getText().toString();

                if(email.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Digite um e-mail valido", Toast.LENGTH_SHORT).show();
                } else if(senha.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Informe uma senha", Toast.LENGTH_SHORT).show();
                } else{
                    Usuario usuario = new Usuario();
                    usuario.setEmail(email);
                    usuario.setSenha(senha);
                    validarLogin(usuario);
                }
            }
        });



    }
    public void validarLogin(Usuario usuario){
        auth = ConfiguracaoFirebase.getAuth();
        auth.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                } else{

                    String excecao = "";

                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        excecao = "E-mail informado e invalido";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Senha invalida";
                    }catch (Exception e){
                        excecao = e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
