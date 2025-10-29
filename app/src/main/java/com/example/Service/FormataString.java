package com.example.Service;

public class FormataString {
    public static String formatarNome(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        texto = texto.toLowerCase(); // tudo minúsculo
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
}
