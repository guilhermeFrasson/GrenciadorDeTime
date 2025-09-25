package com.example.cadastro;

import static com.example.Service.BuscaDadosUser.idTimeUsuario;
import static com.example.Service.BuscaDadosUser.timeUsuario;
import static com.example.lista.ListExtraFinanceiro.saldoFinanceiro;

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

import com.example.Service.ImagemHelper;
import com.example.gerenciadordetime.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CadOperacao extends AppCompatActivity {
    private EditText descricaoEditText, valorEditText;
    Spinner operacaoSpinner;
    private String escolhaTipoOperacao;
    private FirebaseFirestore db;
    double valor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cad_operacao);

        descricaoEditText = findViewById(R.id.descricaoEditText);
        valorEditText = findViewById(R.id.valorEditText);
        operacaoSpinner = findViewById(R.id.tipoOperacaoSpinner);

        Button btnSalvar = findViewById(R.id.btnSalvar);
        ImagemHelper.aplicarImagemNoBotao(this, btnSalvar, R.drawable.btnsalvar, 70, 70);
        btnSalvar.setOnClickListener(v -> {
            validaCampos();
        });

        comboPernadominate();
    }

    private void validaCampos (){
        if (escolhaTipoOperacao.equals("-- Escolha um opção --")) {
            Toast.makeText(this, "Tipo de operação precisa ser escolhido", Toast.LENGTH_SHORT).show();
        } else if (descricaoEditText.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Descrição não pode estar em branco", Toast.LENGTH_SHORT).show();
        } else if (valorEditText.getText().toString().trim().equals("")) {
            Toast.makeText(this, "O valor da operação precisa ser informado", Toast.LENGTH_SHORT).show();
        } else {
            salvardados();
        }
    }

    private void salvardados(){
        Date hoje = new Date();

        db = FirebaseFirestore.getInstance();

        String valorStr = valorEditText.getText().toString().trim();

        valor = valorStr.isEmpty() ? 0 : Double.parseDouble(valorStr);

        Map<String, Object> extrato = new HashMap<>();
        extrato.put("IDTIME", idTimeUsuario);
        extrato.put("TIPOOPERACAO", escolhaTipoOperacao);
        extrato.put("VALOROPERACAO", valor);
        extrato.put("DATAOPERACAO", hoje);
        extrato.put("DSOPERACAO", descricaoEditText.getText().toString().trim());

        db.collection("GTEXTRATOFINANCEIRO")
                .add(extrato)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Operação salva com ID: " + documentReference.getId());

                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Erro ao salvar operação", e);
                });
        if (escolhaTipoOperacao.equals("Entrada")) {
            atualizaSaldoFinanceiro(valor,true);
        } else {
            atualizaSaldoFinanceiro(valor,false);
        }
        finish();
    }

    private void comboPernadominate(){
        String[] tipoOperacao = {"-- Escolha um opção --","Entrada","Saida"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                tipoOperacao
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operacaoSpinner.setAdapter(adapter);

        // pegar valor selecionado
        operacaoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                escolhaTipoOperacao = tipoOperacao[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public static void atualizaSaldoFinanceiro(double valorOperacao, boolean pagamento){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("GTSALDOFINANCEIRO")
                .whereEqualTo("IDTIME", idTimeUsuario)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        DocumentReference docRef = document.getReference();
                        if (pagamento) {
                            docRef.update("SALDOFINANCEIRO", FieldValue.increment(valorOperacao))
                                    .addOnSuccessListener(aVoid ->
                                            Log.d("Firestore", "mensalidade atualizada"))
                                    .addOnFailureListener(e ->
                                            Log.w("Firestore", "Erro ao atualizar", e));
                        } else  {
                            docRef.update("SALDOFINANCEIRO", FieldValue.increment(-valorOperacao))
                                    .addOnSuccessListener(aVoid ->
                                            Log.d("Firestore", "mensalidade atualizada"))
                                    .addOnFailureListener(e ->
                                            Log.w("Firestore", "Erro ao atualizar", e));
                        }

                    }
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Erro na busca", e));
    }
}