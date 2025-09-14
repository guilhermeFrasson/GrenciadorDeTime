package com.example.lista;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Objetos.Estatisticas;
import com.example.Objetos.Jogador;
import com.example.Service.BuscaDadosUser;
import com.example.Service.JogadorAdapter;
import com.example.cadastro.CadMarcadorGols;
import com.example.gerenciadordetime.R;
import com.example.info.InfoJogador;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Comparator;
import java.util.stream.Collectors; // se for usar collect()
import java.util.List;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListJogador extends AppCompatActivity {
    List<Jogador> listaJogadores = new ArrayList<>();
    private FirebaseFirestore db;
    private String timeUser;
    private RecyclerView recyclerView;
    private JogadorAdapter adapter;
    private String tipoUsuario, timeUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_jogador);

        recyclerView = findViewById(R.id.recyclerJogadores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tipoUsuario = getIntent().getStringExtra("TIPOUSUARIO");
        timeUsuario = getIntent().getStringExtra("TIMEUSUARIO");

        listaJogadores = listarJogadoresDoBanco();
    }

    private List<Jogador> listarJogadoresDoBanco() {
        List<Jogador> lista = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        db.collection("GTJOGADOR")
                .whereEqualTo("TIME", timeUsuario)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nome = document.getString("NOME");
                            Date dataNascimento = document.getDate("DATANASCIMENTO");
                            String posicao = document.getString("POSICAO");
                            String pernaDominante = document.getString("PERNADOMINANTE");
                            long gols = document.getLong("GOLS");
                            long assistencias = document.getLong("ASSISTENCIAS");
                            String idJogador = document.getId();

                            Jogador jogador = new Jogador(idJogador,nome, dataNascimento, posicao, pernaDominante, gols, assistencias,timeUsuario);
                            listaJogadores.add(jogador);
                        }

                        listaJogadores.sort(Comparator.comparing(Jogador::getNome));

                        // Aqui você seta a lista no Adapter do RecyclerView
                        adapter = new JogadorAdapter(listaJogadores, jogador -> {
                            Intent intent = new Intent(this, InfoJogador.class);
                            intent.putExtra("TIMEUSER", jogador.getTime());
                            intent.putExtra("NOME_JOGADOR", jogador.getNome());
                            intent.putExtra("POSICAO_JOGADOR", jogador.getPosicao());
                            intent.putExtra("PRENA_DOMINANTE_JOGADOR", jogador.getPernaDominante());
                            intent.putExtra("DATA_NASCIMENTO_JOGADOR", jogador.getDataNascimento());
                            intent.putExtra("GOLS", jogador.getGols());
                            intent.putExtra("ASSISTENCIAS", jogador.getAssistencias());
                            intent.putExtra("IDJOGADOR", jogador.getIdJogador());
                            intent.putExtra("TIPOUSUARIO", tipoUsuario);

                            startActivityForResult(intent, 123);

                        });
                        recyclerView.setAdapter(adapter);

                    } else {
                        Log.e("Firestore", "Erro ao buscar jogadores", task.getException());
                    }
                });

        return lista;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        listaJogadores.clear();
        if(requestCode == 123 && resultCode == RESULT_OK){
            boolean deletado = data.getBooleanExtra("deletado", false);
            if(deletado){
                listarJogadoresDoBanco(); // recarrega a lista do banco
            }
        }
    }
}