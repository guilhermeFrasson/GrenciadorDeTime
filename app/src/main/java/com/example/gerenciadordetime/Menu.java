package com.example.gerenciadordetime;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.Service.BuscaDadosTime;
import com.example.Service.BuscaDadosUser;
import com.example.Service.ImagemHelper;
import com.example.Service.infoTimeCallback;
import com.example.cadastro.CadJogador;
import com.example.cadastro.CadJogo;
import com.example.info.InfoEstatisticas;
import com.example.lista.ListJogos;
import com.example.lista.ListJogador;
import com.example.lista.ListMensalidade;
import com.example.lista.ListExtraFinanceiro;


public class Menu extends AppCompatActivity {
    private BuscaDadosTime buscaDadosTime = new BuscaDadosTime();
    Button btnCadJogo, btnCadJogador, btnFinanceiro, btnMensalidade;
    public static String funcaoUsuario;
    public static int idTimeUsuario;
    View overlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        Button btnListJogos = findViewById(R.id.btnListJogos);
        btnCadJogo = findViewById(R.id.btnCadJogo);
        Button btnListJogador = findViewById(R.id.btnListJogador);
        btnCadJogador = findViewById(R.id.btnCadJogador);
        Button btnEstatisticas = findViewById(R.id.btnEstatisticas);
        btnMensalidade = findViewById(R.id.btnMensalidade);
        btnFinanceiro = findViewById(R.id.btnFinanceiro);
        Button btnSair = findViewById(R.id.btnSair);
        overlay = findViewById(R.id.progressOverlay);

        overlay.setVisibility(View.VISIBLE);

        verificaInfoTimeUsuario("IDTIME");
        verificaFuncaoUsuario("FUNCAO");


        ImagemHelper.aplicarImagemNoBotao(this, btnListJogos, R.drawable.list_jogo, 125, 125);
        btnListJogos.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListJogos.class);
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
            startActivity(intent);
        });

        ImagemHelper.aplicarImagemNoBotao(this, btnCadJogador, R.drawable.novo_jogador, 125, 125);
        btnCadJogador.setOnClickListener(v -> {
            Intent intent = new Intent(this, CadJogador.class);
            intent.putExtra("PRIMEIROACESSO", "NAO");
            startActivity(intent);
        });

        ImagemHelper.aplicarImagemNoBotao(this, btnEstatisticas, R.drawable.estatisticas, 125, 125);
        btnEstatisticas.setOnClickListener(v -> {
            Intent intent = new Intent(this, InfoEstatisticas.class);
            startActivity(intent);
        });

        ImagemHelper.aplicarImagemNoBotao(this, btnMensalidade, R.drawable.mensalidade, 125, 125);
        btnMensalidade.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListMensalidade.class);
            startActivity(intent);
        });

        ImagemHelper.aplicarImagemNoBotao(this, btnFinanceiro, R.drawable.financeiro, 125, 125);
        btnFinanceiro.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListExtraFinanceiro.class);
            startActivity(intent);
        });

        ImagemHelper.aplicarImagemNoBotao(this, btnSair, R.drawable.btnsair, 70, 70);
        btnSair.setOnClickListener(v -> {
            finishAffinity();
            System.exit(0);
        });
    }

    private void verificaConfigTime(String tabela, String coluna, Button botao) {
        buscaDadosTime.verificaConfTime(idTimeUsuario, tabela, coluna, new infoTimeCallback() {
            @Override
            public void onCallback(boolean usaMensalidade) {
                if (usaMensalidade) {
                    botao.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void verificaInfoTimeUsuario(String coluna) {
        BuscaDadosUser buscadadosUser = new BuscaDadosUser();
        buscadadosUser.buscarIdTimeUsuario(coluna, retorno -> {
            if (retorno != 0) {
                    idTimeUsuario = retorno;
            }
        });
    }
    private void verificaFuncaoUsuario(String coluna) {
        BuscaDadosUser buscadadosUser = new BuscaDadosUser();
        buscadadosUser.buscarInfosUsuario(coluna, retorno -> {
            if (retorno != null) {
                funcaoUsuario = retorno;
                if ("Administrador".equals(retorno)) {
                    btnCadJogo.setVisibility(View.VISIBLE);
                    btnCadJogador.setVisibility(View.VISIBLE);
                    verificaConfigTime("GTTIME", "USAMENSALIDADE", btnMensalidade);
                    verificaConfigTime("GTTIME", "USAFINANCEIRO", btnFinanceiro);

                }
            }
            overlay.setVisibility(View.GONE);
        });
    }
}