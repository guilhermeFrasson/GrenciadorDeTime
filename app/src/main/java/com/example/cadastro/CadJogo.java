package com.example.cadastro;

import static com.example.gerenciadordetime.Menu.idTimeUsuario;
import static com.example.gerenciadordetime.Menu.timeUsuario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Objetos.Estatisticas;
import com.example.Service.BuscaDadosUser;
import com.example.Service.ImagemHelper;
import com.example.gerenciadordetime.R;
import com.example.lista.ListMarcadorGols;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

public class CadJogo extends AppCompatActivity {

    private EditText timeAdversarioEditText, golsFeitosEditText, golsSofridosEditText;
    private FirebaseFirestore db;
    private TextInputEditText campoData;
    private String resultado;
    int golsFeitos, golsSofridos, totalGols;
    private Date data;
    private List<Estatisticas> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cad_jogo);

        db = FirebaseFirestore.getInstance();

        timeAdversarioEditText = findViewById(R.id.timeAdversarioEditText);
        golsFeitosEditText = findViewById(R.id.golsFeitosEditText);
        golsSofridosEditText = findViewById(R.id.golsSofridosEditText);

        campoData = findViewById(R.id.campoData);

        Button btnSalvar = findViewById(R.id.btnSalvar);
        Button btnListJogadorMarcador = findViewById(R.id.btnlistJogadormarcador);

        ImagemHelper.aplicarImagemNoBotao(this, btnSalvar, R.drawable.btnsalvar, 70, 70);

        ImagemHelper.aplicarImagemNoBotao(this, btnListJogadorMarcador, R.drawable.btnlistar, 100, 100);
        btnListJogadorMarcador.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListMarcadorGols.class);
            startActivity(intent);
        });
        campoData.setOnClickListener(v -> {
            selecionaData();
        });

        btnSalvar.setOnClickListener(v -> {
            lista = CadMarcadorGols.getListaEstatisticas();
            somaGolsLista();
            validaCampos();
        });
    }

    private void validaCampos() {
        Date hoje = new Date();

        String golsFeitosStr = golsFeitosEditText.getText().toString().trim();
        String golsSofridosStr = golsSofridosEditText.getText().toString().trim();

        golsFeitos = golsFeitosStr.isEmpty() ? 0 : Integer.parseInt(golsFeitosStr);
        golsSofridos = golsSofridosStr.isEmpty() ? 0 : Integer.parseInt(golsSofridosStr);

        if (timeAdversarioEditText.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Nome do time adiversario não pode estar em branco", Toast.LENGTH_SHORT).show();
        } else if (golsFeitos != totalGols) {
            totalGols = 0;
            Toast.makeText(this, "Numero de gols marcados esta diferente da quantidade que os jogadores marcaram", Toast.LENGTH_SHORT).show();
        } else if (data == null || !data.before(hoje)) {
            Toast.makeText(this, "Data do jogo invalida", Toast.LENGTH_SHORT).show();
        } else {
            defineResultado();
            salvarJogoComIdUnico();
        }
    }

    private void defineResultado() {
        if (golsFeitos > golsSofridos) {
            resultado = "Vitoria";
        } else if (golsFeitos < golsSofridos) {
            resultado = "Derrota";
        } else {
            resultado = "Empate";
        }
    }

    private void salvaJogo(int idJogo) {
        Map<String, Object> jogo = new HashMap<>();
        jogo.put("TIMEADVERSARIO", formatarNomeTime(timeAdversarioEditText.getText().toString().trim()));
        jogo.put("TIME", timeUsuario);
        jogo.put("IDTIME", idTimeUsuario);
        jogo.put("DATADOJOGO", data);
        jogo.put("RESULTADO", resultado);
        jogo.put("GOLSFEITOS", golsFeitos);
        jogo.put("GOLSSOFRIDOS", golsSofridos);

        db.collection("GTJOGO")
                .document(String.valueOf(idJogo))
                .set(jogo)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Jogo salvo");

                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Erro ao salvar jogo", e);
                });

        salvaEstatisticasJogo(idJogo);
        finish();
    }

    private String formatarNomeTime(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        texto = texto.toLowerCase(); // tudo minúsculo
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }

    private void selecionaData() {
        // Criar o date picker
        MaterialDatePicker<Long> datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Selecione a data de nascimento")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

        // Mostrar o picker
        datePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

        // Listener quando o usuário confirmar
        datePicker.addOnPositiveButtonClickListener(selection -> {
            // Cria um Calendar com o fuso UTC para pegar o timestamp do picker
            Calendar calUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calUTC.setTimeInMillis(selection);

            // Converte para um Calendar no fuso local
            Calendar calLocal = Calendar.getInstance();
            calLocal.set(calUTC.get(Calendar.YEAR),
                    calUTC.get(Calendar.MONTH),
                    calUTC.get(Calendar.DAY_OF_MONTH),
                    0, 0, 0); // hora, minuto, segundo zerados

            // Pega o Date final
            Date dataNascimento = calLocal.getTime();

            // Mostrar no campo
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            campoData.setText(sdf.format(dataNascimento));

            // Salvar a data
            this.data = dataNascimento;
        });
    }

    private void salvaEstatisticasJogo(int idJogo) {
        Map<String, Object> estatJogo = new HashMap<>();
        for (Estatisticas estat : lista) {
            estatJogo.put("GOLS", estat.getGols());
            estatJogo.put("ASSISTENCIAS", estat.getAssistencias());
            estatJogo.put("NOME", estat.getNome());
            estatJogo.put("IDJOGO", idJogo);

            db.collection("GTESTATISTICAS")
                    .add(estatJogo)
                    .addOnSuccessListener(ref -> Log.d("FIREBASE", "Salvo com ID: " + ref.getId()))
                    .addOnFailureListener(e -> Log.w("FIREBASE", "Erro", e));

            atualizarEstatisticasJogador(estat.getNome(), estat.getGols(), estat.getAssistencias());

        }
        lista.clear();
    }

    public void salvarJogoComIdUnico() {
        gerarIdUnico(0);
    }

    private void gerarIdUnico(final int tentativa) {
        if (tentativa > 1000) {
            // Evitar loop infinito
            System.out.println("Não foi possível gerar um ID único após muitas tentativas.");
            return;
        }

        Random random = new Random();
        int idJogo = random.nextInt(1000000); // exemplo: número aleatório de 0 a 999999

        db.collection("jogos").document(String.valueOf(idJogo)).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // ID já existe, tentar outro
                            gerarIdUnico(tentativa + 1);
                        } else {
                            // ID único encontrado, salvar o jogo
                            salvaJogo(idJogo);
                        }
                    }
                });
    }

    private void somaGolsLista() {
        for (Estatisticas e : lista) {
            totalGols += e.getGols();
        }
    }

    private void atualizarEstatisticasJogador(String nome, int gols, int assistencias) {
        db.collection("GTJOGADOR")
                .whereEqualTo("NOME", nome)
                .whereEqualTo("TIME", timeUsuario)
                .whereEqualTo("IDTIME", idTimeUsuario)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        // Aqui você tem o id do documento
                        DocumentReference docRef = document.getReference();

                        // Exemplo: incrementando gols em +1
                        docRef.update("GOLS", FieldValue.increment(gols), "ASSISTENCIAS", FieldValue.increment(assistencias))
                                .addOnSuccessListener(aVoid ->
                                        Log.d("Firestore", "Gols atualizados para " + document.getId()))
                                .addOnFailureListener(e ->
                                        Log.w("Firestore", "Erro ao atualizar", e));
                    }
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Erro na busca", e));
    }
}