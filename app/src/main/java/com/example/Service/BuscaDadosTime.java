package com.example.Service;

import com.google.firebase.firestore.FirebaseFirestore;

public class BuscaDadosTime {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void verificaConfTime(String idTime, String tabela, String coluna, infoTimeCallback callback) {
        db.collection(tabela).document(idTime).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        boolean usaMensalidade = documentSnapshot.getBoolean(coluna);
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
