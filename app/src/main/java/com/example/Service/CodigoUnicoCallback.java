package com.example.Service;

public interface CodigoUnicoCallback {
    void onCodigoEncontrado(String codigo);
    void onFalha(Exception e);
}
