package com.example.adpter;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Objetos.Extrato;
import com.example.gerenciadordetime.R;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class ExtratoAdapter extends RecyclerView.Adapter<ExtratoAdapter.ExtratoViewHolder>{

    private List<Extrato> lista;
    private ExtratoAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Extrato extrato);
    }

    public ExtratoAdapter(List<Extrato> lista, ExtratoAdapter.OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExtratoAdapter.ExtratoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_extrato, parent, false);
        return new ExtratoAdapter.ExtratoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExtratoViewHolder holder, int position) {
        Extrato extrato = lista.get(position);
        if (extrato.getTipoOperacao().equals("Entrada")) {
            holder.textValorOperacao.setText("+R$ " + String.valueOf(extrato.getValorOperacao()));
            holder.textValorOperacao.setTextColor(Color.parseColor("#4CAF50")); // verde
        } else {
            holder.textValorOperacao.setText("-R$ " + String.valueOf(extrato.getValorOperacao()));
            holder.textValorOperacao.setTextColor(Color.parseColor("#F44336")); // vermelho
        }
        holder.textDescricao.setText(extrato.getDsOperacao());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dataFormatada = sdf.format(extrato.getDataOperacao());
        holder.textDataOperacao.setText(dataFormatada);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(extrato));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ExtratoViewHolder extends RecyclerView.ViewHolder {
        TextView textDescricao;
        TextView textValorOperacao;
        TextView textDataOperacao;


        public ExtratoViewHolder(@NonNull View itemView) {
            super(itemView);
            textDescricao = itemView.findViewById(R.id.textDescricao);
            textValorOperacao = itemView.findViewById(R.id.textValorOperacao);
            textDataOperacao = itemView.findViewById(R.id.textDataOperacao);
        }

    }
}
