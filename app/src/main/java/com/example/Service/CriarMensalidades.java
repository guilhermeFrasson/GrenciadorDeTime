package com.example.Service;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.util.Log;

import com.example.Objetos.Jogador;
import com.example.info.InfoJogador;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CriarMensalidades {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<Jogador> listaJogadores = new ArrayList<>();


    private void listarJogadoresDoBanco() {
        String timeUsuario = "Bonde da stela";


        db.collection("GTJOGADOR")
                .whereEqualTo("TIME", timeUsuario)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nome = document.getString("NOME");
                            String idJogador = document.getId();

                            Jogador jogador = new Jogador(idJogador,nome,timeUsuario);
                            listaJogadores.add(jogador);
                        }
                        
                    } else {
                        Log.e("Firestore", "Erro ao buscar jogadores", task.getException());
                    }
                });
    }

    public void verificarRegistroMensal(String jogadorId) {
        String mesAtual = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());



        // ID único = jogador + mês
        String docId = jogadorId + "_" + mesAtual;

        DocumentReference ref = db.collection("GTMENSALIDADE").document(docId);

        ref.get().addOnSuccessListener(document -> {
            if (!document.exists()) {
                criarNovoRegistro(ref, jogadorId, mesAtual);
            } else {
                Log.d("Firestore", "Já existe registro para " + jogadorId + " no mês " + mesAtual);
            }
        });


    }

    private void criarNovoRegistro(DocumentReference ref, String jogadorId, String mesAtual) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("jogadorId", jogadorId);
        dados.put("mes", mesAtual);
        dados.put("criadoEm", new Date());
        dados.put("valor", 0);

        ref.set(dados).addOnSuccessListener(aVoid ->
                Log.d("Firestore", "Novo registro criado para " + jogadorId + " no mês " + mesAtual)
        );
    }
}
