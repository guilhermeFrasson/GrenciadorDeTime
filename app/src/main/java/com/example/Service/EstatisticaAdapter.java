package com.example.Service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Objetos.Estatisticas;
import com.example.gerenciadordetime.R;

import java.util.List;

public class EstatisticaAdapter extends RecyclerView.Adapter<EstatisticaAdapter.ViewHolder>{
    private List<Estatisticas> lista;
    private OnItemClickListener listener;
    private boolean mostrarBotao;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public EstatisticaAdapter(List<Estatisticas> lista, OnItemClickListener listener, boolean mostrarBotao) {
        this.lista = lista;
        this.listener = listener;
        this.mostrarBotao = mostrarBotao;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_estatistica, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Estatisticas estat = lista.get(position);
        holder.textView.setText(estat.toString()); // usa o toString igual no Swing

        if (!mostrarBotao) {
            holder.btnDelete.setVisibility(View.GONE);
        } else {
            holder.btnDelete.setVisibility(View.VISIBLE);
        }
        ImagemHelper.aplicarImagemNoBotao(holder.itemView.getContext(), holder.btnDelete, R.drawable.btndelete, 70, 70);
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewEstatistica);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }

    public void setLista(List<Estatisticas> novaLista) {
        this.lista = novaLista;
        notifyDataSetChanged(); // avisa o RecyclerView que os dados mudaram
    }

}
