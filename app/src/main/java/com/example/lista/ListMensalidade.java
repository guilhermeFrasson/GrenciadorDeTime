package com.example.lista;

import static com.example.gerenciadordetime.Menu.idTimeUsuario;
import static com.example.cadastro.CadOperacao.atualizaSaldoFinanceiro;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Objetos.Mensalidade;
import com.example.Service.ImagemHelper;
import com.example.Service.PopupUtils;
import com.example.adpter.MensalidadeAdapter;
import com.example.gerenciadordetime.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListMensalidade extends AppCompatActivity {
    List<Mensalidade> listaMensalidade = new ArrayList<>();
    private FirebaseFirestore db;
    private String escolhaMes;
    private Integer escolhaAno;
    private RecyclerView recyclerMensalidades;
    private MensalidadeAdapter adapter;
    private TextView textlayoutResumo;
    private Spinner mesSpinner;
    private Spinner anoSpinner;
    private Calendar calendar;
    View overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_mensalidade);

        textlayoutResumo = findViewById(R.id.textlayoutResumo);
        Button btnFiltrar = findViewById(R.id.btnFiltrar);
        mesSpinner = findViewById(R.id.spinnerMes);
        anoSpinner = findViewById(R.id.spinnerAno);
        overlay = findViewById(R.id.progressOverlay);

        recyclerMensalidades = findViewById(R.id.recyclerMensalidades);
        recyclerMensalidades.setLayoutManager(new LinearLayoutManager(this));

        ImagemHelper.aplicarImagemNoBotao(this, btnFiltrar, R.drawable.btnbuscar, 70, 70);
        btnFiltrar.setOnClickListener(v -> {
            listarMensalidadeDoBanco(escolhaAno, buscaNumeroMes(escolhaMes));
        });

        calendar = Calendar.getInstance();
        int anoAtual = calendar.get(Calendar.YEAR);
        int mesAtual = Calendar.getInstance().get(Calendar.MONTH);

        listarMensalidadeDoBanco(anoAtual, mesAtual);
        comboMes();
        comboAno();
    }

    private void comboMes() {
        String[] mes = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

        int mesAtual = Calendar.getInstance().get(Calendar.MONTH);

        ArrayAdapter<String> mesAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                mes
        );
        mesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mesSpinner.setAdapter(mesAdapter);

        mesSpinner.setSelection(mesAtual);

        // pegar valor selecionado
        mesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                escolhaMes = mes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private int buscaNumeroMes(String mes) {
        int numeroMes = 0;

        if (mes.equals("Janeiro")) {
            numeroMes = 0;
        } else if (mes.equals("Fevereiro")) {
            numeroMes = 1;
        } else if (mes.equals("Março")) {
            numeroMes = 2;
        } else if (mes.equals("Abril")) {
            numeroMes = 3;
        } else if (mes.equals("Maio")) {
            numeroMes = 4;
        } else if (mes.equals("Junho")) {
            numeroMes = 5;
        } else if (mes.equals("Julho")) {
            numeroMes = 6;
        } else if (mes.equals("Agosto")) {
            numeroMes = 7;
        } else if (mes.equals("Setembro")) {
            numeroMes = 8;
        } else if (mes.equals("Outubro")) {
            numeroMes = 9;
        } else if (mes.equals("Novembro")) {
            numeroMes = 10;
        } else if (mes.equals("Dezembro")) {
            numeroMes = 11;
        }
        return numeroMes;
    }

    private void comboAno() {

        calendar = Calendar.getInstance();
        int anoAtual = calendar.get(Calendar.YEAR);


        List<Integer> anos = new ArrayList<>();
        for (int i = 2020; i <= anoAtual; i++) {
            anos.add(i);
        }

        int posicaoAnoAtual = anos.indexOf(anoAtual);

        ArrayAdapter<Integer> anoAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item, anos);
        anoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        anoSpinner.setAdapter(anoAdapter);

        if (posicaoAnoAtual >= 0) {
            anoSpinner.setSelection(posicaoAnoAtual);
        }

        // pegar valor selecionado
        anoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                escolhaAno = anos.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void listarMensalidadeDoBanco(int ano, int mes) {
        db = FirebaseFirestore.getInstance();

        Date inicioMes = getPrimeiroDiaDoMes(ano, mes);
        Date fimMes = getUltimoDiaDoMes(ano, mes);

        db.collection("GTMENSALIDADE")
                .whereEqualTo("IDTIME", idTimeUsuario)
                .whereGreaterThanOrEqualTo("DATAMES", inicioMes)
                .whereLessThanOrEqualTo("DATAMES", fimMes)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaMensalidade.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String time = document.getString("TIME");
                            String nomeJogador = document.getString("NOMEJOGADOR");
                            Date dataMensalidade = document.getDate("DATAMES");
                            boolean mensalidadePaga = document.getBoolean("PAGO");
                            String idJogador = document.getString("IDJOGADOR");
                            long valorMensalidade = document.getLong("VALORMENSALIDADE");
                            String idDoc = document.getId();
                            Date dataPagamento = new Date();

                            Mensalidade mensalidade = new Mensalidade(time, idJogador, nomeJogador, mensalidadePaga, dataMensalidade, valorMensalidade, idDoc, dataPagamento);
                            listaMensalidade.add(mensalidade);
                        }

                        listaMensalidade.sort(Comparator.comparing(Mensalidade::getNomeJogador));

                        // Aqui você seta a lista no Adapter do RecyclerView
                        adapter = new MensalidadeAdapter(listaMensalidade, mensalidade -> {
                            if (!mensalidade.isMensalidadePaga()) {
                                PopupUtils.mostrarConfirmacao(
                                        this,
                                        "Confirmar pagamento",
                                        "Deseja marcar esta mensalidade como paga?",
                                        (dialog, which) -> {
                                            overlay.setVisibility(View.VISIBLE);
                                            atualizarStatusMensalidade(mensalidade.getIdDoc(), mensalidade.getIdJogador(), true);
                                            atualizaSaldoFinanceiro(mensalidade.getValorMensalidade(), true);
                                            salvaOpreracao(mensalidade.getValorMensalidade(), mensalidade.getDataPagamento(), "Entrada", "Pag. Mensalidade");
                                        }
                                );
                            } else {
                                PopupUtils.mostrarConfirmacao(
                                        this,
                                        "Cencelar pagamento",
                                        "Deseja Cencelar o pagamento da mensalidade?",
                                        (dialog, which) -> {
                                            overlay.setVisibility(View.VISIBLE);
                                            atualizarStatusMensalidade(mensalidade.getIdDoc(), mensalidade.getIdJogador(), false);
                                            atualizaSaldoFinanceiro(mensalidade.getValorMensalidade(), false);
                                            salvaOpreracao(mensalidade.getValorMensalidade(), mensalidade.getDataPagamento(), "Saida", "Estorno Mensalidade");
                                        }
                                );
                            }
                        });
                        recyclerMensalidades.setAdapter(adapter);
                        infoMensalidadesPagas();
                        overlay.setVisibility(View.GONE);

                    } else {
                        Log.e("Firestore", "Erro ao buscar jogadores", task.getException());
                    }
                }).addOnFailureListener(e -> Log.e("Firestore", "Falha na query", e));
    }

    private Date getPrimeiroDiaDoMes(int ano, int mes) {
        calendar = Calendar.getInstance();

        // Define ano e mês
        calendar.set(Calendar.YEAR, ano);
        calendar.set(Calendar.MONTH, mes);

        // Define para o primeiro dia do mês às 00:00:00
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    private Date getUltimoDiaDoMes(int ano, int mes) {
        calendar = Calendar.getInstance();

        // Define ano e mês
        calendar.set(Calendar.YEAR, ano);
        calendar.set(Calendar.MONTH, mes + 1);

        // Define para o ultimo dia do mês às 00:00:00
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }

    private void infoMensalidadesPagas() {

        long pago = 0;
        long naoPago = 0;
        pago = listaMensalidade.stream().filter(Mensalidade::isMensalidadePaga).count();
        naoPago = listaMensalidade.stream().filter(mensalidade -> !mensalidade.isMensalidadePaga()).count();

        textlayoutResumo.setText("Pago: " + pago + " | Faltando: " + naoPago);
    }

    private void atualizarStatusMensalidade(String idDoc, String idJogador, boolean statusPagamento) {

        DocumentReference docRef = db.collection("GTMENSALIDADE").document(idDoc);

        docRef.update("PAGO", statusPagamento)
                .addOnSuccessListener(aVoid ->
                        listarMensalidadeDoBanco(escolhaAno, buscaNumeroMes(escolhaMes)))
                .addOnFailureListener(e ->
                        Log.w("Firestore", "Erro ao atualizar", e));
    }

    private void salvaOpreracao(long valorMensalidade, Date dataOperacao, String tipoOperacao, String dsOperacao) {
        Map<String, Object> operacao = new HashMap<>();
        operacao.put("IDTIME", idTimeUsuario);
        operacao.put("TIPOOPERACAO", tipoOperacao);
        operacao.put("VALOROPERACAO", valorMensalidade);
        operacao.put("DATAOPERACAO", dataOperacao);
        operacao.put("DSOPERACAO", dsOperacao);

        db.collection("GTEXTRATOFINANCEIRO")
                .add(operacao)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Jogador salvo com ID: " + documentReference.getId());

                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Erro ao salvar jogador", e);
                });
    }
}