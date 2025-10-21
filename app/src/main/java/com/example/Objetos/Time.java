package com.example.Objetos;

public class Time {
    public static String idTime;
    public static String nomeTime;
    public static boolean mensalidade;
    public static boolean financeiro;
    public static double valorMensalidadeMembro;
    public static double valorMensalidadeTime;

    public Time(String idTime, String nomeTime) {
        this.idTime = idTime;
        this.nomeTime = nomeTime;

    }
    public Time() {
    }

    public static double getValorMensalidadeTime() {
        return valorMensalidadeTime;
    }

    public void setValorMensalidadeTime(double valorMensalidadeTime) {
        this.valorMensalidadeTime = valorMensalidadeTime;
    }

    public static double getValorMensalidadeMembro() {
        return valorMensalidadeMembro;
    }

    public void setValorMensalidadeMembro(double valorMensalidadeMembro) {
        this.valorMensalidadeMembro = valorMensalidadeMembro;
    }

    public static boolean isFinanceiro() {
        return financeiro;
    }

    public void setFinanceiro(boolean financeiro) {
        this.financeiro = financeiro;
    }

    public static boolean isMensalidade() {
        return mensalidade;
    }

    public void setMensalidade(boolean mensalidade) {
        this.mensalidade = mensalidade;
    }

    public static String getNomeTime() {
        return nomeTime;
    }

    public void setNomeTime(String nomeTime) {
        this.nomeTime = nomeTime;
    }

    public static String getIdTime() {
        return idTime;
    }

    public void setIdTime(String idTime) {
        this.idTime = idTime;
    }


}
