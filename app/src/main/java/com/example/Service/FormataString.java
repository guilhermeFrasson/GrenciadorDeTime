package com.example.Service;

public class FormataString {
    public static String formatarNome(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        texto = texto.toLowerCase(); // tudo minúsculo
        String semEspacos = texto.replace(" ", "");
        return semEspacos.substring(0, 1).toUpperCase() + semEspacos.substring(1);
    }
}
