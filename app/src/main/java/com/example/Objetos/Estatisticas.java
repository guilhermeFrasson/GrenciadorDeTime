package com.example.Objetos;

public class Estatisticas {

    private String nome;
    private int gols;
    private int assistencias;

    public Estatisticas(String nome, int gols, int assistencias) {
        this.nome = nome;
        this.gols = gols;
        this.assistencias = assistencias;
    }

    public Estatisticas(String nome, int gols) {
        this.nome = nome;
        this.gols = gols;
    }

    public String getNome() {
        return nome;
    }

    public int getGols() {
        return gols;
    }

    public int getAssistencias() {
        return assistencias;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setGols(int gols) {
        this.gols = gols;
    }

    public void setAssistencias(int assistencias) {
        this.assistencias = assistencias;
    }

    @Override
    public String toString() {
        if (gols == 0 && assistencias == 0) {
            return nome;
        } else if (gols == 0) {
            if (assistencias > 1) {
                return nome + " - " + assistencias + " Assistências";
            } else {
                return nome + " - " + assistencias + " Assistência";
            }
        } else if (assistencias == 0) {
            if (gols > 1) {
                return nome + " - " + gols + " Gols";
            } else {
                return nome + " - " + gols + " Gol";
            }
        } else if (gols == 1 && assistencias == 1) {
            return nome + " - " + gols + " Gol e " + assistencias + " Assistência";
        } else if (gols == 1 && assistencias > 1) {
            return nome + " - " + gols + " Gol e " + assistencias + " Assistências";
        } else if (gols > 1 && assistencias == 1) {
            return nome + " - " + gols + " Gols e " + assistencias + " Assistência";
        } else {
            return nome + " - " + gols + " Gols e " + assistencias + " Assistências";
        }
    }
}
