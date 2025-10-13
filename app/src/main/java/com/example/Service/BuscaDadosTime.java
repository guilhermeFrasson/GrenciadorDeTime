package com.example.Service;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

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

    public void gerarEVerificarCodigoUnico(CodigoUnicoCallback callback) {
        // 1. Gera um código aleatório
        String caracteresPermitidos = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigo = new StringBuilder(8);
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            codigo.append(caracteresPermitidos.charAt(random.nextInt(caracteresPermitidos.length())));
        }
        final String codigoGerado = codigo.toString();

        // 2. Verifica se o documento com este ID/código já existe na coleção "GTTIME"
        db.collection("GTTIME").whereEqualTo("CODIGOTIME", codigoGerado).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        // Se a busca não retornou nada, o código é ÚNICO! Sucesso!
                        callback.onCodigoEncontrado(codigoGerado);
                    } else {
                        // Se a busca retornou algo, o código JÁ EXISTE. Tentamos de novo.
                        gerarEVerificarCodigoUnico(callback); // <-- A MÁGICA DA RECURSÃO ACONTECE AQUI
                    }
                })
                .addOnFailureListener(e -> {
                    // Se deu erro na comunicação com o Firebase
                    callback.onFalha(e);
                });
    }


}
