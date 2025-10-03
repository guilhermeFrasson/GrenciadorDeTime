package com.example.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class BuscaDadosUser {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public String loginUser() {
        user = auth.getCurrentUser();

        String uid = user.getUid();
        if (user != null) {
            uid = user.getUid();
        }
        return uid;
    }

    public void buscarInfosUsuario(String coluna, infoUsuarioCallback callback) {
        db.collection("GTUSUARIOS").document(loginUser()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String time = documentSnapshot.getString(coluna);
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
