package com.example.gerenciadordetime;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.example.Service.BuscaDadosUser;
import com.example.Service.ImagemHelper;
import com.example.Service.CriarMensalidades;
import com.example.cadastro.CadJogador;
import com.example.cadastro.CadJogo;
import com.example.info.InfoEstatisticas;
import com.example.lista.ListJogos;
import com.example.lista.ListJogador;
import com.example.lista.ListMensalidade;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Menu extends AppCompatActivity {
    Button btnListJogos,btnCadJogo,btnListJogador,btnCadJogador,btnEstatisticas,btnMensalidade,btnFinanceiro;
    private BuscaDadosUser buscadadosUser = new BuscaDadosUser();
    private CriarMensalidades mensalidade = new CriarMensalidades();
    FirebaseFirestore db;
    private String timeUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_black);

        btnListJogos = findViewById(R.id.btnListJogos);
        btnCadJogo = findViewById(R.id.btnCadJogo);
        btnListJogador = findViewById(R.id.btnListJogador);
        btnCadJogador = findViewById(R.id.btnCadJogador);
        btnEstatisticas = findViewById(R.id.btnEstatisticas);
        btnEstatisticas = findViewById(R.id.btnEstatisticas);
        btnMensalidade = findViewById(R.id.btnMensalidade);
        btnFinanceiro = findViewById(R.id.btnFinanceiro);

        String tipoUsuario = getIntent().getStringExtra("tipoUsuario");

        buscadadosUser.buscarTime(time -> {
            if (time != null) {
                timeUser = time;
            }
        });

        if ("Administrador".equals(tipoUsuario)) {
            btnCadJogo.setVisibility(View.VISIBLE);
            btnCadJogador.setVisibility(View.VISIBLE);
        }

        ImagemHelper.aplicarImagemNoBotao(this, btnListJogos, R.drawable.list_jogo, 125, 125);
        btnListJogos.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListJogos.class);
            intent.putExtra("TIPOUSUARIO", tipoUsuario);
            intent.putExtra("TIMEUSUARIO", timeUser);
            startActivity(intent);
        });

        ImagemHelper.aplicarImagemNoBotao(this, btnCadJogo, R.drawable.novo_jogo, 125, 125);
        btnCadJogo.setOnClickListener(v -> {
            Intent intent = new Intent(this, CadJogo.class);
            startActivity(intent);
        });

        ImagemHelper.aplicarImagemNoBotao(this, btnListJogador, R.drawable.list_jogador, 125, 125);
        btnListJogador.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListJogador.class);
            intent.putExtra("TIPOUSUARIO", tipoUsuario);
            intent.putExtra("TIMEUSUARIO", timeUser);
            startActivity(intent);
        });

        ImagemHelper.aplicarImagemNoBotao(this, btnCadJogador, R.drawable.novo_jogador, 125, 125);
        btnCadJogador.setOnClickListener(v -> {
            Intent intent = new Intent(this, CadJogador.class);
            startActivity(intent);
        });

        ImagemHelper.aplicarImagemNoBotao(this, btnEstatisticas, R.drawable.estatisticas, 125, 125);
        btnEstatisticas.setOnClickListener(v -> {
            Intent intent = new Intent(this, InfoEstatisticas.class);
            intent.putExtra("TIPOUSUARIO", tipoUsuario);
            intent.putExtra("TIMEUSUARIO", timeUser);
            startActivity(intent);
        });

        ImagemHelper.aplicarImagemNoBotao(this, btnMensalidade, R.drawable.mensalidade, 125, 125);
        btnMensalidade.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListMensalidade.class);
            intent.putExtra("TIPOUSUARIO", tipoUsuario);
            intent.putExtra("TIMEUSUARIO", timeUser);
            startActivity(intent);
        });

        ImagemHelper.aplicarImagemNoBotao(this, btnFinanceiro, R.drawable.financeiro, 125, 125);
    }

}