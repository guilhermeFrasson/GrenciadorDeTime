package com.example.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Objetos.Jogo;
import com.example.gerenciadordetime.R;

import java.util.List;

public class JogoAdapter extends RecyclerView.Adapter<JogoAdapter.JogoViewHolder>{
    private List<Jogo> lista;
    private JogoAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Jogo jogo);
    }

    public JogoAdapter(List<Jogo> lista, JogoAdapter.OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }
    @NonNull
    @Override
    public JogoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_jogo, parent, false);
        return new JogoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JogoViewHolder holder, int position) {
        Jogo jogo = lista.get(position);
        holder.textTimeAdiversario.setText(jogo.getTimeAdversario());
        holder.textResultado.setText(jogo.getResultado());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(jogo));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class JogoViewHolder extends RecyclerView.ViewHolder {

        TextView textTimeAdiversario, textResultado;

        public JogoViewHolder(@NonNull View itemView) {
            super(itemView);
            textTimeAdiversario = itemView.findViewById(R.id.textTimeAdiversario);
            textResultado = itemView.findViewById(R.id.textResultado);
        }
    }
}
