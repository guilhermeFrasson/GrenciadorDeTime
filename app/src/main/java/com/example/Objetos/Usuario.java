package com.example.Objetos;

public class Usuario {

    public static String idTimeUsuario;
    public static String nome;
    public static String timeUsuario;
    public static String funcaoUsuario;
    public static String email;
    public static String senha;
    public static String telefone;

    public Usuario(String idTimeUsuario, String nome, String timeUsuario, String funcaoUsuario, String email, String senha, String telefone) {
        this.idTimeUsuario = idTimeUsuario;
        this.nome = nome;
        this.timeUsuario = timeUsuario;
        this.funcaoUsuario = funcaoUsuario;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

}
