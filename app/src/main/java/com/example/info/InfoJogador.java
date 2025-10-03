package com.example.info;

import static com.example.gerenciadordetime.Menu.funcaoUsuario;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Service.DeletarRegistroBanco;
import com.example.Service.ImagemHelper;
import com.example.Service.PopupUtils;
import com.example.gerenciadordetime.R;

import java.util.Calendar;
import java.util.Date;

public class InfoJogador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_jogador);

        TextView textNomeJogador = findViewById(R.id.textNomeJogador);
        TextView textNomeTime = findViewById(R.id.textNomeTime);
        TextView textPosicao = findViewById(R.id.textPosicao);
        TextView textPernaDominante = findViewById(R.id.textPernaDominante);
        TextView textGols = findViewById(R.id.textGols);
        TextView textAssistencias = findViewById(R.id.textAssistencias);
        TextView textIdade = findViewById(R.id.textIdade);
        Button btnDelete = findViewById(R.id.btnDelete);

        // Mostrar o nome no TextView
        textNomeJogador.setText(getIntent().getStringExtra("NOME_JOGADOR"));
        textNomeTime.setText(getIntent().getStringExtra("TIMEUSER"));
        textPosicao.setText(getIntent().getStringExtra("POSICAO_JOGADOR"));
        textPernaDominante.setText(getIntent().getStringExtra("PRENA_DOMINANTE_JOGADOR"));
        textGols.setText("" + getIntent().getLongExtra("GOLS", 0));
        textAssistencias.setText("" + getIntent().getLongExtra("ASSISTENCIAS", 0));
        textIdade.setText(calcularIdade((Date) getIntent().getSerializableExtra("DATA_NASCIMENTO_JOGADOR")) + " anos");

        if ("Administrador" .equals(funcaoUsuario)) {
            btnDelete.setVisibility(View.VISIBLE);
        }

        ImagemHelper.aplicarImagemNoBotao(this, btnDelete, R.drawable.btndelete, 100, 100);
        btnDelete.setOnClickListener(v -> {
            PopupUtils.mostrarConfirmacao(
                    this,
                    "Confirmação",
                    "Você tem certeza que deseja excluir?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DeletarRegistroBanco.deleteRegistro("GTJOGADOR", getIntent().getStringExtra("IDJOGADOR"));
                            Intent intent = new Intent();
                            intent.putExtra("deletado", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
            );

        });
    }

    private int calcularIdade(Date dataNascimento) {
        Calendar nascimento = Calendar.getInstance();
        nascimento.setTime(dataNascimento);

        Calendar hoje = Calendar.getInstance();

        int idade = hoje.get(Calendar.YEAR) - nascimento.get(Calendar.YEAR);

        if (hoje.get(Calendar.MONTH) < nascimento.get(Calendar.MONTH) ||
                (hoje.get(Calendar.MONTH) == nascimento.get(Calendar.MONTH) &&
                        hoje.get(Calendar.DAY_OF_MONTH) < nascimento.get(Calendar.DAY_OF_MONTH))) {
            idade--;
        }

        return idade;
    }

}