package com.example.lista;

import static com.example.gerenciadordetime.Menu.idTimeUsuario;
import static com.example.cadastro.CadOperacao.atualizaSaldoFinanceiro;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Objetos.Extrato;
import com.example.Service.DeletarRegistroBanco;
import com.example.Service.ImagemHelper;
import com.example.Service.PopupUtils;
import com.example.adpter.ExtratoAdapter;
import com.example.cadastro.CadOperacao;
import com.example.gerenciadordetime.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ListExtraFinanceiro extends AppCompatActivity {

    List<Extrato> listaExtrato = new ArrayList<>();
    private FirebaseFirestore db;
    private RecyclerView recyclerViewExtrato;
    private ExtratoAdapter adapter;
    public static double saldoFinanceiro;
    TextView textSaldoFinanceiro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_extra_financeiro);

        Button btnadicionar = findViewById(R.id.btnNovaOperacao);

        recyclerViewExtrato = findViewById(R.id.recyclerViewExtrato);
        recyclerViewExtrato.setLayoutManager(new LinearLayoutManager(this));

        listarExtratoDoBanco();

        ImagemHelper.aplicarImagemNoBotao(this, btnadicionar, R.drawable.btnadicionar, 125, 125);
        btnadicionar.setOnClickListener(v -> {
            Intent intent = new Intent(this, CadOperacao.class);
            startActivity(intent);
        });

        textSaldoFinanceiro = findViewById(R.id.textsaldo);
        buscaSaldoFinanceiro();
    }

    private void listarExtratoDoBanco() {
        db = FirebaseFirestore.getInstance();

        db.collection("GTEXTRATOFINANCEIRO")
                .whereEqualTo("IDTIME", idTimeUsuario)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaExtrato.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Long idTimeLong = document.getLong("IDTIME");
                            int idTime = (int) idTimeLong.intValue();
                            String tipooperacao = document.getString("TIPOOPERACAO");
                            Date dataOperacao = document.getDate("DATAOPERACAO");
                            double valorOperacao = document.getDouble("VALOROPERACAO");
                            String decricao = document.getString("DSOPERACAO");
                            String idDoc = document.getId();

                            Extrato extrato = new Extrato(decricao, idTime, tipooperacao, dataOperacao, valorOperacao, idDoc);
                            listaExtrato.add(extrato);
                        }

                        listaExtrato.sort(Comparator.comparing(Extrato::getDataOperacao));

                        // Aqui você seta a lista no Adapter do RecyclerView
                        adapter = new ExtratoAdapter(listaExtrato, extrato -> {
                            if (!extrato.getDsOperacao().equals("Pag. Mensalidade") && !extrato.getDsOperacao().equals("Estorno Mensalidade")) {
                                PopupUtils.mostrarConfirmacao(
                                        this,
                                        "Confirmação",
                                        "Você tem certeza que deseja excluir a operação?",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                DeletarRegistroBanco.deleteRegistro("GTEXTRATOFINANCEIRO", extrato.getIdDoc());
                                                if (extrato.getTipoOperacao().equals("Entrada")) {
                                                    atualizaSaldoFinanceiro(extrato.getValorOperacao(), false);
                                                } else {
                                                    atualizaSaldoFinanceiro(extrato.getValorOperacao(), true);
                                                }
                                                listarExtratoDoBanco();
                                            }
                                        }
                                );
                            } else {
                                Toast.makeText(this, "Esta operação não pode ser excluida", Toast.LENGTH_SHORT).show();
                            }
                        });
                        recyclerViewExtrato.setAdapter(adapter);
                    } else {
                        Log.e("Firestore", "Erro ao buscar jogadores", task.getException());
                    }
                }).addOnFailureListener(e -> Log.e("Firestore", "Falha na query", e));
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarExtratoDoBanco();
    }

    private void buscaSaldoFinanceiro() {
        db.collection("GTSALDOFINANCEIRO")
                .whereEqualTo("IDTIME", idTimeUsuario)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w("Firestore", "Erro no listener", e);
                        return;
                    }

                    if (snapshots != null && !snapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : snapshots) {
                            saldoFinanceiro = document.getDouble("SALDOFINANCEIRO");
                            textSaldoFinanceiro.setText(String.valueOf(saldoFinanceiro));
                        }
                    }
                });
    }

}