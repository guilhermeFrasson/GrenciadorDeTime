package com.example.Objetos;

public class Time {
    private String idTime;
    private String nomeTime;
    private boolean mensalidade;
    private boolean financeiro;
    private double valorMensalidadeMembro;
    private double valorMensalidadeTime;

    public Time(String idTime, String nomeTime) {
        this.idTime = idTime;
        this.nomeTime = nomeTime;

    }
    public Time() {
    }


    public double getValorMensalidadeTime() {
        return valorMensalidadeTime;
    }

    public void setValorMensalidadeTime(double valorMensalidadeTime) {
        this.valorMensalidadeTime = valorMensalidadeTime;
    }

    public double getValorMensalidadeMembro() {
        return valorMensalidadeMembro;
    }

    public void setValorMensalidadeMembro(double valorMensalidadeMembro) {
        this.valorMensalidadeMembro = valorMensalidadeMembro;
    }

    public boolean isFinanceiro() {
        return financeiro;
    }

    public void setFinanceiro(boolean financeiro) {
        this.financeiro = financeiro;
    }

    public boolean isMensalidade() {
        return mensalidade;
    }

    public void setMensalidade(boolean mensalidade) {
        this.mensalidade = mensalidade;
    }

    public String getNomeTime() {
        return nomeTime;
    }

    public void setNomeTime(String nomeTime) {
        this.nomeTime = nomeTime;
    }

    public String getIdTime() {
        return idTime;
    }

    public void setIdTime(String idTime) {
        this.idTime = idTime;
    }


}
