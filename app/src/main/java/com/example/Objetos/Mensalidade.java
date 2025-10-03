package com.example.Objetos;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class Mensalidade {
    private String time, idJogador, nomeJogador;
    boolean mensalidadePaga;
    Date dataMensalidade;
    long valorMensalidade;
    String idDoc;
    Date dataPagamento;

    public Mensalidade(String time, String idJogador, String nomeJogador, boolean mensalidadePaga, Date dataMensalidade, long valorMensalidade, String idDoc, Date dataPagamento) {
        this.time = time;
        this.idJogador = idJogador;
        this.nomeJogador = nomeJogador;
        this.mensalidadePaga = mensalidadePaga;
        this.dataMensalidade = dataMensalidade;
        this.valorMensalidade = valorMensalidade;
        this.idDoc = idDoc;
        this.dataPagamento = dataPagamento;
    }

    public Mensalidade() {
    }


    public String getTime() {
        return time;
    }

    public String getIdJogador() {
        return idJogador;
    }

    public String getNomeJogador() {
        return nomeJogador;
    }

    public boolean isMensalidadePaga() {
        return mensalidadePaga;
    }

    public Date getDataMensalidade() {
        return dataMensalidade;
    }

    public long getValorMensalidade() {
        return valorMensalidade;
    }

    public String getIdDoc() {
        return idDoc;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

}
