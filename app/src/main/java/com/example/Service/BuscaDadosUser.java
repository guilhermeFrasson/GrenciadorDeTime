package com.example.Service;

import android.content.Intent;
import android.widget.Toast;

import com.example.gerenciadordetime.Login;
import com.example.gerenciadordetime.Menu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class BuscaDadosUser {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;

    public static String timeUsuario;

    public static String idTimeUsuario;

    public static String funcaoUsuario;



    public String loginUser() {
        user = auth.getCurrentUser();

        String uid = user.getUid();
        if (user != null) {
            uid = user.getUid();
        }
        return uid;
    }

    public void buscarTime() {
        db.collection("GTUSUARIOS").document(loginUser()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        timeUsuario = documentSnapshot.getString("TIME");
                    }
                });
    }

    public void buscarIDTime( ) {
        db.collection("GTUSUARIOS").document(loginUser()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        idTimeUsuario = documentSnapshot.getString("IDTIME");
                    }
                });
    }

    public void buscarFuncaoUsuario() {
        db.collection("GTUSUARIOS").document(loginUser()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        funcaoUsuario = documentSnapshot.getString("FUNCAO");
                    }
                });
    }


}
