package com.example.Objetos;

import java.time.LocalDate;
import java.util.Date;

public class Jogador {

    private String idJogador;
    private String nome;
    private Date dataNascimento;
    private String posicao;
    private String pernaDominante;
    private String time;
    private String numCamiseta;
    private long gols;
    private long assistencias;


    public Jogador(String idJogador, String nome, Date dataNascimento, String posicao, String pernaDominante, long gols, long assistencias, String time) {
        this.idJogador = idJogador;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.posicao = posicao;
        this.pernaDominante = pernaDominante;
        this.gols = gols;
        this.assistencias = assistencias;
        this.time = time;
    }

    public Jogador(String nome, long gols, long assistencias) {
        this.nome = nome;
        this.gols = gols;
        this.assistencias = assistencias;
    }

    public Jogador(String nome) {
        this.nome = nome;
    }

    public String getIdJogador() {
        return idJogador;
    }

    public Jogador(String idJogador, String nome, String time) {
        this.idJogador = idJogador;
        this.nome = nome;
        this.time = time;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getPosicao() {
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }

    public String getPernaDominante() {
        return pernaDominante;
    }

    public void setPernaDominante(String pernaDominante) {
        this.pernaDominante = pernaDominante;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumCamiseta() {
        return numCamiseta;
    }

    public void setNumCamiseta(String numCamiseta) {
        this.numCamiseta = numCamiseta;
    }

    public long getGols() {
        return gols;
    }

    public void setGols(long gols) {
        this.gols = gols;
    }

    public long getAssistencias() {
        return assistencias;
    }

    public void setAssistencias(long assistencias) {
        this.assistencias = assistencias;
    }


    @Override
    public String toString() {
        return nome;
    }

}
