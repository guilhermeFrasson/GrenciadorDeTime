package com.example.Service;

import com.google.firebase.firestore.FirebaseFirestore;

public class BuscaDadosTime {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void verificaUsaMensalidade(String idTime, UsaMensalidadeCallback callback) {
        db.collection("GTTIME").document(idTime).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        boolean usaMensalidade = documentSnapshot.getBoolean("USAMENSALIDADE");
                        callback.onCallback(usaMensalidade); // devolve pelo callback
                    } else {
                        callback.onCallback(false); // documento não existe
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onCallback(false); // erro na consulta
                });
    }
}
