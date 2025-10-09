package com.example.Service;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MaskTextWatcher implements TextWatcher {
    private final EditText editText;
    private boolean isUpdating = false;
    private String oldText = "";

    // Construtor que recebe o campo de texto que vamos mascarar
    public MaskTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Não precisamos fazer nada aqui
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Não precisamos fazer nada aqui
    }

    @Override
    public void afterTextChanged(Editable s) {
        String str = s.toString();
        // Remove tudo que não for dígito para trabalhar com o número "limpo"
        String unmaskedStr = str.replaceAll("[^\\d]", "");

        // Evita chamadas recursivas infinitas
        if (isUpdating || unmaskedStr.equals(oldText)) {
            return;
        }

        isUpdating = true;
        String formatted = "";
        int len = unmaskedStr.length();

        if (len >= 1) {
            formatted = "(" + unmaskedStr.substring(0, Math.min(2, len));
        }
        if (len > 2) {
            formatted += ") " + unmaskedStr.substring(2, Math.min(7, len));
        }
        if (len > 7) {
            formatted += "-" + unmaskedStr.substring(7, Math.min(11, len));
        }

        oldText = unmaskedStr;
        editText.setText(formatted);
        // Coloca o cursor no final do texto formatado
        editText.setSelection(formatted.length());

        isUpdating = false;
    }

}
