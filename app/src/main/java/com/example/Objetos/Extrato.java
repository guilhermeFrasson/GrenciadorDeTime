package com.example.Objetos;

import java.util.Date;

public class Extrato {
    String idTime;
    String tipoOperacao;
    Date dataOperacao;
    double valorOperacao;
    String dsOperacao;
    String idDoc;

    public Extrato(String dsOperacao, String idTime, String tipoOperacao, Date dataPagamento, double valorPagamento, String idDoc) {
        this.dsOperacao = dsOperacao;
        this.idTime = idTime;
        this.tipoOperacao = tipoOperacao;
        this.dataOperacao = dataPagamento;
        this.valorOperacao = valorPagamento;
        this.idDoc = idDoc;
    }

    public String getDsOperacao() {
        return dsOperacao;
    }

    public String getIdTime() {
        return idTime;
    }

    public String getTipoOperacao() {
        return tipoOperacao;
    }

    public Date getDataOperacao() {
        return dataOperacao;
    }

    public double getValorOperacao() {
        return valorOperacao;
    }

    public String getIdDoc() {
        return idDoc;
    }

}
