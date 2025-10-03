package com.example.info;

import static com.example.gerenciadordetime.Menu.idTimeUsuario;
import static com.example.gerenciadordetime.Menu.timeUsuario;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Objetos.Estatisticas;
import com.example.Objetos.Jogo;
import com.example.adpter.EstatisticaAdapter;
import com.example.gerenciadordetime.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InfoEstatisticas extends AppCompatActivity {
    long qtdVitorias, qtdEmpates;
    private TextView textqtdJogos, textqtdVitorias, textqtdEmpate, textqtdDerrotas, textAproveitamento, textGolsFeitos, textGolsSofridos;
    private RecyclerView recyclerViewArtilheiros, recyclerViewAssistentes;
    private EstatisticaAdapter adpterGols;
    private EstatisticaAdapter adpterAssistencias;
    private FirebaseFirestore db;
    private List<Jogo> listInfoTime = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_estatisticas);

        db = FirebaseFirestore.getInstance();

        textqtdJogos = findViewById(R.id.textsaldo);
        textqtdVitorias = findViewById(R.id.textqtdVitorias);
        textqtdEmpate = findViewById(R.id.textqtdEmpate);
        textqtdDerrotas = findViewById(R.id.textqtdDerrotas);
        textAproveitamento = findViewById(R.id.textAproveitamento);
        textGolsFeitos = findViewById(R.id.textGolsFeitos);
        textGolsSofridos = findViewById(R.id.textGolsSofridos);

        recyclerViewArtilheiros = findViewById(R.id.recyclerViewArtilheiros);
        recyclerViewAssistentes = findViewById(R.id.recyclerViewAssistentes);

        recyclerViewArtilheiros.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAssistentes.setLayoutManager(new LinearLayoutManager(this));

        buscaInfojogo();
        buscaTopJogadores();
    }

    private void receberDadosJogo() {

        String aproveitamentoFormatado = String.format("%.2f", calcularAproveitamento());

        textqtdJogos.setText("" + listInfoTime.size());
        textqtdVitorias.setText("" + listInfoTime.stream().filter(jogo -> jogo.getResultado().toLowerCase().contains("vitoria")).count());
        textqtdEmpate.setText("" + listInfoTime.stream().filter(jogo -> jogo.getResultado().toLowerCase().contains("empate")).count());
        textqtdDerrotas.setText("" + listInfoTime.stream().filter(jogo -> jogo.getResultado().toLowerCase().contains("derrota")).count());
        textAproveitamento.setText(aproveitamentoFormatado + "%");
        textGolsFeitos.setText("" + listInfoTime.stream()
                .mapToLong(Jogo::getGolsFeitos) // pega só os gols
                .sum());
        textGolsSofridos.setText("" + listInfoTime.stream()
                .mapToLong(Jogo::getGolsSofridos) // pega só os gols
                .sum());
    }

    private void buscaInfojogo() {
        db.collection("GTJOGO")
                .whereEqualTo("TIME", timeUsuario)
                .whereEqualTo("IDTIME", idTimeUsuario)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String resultado = document.getString("RESULTADO");
                            long golsFeitos = document.getLong("GOLSFEITOS");
                            long golsSofridos = document.getLong("GOLSSOFRIDOS");

                            Jogo jogo = new Jogo(resultado, golsFeitos, golsSofridos);
                            listInfoTime.add(jogo);
                        }
                        receberDadosJogo();
                    }
                });
    }

    private double calcularAproveitamento() {
        qtdVitorias = listInfoTime.stream().filter(jogo -> jogo.getResultado().toLowerCase().contains("vitoria")).count();
        qtdEmpates = listInfoTime.stream().filter(jogo -> jogo.getResultado().toLowerCase().contains("empate")).count();
        double aproveitamento;
        int pontosJogados = listInfoTime.size() * 3;
        int pontosConquistados = ((int) qtdVitorias * 3) + (int) qtdEmpates;
        if (pontosConquistados > 0) {
            aproveitamento = ((double) pontosConquistados / pontosJogados) * 100;
        } else {
            aproveitamento = 0;
        }
        return aproveitamento;
    }

    private void buscaTopJogadores() {
        db.collection("GTJOGADOR")
                .whereEqualTo("TIME", timeUsuario)
                .whereEqualTo("IDTIME", idTimeUsuario)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Estatisticas> lista = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String nome = doc.getString("NOME");
                            int gols = doc.getLong("GOLS").intValue();
                            int assistencias = doc.getLong("ASSISTENCIAS").intValue();

                            lista.add(new Estatisticas(nome, gols, assistencias));
                        }

                        // Top 3 goleadores
                        List<Estatisticas> topGoleadores = lista.stream()
                                .filter(est -> est.getGols() > 0)
                                .sorted((a, b) -> Integer.compare(b.getGols(), a.getGols()))
                                .limit(3)
                                .map(est -> new Estatisticas(est.getNome(), est.getGols(), 0))
                                .collect(Collectors.toList());

                        // Top 3 assistentes
                        List<Estatisticas> topAssistentes = lista.stream()
                                .filter(est -> est.getAssistencias() > 0)
                                .sorted((a, b) -> Integer.compare(b.getAssistencias(), a.getAssistencias()))
                                .limit(3)
                                .map(est -> new Estatisticas(est.getNome(), 0, est.getAssistencias()))
                                .collect(Collectors.toList());

                        // Preenche os RecyclerViews
                        adpterGols = new EstatisticaAdapter(topGoleadores, position -> {
                            lista.remove(position);
                            adpterGols.notifyItemRemoved(position);
                        }, false);

                        adpterAssistencias = new EstatisticaAdapter(topAssistentes, position -> {
                            lista.remove(position);
                            adpterAssistencias.notifyItemRemoved(position);
                        }, false);

                        recyclerViewArtilheiros.setAdapter(adpterGols);
                        recyclerViewAssistentes.setAdapter(adpterAssistencias);
                    }
                });
    }
}