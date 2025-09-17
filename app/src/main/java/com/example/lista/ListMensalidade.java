package com.example.lista;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Objetos.Jogador;
import com.example.Objetos.Jogo;
import com.example.Objetos.Mensalidade;
import com.example.Service.ImagemHelper;
import com.example.Service.JogadorAdapter;
import com.example.Service.JogoAdapter;
import com.example.Service.MensalidadeAdapter;
import com.example.gerenciadordetime.R;
import com.example.info.InfoJogo;
import com.example.info.InfoMensalidade;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ListMensalidade extends AppCompatActivity {
    List<Mensalidade> listaMensalidade = new ArrayList<>();
    private FirebaseFirestore db;
    private String timeUsuario, tipoUsuario;
    private RecyclerView recyclerMensalidades;
    private MensalidadeAdapter adapter;
    private TextView textlayoutResumo;
    private Button btnFiltrar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_mensalidade);

        textlayoutResumo = findViewById(R.id.textlayoutResumo);
        btnFiltrar = findViewById(R.id.btnFiltrar);

        recyclerMensalidades = findViewById(R.id.recyclerMensalidades);
        recyclerMensalidades.setLayoutManager(new LinearLayoutManager(this));

        timeUsuario = getIntent().getStringExtra("TIMEUSUARIO");
        tipoUsuario = getIntent().getStringExtra("TIPOUSUARIO");

        ImagemHelper.aplicarImagemNoBotao(this, btnFiltrar, R.drawable.btnbuscar, 70, 70);
        btnFiltrar.setOnClickListener(v -> {
            
        });

        listaMensalidade = listarMensalidadeDoBanco();


    }

    private List<Mensalidade> listarMensalidadeDoBanco() {
        List<Mensalidade> lista = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        db.collection("GTMENSALIDADE")
                .whereEqualTo("TIME", timeUsuario)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String time = document.getString("TIME");
                            String nomeJogador = document.getString("NOMEJOGADOR");
                            Date dataMensalidade = document.getDate("DATAMENSALIDADE");
                            boolean mensalidadePaga = document.getBoolean("PAGO");
                            String idJogador = document.getString("IDJOGADOR");

                            Mensalidade mensalidade = new Mensalidade(time, idJogador, nomeJogador,mensalidadePaga, dataMensalidade);
                            listaMensalidade.add(mensalidade);
                        }

                        listaMensalidade.sort(Comparator.comparing(Mensalidade::getNomeJogador));

                        // Aqui você seta a lista no Adapter do RecyclerView
                        adapter = new MensalidadeAdapter(listaMensalidade, jogo -> {
                            Intent intent = new Intent(this, InfoMensalidade.class);
                            intent.putExtra("TIME_ADIVERSARIO", jogo.getNomeJogador());
                            intent.putExtra("RESULTADO", jogo.getDataMensalidade());
                            intent.putExtra("DATA_JOGO", jogo.isMensalidadePaga());
                            startActivityForResult(intent, 1);
                        });
                        recyclerMensalidades.setAdapter(adapter);
                        receberDadosJogo();

                    } else {
                        Log.e("Firestore", "Erro ao buscar jogadores", task.getException());
                    }
                });
        return lista;
    }

    private void receberDadosJogo() {

        long pago = 0;
        long naoPago = 0;
        pago = listaMensalidade.stream().filter(Mensalidade::isMensalidadePaga).count();
        naoPago = listaMensalidade.stream().filter(mensalidade -> !mensalidade.isMensalidadePaga()).count();


        textlayoutResumo.setText("Pago: " + pago + " | Faltando: " + naoPago);


    }
}