package com.example.gerenciadordetime;

import static com.example.Service.BuscaDadosUser.funcaoUsuario;
import static com.example.Service.BuscaDadosUser.idTimeUsuario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.example.Service.BuscaDadosTime;
import com.example.Service.ImagemHelper;
import com.example.Service.UsaMensalidadeCallback;
import com.example.cadastro.CadJogador;
import com.example.cadastro.CadJogo;
import com.example.info.InfoEstatisticas;
import com.example.lista.ListJogos;
import com.example.lista.ListJogador;
import com.example.lista.ListMensalidade;
import com.example.lista.ListExtraFinanceiro;


public class Menu extends AppCompatActivity {
    private BuscaDadosTime buscaDadosTime = new BuscaDadosTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        Button btnListJogos = findViewById(R.id.btnListJogos);
        Button btnCadJogo = findViewById(R.id.btnCadJogo);
        Button btnListJogador = findViewById(R.id.btnListJogador);
        Button btnCadJogador = findViewById(R.id.btnCadJogador);
        Button btnEstatisticas = findViewById(R.id.btnEstatisticas);
        Button btnMensalidade = findViewById(R.id.btnMensalidade);
        Button btnFinanceiro = findViewById(R.id.btnFinanceiro);

        if ("Administrador".equals(funcaoUsuario)) {
            btnCadJogo.setVisibility(View.VISIBLE);
            btnCadJogador.setVisibility(View.VISIBLE);
            buscaDadosTime.verificaUsaMensalidade(idTimeUsuario, new UsaMensalidadeCallback() {
                @Override
                public void onCallback(boolean usaMensalidade) {
                    if (usaMensalidade) {
                        btnMensalidade.setVisibility(View.VISIBLE);
                    }
                }
            });
            buscaDadosTime.verificaUsaFinanceiro(idTimeUsuario, new UsaMensalidadeCallback() {
                @Override
                public void onCallback(boolean usaFinanceiro) {
                    if (usaFinanceiro) {
                        btnFinanceiro.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

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
    }

}