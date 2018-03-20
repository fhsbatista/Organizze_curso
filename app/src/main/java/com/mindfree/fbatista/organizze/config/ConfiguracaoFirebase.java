package com.mindfree.fbatista.organizze.config;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by fbatista on 18/03/18.
 */

public class ConfiguracaoFirebase {

    private static FirebaseAuth auth;

    public static FirebaseAuth getAuth(){
        if (auth == null) {
            auth = FirebaseAuth.getInstance();

        }
        return auth;

    }
}
