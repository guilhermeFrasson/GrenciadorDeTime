package com.example.lista;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Objetos.Estatisticas;
import com.example.adpter.EstatisticaAdapter;
import com.example.Service.ImagemHelper;
import com.example.cadastro.CadMarcadorGols;
import com.example.gerenciadordetime.R;

import java.util.List;

public class ListMarcadorGols extends AppCompatActivity {
    private EstatisticaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_marcador_gols);

        Button btnNovo = findViewById(R.id.btnnovo);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewMarcadores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Estatisticas> lista = CadMarcadorGols.getListaEstatisticas();
        adapter = new EstatisticaAdapter(lista, position -> {
            lista.remove(position);
            adapter.notifyItemRemoved(position);
        }, true);

        recyclerView.setAdapter(adapter);

        ImagemHelper.aplicarImagemNoBotao(this, btnNovo, R.drawable.btnadicionar, 120, 120);

        btnNovo.setOnClickListener(v -> {
            Intent intent = new Intent(this, CadMarcadorGols.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // sempre que voltar pra tela, recarrega a lista
        List<Estatisticas> lista = CadMarcadorGols.getListaEstatisticas();
        adapter.setLista(lista);
    }
}