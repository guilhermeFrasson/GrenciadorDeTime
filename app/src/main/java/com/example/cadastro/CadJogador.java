package com.example.cadastro;

import static com.example.Objetos.Time.getNomeTime;

import static com.example.Service.BuscaDadosTime.proximoCodigo;
import static com.example.Service.FormataString.formatarNome;
import static com.example.gerenciadordetime.Menu.idTimeUsuario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Objetos.Usuario;
import com.example.Service.ImagemHelper;

import com.example.gerenciadordetime.Menu;
import com.example.gerenciadordetime.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;

import java.util.*;

public class CadJogador extends AppCompatActivity {

    private EditText nomeEditText;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextInputEditText campoData;
    private Spinner posicaoSpinner;
    private Spinner pernaDominanteSpinner;
    private String escolhaPernaDominante, escolhaPosicao;
    private Date data;
    private String primeiroAcesso;
    Usuario usuario = new Usuario();
    View overlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cad_jogador);

        bindViews();

        Button btnSalvar = findViewById(R.id.btnSalvar);

        ImagemHelper.aplicarImagemNoBotao(this, btnSalvar, R.drawable.btnsalvar, 70, 70);

        campoData.setOnClickListener(v -> {
            selecionaData();
        });

        comboPosicao();
        comboPernadominate();

        primeiroAcesso = getIntent().getStringExtra("PRIMEIROACESSO");
        if ("NAO".equals(primeiroAcesso)) {
            nomeEditText.setVisibility(View.VISIBLE);
        }

        btnSalvar.setOnClickListener(v -> {
            overlay.setVisibility(View.VISIBLE);
            validaCampoNome();
        });
    }

    private void bindViews() {
        nomeEditText = findViewById(R.id.nomeEditText);
        campoData = findViewById(R.id.campoData);
        posicaoSpinner = findViewById(R.id.posicaoSpinner);
        pernaDominanteSpinner = findViewById(R.id.pernaDominanteSpinner);
        overlay = findViewById(R.id.progressOverlay);
    }

    private void validaCampoNome() {
        if ("NAO".equals(primeiroAcesso)) {
            if (nomeEditText.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Nome do jogador não pode estar em branco", Toast.LENGTH_SHORT).show();
                overlay.setVisibility(View.GONE);
            }else {
                validaOutrosCampos();
            }
        } else {
            validaOutrosCampos();
        }
    }

    private void validaOutrosCampos() {
        Date hoje = new Date();
        if (escolhaPosicao.equals("-- Escolha um opção --")) {
            Toast.makeText(this, "Ecolha uma posicao", Toast.LENGTH_SHORT).show();
            overlay.setVisibility(View.GONE);
        } else if (escolhaPernaDominante.equals("-- Escolha um opção --")) {
            Toast.makeText(this, "Escolha a perna dominante", Toast.LENGTH_SHORT).show();
            overlay.setVisibility(View.GONE);
        } else if (data == null || !data.before(hoje)) {
            Toast.makeText(this, "Data de nascimento invalida", Toast.LENGTH_SHORT).show();
            overlay.setVisibility(View.GONE);
        } else {
            if ("NAO".equals(primeiroAcesso)){
                verificaJogadorDuplicado();
            } else {
                salvardados();
            }
        }
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

    private void salvardados() {
        Map<String, Object> infoJogador = new HashMap<>();

        if ("SIM".equals(primeiroAcesso)) {

            CadTime cadTime = new CadTime();
            cadTime.salvaDadosTimeBanco();

            criarUsuarioAuth(usuario.getEmail(), usuario.getSenha());

            infoJogador.put("IDTIME", proximoCodigo);
            infoJogador.put("TIME", getNomeTime());
            infoJogador.put("NOME", usuario.getNome());
            infoJogador.put("POSICAO", escolhaPosicao);
            infoJogador.put("PERNADOMINANTE", escolhaPernaDominante);
            infoJogador.put("DATANASCIMENTO", data);
            infoJogador.put("GOLS", 0);
            infoJogador.put("ASSISTENCIAS", 0);

            db.collection("GTJOGADOR").add(infoJogador);

            Log.d("SALVAR_DADOS", "Todos os dados salvos com sucesso!");

            salvaSaldoFinanceiro();
        } else {
            String email = formatarNome(nomeEditText.getText().toString().trim()) + idTimeUsuario + "@gmail.com";

            criarUsuarioAuth(email,"123456");

            infoJogador.put("NOME", formatarNome(nomeEditText.getText().toString().trim()));
            infoJogador.put("IDTIME", idTimeUsuario);
            infoJogador.put("POSICAO", escolhaPosicao);
            infoJogador.put("PERNADOMINANTE", escolhaPernaDominante);
            infoJogador.put("DATANASCIMENTO", data);
            infoJogador.put("GOLS", 0);
            infoJogador.put("ASSISTENCIAS", 0);

            db.collection("GTJOGADOR").add(infoJogador);
        }
    }

    private void salvaSaldoFinanceiro(){
        Map<String, Object> saldoFinanceiro = new HashMap<>();

        saldoFinanceiro.put("IDTIME", proximoCodigo);
        saldoFinanceiro.put("SALDOFINANCEIRO", 0);

        db.collection("GTSALDOFINANCEIRO").add(saldoFinanceiro);
    }

    private void comboPosicao() {
        String[] listaPosicoes = new String[8];
        listaPosicoes[0] = "-- Escolha um opção --";

        db.collection("GTPOSICAO").get()
                .addOnSuccessListener(documentSnapshot -> {
                    int i = 1;
                    for (QueryDocumentSnapshot document : documentSnapshot) {
                        String posicao = document.getString("DSPOSICAO"); // pega o campo DSPOSICAO
                        if (posicao != null) {
                            listaPosicoes[i] = posicao;
                            i++;
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao buscar posicao", Toast.LENGTH_SHORT).show();
                });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaPosicoes
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        posicaoSpinner.setAdapter(adapter);

        // pegar valor selecionado
        posicaoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                escolhaPosicao = listaPosicoes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void comboPernadominate() {
        String[] pernaDominante = {"-- Escolha um opção --", "Ambidestro", "Canhoto", "Destro"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                pernaDominante
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pernaDominanteSpinner.setAdapter(adapter);

        pernaDominanteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                escolhaPernaDominante = pernaDominante[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void verificaJogadorDuplicado() {
        db.collection("GTJOGADOR")
                .whereEqualTo("IDTIME", idTimeUsuario)
                .whereEqualTo("NOME", formatarNome(nomeEditText.getText().toString().trim()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            salvardados();
                        } else {
                            Toast.makeText(this, "Este nome já pertence a um jogador, tente adicionar um sobrenome", Toast.LENGTH_SHORT).show();
                            overlay.setVisibility(View.GONE);
                        }
                    } else {
                        Log.w("FIREBASE", "Erro na busca", task.getException());
                        overlay.setVisibility(View.GONE);
                    }
                });
    }

    private void criarUsuarioAuth(String email, String senha){
        FirebaseAuth auth  = FirebaseAuth.getInstance();

        CadUsuario cadUsuario = new CadUsuario();

        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // usuário criado e automaticamente logado
                        FirebaseUser user = auth.getCurrentUser(); // ou task.getResult().getUser();
                        if (user != null) {
                            String uid = user.getUid();

                            if ("SIM".equals(primeiroAcesso)){
                                cadUsuario.salvaDadosUsuarioPrimeiroAcesso(uid);
                                loginUser(usuario.getEmail(),usuario.getSenha());
                            }else {
                                cadUsuario.salvaDadosUsuarioCadJogador(uid,nomeEditText.getText().toString().trim());
                                overlay.setVisibility(View.GONE);
                                finish();
                            }
                        }
                    } else {
                        Log.e("APP", "Erro ao criar usuário: " + task.getException().getMessage());
                        overlay.setVisibility(View.GONE);
                    }
                });
    }

    public void loginUser(String email, String senha) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(this, Menu.class);
                        intent.putExtra("PRIMEIROACESSO", primeiroAcesso);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
    }
}