package com.example.Objetos;

import java.util.Date;

public class Jogo {

    private String timeAdversario;
    private String resultado;
    private Date dataJogo;
    private long golsFeitos;
    private long golsSofridos;
    private String time, idJogo;


    public Jogo(String timeAdversario, String resultado, Date dataJogo, long golsFeitos, long golsSofridos, String time, String idJogo) {
        this.timeAdversario = timeAdversario;
        this.resultado = resultado;
        this.dataJogo = dataJogo;
        this.golsFeitos = golsFeitos;
        this.golsSofridos = golsSofridos;
        this.time = time;
        this.idJogo = idJogo;
    }

    public Jogo(String resultado, long golsFeitos, long golsSofridos) {
        this.resultado = resultado;
        this.golsFeitos = golsFeitos;
        this.golsSofridos = golsSofridos;
    }

    public String getTimeAdversario() {
        return timeAdversario;
    }

    public String getResultado() {
        return resultado;
    }

    public Date getDataJogo() {
        return dataJogo;
    }

    public long getGolsSofridos() {
        return golsSofridos;
    }

    public long getGolsFeitos() {
        return golsFeitos;
    }

    public String getIdJogo() {
        return idJogo;
    }

    public String getTime() {
        return time;
    }

}
