package com.example.cadastro;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.CheckBox;

import com.example.Service.ImagemHelper;
import com.example.Service.MaskTextWatcher;
import com.example.gerenciadordetime.R;
import com.example.info.InfoTermosUso;
import com.google.android.material.textfield.TextInputEditText;

public class CadUsuario extends AppCompatActivity {

    private TextInputEditText telefoneEditText;
    private CheckBox checkTermos; // Renomeei para maior clareza
    private Button btnProximo, btnTermos, btnVoltar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cad_usuario);

        telefoneEditText = findViewById(R.id.telefoneEditText);
        telefoneEditText.addTextChangedListener(new MaskTextWatcher(telefoneEditText));

        bindViews();

        btnTermos.setPaintFlags(btnTermos.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btnProximo.setEnabled(false);

        ImagemHelper.aplicarImagemNoBotao(this, btnVoltar, R.drawable.btnvoltar, 70, 70);

        setupListeners();

    }

    private void bindViews() {
        btnTermos = findViewById(R.id.btnTermosUso);
        checkTermos = findViewById(R.id.checkGolContra);
        btnProximo = findViewById(R.id.btnProximo);
        btnVoltar = findViewById(R.id.btnVoltar);

    }

    private void setupListeners() {

        btnTermos.setOnClickListener(v -> {
            Intent intent = new Intent(this, InfoTermosUso.class);
            startActivity(intent);
        });

        btnProximo.setOnClickListener(v -> {
            Intent intent = new Intent(this, CadTime.class);
            startActivity(intent);
        });

        checkTermos.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnProximo.setEnabled(isChecked);
        });

        btnVoltar.setOnClickListener(v -> finish());

    }
}