package com.example.info;

import static com.example.gerenciadordetime.Menu.funcaoUsuario;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Objetos.Estatisticas;
import com.example.Service.DeletarRegistroBanco;
import com.example.adpter.EstatisticaAdapter;
import com.example.Service.ImagemHelper;
import com.example.Service.PopupUtils;
import com.example.gerenciadordetime.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InfoJogo extends AppCompatActivity {
    private Date dataJogo;
    private RecyclerView recyclerView;
    private EstatisticaAdapter adapter;
    private FirebaseFirestore db;
    List<Estatisticas> listaInfoJogo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_jogo);

        db = FirebaseFirestore.getInstance();

        TextView textTimeAdversario = findViewById(R.id.textTimeAdversario);
        TextView textResultado = findViewById(R.id.textResultado);
        TextView textDataJogo = findViewById(R.id.textDataJogo);
        TextView textGolsFeitos = findViewById(R.id.textGolsFeitos);
        TextView textGolsSofridos = findViewById(R.id.textGolsSofridos);
        TextView textGolsMarcados = findViewById(R.id.textGolsMarcados);
        Button btnDelete = findViewById(R.id.btnDelete);

        recyclerView = findViewById(R.id.recyclerViewMarcadores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        textTimeAdversario.setText("" + getIntent().getStringExtra("TIME_ADIVERSARIO"));
        textResultado.setText("" + getIntent().getStringExtra("RESULTADO"));

        dataJogo = (Date) getIntent().getSerializableExtra("DATA_JOGO");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        textDataJogo.setText(sdf.format(dataJogo));
        textGolsFeitos.setText("" + getIntent().getLongExtra("GOLS_FEITOS", 0));
        textGolsSofridos.setText("" + getIntent().getLongExtra("GOLS_SOFRIDOS", 0));
        textGolsMarcados.setText("Marcadores dos gols: ");

        listarEstatisticasJogo();
        adapter = new EstatisticaAdapter(listaInfoJogo, position -> {
            listaInfoJogo.remove(position);
            adapter.notifyItemRemoved(position);
        }, false);

        recyclerView.setAdapter(adapter);

        if ("Administrador" .equals(funcaoUsuario)) {
            btnDelete.setVisibility(View.VISIBLE);
        }

        ImagemHelper.aplicarImagemNoBotao(this, btnDelete, R.drawable.btndelete, 100, 100);
        btnDelete.setOnClickListener(v -> {
            PopupUtils.mostrarConfirmacao(
                    this,
                    "Confirmação",
                    "Você tem certeza que deseja excluir?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deletarEstaticas();
                            DeletarRegistroBanco.deleteRegistro("GTJOGO", getIntent().getStringExtra("ID_JOGO"));
                            Intent intent = new Intent();
                            intent.putExtra("deletado", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
            );
        });

    }

    private void listarEstatisticasJogo() {
        String idJogoStr = getIntent().getStringExtra("ID_JOGO");
        long idJogo = Long.parseLong(idJogoStr);

        db.collection("GTESTATISTICAS")
                .whereEqualTo("IDJOGO", idJogo)
                .whereGreaterThan("GOLS", 0)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nome = document.getString("NOME");
                            long gols = document.getLong("GOLS");

                            Estatisticas estatisticaJogo = new Estatisticas(nome, (int) gols);
                            listaInfoJogo.add(estatisticaJogo);
                        }

                        listaInfoJogo.sort(Comparator.comparing(Estatisticas::getNome).reversed());

                        recyclerView.setAdapter(adapter);

                    } else {
                        Log.e("Firestore", "Erro ao buscar jogadores", task.getException());
                    }
                });
    }

    private void deletarEstaticas() {

        String idJogoStr = getIntent().getStringExtra("ID_JOGO");
        long idJogo = Long.parseLong(idJogoStr);

        // Exemplo: deletar todos os jogadores com nome "João"
        db.collection("GTESTATISTICAS")
                .whereEqualTo("IDJOGO", idJogo)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        long golsLong = doc.getLong("GOLS");
                        long assistenciasLong = doc.getLong("ASSISTENCIAS");

                        atualizarDadosJogador(doc.getString("NOME"), getIntent().getStringExtra("TIME"), (int) golsLong, (int) assistenciasLong);
                        db.collection("GTESTATISTICAS").document(doc.getId()).delete();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w("FIRESTORE", "Erro ao deletar: ", e);
                });
    }

    private void atualizarDadosJogador(String nome, String time, int gols, int assistencias) {
        db.collection("GTJOGADOR")
                .whereEqualTo("NOME", nome)
                .whereEqualTo("TIME", time)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        // Aqui você tem o id do documento
                        DocumentReference docRef = document.getReference();

                        // Exemplo: incrementando gols em +1
                        docRef.update("GOLS", FieldValue.increment(-gols), "ASSISTENCIAS", FieldValue.increment(-assistencias))
                                .addOnSuccessListener(aVoid ->
                                        Log.d("Firestore", "Gols atualizados para " + document.getId()))
                                .addOnFailureListener(e ->
                                        Log.w("Firestore", "Erro ao atualizar", e));
                    }
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Erro na busca", e));
    }
}