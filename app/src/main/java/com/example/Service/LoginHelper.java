package com.example.Service;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class LoginHelper {

    /**
     * Configura os campos de login para que o Enter passe para o próximo campo
     * e o último campo acione o botão de login.
     *
     * @param campos Array de EditTexts na ordem em que devem receber foco
     * @param botao Botão de login que será acionado no último campo
     */
    public static void configurarPularCampos(EditText[] campos, Button botao) {
        for (int i = 0; i < campos.length; i++) {
            final int index = i;
            EditText campo = campos[i];

            campo.setOnEditorActionListener((v, actionId, event) -> {
                boolean isEnter = event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN;

                if (actionId == EditorInfo.IME_ACTION_NEXT || isEnter) {
                    if (index < campos.length - 1) {
                        // Passa para o próximo campo
                        campos[index + 1].requestFocus();
                    } else {
                        // Último campo → aciona o botão
                        botao.performClick();
                    }
                    return true;
                } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Caso o último campo esteja com actionDone
                    botao.performClick();
                    return true;
                }
                return false;
            });
        }
    }
}
