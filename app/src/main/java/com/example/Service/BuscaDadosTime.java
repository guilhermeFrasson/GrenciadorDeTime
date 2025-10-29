package com.example.Service;

import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class BuscaDadosTime {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static int proximoCodigo;

    public void verificaConfTime(int idTime, String tabela, String coluna, infoTimeCallback callback) {
        db.collection(tabela).document(String.valueOf(idTime)).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        boolean usaMensalidade = documentSnapshot.getBoolean(coluna);
                        callback.onCallback(usaMensalidade);
                    } else {
                        callback.onCallback(false);
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onCallback(false);
                });
    }

    public void buscaProximoCodigo() {
        db.collection("GTTIME")
                .orderBy("IDTIME", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()){
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        Long codigo = doc.getLong("IDTIME");
                        if (codigo != null) {
                            proximoCodigo = codigo.intValue() + 1;
                        } else {
                            proximoCodigo = 1; // caso o campo codigo seja null
                        }
                    } else {
                        proximoCodigo = 1; // caso a coleção esteja vazia
                    }
                }
        );
    }
}
