package com.example.Objetos;

import java.util.Date;

public class Mensalidade {
    private String time, idJogador, nomeJogador;
    boolean mensalidadePaga;
    Date dataMensalidade;

    public Mensalidade(String time,String idJogador, String nomeJogador, boolean mensalidadePaga, Date dataMensalidade) {
        this.time = time;
        this.idJogador = idJogador;
        this.nomeJogador = nomeJogador;
        this.mensalidadePaga = mensalidadePaga;
        this.dataMensalidade = dataMensalidade;

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

}
