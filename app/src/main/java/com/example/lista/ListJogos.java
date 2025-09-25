package com.example.lista;


import static com.example.Service.BuscaDadosUser.funcaoUsuario;
import static com.example.Service.BuscaDadosUser.idTimeUsuario;
import static com.example.Service.BuscaDadosUser.timeUsuario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Objetos.Jogo;
import com.example.adpter.JogoAdapter;
import com.example.gerenciadordetime.R;
import com.example.info.InfoJogo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ListJogos extends AppCompatActivity {
    List<Jogo> listaJogos = new ArrayList<Jogo>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_jogos);

        recyclerView = findViewById(R.id.recyclerJogos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listarJogoDoBanco();
    }

    private void listarJogoDoBanco() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("GTJOGO")
                .whereEqualTo("TIME", timeUsuario)
                .whereEqualTo("IDTIME", idTimeUsuario)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String timeadversario = document.getString("TIMEADVERSARIO");
                            String resultado = document.getString("RESULTADO");
                            Date dataJogo = document.getDate("DATADOJOGO");
                            long golsFeitos = document.getLong("GOLSFEITOS");
                            long golsSofridos = document.getLong("GOLSSOFRIDOS");
                            String idJogo = document.getId();


                            Jogo jogo = new Jogo(timeadversario, resultado, dataJogo, golsFeitos, golsSofridos, timeUsuario, idJogo);
                            listaJogos.add(jogo);
                        }

                        listaJogos.sort(Comparator.comparing(Jogo::getDataJogo).reversed());

                        // Aqui você seta a lista no Adapter do RecyclerView
                        JogoAdapter adapter = new JogoAdapter(listaJogos, jogo -> {
                            Intent intent = new Intent(this, InfoJogo.class);
                            intent.putExtra("TIME_ADIVERSARIO", jogo.getTimeAdversario());
                            intent.putExtra("RESULTADO", jogo.getResultado());
                            intent.putExtra("DATA_JOGO", jogo.getDataJogo());
                            intent.putExtra("GOLS_FEITOS", jogo.getGolsFeitos());
                            intent.putExtra("GOLS_SOFRIDOS", jogo.getGolsSofridos());
                            intent.putExtra("ID_JOGO", jogo.getIdJogo());
                            intent.putExtra("TIME", jogo.getTime());
                            startActivityForResult(intent, 1);
                        });
                        recyclerView.setAdapter(adapter);

                    } else {
                        Log.e("Firestore", "Erro ao buscar jogadores", task.getException());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        listaJogos.clear();
        if(requestCode == 1 && resultCode == RESULT_OK){
            boolean deletado = data.getBooleanExtra("deletado", false);
            if(deletado){
                listarJogoDoBanco(); // recarrega a lista do banco
            }
        }
    }
}