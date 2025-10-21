package com.example.Service;

import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class BuscaDadosTime {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static int proximoCodigo;

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

    public void buscaProximoCodigo() {
        db.collection("GTTIME").get()
                .addOnSuccessListener(documentSnapshot -> {
                    int i = 1;
                    long codigoAtual = 0;
                    for (QueryDocumentSnapshot document : documentSnapshot) {
                        long ultimoCodigo = document.getLong("IDTIME");
                        if (ultimoCodigo > codigoAtual) {
                            codigoAtual = ultimoCodigo;
                        }
                    }
                    proximoCodigo = (int) codigoAtual + 1;
                });

    }


}
