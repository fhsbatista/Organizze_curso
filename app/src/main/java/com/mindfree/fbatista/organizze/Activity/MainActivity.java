package com.mindfree.fbatista.organizze.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.mindfree.fbatista.organizze.Activity.CadastroActivity;
import com.mindfree.fbatista.organizze.Activity.LoginActivity;
import com.mindfree.fbatista.organizze.Config.ConfiguracaoFirebase;
import com.mindfree.fbatista.organizze.R;

public class MainActivity extends IntroActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonNextVisible(
                false);
        setButtonBackVisible(false);

        addSlide(new FragmentSlide.Builder()
                    .background(android.R.color.white)
                    .fragment(R.layout.intro1)
                    .build());

        addSlide(new FragmentSlide.Builder()
                    .background(android.R.color.white)
                    .fragment(R.layout.intro2)
                    .build());

        addSlide(new FragmentSlide.Builder()
                    .background(android.R.color.white)
                    .fragment(R.layout.intro3)
                    .build());

        addSlide(new FragmentSlide.Builder()
                    .background(android.R.color.white)
                    .fragment(R.layout.intro_login)
                    .canGoForward(false)
                    .build());


    }

    @Override
    protected void onStart() {
        super.onStart();
        validarUsuarioLogado();
    }

    public void btLogin(View view){

        startActivity( new Intent(this, LoginActivity.class));
    }

    public void btCadastrar(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void validarUsuarioLogado(){

        auth = ConfiguracaoFirebase.getAuth();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, PrincipalActivity.class));
        }

    }


}
