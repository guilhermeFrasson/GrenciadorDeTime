package com.example.Service;



import com.example.Objetos.Estatisticas;
import com.example.Objetos.Jogador;
import com.example.gerenciadordetime.R;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class JogadorAdapter extends RecyclerView.Adapter<JogadorAdapter.JogadorViewHolder>{

    private List<Jogador> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Jogador jogador);
    }

    public JogadorAdapter(List<Jogador> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public JogadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_jogador, parent, false);
        return new JogadorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JogadorViewHolder holder, int position) {
        Jogador jogador = lista.get(position);
        holder.textNome.setText(jogador.getNome());
        holder.textPosicao.setText(jogador.getPosicao());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(jogador));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class JogadorViewHolder extends RecyclerView.ViewHolder {
        TextView textNome, textPosicao;

        public JogadorViewHolder(@NonNull View itemView) {
            super(itemView);
            textNome = itemView.findViewById(R.id.textNome);
            textPosicao = itemView.findViewById(R.id.textPosicao);
        }
    }

    public void setLista(List<Jogador> novaLista) {
        this.lista = novaLista;
        notifyDataSetChanged();
    }
}
