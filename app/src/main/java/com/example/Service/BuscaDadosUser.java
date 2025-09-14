package com.example.Service;

import android.content.Intent;
import android.widget.Toast;

import com.example.gerenciadordetime.Login;
import com.example.gerenciadordetime.Menu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class BuscaDadosUser {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    String time;

    public String loginUser() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        String uid = user.getUid();
        if (user != null) {
            uid = user.getUid();
        }
        return uid;
    }

    public void buscarTime(TimeCallback callback) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        db.collection("GTUSUARIOS").document(loginUser()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String time = documentSnapshot.getString("TIME");
                        callback.onCallback(time); // devolve pelo callback
                    } else {
                        callback.onCallback(null); // documento não existe
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onCallback(null); // erro na busca
                });
    }
}
