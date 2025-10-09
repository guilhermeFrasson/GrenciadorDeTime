package com.example.info;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.Service.ImagemHelper;
import com.example.gerenciadordetime.R;

public class InfoTermosUso extends AppCompatActivity {

    private Button btnVoltar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_termos_uso);
        bindViews();

        ImagemHelper.aplicarImagemNoBotao(this, btnVoltar, R.drawable.btnvoltar, 70, 70);

        setupListeners();
    }

    private void bindViews() {
        btnVoltar = findViewById(R.id.btnVoltar);
    }

    private void setupListeners() {
        btnVoltar.setOnClickListener(v -> finish());
    }
}