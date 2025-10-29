package com.example.Objetos;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class Mensalidade {
    private String idJogador, nomeJogador;
    boolean mensalidadePaga;
    Date dataMensalidade;
    long valorMensalidade;
    String idDoc;
    Date dataPagamento;

    public Mensalidade(String idJogador, String nomeJogador, boolean mensalidadePaga, Date dataMensalidade, long valorMensalidade, String idDoc, Date dataPagamento) {
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
