package com.mindfree.fbatista.organizze.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by fbatista on 18/03/18.
 */

public class ConfiguracaoFirebase {

    private static FirebaseAuth auth;
    private static DatabaseReference firebase;

    public static FirebaseAuth getAuth(){
        if (auth == null) {
            auth = FirebaseAuth.getInstance();

        }
        return auth;

    }

    public static DatabaseReference getFirebaseDatabase(){

        if (firebase == null) {
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;

    }
}
