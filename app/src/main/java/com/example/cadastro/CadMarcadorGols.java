package com.example.cadastro;

import static com.example.Service.BuscaDadosUser.idTimeUsuario;
import static com.example.Service.BuscaDadosUser.timeUsuario;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Objetos.Estatisticas;
import com.example.Objetos.Jogador;
import com.example.Service.ImagemHelper;
import com.example.gerenciadordetime.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CadMarcadorGols extends AppCompatActivity {

    private Spinner jogadorSpinner;
    private Jogador escolhaJogador;
    private static List<Estatisticas> listaEstatisticas = new ArrayList<>();
    TextView txtGols, txtAssistencias;
    FirebaseFirestore db;
    int gols, assistencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cad_marcador_gols);

        jogadorSpinner = findViewById(R.id.JogadorSpinner);

        Button btnMaisGols = findViewById(R.id.btnMaisGols);
        Button btnMenosGols = findViewById(R.id.btnMenosGols);
        txtGols = findViewById(R.id.txtGols);
        Button btnMaisAssistencias = findViewById(R.id.btnMaisAssistencias);
        Button btnMenosAssistencias = findViewById(R.id.btnMenosAssistencias);
        txtAssistencias = findViewById(R.id.txtAssistencias);
        Button btnSalvar = findViewById(R.id.btnSalvar);
        CheckBox checkGolContra = findViewById(R.id.checkGolContra);

        comboJogador();

        ImagemHelper.aplicarImagemNoBotao(this, btnMaisGols, R.drawable.btnadicionar, 70, 70);
        btnMaisGols.setOnClickListener(v -> {
            inseriGolsAssitencias(txtGols,"soma");
        });

        ImagemHelper.aplicarImagemNoBotao(this, btnMenosGols, R.drawable.menos, 70, 70);
        btnMenosGols.setOnClickListener(v -> {
            inseriGolsAssitencias(txtGols,"Subtracao");
        });

        ImagemHelper.aplicarImagemNoBotao(this, btnMaisAssistencias, R.drawable.btnadicionar, 70, 70);
        btnMaisAssistencias.setOnClickListener(v -> {
            inseriGolsAssitencias(txtAssistencias,"soma");
        });

        ImagemHelper.aplicarImagemNoBotao(this, btnMenosAssistencias, R.drawable.menos, 70, 70);
        btnMenosAssistencias.setOnClickListener(v -> {
            inseriGolsAssitencias(txtAssistencias,"Subtracao");
        });

        ImagemHelper.aplicarImagemNoBotao(this, btnSalvar, R.drawable.btnsalvar, 70, 70);
        btnSalvar.setOnClickListener(v -> {
            if (checkGolContra.isChecked()) {
                Estatisticas estat = new Estatisticas("Gol contra", 1, 0);
                adicionarEstatistica(estat);
                finish();
            } else {
                validaCampos();
            }

        });

        checkGolContra.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                btnMaisGols.setEnabled(false);
                btnMenosGols.setEnabled(false);
                jogadorSpinner.setEnabled(false);
                btnMaisAssistencias.setEnabled(false);
                btnMenosAssistencias.setEnabled(false);
                txtGols.setText(String.valueOf(0));
                txtAssistencias.setText(String.valueOf(0));
                comboJogador();


            } else {
                btnMaisGols.setEnabled(true);
                btnMenosGols.setEnabled(true);
                jogadorSpinner.setEnabled(true);
                btnMaisAssistencias.setEnabled(true);
                btnMenosAssistencias.setEnabled(true);
            }
        });
    }

    private void inseriGolsAssitencias(TextView txtValor, String operador) {
        if (operador.equals("soma")){
            int valor = Integer.parseInt(txtValor.getText().toString());
            txtValor.setText(String.valueOf(valor + 1));
        } else {
            int valor = Integer.parseInt(txtValor.getText().toString());
            if (valor > 0) {
                txtValor.setText(String.valueOf(valor - 1));
            }
        }
    }

    private void comboJogador() {
        List<Jogador> lista = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        Object idTimeUsusuario;
        db.collection("GTJOGADOR")
                .whereEqualTo("TIME", timeUsuario)
                .whereEqualTo("IDTIME", idTimeUsuario)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nome = document.getString("NOME");
                            Jogador jogador = new Jogador(nome);
                            lista.add(jogador);
                        }

                        lista.sort(Comparator.comparing(Jogador::getNome));

                        // Adapter só aqui dentro!
                        ArrayAdapter<Jogador> adapter = new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                lista
                        );
                        lista.add(0, new Jogador("-- Selecione um jogador --"));

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        jogadorSpinner.setAdapter(adapter);

                        jogadorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                escolhaJogador = lista.get(position); // agora sim
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    } else {
                        Log.e("Firestore", "Erro ao buscar jogadores", task.getException());
                    }
                });
    }

    private void validaCampos (){

        String golsStr = txtGols.getText().toString().trim();
        String assistenciasStr = txtAssistencias.getText().toString().trim();

        gols = golsStr.isEmpty() ? 0 : Integer.parseInt(golsStr);
        assistencias = assistenciasStr.isEmpty() ? 0 : Integer.parseInt(assistenciasStr);

        if(gols == 0 && assistencias == 0){
            Toast.makeText(this, "O numero de gols e assistencias não pode ser 0", Toast.LENGTH_SHORT).show();
        }else if ("-- Selecione um jogador --".equals(escolhaJogador.toString()))  {
            Toast.makeText(this, "Um jogador precisa ser selecionado", Toast.LENGTH_SHORT).show();
        }else {
            salvarJogadorList();
        }
    }

    public void salvarJogadorList() {
        String nome = escolhaJogador.toString();

        Estatisticas estat = new Estatisticas(nome, gols, assistencias);
        adicionarEstatistica(estat);
        finish();
    }

    // adiciona um item
    public static void adicionarEstatistica(Estatisticas estat) {
        listaEstatisticas.add(estat);
    }

    // devolve a lista completa
    public static List<Estatisticas> getListaEstatisticas() {
        return listaEstatisticas;
    }

    // se quiser resetar
    public static void limparLista() {
        listaEstatisticas.clear();
    }
}