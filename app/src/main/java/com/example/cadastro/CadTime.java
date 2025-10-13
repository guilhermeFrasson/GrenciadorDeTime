package com.example.cadastro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Objetos.Time;
import com.example.Objetos.Usuario;
import com.example.Service.ImagemHelper;
import com.example.gerenciadordetime.R;

public class CadTime extends AppCompatActivity {

    private Button btnProximo, btnVoltar;
    private Switch financeiroSwitch, mensalidadeSwitch;
    private EditText nomeTimeEditText;
    private TextView campoMensalidadeTextView;
    private LinearLayout painelMensalidade;
    private Spinner modalidadeSpinner, sexoSpinner;
    String escolhaModalidade, escolhaSexo;
    private EditText mensalidadeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cad_time);

        bindViews();

        ImagemHelper.aplicarImagemNoBotao(this, btnVoltar, R.drawable.btnvoltar, 70, 70);

        setupListeners();
        camboModalidade();
        camboSexo();
    }

    private void bindViews() {
        btnProximo = findViewById(R.id.btnProximo);
        btnVoltar = findViewById(R.id.btnVoltar);
        nomeTimeEditText = findViewById(R.id.nomeTimeEditText);
        financeiroSwitch = findViewById(R.id.financeiroSwitch);
        mensalidadeSwitch = findViewById(R.id.mensalidadeSwitch);
        campoMensalidadeTextView = findViewById(R.id.campoMensalidadeTextView);
        painelMensalidade = findViewById(R.id.painelMensalidade);
        modalidadeSpinner = findViewById(R.id.modalidadeSpinner);
        sexoSpinner = findViewById(R.id.sexoSpinner);
        mensalidadeEditText = findViewById(R.id.mensalidadeEditText);
    }

    private void setupListeners() {

        btnProximo.setOnClickListener(v -> {
            if (verificaCampos()) {
                salvaDadosTime();
                String testes = "SIM";
                Intent intent = new Intent(this, CadJogador.class);
                intent.putExtra("PRIMEIROACESSO", testes);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Preencha os campos obrigatorios", Toast.LENGTH_SHORT).show();
            }
        });

        financeiroSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
            } else {
            }
        });

        mensalidadeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                campoMensalidadeTextView.setVisibility(View.VISIBLE);
                painelMensalidade.setVisibility(View.VISIBLE);
            } else {
                campoMensalidadeTextView.setVisibility(View.GONE);
                painelMensalidade.setVisibility(View.GONE);
            }
        });

        btnVoltar.setOnClickListener(v -> finish());

    }

    private void salvaDadosTime() {
        Time time = new Time();
        time.setNomeTime(nomeTimeEditText.getText().toString());
        time.setFinanceiro(Boolean.parseBoolean(financeiroSwitch.getText().toString()));
        time.setMensalidade(Boolean.parseBoolean(mensalidadeSwitch.getText().toString()));
        try {
            time.setValorMensalidadeMembro(Double.parseDouble(mensalidadeEditText.getText().toString()));
        } catch (NumberFormatException e) {
            time.setValorMensalidadeMembro(0.0);
        }
        time.setValorMensalidadeTime(20);
    }


    public boolean verificaCampos() {
        if (nomeTimeEditText.getText().toString().isEmpty()) {
            return false;
        }else {
            return true;
        }
    }

    private void camboModalidade() {
        String[] modalidade = {"-- Escolha um opção --", "Fut7", "Futsal", "Campo"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                modalidade
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modalidadeSpinner.setAdapter(adapter);

        // pegar valor selecionado
        modalidadeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                escolhaModalidade = modalidade[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void camboSexo() {
        String[] sexo = {"-- Escolha um opção --", "Masculino", "Feminino"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                sexo
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexoSpinner.setAdapter(adapter);

        // pegar valor selecionado
        sexoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                escolhaSexo = sexo[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}