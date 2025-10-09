package com.example.Objetos;

import java.util.Date;

public class Usuario {

    private String idTimeUsuario;
    private String nome;
    private String timeUsuario;
    private String funcaoUsuario;

    public Usuario(String idTimeUsuario, String nome, String timeUsuario, String funcaoUsuario) {
        this.idTimeUsuario = idTimeUsuario;
        this.nome = nome;
        this.timeUsuario = timeUsuario;
        this.funcaoUsuario = funcaoUsuario;

    }
    public Usuario() {
    }

    public String getFuncaoUsuario() {
        return funcaoUsuario;
    }

    public void setFuncaoUsuario(String funcaoUsuario) {
        this.funcaoUsuario = funcaoUsuario;
    }

    public String getTimeUsuario() {
        return timeUsuario;
    }

    public void setTimeUsuario(String timeUsuario) {
        this.timeUsuario = timeUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdTimeUsuario() {
        return idTimeUsuario;
    }

    public void setIdTimeUsuario(String idTimeUsuario) {
        this.idTimeUsuario = idTimeUsuario;
    }

}
