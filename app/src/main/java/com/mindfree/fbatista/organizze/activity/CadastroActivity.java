package com.mindfree.fbatista.organizze.activity;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.mindfree.fbatista.organizze.config.ConfiguracaoFirebase;
import com.mindfree.fbatista.organizze.helper.Base64Custom;
import com.mindfree.fbatista.organizze.model.Usuario;
import com.mindfree.fbatista.organizze.R;

public class CadastroActivity extends AppCompatActivity {

    private EditText mNome, mEmail, mSenha;
    private Button mCadastrar;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mNome = findViewById(R.id.et_nome);
        mEmail = findViewById(R.id.et_email);
        mSenha = findViewById(R.id.et_senha);
        mCadastrar = findViewById(R.id.bt_cadastrar);

        mCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = mNome.getText().toString();
                String email = mEmail.getText().toString();
                String senha = mSenha.getText().toString();

                if(nome.isEmpty()){
                    Toast.makeText(CadastroActivity.this, "Preencha o nome", Toast.LENGTH_SHORT).show();
                    mNome.requestFocus();
                } else if(email.isEmpty()){
                    Toast.makeText(CadastroActivity.this, "Preencha um email", Toast.LENGTH_SHORT).show();
                    mEmail.requestFocus();
                } else if(senha.isEmpty()){
                    Toast.makeText(CadastroActivity.this, "Preencha uma senha", Toast.LENGTH_SHORT).show();
                    mSenha.requestFocus();
                } else{
                    Usuario usuario = new Usuario();
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setSenha(senha);
                    cadastrarUsuario(usuario);
                }
            }
        });



    }

    public void cadastrarUsuario(final Usuario usuario){

        auth = ConfiguracaoFirebase.getAuth();
        auth.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar();
                    finish();
                } else{

                    String mensagemErro = "";
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthWeakPasswordException e){
                        mensagemErro = "Digite uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        mensagemErro = "Digite um e-mail valido";
                    }catch(FirebaseAuthUserCollisionException e){
                        mensagemErro = "Ja exite um cadastro com e-mail informado";
                    }catch (Exception e){
                        mensagemErro = e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this, mensagemErro, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
