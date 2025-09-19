package com.example.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Objetos.Mensalidade;
import com.example.gerenciadordetime.R;

import java.util.List;
import android.widget.ImageView;

public class MensalidadeAdapter extends RecyclerView.Adapter<MensalidadeAdapter.MensalidadeViewHolder>{

    private List<Mensalidade> lista;
    private MensalidadeAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Mensalidade mensalidade);
    }

    public MensalidadeAdapter(List<Mensalidade> lista, MensalidadeAdapter.OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MensalidadeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mensalidade, parent, false);
        return new MensalidadeViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(@NonNull MensalidadeViewHolder holder, int position) {
        Mensalidade mensalidade = lista.get(position);
        holder.txtNomeJogador.setText(mensalidade.getNomeJogador());

        if (mensalidade.isMensalidadePaga()) {
            holder.itemImagem.setImageResource(R.drawable.pago);
        } else {
            holder.itemImagem.setImageResource(R.drawable.naopago);
        }
        holder.itemView.setOnClickListener(v -> listener.onItemClick(mensalidade));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class MensalidadeViewHolder extends RecyclerView.ViewHolder {
        TextView txtNomeJogador;
        ImageView itemImagem;

        public MensalidadeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNomeJogador = itemView.findViewById(R.id.txtNomeJogador);
            itemImagem = itemView.findViewById(R.id.item_imagem);
        }

    }
}
