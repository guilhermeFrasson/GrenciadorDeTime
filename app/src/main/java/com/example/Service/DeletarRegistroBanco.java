package com.example.Service;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.getIntent;

import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class DeletarRegistroBanco extends AppCompatActivity {

    public static void deleteRegistro(String tabela, String IDDOC) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(tabela)
                .document(IDDOC) // id único do doc
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("FIRESTORE", "Documento deletado com sucesso!");
                })
                .addOnFailureListener(e -> {
                    Log.w("FIRESTORE", "Erro ao deletar documento", e);
                });

    }

}
